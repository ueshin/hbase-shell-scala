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

import org.apache.hadoop.hbase.{ HTableDescriptor => AHTableDescriptor, HColumnDescriptor => AHColumnDescriptor }
import org.apache.hadoop.hbase.client.{ HBaseAdmin => AHBaseAdmin }

import st.happy_camper.hbase.shell.{ HTableDescriptor, ColumnAttribute }

/**
 * A trait of scala wrapper of class HBaseAdmin.
 * @author ueshin
 */
trait HBaseAdmin {

  val admin: AHBaseAdmin

  /**
   * Returns table list.
   * @return the table list
   */
  def list: Seq[AHTableDescriptor] = {
    admin.listTables.toSeq
  }

  /**
   * Returns a table descriptor.
   * @param tablename the table name
   * @return the table descriptor
   */
  def describe(tablename: String) = {
    admin.getTableDescriptor(tablename)
  }

  /**
   * Creates a new table.
   * @param tablename the table name
   * @param familyName a column family name
   * @param familyNames other column family names
   * @return the new table descriptor
   */
  def create(tablename: String,
             familyName: String,
             familyNames: String*): AHTableDescriptor = {
    create(HTableDescriptor(tablename, familyName, familyNames: _*))
  }

  /**
   * Creates a new table.
   * @param tablename the table name
   * @param family a column family name and its attributes
   * @param families other column family names and their attributes
   * @return the new table descriptor
   */
  def create[A <: ColumnAttribute](tablename: String,
                                   family: (String, Seq[A]),
                                   families: (String, Seq[A])*): AHTableDescriptor = {
    create(HTableDescriptor(tablename, family, families: _*))
  }

  /**
   * Creates a new table.
   * @param tablename the table name
   * @param columnDescriptor a column family descriptor
   * @param columnDescriptors other column family descriptors
   * @return the new table descriptor
   */
  def create(tablename: String,
             columnDescriptor: AHColumnDescriptor,
             columnDescriptors: AHColumnDescriptor*): AHTableDescriptor = {
    create(HTableDescriptor(tablename, columnDescriptor, columnDescriptors: _*))
  }

  /**
   * Creates a new table.
   * @param tableDescriptor the table descriptor
   * @return the new table descriptor
   */
  def create(tableDescriptor: AHTableDescriptor): AHTableDescriptor = {
    create(tableDescriptor, null)
  }

  /**
   * Creates a new table.
   * @param tableDescriptor the table descriptor
   * @param splitKeys the table split keys
   * @return the new table descriptor
   */
  def create(tableDescriptor: AHTableDescriptor,
             splitKeys: Array[Array[Byte]]): AHTableDescriptor = {
    admin.createTable(tableDescriptor, splitKeys)
    tableDescriptor
  }

  /**
   * Creates a new table.
   * @param tableDescriptor the table descriptor
   * @param startKey the table split start key
   * @param endKey the table split end key
   * @param numRegions the number of the regions
   * @return the new table descriptor
   */
  def create(tableDescriptor: AHTableDescriptor,
             startKey: Array[Byte],
             endKey: Array[Byte],
             numRegions: Int): AHTableDescriptor = {
    admin.createTable(tableDescriptor, startKey, endKey, numRegions)
    tableDescriptor
  }

  /**
   * Creates a new table asynchronously.
   * @param tableDescriptor the table descriptor
   * @param splitKeys the table split keys
   * @return the new table descriptor
   */
  def createAsync(tableDescriptor: AHTableDescriptor,
                  splitKeys: Array[Array[Byte]]): AHTableDescriptor = {
    admin.createTableAsync(tableDescriptor, splitKeys)
    tableDescriptor
  }
}
