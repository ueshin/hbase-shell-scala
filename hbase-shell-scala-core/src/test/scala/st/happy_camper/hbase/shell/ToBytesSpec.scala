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

import org.apache.hadoop.hbase.util.Bytes
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

/**
 * A spec for {@link ToBytes}.
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object ToBytesSpecTest extends ToBytesSpec

class ToBytesSpec extends Specification {

  "ToBytes" should {

    "convert String to byte array" in {
      "foo".bytes must equalTo(Bytes.toBytes("foo"))
      "baa".bytes must equalTo(Bytes.toBytes("baa"))
    }

    "convert Boolean to byte array" in {
      true.bytes must equalTo(Bytes.toBytes(true))
      false.bytes must equalTo(Bytes.toBytes(false))
    }

    "convert Long to byte array" in {
      1L.bytes must equalTo(Bytes.toBytes(1L))
      10L.bytes must equalTo(Bytes.toBytes(10L))
    }

    "convert Float to byte array" in {
      1.0F.bytes must equalTo(Bytes.toBytes(1.0F))
      10.0F.bytes must equalTo(Bytes.toBytes(10.0F))
    }

    "convert Double to byte array" in {
      1.0.bytes must equalTo(Bytes.toBytes(1.0))
      10.0.bytes must equalTo(Bytes.toBytes(10.0))
    }

    "convert Int to byte array" in {
      1.bytes must equalTo(Bytes.toBytes(1))
      10.bytes must equalTo(Bytes.toBytes(10))
    }

    "convert Short to byte array" in {
      1.toShort.bytes must equalTo(Bytes.toBytes(1.toShort))
      10.toShort.bytes must equalTo(Bytes.toBytes(10.toShort))
    }

    "convert Byte to byte array" in {
      1.toByte.bytes must equalTo(Array(1.toByte))
      10.toByte.bytes must equalTo(Array(10.toByte))
    }

    "convert BigDecimal to byte array" in {
      BigDecimal(1).bytes must equalTo(Bytes.toBytes(BigDecimal(1).underlying))
      BigDecimal(10).bytes must equalTo(Bytes.toBytes(BigDecimal(10).underlying))
    }

    "convert Test to byte array" in {

      case class Test(i: Int, s: String)

      implicit object TestToBytes extends ToBytes[Test] {
        override def bytes(a: Test) = Bytes.add(Bytes.toBytes(a.i), Bytes.toBytes(a.s))
      }

      Test(1, "foo").bytes must equalTo(Bytes.add(Bytes.toBytes(1), Bytes.toBytes("foo")))
      Test(10, "baa").bytes must equalTo(Bytes.add(Bytes.toBytes(10), Bytes.toBytes("baa")))
    }
  }
}
