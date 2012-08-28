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
 * A spec for {@link BytesTo}.
 * @author ueshin
 */
@RunWith(classOf[JUnitRunner])
object BytesToSpecTest extends BytesToSpec

class BytesToSpec extends Specification {

  "BytesTo" should {

    "convert byte array to String" in {
      Bytes.toBytes("foo").to[String] must equalTo("foo")
      Bytes.toBytes("baa").to[String] must equalTo("baa")
    }

    "convert byte array to Boolean" in {
      Bytes.toBytes(true).to[Boolean] must equalTo(true)
      Bytes.toBytes(false).to[Boolean] must equalTo(false)
    }

    "convert byte array to Long" in {
      Bytes.toBytes(1L).to[Long] must equalTo(1L)
      Bytes.toBytes(10L).to[Long] must equalTo(10L)
    }

    "convert byte array to Float" in {
      Bytes.toBytes(1.0F).to[Float] must equalTo(1.0F)
      Bytes.toBytes(10.0F).to[Float] must equalTo(10.0F)
    }

    "convert byte array to Double" in {
      Bytes.toBytes(1.0).to[Double] must equalTo(1.0)
      Bytes.toBytes(10.0).to[Double] must equalTo(10.0)
    }

    "convert byte array to Int" in {
      Bytes.toBytes(1).to[Int] must equalTo(1)
      Bytes.toBytes(10).to[Int] must equalTo(10)
    }

    "convert byte array to Short" in {
      Bytes.toBytes(1.toShort).to[Short] must equalTo(1.toShort)
      Bytes.toBytes(10.toShort).to[Short] must equalTo(10.toShort)
    }

    "convert byte array to Byte" in {
      Array(1.toByte).to[Byte] must equalTo(1.toByte)
      Array(10.toByte).to[Byte] must equalTo(10.toByte)
    }

    "convert byte array to BigDecimal" in {
      Bytes.toBytes(BigDecimal(1).underlying).to[BigDecimal] must equalTo(BigDecimal(1))
      Bytes.toBytes(BigDecimal(10).underlying).to[BigDecimal] must equalTo(BigDecimal(10))
    }

    "convert byte array to Test" in {

      case class Test(i: Int, s: String)

      implicit object BytesToTest extends BytesTo[Test] {
        override def to(bytes: Array[Byte]) = Test(
          Bytes.toInt(bytes),
          Bytes.toString(bytes, Bytes.SIZEOF_INT, bytes.length - Bytes.SIZEOF_INT))
      }

      Bytes.add(Bytes.toBytes(1), Bytes.toBytes("foo")).to[Test] must equalTo(Test(1, "foo"))
      Bytes.add(Bytes.toBytes(10), Bytes.toBytes("baa")).to[Test] must equalTo(Test(10, "baa"))
    }
  }
}
