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
package st.happy_camper.hbase.shell.client

import org.apache.hadoop.hbase.HBaseConfiguration

import st.happy_camper.hbase.shell.client.command.ddl.AlterCommand
import st.happy_camper.hbase.shell.client.command.ddl.CreateCommand
import st.happy_camper.hbase.shell.client.command.ddl.DescribeCommand
import st.happy_camper.hbase.shell.client.command.ddl.DisableCommand
import st.happy_camper.hbase.shell.client.command.ddl.DropCommand
import st.happy_camper.hbase.shell.client.command.ddl.EnableCommand
import st.happy_camper.hbase.shell.client.command.ddl.ListCommand
import st.happy_camper.hbase.shell.client.command.dml.CountCommand

/**
 * Represents HBase admin shell.
 * @author ueshin
 */
trait HBaseAdminShell
    extends HBaseAdmin
    with ListCommand
    with DescribeCommand
    with CreateCommand
    with EnableCommand
    with DisableCommand
    with DropCommand
    with AlterCommand {
  self: Client =>
}

/**
 * Represents HTable shell.
 * @author ueshin
 */
trait HTableShell
    extends HTable
    with CountCommand {
  self: Client =>
}

/**
 * Represents HBase shell.
 * @author ueshin
 */
trait Shell
  extends Client
  with HBaseAdminShell
  with HTableShell

/**
 * An object Shell initialized by default configurations.
 * @author ueshin
 */
object Shell extends Shell {
  implicit val conf = HBaseConfiguration.create
}
