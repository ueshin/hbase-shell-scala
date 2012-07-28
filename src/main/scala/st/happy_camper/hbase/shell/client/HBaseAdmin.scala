/*
 * Copyright 2011 Happy-Camper Street.
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

import org.apache.hadoop.hbase.{ HTableDescriptor => AHTableDescriptor, HColumnDescriptor => AHColumnDescriptor }
import org.apache.hadoop.hbase.client.{ HBaseAdmin => AHBaseAdmin }

import st.happy_camper.hbase.shell.{ HTableDescriptor, ColumnAttribute }

/**
 * @author ueshin
 */
trait HBaseAdmin {

  val admin: AHBaseAdmin

  /**
   *
   */
  def list: Seq[AHTableDescriptor] = {
    val s = System.currentTimeMillis
    val tables = admin.listTables
    tables.foreach { table =>
      println(table.getNameAsString)
    }
    println(tables.size + " row(s) in " + (System.currentTimeMillis - s) + " milliseconds.")
    tables.toSeq
  }

  /**
   * @param tablename
   * @return
   */
  def describe(tablename: String) = {
    val s = System.currentTimeMillis
    val table = admin.getTableDescriptor(tablename)
    println(table.toString)
    println("1 row in " + (System.currentTimeMillis - s) + " milliseconds.")
    table
  }

  /**
   * @param tablename
   * @param familyName
   * @param familyNames
   * @return
   */
  def create(tablename: String,
             familyName: String,
             familyNames: String*): AHTableDescriptor = {
    create(HTableDescriptor(tablename, familyName, familyNames: _*))
  }

  /**
   * @param tablename
   * @param family
   * @param families
   * @return
   */
  def create[A <: ColumnAttribute](tablename: String,
                                   family: (String, Seq[A]),
                                   families: (String, Seq[A])*): AHTableDescriptor = {
    create(HTableDescriptor(tablename, family, families: _*))
  }

  /**
   * @param tablename
   * @param columnDescriptor
   * @param columnDescriptors
   * @return
   */
  def create(tablename: String,
             columnDescriptor: AHColumnDescriptor,
             columnDescriptors: AHColumnDescriptor*): AHTableDescriptor = {
    create(HTableDescriptor(tablename, columnDescriptor, columnDescriptors: _*))
  }

  /**
   * @param tableDescriptor
   * @return
   */
  def create(tableDescriptor: AHTableDescriptor): AHTableDescriptor = {
    create(tableDescriptor, null)
  }

  /**
   * @param tableDescriptor
   * @param splitKeys
   * @return
   */
  def create(tableDescriptor: AHTableDescriptor,
             splitKeys: Array[Array[Byte]]): AHTableDescriptor = {
    val s = System.currentTimeMillis
    admin.createTable(tableDescriptor, splitKeys)
    println("created in " + (System.currentTimeMillis - s) + " milliseconds.")
    tableDescriptor
  }

  /**
   * @param tableDescriptor
   * @param startKey
   * @param endKey
   * @param numRegions
   * @return
   */
  def create(tableDescriptor: AHTableDescriptor,
             startKey: Array[Byte],
             endKey: Array[Byte],
             numRegions: Int): AHTableDescriptor = {
    val s = System.currentTimeMillis
    admin.createTable(tableDescriptor, startKey, endKey, numRegions)
    println("created in " + (System.currentTimeMillis - s) + " milliseconds.")
    tableDescriptor
  }

  /**
   * @param tableDescriptor
   * @param splitKeys
   * @return
   */
  def createAsync(tableDescriptor: AHTableDescriptor,
                  splitKeys: Array[Array[Byte]]): AHTableDescriptor = {
    val s = System.currentTimeMillis
    admin.createTableAsync(tableDescriptor, splitKeys)
    println("created in " + (System.currentTimeMillis - s) + " milliseconds.")
    tableDescriptor
  }
}
