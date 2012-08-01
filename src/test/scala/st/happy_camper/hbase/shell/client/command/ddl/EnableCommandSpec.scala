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
import org.specs2.mutable.{ Specification, BeforeAfter }
import org.specs2.runner.JUnitRunner

/**
 * A test for {@link EnableCommand}
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object EnableCommandSpecTest extends EnableCommandSpec

class EnableCommandSpec extends Specification {

  "EnableCommand" should {

    "check if a table is enabled" in new Context {
      shell.isEnabled("test") must beTrue

      shell.disable("test")
      shell.isEnabled("test") must beFalse
    }

    "enable a table" in new Context {
      shell.disable("test")

      shell.isEnabled("test") must beFalse

      shell.enable("test")
      shell.isEnabled("test") must beTrue
    }
  }

  trait Context extends BeforeAfter {

    val testingUtility = new HBaseTestingUtility()

    val shell = new HBaseAdmin with EnableCommand with DisableCommand {
      val conf = testingUtility.getConfiguration()
    }

    def before = {
      testingUtility.startMiniCluster
      testingUtility.createTable("test", "family")
    }

    def after = {
      testingUtility.shutdownMiniCluster
    }
  }
}
