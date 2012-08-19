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
import org.junit.runner.RunWith
import org.specs2.mutable.BeforeAfter
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
 * A spec for {@link DropCommand}
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object DropCommandSpecTest extends DropCommandSpec

class DropCommandSpec extends Specification {

  val testingUtility = new HBaseTestingUtility()

  step {
    testingUtility.startMiniCluster
  }

  "DropCommand" should {

    "drop table" in new Context {
      table("test").exists must beTrue

      table("test").disable.drop
      table("test").exists must beFalse
    }
  }

  step {
    testingUtility.shutdownMiniCluster
  }

  trait Context extends BeforeAfter with Client with HBaseAdmin with DropCommand with DisableCommand with DescribeCommand {

    val conf = testingUtility.getConfiguration()

    def before = {
      testingUtility.createTable("test", "family")
    }

    def after = {
      admin.close()
    }
  }
}
