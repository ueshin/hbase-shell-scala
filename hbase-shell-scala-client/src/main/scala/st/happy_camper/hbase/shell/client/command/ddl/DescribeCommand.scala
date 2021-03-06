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
package command.ddl

import org.apache.hadoop.hbase.client.{ HBaseAdmin => AHBaseAdmin }
import org.apache.hadoop.hbase.HTableDescriptor

/**
 * A trait to handle describe command.
 * @author ueshin
 */
trait DescribeCommand {
  self: Client with HBaseAdmin =>

  implicit def describingTable(table: Table) = DescribeCommand.DescribingTable(table.tablename)
}

/**
 * A companion object of trait {@link DescribeCommand}.
 * @author ueshin
 */
object DescribeCommand {

  /**
   * Represents a describing table.
   * @author ueshin
   */
  case class DescribingTable(
      override val tablename: Array[Byte])(implicit admin: AHBaseAdmin) extends Table(tablename) {

    /**
     * Checks if a table exists or not.
     * @return true if the table exists, false otherwise
     */
    def exists(): Boolean = {
      admin.tableExists(tablename)
    }

    /**
     * Returns a table descriptor.
     * @return the table descriptor
     */
    def describe(): Option[HTableDescriptor] = {
      if (exists()) {
        Option(admin.getTableDescriptor(tablename))
      } else {
        None
      }
    }
  }
}
