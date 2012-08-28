/*
 * Copyright 2011-2012 Happy-Camper Street.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package st.happy_camper.hbase.shell
package client
package command.dml

import java.util.concurrent.atomic.AtomicLong

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Scan

import st.happy_camper.hbase.shell.coprocessor.protocol.RowCountProtocol

/**
 * A trait to handle count commands.
 * @author ueshin
 */
trait CountCommand {
  self: Client with HTable =>

  implicit def countingTable(table: Table) = CountCommand.CountingTable(table.tablename)
}

/**
 * A companion object of trait {@link CountCommand}.
 * @author ueshin
 */
object CountCommand {

  /**
   * Represents a counting table.
   * @author ueshin
   */
  case class CountingTable(
      override val tablename: Array[Byte])(implicit conf: Configuration) extends Table(tablename) {

    /**
     * Counts rows of the table with filter.
     * @param filter the filter
     * @return the row count
     */
    def count(implicit scan: Scan = new Scan()): Long = {
      val result = new AtomicLong(0L)
      coprocessorExec(
        tablename,
        scan.getStartRow,
        scan.getStopRow,
        call = { counter: RowCountProtocol =>
          counter.count(scan)
        },
        callback = { (region, row, value: Long) =>
          result.addAndGet(value)
        })
      result.longValue
    }
  }
}
