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
 * A trait to handle drop commands.
 * @author ueshin
 */
trait DropCommand {
  self: HBaseAdmin =>

  implicit def droppingTable(table: Table) = DropCommand.DroppingTable(table.tablename)
}

/**
 * A companion object of trait {@link DropCommand}.
 * @author ueshin
 */
object DropCommand {

  /**
   * Represents a dropping table.
   * @author ueshin
   */
  case class DroppingTable(
      override val tablename: Array[Byte]) extends Table(tablename) {

    /**
     * Drops a table.
     * @param admin implicit {@link AHBaseAdmin} instance
     */
    def drop()(implicit admin: AHBaseAdmin): Unit = {
      admin.deleteTable(tablename)
    }
  }
}