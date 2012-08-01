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

/**
 * A trait to handle disable commands.
 * @author ueshin
 */
trait DisableCommand {
  self: HBaseAdmin =>

  /**
   * Checks if a table is disabled.
   * @param tablename the table name
   * @return true if the table is disabled, false otherwise.
   */
  def isDisabled(tablename: String): Boolean = {
    isDisabled(toBytes(tablename))
  }

  /**
   * Checks if a table is disabled.
   * @param tablename the table name
   * @return true if the table is disabled, false otherwise.
   */
  def isDisabled(tablename: Array[Byte]): Boolean = {
    admin.isTableDisabled(tablename)
  }

  /**
   * Disables a table.
   * @param tablename the table name
   */
  def disable(tablename: String): Unit = {
    disable(toBytes(tablename))
  }

  /**
   * Disables a table.
   * @param tablename the table name
   */
  def disable(tablename: Array[Byte]): Unit = {
    admin.disableTable(tablename)
  }

  /**
   * Disables a table asynchronously.
   * @param tablename the table name
   */
  def disableAsync(tablename: String): Unit = {
    disableAsync(toBytes(tablename))
  }

  /**
   * Disables a table asynchronously.
   * @param tablename the table name
   */
  def disableAsync(tablename: Array[Byte]) = {
    admin.disableTableAsync(tablename)
  }
}
