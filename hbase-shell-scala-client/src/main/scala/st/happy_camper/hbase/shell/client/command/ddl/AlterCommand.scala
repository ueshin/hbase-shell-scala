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

import scala.collection.JavaConversions.mapAsJavaMap

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.Coprocessor
import org.apache.hadoop.hbase.HColumnDescriptor
import org.apache.hadoop.hbase.HTableDescriptor
import org.apache.hadoop.hbase.client.{ HBaseAdmin => AHBaseAdmin }

/**
 * A trait to handle alter commands.
 * @author ueshin
 */
trait AlterCommand {
  self: Client with HBaseAdmin =>

  implicit def alteringTable(table: Table) = AlterCommand.AlteringTable(table.tablename)
}

/**
 * A companion object of trait {@link AlterCommand}.
 * @author ueshin
 */
object AlterCommand {

  /**
   * Represents a altering table.
   * @author ueshin
   */
  case class AlteringTable(
      override val tablename: Array[Byte],
      alterFamilies: Map[Array[Byte], Seq[ColumnAttribute]] = Map.empty,
      removeFamilies: Set[Array[Byte]] = Set.empty,
      alterCoprocessors: Map[String, (Path, Int, Map[String, String])] = Map.empty,
      removeCoprocessors: Set[String] = Set.empty)(implicit admin: AHBaseAdmin) extends Table(tablename) {

    /**
     * Alters a column family.
     * @param name the column family name
     * @param attributes the column family's attributes
     * @return the copy of this having the altered column family
     */
    def alterFamily(name: Array[Byte], attributes: ColumnAttribute*): AlteringTable = {
      copy(
        alterFamilies = alterFamilies + (name -> attributes),
        removeFamilies = removeFamilies - name)
    }

    /**
     * Removes a column family.
     * @param name the column family name
     * @return the copy of this not having the removed column family
     */
    def removeFamily(name: Array[Byte]): AlteringTable = {
      copy(
        alterFamilies = alterFamilies - name,
        removeFamilies = removeFamilies + name)
    }

    /**
     * Alters a coprocessor.
     * @param className the coprocessor class name
     * @return the copy of this having the altered coprocessor
     */
    def alterCoprocessor(className: String): AlteringTable = {
      alterCoprocessor(className, null, Coprocessor.PRIORITY_USER)
    }

    /**
     * Alters a coprocessor.
     * @param className the coprocessor class name
     * @param jarFilePath the Path of the jar file. If it's null, the class will be loaded from default classloader.
     * @param priority the priority
     * @param kvs arbitrary key-value parameter pairs passed into the coprocessor
     * @return the copy of this having the modified coprocessor
     */
    def alterCoprocessor(className: String, jarFilePath: Path,
                         priority: Int, kvs: (String, String)*): AlteringTable = {
      copy(
        alterCoprocessors = alterCoprocessors + (className -> (jarFilePath, priority, kvs.toMap)),
        removeCoprocessors = removeCoprocessors - className)
    }

    /**
     * Removes a coprocessor.
     * @param className the coprocessor class name
     * @return the copy of this having the removed coprocessor
     */
    def removeCoprocessor(className: String): AlteringTable = {
      copy(
        alterCoprocessors = alterCoprocessors - className,
        removeCoprocessors = removeCoprocessors + className)
    }

    /**
     * Alters a table.
     * @return the descriptor of the altered table
     */
    def alter(): HTableDescriptor = {
      val descriptor = admin.getTableDescriptor(tablename)
      for (name <- removeFamilies) {
        descriptor.removeFamily(name)
      }
      for ((name, attributes) <- alterFamilies) {
        val column = Option(descriptor.getFamily(name)).getOrElse(new HColumnDescriptor(name))
        modifyColumnAttributes(column, attributes: _*)
        descriptor.addFamily(column)
      }
      for (className <- removeCoprocessors) {
        descriptor.removeCoprocessor(className)
      }
      for ((className, (jarFilePath, priority, kvs)) <- alterCoprocessors) {
        descriptor.addCoprocessor(className, jarFilePath, priority, kvs)
      }
      admin.modifyTable(tablename, descriptor)
      admin.getTableDescriptor(tablename)
    }
  }
}
