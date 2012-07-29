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

import org.apache.hadoop.hbase.{ HColumnDescriptor => AHColumnDescriptor }
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm
import org.apache.hadoop.hbase.regionserver.StoreFile

/**
 * @author ueshin
 */
object BloomType {
  val NONE = StoreFile.BloomType.NONE
  val ROW = StoreFile.BloomType.ROW
  val ROWCOL = StoreFile.BloomType.ROWCOL
}

/**
 * @author ueshin
 */
object CompressionType {
  val LZO = Algorithm.LZO
  val GZ = Algorithm.GZ
  val SNAPPY = Algorithm.SNAPPY
  val NONE = Algorithm.NONE
}

sealed trait ColumnAttribute
case class BlockCache(enabled: Boolean) extends ColumnAttribute
case class BlockSize(size: Int) extends ColumnAttribute
case class BloomFilter(bloomType: StoreFile.BloomType) extends ColumnAttribute
case class Compression(compressionType: Algorithm) extends ColumnAttribute
case class InMemory(inMemory: Boolean) extends ColumnAttribute
case class ReplicationScope(scope: Int) extends ColumnAttribute
case class TTL(ttl: Int) extends ColumnAttribute
case class Versions(maxVersions: Int) extends ColumnAttribute

/**
 * @author ueshin
 */
object HColumnDescriptor {

  /**
   * @param descriptor
   * @return
   */
  def apply[A <: ColumnAttribute](descriptor: (String, Seq[A])): AHColumnDescriptor = {
    val columnDescriptor = new AHColumnDescriptor(descriptor._1)
    descriptor._2.foreach {
      case BlockCache(enabled)          => columnDescriptor.setBlockCacheEnabled(enabled)
      case BlockSize(size)              => columnDescriptor.setBlocksize(size)
      case BloomFilter(bloomType)       => columnDescriptor.setBloomFilterType(bloomType)
      case Compression(compressionType) => columnDescriptor.setCompressionType(compressionType)
      case InMemory(inMemory)           => columnDescriptor.setInMemory(inMemory)
      case ReplicationScope(scope)      => columnDescriptor.setScope(scope)
      case TTL(ttl)                     => columnDescriptor.setTimeToLive(ttl)
      case Versions(maxVersions)        => columnDescriptor.setMaxVersions(maxVersions)
    }
    columnDescriptor
  }
}
