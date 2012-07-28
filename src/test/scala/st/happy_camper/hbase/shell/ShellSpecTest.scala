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
import org.specs2.mutable.{ Specification, BeforeAfter }
import org.specs2.runner.JUnitRunner

/**
 * @author ueshin
 *
 */
@RunWith(classOf[JUnitRunner])
object ShellSpecTest extends ShellSpec

class ShellSpec extends Specification {

  "Shell" should {

    trait Context extends BeforeAfter {

      val testingUtility = new HBaseTestingUtility()

      def before = {
        testingUtility.startMiniCluster
        testingUtility.createTable("test", "family")
      }

      def after = {
        testingUtility.shutdownMiniCluster
      }
    }

    "list tables" in new Context {
      val shell = new Shell(testingUtility.getConfiguration())
      val tables = shell.list
      tables.size must equalTo(1)
      tables(0).getNameAsString() must equalTo("test")
    }

    "describe table" in new Context {
      val shell = new Shell(testingUtility.getConfiguration())
      val table = shell.describe("test")
      table.getNameAsString() must equalTo("test")
    }

    "create table" in new Context {
      val shell = new Shell(testingUtility.getConfiguration())
      shell.create("test1", "family")
      testingUtility.getHBaseAdmin().tableExists("test1") must beTrue
    }

    "create table with ColumnAttribute" in new Context {
      val shell = new Shell(testingUtility.getConfiguration())
      val table = shell.create("test1", "family" -> (BlockCache(false) :: BlockSize(1) ::
        BloomFilter(BloomType.ROWCOL) :: Compression(CompressionType.GZ) :: InMemory(true) ::
        ReplicationScope(1) :: TTL(10) :: Versions(5) :: Nil))
      testingUtility.getHBaseAdmin().tableExists("test1") must beTrue

      val family = table.getFamily("family")
      family.isBlockCacheEnabled() must beFalse
      family.getBlocksize() must equalTo(1)
      family.getBloomFilterType() must equalTo(BloomType.ROWCOL)
      family.getCompressionType() must equalTo(CompressionType.GZ)
      family.isInMemory() must beTrue
      family.getScope() must equalTo(1)
      family.getTimeToLive() must equalTo(10)
      family.getMaxVersions() must equalTo(5)
    }
  }
}
