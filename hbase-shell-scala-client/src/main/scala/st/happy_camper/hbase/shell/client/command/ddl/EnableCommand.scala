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

/**
 * A trait to handle enable commands.
 * @author ueshin
 */
trait EnableCommand {
  self: Client with HBaseAdmin =>

  implicit def enablingTable(table: Table) = EnableCommand.EnablingTable(table.tablename)
}

/**
 * A companion object of trait {@link DisableCommand}.
 * @author ueshin
 */
object EnableCommand {

  /**
   * Represents a enabling table.
   * @author ueshin
   */
  case class EnablingTable(
      override val tablename: Array[Byte])(implicit admin: AHBaseAdmin) extends Table(tablename) {

    /**
     * Checks if a table is enabled.
     * @return true if the table is enabled, false otherwise.
     */
    def isEnabled(): Boolean = {
      admin.isTableEnabled(tablename)
    }

    /**
     * Enables a table.
     * @return this
     */
    def enable(): Table = {
      if (!isEnabled) admin.enableTable(tablename)
      this
    }

    /**
     * Enables a table asynchronously.
     * @return this
     */
    def enableAsync(): Table = {
      if (!isEnabled) admin.enableTableAsync(tablename)
      this
    }
  }
}
