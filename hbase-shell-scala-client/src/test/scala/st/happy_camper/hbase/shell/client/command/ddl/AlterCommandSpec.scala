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

import org.apache.hadoop.hbase.HBaseTestingUtility
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

/**
 * A spec for {@link AlterCommand}.
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object AlterCommandSpecTest extends AlterCommandSpec

class AlterCommandSpec extends Specification {

  val testingUtility = new HBaseTestingUtility()

  step {
    testingUtility.startMiniCluster
  }

  "AlterCommand" should {

    "alter table with ColumnAttribute" in new Context {
      import ColumnAttribute._
      table("test").disable()
      table("test").alterFamily("family", BlockCache(false), BlockSize(1),
        BloomFilter(BloomType.ROWCOL), Compression(CompressionType.GZ), InMemory(true),
        ReplicationScope(1), TTL(10), Versions(5), MinVersions(2)).alter()
      table("test").enable()

      val family = table("test").describe.get.getFamily("family")
      family.isBlockCacheEnabled must beFalse
      family.getBlocksize must equalTo(1)
      family.getBloomFilterType must equalTo(BloomType.ROWCOL)
      family.getCompressionType must equalTo(CompressionType.GZ)
      family.isInMemory must beTrue
      family.getScope must equalTo(1)
      family.getTimeToLive must equalTo(10)
      family.getMaxVersions must equalTo(5)
      family.getMinVersions must equalTo(2)
    }
  }

  step {
    testingUtility.shutdownMiniCluster
  }

  trait Context extends BeforeAfter with Client with HBaseAdmin with AlterCommand with EnableCommand with DisableCommand with DescribeCommand {

    val conf = testingUtility.getConfiguration()

    def before = {
      testingUtility.createTable("test", "family")
    }

    def after = {
      admin.close()
      testingUtility.deleteTable("test")
    }
  }
}
