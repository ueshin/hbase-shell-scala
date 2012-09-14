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
object BytesAsSpecTest extends BytesAsSpec

class BytesAsSpec extends Specification {

  "BytesAs" should {

    "treat byte array as String" in {
      Bytes.toBytes("foo").as[String] must equalTo("foo")
      Bytes.toBytes("baa").as[String] must equalTo("baa")
    }

    "treat byte array as Boolean" in {
      Bytes.toBytes(true).as[Boolean] must equalTo(true)
      Bytes.toBytes(false).as[Boolean] must equalTo(false)
    }

    "treat byte array as Long" in {
      Bytes.toBytes(1L).as[Long] must equalTo(1L)
      Bytes.toBytes(10L).as[Long] must equalTo(10L)
    }

    "treat byte array as Float" in {
      Bytes.toBytes(1.0F).as[Float] must equalTo(1.0F)
      Bytes.toBytes(10.0F).as[Float] must equalTo(10.0F)
    }

    "treat byte array as Double" in {
      Bytes.toBytes(1.0).as[Double] must equalTo(1.0)
      Bytes.toBytes(10.0).as[Double] must equalTo(10.0)
    }

    "treat byte array as Int" in {
      Bytes.toBytes(1).as[Int] must equalTo(1)
      Bytes.toBytes(10).as[Int] must equalTo(10)
    }

    "treat byte array as Short" in {
      Bytes.toBytes(1.toShort).as[Short] must equalTo(1.toShort)
      Bytes.toBytes(10.toShort).as[Short] must equalTo(10.toShort)
    }

    "treat byte array as Byte" in {
      Array(1.toByte).as[Byte] must equalTo(1.toByte)
      Array(10.toByte).as[Byte] must equalTo(10.toByte)
    }

    "treat byte array as BigDecimal" in {
      Bytes.toBytes(BigDecimal(1).underlying).as[BigDecimal] must equalTo(BigDecimal(1))
      Bytes.toBytes(BigDecimal(10).underlying).as[BigDecimal] must equalTo(BigDecimal(10))
    }

    "treat byte array as Test" in {

      case class Test(i: Int, s: String)

      implicit object BytesAsTest extends BytesAs[Test] {
        override def as(bytes: Array[Byte]) = Test(
          Bytes.toInt(bytes),
          Bytes.toString(bytes, Bytes.SIZEOF_INT, bytes.length - Bytes.SIZEOF_INT))
      }

      Bytes.add(Bytes.toBytes(1), Bytes.toBytes("foo")).as[Test] must equalTo(Test(1, "foo"))
      Bytes.add(Bytes.toBytes(10), Bytes.toBytes("baa")).as[Test] must equalTo(Test(10, "baa"))
    }
  }
}
