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
package command.dml

import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor
import org.apache.hadoop.hbase.HBaseTestingUtility
import org.apache.hadoop.hbase.HColumnDescriptor
import org.apache.hadoop.hbase.HTableDescriptor
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

import st.happy_camper.hbase.shell.coprocessor.protocol.RowCountProtocol

/**
 * A spec for {@link CountCommand}.
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object CountCommandSpecTest extends CountCommandSpec

class CountCommandSpec extends Specification {

  sequential

  val testingUtility = new HBaseTestingUtility()

  step {
    testingUtility.startMiniCluster
  }

  "CountCommand" should {

    "count rows" in new Context {
      val result = table("test").count
      result must equalTo(10L)
    }
  }

  step {
    testingUtility.shutdownMiniCluster
  }

  trait Context extends BeforeAfter with Client with HTable with CountCommand {

    val conf = testingUtility.getConfiguration()

    def before = {
      val table = new HTableDescriptor("test")
      table.addFamily(new HColumnDescriptor("family"))
      table.addCoprocessor(classOf[RowCountEndpoint].getName)
      testingUtility.getHBaseAdmin.createTable(table)
    }

    def after = {
      testingUtility.deleteTable("test")
    }
  }
}

class RowCountEndpoint extends BaseEndpointCoprocessor
    with RowCountProtocol {
  override def count(implicit scan: Scan): Long = 10L
}
