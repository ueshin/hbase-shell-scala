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
 * A spec for {@link DescribeCommand}.
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object DescribeCommandSpecTest extends DescribeCommandSpec

class DescribeCommandSpec extends Specification {

  sequential

  val testingUtility = new HBaseTestingUtility()

  step {
    testingUtility.startMiniCluster
  }

  "DescribeCommand" should {

    "check if the table exists" in new Context {
      table("test").exists must beTrue
      table("test1").exists must beFalse
    }

    "describe table" in new Context {
      val t = table("test").describe
      t.map(_.getNameAsString) must beSome("test")
    }
  }

  step {
    testingUtility.shutdownMiniCluster
  }

  trait Context extends BeforeAfter with Client with HBaseAdmin with DescribeCommand {

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
