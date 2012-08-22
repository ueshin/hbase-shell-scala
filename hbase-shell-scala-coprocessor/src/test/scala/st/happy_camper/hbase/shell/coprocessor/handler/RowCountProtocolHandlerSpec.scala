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
package coprocessor.handler

import scala.collection.JavaConversions._

import org.apache.hadoop.hbase.client.coprocessor.Batch
import org.apache.hadoop.hbase.client.HTable
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor
import org.apache.hadoop.hbase.HBaseTestingUtility
import org.apache.hadoop.hbase.HColumnDescriptor
import org.apache.hadoop.hbase.HConstants
import org.apache.hadoop.hbase.HTableDescriptor
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

import st.happy_camper.hbase.shell.coprocessor.protocol.RowCountProtocol

/**
 * A spec for {@link RowCountProtocolHandler}.
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object RowCountProtocolHandlerSpecTest extends RowCountProtocolHandlerSpec

class RowCountProtocolHandlerSpec extends Specification {

  val testingUtility = new HBaseTestingUtility()

  step {
    testingUtility.startMiniCluster
  }

  "RowCountProtocolHandler" should {

    "count 0 rows" in new Context {
      val table = new HTable(testingUtility.getConfiguration(), "test")

      val result = table.coprocessorExec(classOf[RowCountProtocol],
        null, null,
        new Batch.Call[RowCountProtocol, Long]() {
          def call(counter: RowCountProtocol) = {
            counter.count
          }
        }).foldLeft(0L) {
          case (total, (region, count)) =>
            total + count
        }

      result must equalTo(0L)
    }

    "count 10 rows" in new Context {
      val table = new HTable(testingUtility.getConfiguration(), "test")
      table.put(for (i <- 0 until 10) yield {
        val put = new Put(i)
        put.add("family", i, i)
        put
      })

      val result = table.coprocessorExec(classOf[RowCountProtocol],
        null, null,
        new Batch.Call[RowCountProtocol, Long]() {
          def call(counter: RowCountProtocol) = {
            counter.count
          }
        }).foldLeft(0L) {
          case (total, (region, count)) =>
            total + count
        }

      result must equalTo(10L)
    }

    "count 10 rows with key range" in new Context {
      val table = new HTable(testingUtility.getConfiguration(), "test")
      table.put(for (i <- 0 until 10) yield {
        val put = new Put(i)
        put.add("family", i, i)
        put
      })

      val scan = new Scan(HConstants.EMPTY_START_ROW, 5)
      val result = table.coprocessorExec(classOf[RowCountProtocol],
        scan.getStartRow,
        scan.getStopRow,
        new Batch.Call[RowCountProtocol, Long]() {
          def call(counter: RowCountProtocol) = {
            counter.count(scan)
          }
        }).foldLeft(0L) {
          case (total, (region, count)) =>
            total + count
        }

      result must equalTo(5L)
    }
  }

  step {
    testingUtility.shutdownMiniCluster
  }

  trait Context extends BeforeAfter {

    val conf = testingUtility.getConfiguration()

    def before = {
      val table = new HTableDescriptor("test")
      table.addFamily(new HColumnDescriptor("family"))
      table.addCoprocessor(classOf[RowCountEndpoint].getName)
      testingUtility.getHBaseAdmin.createTable(table, Array(1, 2, 3, 4, 5, 6, 7, 8, 9))
    }

    def after = {
      testingUtility.deleteTable("test")
    }
  }
}

class RowCountEndpoint extends BaseEndpointCoprocessor
  with RowCountProtocol with RowCountProtocolHandler
