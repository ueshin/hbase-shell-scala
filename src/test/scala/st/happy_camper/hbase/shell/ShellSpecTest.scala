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

import org.apache.hadoop.hbase.HBaseTestingUtility
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfterEach
import org.scalatest.Spec
import org.scalatest.junit.JUnitRunner

import util.Bytes.toBytes

/**
 * @author ueshin
 *
 */
@RunWith(classOf[JUnitRunner])
class ShellSpecTest extends Spec with BeforeAndAfterAll with BeforeAndAfterEach {

  val testingUtility = new HBaseTestingUtility()

  override def beforeAll {
    testingUtility.startMiniCluster
  }

  override def afterAll {
    testingUtility.shutdownMiniCluster
  }

  override def beforeEach {
    testingUtility.createTable("test", "family")
  }

  override def afterEach {
    testingUtility.getHBaseAdmin.listTables.foreach { table =>
      testingUtility.deleteTable(table.getName())
    }
  }

  describe("Shell") {

    it("should list tables") {
      val shell = new Shell(testingUtility.getConfiguration())
      val tables = shell.list
      assert(tables.size == 1)
      assert(tables(0).getNameAsString() == "test")
    }

    it("should describe table") {
      val shell = new Shell(testingUtility.getConfiguration())
      val table = shell.describe("test")
      assert(table.getNameAsString() == "test")
    }

    it("should create table") {
      val shell = new Shell(testingUtility.getConfiguration())
      shell.create("test1", "family")
      assert(testingUtility.getHBaseAdmin().tableExists("test1"))
    }

    it("should create table with ColumnAttribute") {
      val shell = new Shell(testingUtility.getConfiguration())
      val table = shell.create("test1", "family" -> (BlockCache(false) :: BlockSize(1) ::
        BloomFilter(BloomType.ROWCOL) :: Compression(CompressionType.GZ) :: InMemory(true) ::
        ReplicationScope(1) :: TTL(10) :: Versions(5) :: Nil))
      assert(testingUtility.getHBaseAdmin().tableExists("test1"))
      val family = table.getFamily("family")
      assert(!family.isBlockCacheEnabled())
      assert(family.getBlocksize() == 1)
      assert(family.getBloomFilterType() == BloomType.ROWCOL)
      assert(family.getCompressionType() == CompressionType.GZ)
      assert(family.isInMemory())
      assert(family.getScope() == 1)
      assert(family.getTimeToLive() == 10)
      assert(family.getMaxVersions() == 5)
    }
  }
}
