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

import scala.collection.JavaConversions.mapAsScalaMap

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HColumnDescriptor
import org.apache.hadoop.hbase.HConstants
import org.apache.hadoop.hbase.client.{ HTable => AHTable }
import org.apache.hadoop.hbase.client.coprocessor.Batch
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol
import org.apache.hadoop.hbase.regionserver.StoreFile

/**
 * A client package object.
 * @author ueshin
 */
package object client {

  /**
   * Represents bloom filter types.
   * @author ueshin
   */
  object BloomType {
    val NONE = StoreFile.BloomType.NONE
    val ROW = StoreFile.BloomType.ROW
    val ROWCOL = StoreFile.BloomType.ROWCOL
  }

  /**
   * Represents compression types.
   * @author ueshin
   */
  object CompressionType {
    val LZO = Algorithm.LZO
    val GZ = Algorithm.GZ
    val SNAPPY = Algorithm.SNAPPY
    val NONE = Algorithm.NONE
  }

  /**
   * Represents column attributes.
   * @author ueshin
   */
  sealed trait ColumnAttribute

  object ColumnAttribute {
    case class BlockCache(enabled: Boolean) extends ColumnAttribute
    case class BlockSize(size: Int) extends ColumnAttribute
    case class BloomFilter(bloomType: StoreFile.BloomType) extends ColumnAttribute
    case class Compression(compressionType: Algorithm) extends ColumnAttribute
    case class InMemory(inMemory: Boolean) extends ColumnAttribute
    case class ReplicationScope(scope: Int) extends ColumnAttribute
    case class TTL(ttl: Int) extends ColumnAttribute
    case class Versions(maxVersions: Int) extends ColumnAttribute
    case class MinVersions(minVersions: Int) extends ColumnAttribute
  }

  /**
   * Represents a table used by the client.
   * @author ueshin
   */
  protected[client] class Table(val tablename: Array[Byte])

  protected[client] def modifyColumnAttributes(column: HColumnDescriptor, attributes: ColumnAttribute*) = {
    for (attribute <- attributes) {
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
    }
  }

  /**
   * Calls a coprocessor endpoint.
   * @param tablename the target table name
   * @param startKey the start key of the range of the target row keys
   * @param endKey the end key of the range of the target row keys
   * @param call coprocessor call block
   * @param conf implicit configuration
   * @param manifest implicit manifest
   * @return the result map (region: Array[Byte] -> result: R)
   */
  def coprocessorCall[P <: CoprocessorProtocol, R](
    tablename: Array[Byte],
    startKey: Array[Byte] = HConstants.EMPTY_START_ROW,
    endKey: Array[Byte] = HConstants.EMPTY_END_ROW)(call: P => R)(implicit conf: Configuration, manifest: Manifest[P]): Map[Array[Byte], R] = {
    val batchcall = call
    val results = new AHTable(conf, tablename).coprocessorExec[P, R](
      manifest.erasure.asInstanceOf[Class[P]],
      startKey,
      endKey,
      new Batch.Call[P, R]() {
        override def call(p: P): R = batchcall(p)
      })
    results.toMap
  }

  /**
   * Executes a coprocessor endpoint.
   * @param tablename the target table name
   * @param startKey the start key of the range of the target row keys
   * @param endKey the end key of the range of the target row keys
   * @param call coprocessor call block
   * @param callback coprocessor callback
   * @param conf implicit configuration
   * @param manifest implicit manifest
   */
  def coprocessorExec[P <: CoprocessorProtocol, R](
    tablename: Array[Byte],
    startKey: Array[Byte] = HConstants.EMPTY_START_ROW,
    endKey: Array[Byte] = HConstants.EMPTY_END_ROW,
    call: P => R,
    callback: (Array[Byte], Array[Byte], R) => Unit)(implicit conf: Configuration, manifest: Manifest[P]): Unit = {
    val batchcall = call
    new AHTable(conf, tablename).coprocessorExec[P, R](
      manifest.erasure.asInstanceOf[Class[P]],
      startKey,
      endKey,
      new Batch.Call[P, R]() {
        override def call(p: P): R = batchcall(p)
      }, new Batch.Callback[R]() {
        override def update(region: Array[Byte], row: Array[Byte], value: R) = callback(region, row, value)
      })
  }
}
