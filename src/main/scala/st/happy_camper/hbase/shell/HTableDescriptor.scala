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

import org.apache.hadoop.hbase.{ HTableDescriptor => AHTableDescriptor, HColumnDescriptor => AHColumnDescriptor }

/**
 * Represents scala wrapper of class HTableDescriptor
 * @author ueshin
 */
object HTableDescriptor {

  /**
   * Returns new instance of HTableDescriptor.
   * @param tablename the table name
   * @param familyName a column family name
   * @param familyNames other column family names
   * @return the new instance of HTableDescriptor
   */
  def apply(tablename: String,
            familyName: String,
            familyNames: String*): AHTableDescriptor = {
    apply(tablename, familyName -> Nil, familyNames.map(_ -> Nil): _*)
  }

  /**
   * Returns new instance of HTableDescriptor.
   * @param tablename the table name
   * @param family a column family name and its attributes
   * @param families other column family names and their attributes
   * @return the new instance of HTableDescriptor
   */
  def apply[A <: ColumnAttribute](tablename: String,
                                  family: (String, Seq[A]),
                                  families: (String, Seq[A])*): AHTableDescriptor = {
    apply(tablename,
      HColumnDescriptor(family._1, family._2: _*),
      families.map { family => HColumnDescriptor(family._1, family._2: _*) }: _*)
  }

  /**
   * Returns new instance of HTableDescriptor.
   * @param tablename the table name
   * @param columnDescriptor a column family descriptor
   * @param columnDescriptors other column family descriptors
   * @return the new instance of HTableDescriptor
   */
  def apply(tablename: String,
            columnDescriptor: AHColumnDescriptor,
            columnDescriptors: AHColumnDescriptor*): AHTableDescriptor = {
    val tableDescriptor = new AHTableDescriptor(tablename)
    (columnDescriptor :: columnDescriptors.toList).foreach { descriptor =>
      tableDescriptor.addFamily(descriptor)
    }
    tableDescriptor
  }
}
