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

import org.apache.hadoop.hbase.HColumnDescriptor

/**
 * A client package object.
 * @author ueshin
 */
package object client {

  /**
   * Represents a table used by the client.
   * @author ueshin
   */
  protected[client] class Table(val tablename: Array[Byte])

  /**
   * @param descriptor
   * @param attributes
   * @return
   */
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
}
