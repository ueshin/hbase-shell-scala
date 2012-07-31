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

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{ HTableDescriptor => AHTableDescriptor, HBaseConfiguration }
import org.apache.hadoop.hbase.client.{ HBaseAdmin => AHBaseAdmin }

import st.happy_camper.hbase.shell.client.HBaseAdmin

/**
 * @author ueshin
 */
class Shell(val conf: Configuration) extends HBaseAdmin {

  val admin = new AHBaseAdmin(conf)

  override def list: Seq[AHTableDescriptor] = {
    val s = System.currentTimeMillis
    val tables = super.list
    tables.foreach { table =>
      println(table.getNameAsString)
    }
    println(tables.size + " row(s) in " + (System.currentTimeMillis - s) + " milliseconds.")
    tables
  }

  override def describe(tablename: String) = {
    val s = System.currentTimeMillis
    val table = super.describe(tablename)
    println(table.toString)
    println("1 row in " + (System.currentTimeMillis - s) + " milliseconds.")
    table
  }

  override def create(tableDescriptor: AHTableDescriptor,
                      splitKeys: Array[Array[Byte]]): AHTableDescriptor = {
    val s = System.currentTimeMillis
    super.create(tableDescriptor, splitKeys)
    println("created in " + (System.currentTimeMillis - s) + " milliseconds.")
    tableDescriptor
  }

  override def create(tableDescriptor: AHTableDescriptor,
                      startKey: Array[Byte],
                      endKey: Array[Byte],
                      numRegions: Int): AHTableDescriptor = {
    val s = System.currentTimeMillis
    super.create(tableDescriptor, startKey, endKey, numRegions)
    println("created in " + (System.currentTimeMillis - s) + " milliseconds.")
    tableDescriptor
  }

  override def createAsync(tableDescriptor: AHTableDescriptor,
                           splitKeys: Array[Array[Byte]]): AHTableDescriptor = {
    val s = System.currentTimeMillis
    super.createAsync(tableDescriptor, splitKeys)
    println("created in " + (System.currentTimeMillis - s) + " milliseconds.")
    tableDescriptor
  }
}

/**
 * @author ueshin
 */
object Shell extends Shell(HBaseConfiguration.create)
