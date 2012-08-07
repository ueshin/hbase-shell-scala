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
 * A trait to handle disable commands.
 * @author ueshin
 */
trait DisableCommand {
  self: HBaseAdmin =>

  implicit def disablingTable(table: Table) = DisableCommand.DisablingTable(table.tablename)
}

/**
 * A companion object of trait {@link DisableCommand}.
 * @author ueshin
 */
object DisableCommand {

  /**
   * Represents a disabling table.
   * @author ueshin
   */
  case class DisablingTable(
      override val tablename: Array[Byte]) extends Table(tablename) {

    /**
     * Checks if a table is disabled.
     * @param admin implicit {@link AHBaseAdmin} instance
     * @return true if the table is disabled, false otherwise.
     */
    def isDisabled()(implicit admin: AHBaseAdmin): Boolean = {
      admin.isTableDisabled(tablename)
    }

    /**
     * Disables a table.
     * @param admin implicit {@link AHBaseAdmin} instance
     * @return this
     */
    def disable()(implicit admin: AHBaseAdmin): Table = {
      if (!isDisabled) admin.disableTable(tablename)
      this
    }

    /**
     * Disables a table asynchronously.
     * @param admin implicit {@link AHBaseAdmin} instance
     * @return this
     */
    def disableAsync()(implicit admin: AHBaseAdmin): Table = {
      if (!isDisabled) admin.disableTableAsync(tablename)
      this
    }
  }
}
