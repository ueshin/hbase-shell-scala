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
 * A trait to handle create commands.
 * @author ueshin
 */
trait CreateCommand {
  self: HBaseAdmin =>

  implicit def creatingTable(table: Table) = CreateCommand.CreatingTable(table.tablename)
}

/**
 * A companion object of trait {@link CreateCommand}.
 * @author ueshin
 */
object CreateCommand {

  private[CreateCommand] sealed trait HasColumnFamily

  /**
   * Represents a describing table.
   * @param <HCF> the type of the evidence if this has column families or not
   * @author ueshin
   */
  case class CreatingTable[HCF](
      override val tablename: Array[Byte],
      families: Map[Array[Byte], Seq[ColumnAttribute]] = Map.empty,
      coprocessors: List[(String, Path, Int, Map[String, String])] = Nil) extends Table(tablename) {

    /**
     * Adds a column family.
     * @param name the column family name
     * @param attributes the column family's attributes
     * @return the copy of this having the column family
     */
    def addFamily(name: Array[Byte], attributes: ColumnAttribute*): CreatingTable[HasColumnFamily] = {
      copy(families = families + (name -> attributes))
    }

    /**
     * Adds a coprocessor.
     * @param className the coprocessor class name
     * @return the copy of this having the coprocessor
     */
    def addCoprocessor(className: String): CreatingTable[HCF] = {
      addCoprocessor(className, null, Coprocessor.PRIORITY_USER)
    }

    /**
     * Adds a coprocessor.
     * @param className the coprocessor class name
     * @param jarFilePath the Path of the jar file. If it's null, the class will be loaded from default classloader.
     * @param priority the priority
     * @param kvs arbitrary key-value parameter pairs passed into the coprocessor
     * @return the copy of this having the coprocessor
     */
    def addCoprocessor(className: String, jarFilePath: Path,
                       priority: Int, kvs: (String, String)*): CreatingTable[HCF] = {
      copy(coprocessors = coprocessors :+ (className, jarFilePath, priority, kvs.toMap))
    }

    /**
     * Creates a new table.
     * @param admin implicit {@link AHBaseAdmin} instance
     * @param ev implicit evidence instance
     * @return the descriptor of the created table
     */
    def create()(implicit admin: AHBaseAdmin, ev: HCF =:= HasColumnFamily) = {
      val descriptor = toHTableDescriptor
      admin.createTable(descriptor)
      descriptor
    }

    /**
     * Creates a new table with pre-split region keys.
     * @param splitKeys the split keys
     * @param admin implicit {@link AHBaseAdmin} instance
     * @param ev implicit evidence instance
     * @return the descriptor of the created table
     */
    def create(splitKeys: Array[Array[Byte]])(implicit admin: AHBaseAdmin, ev: HCF =:= HasColumnFamily) = {
      val descriptor = toHTableDescriptor
      admin.createTable(descriptor, splitKeys)
      descriptor
    }

    /**
     * Creates a new table with pre-split region key range.
     * @param startKey beginning of key range
     * @param endKey end of key range
     * @param numRegions the total number of regions to create
     * @param admin implicit {@link AHBaseAdmin} instance
     * @param ev implicit evidence instance
     * @return the descriptor of the created table
     */
    def create(startKey: Array[Byte], endKey: Array[Byte], numRegions: Int)(implicit admin: AHBaseAdmin, ev: HCF =:= HasColumnFamily) = {
      val descriptor = toHTableDescriptor
      admin.createTable(descriptor, startKey, endKey, numRegions)
      descriptor
    }

    /**
     * @param splitKeys
     * @param admin implicit {@link AHBaseAdmin} instance
     * @param ev implicit evidence instance
     * @return the descriptor of the created table
     */
    def createAsync(splitKeys: Array[Array[Byte]])(implicit admin: AHBaseAdmin, ev: HCF =:= HasColumnFamily) = {
      val descriptor = toHTableDescriptor
      admin.createTableAsync(descriptor, splitKeys)
      descriptor
    }

    private def toHTableDescriptor()(implicit ev: HCF =:= HasColumnFamily) = {
      val tableDescriptor = new HTableDescriptor(tablename)
      for ((name, attributes) <- families) {
        tableDescriptor.addFamily((new HColumnDescriptor(name) /: attributes) {
          (column, attribute) =>
            import ColumnAttribute._
            attribute match {
              case BlockCache(enabled)          => column.setBlockCacheEnabled(enabled)
              case BlockSize(size)              => column.setBlocksize(size)
              case BloomFilter(bloomType)       => column.setBloomFilterType(bloomType)
              case Compression(compressionType) => column.setCompressionType(compressionType)
              case InMemory(inMemory)           => column.setInMemory(inMemory)
              case ReplicationScope(scope)      => column.setScope(scope)
              case TTL(ttl)                     => column.setTimeToLive(ttl)
              case Versions(maxVersions)        => column.setMaxVersions(maxVersions)
              case MinVersions(minVersions)     => column.setMinVersions(minVersions)
            }
            column
        })
      }
      for ((className, jarFilePath, priority, kvs) <- coprocessors) {
        tableDescriptor.addCoprocessor(className, jarFilePath, priority, kvs)
      }
      tableDescriptor
    }
  }
}
