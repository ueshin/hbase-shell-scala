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

/**
 * A trait of a converter from some type to bytes.
 * @author ueshin
 */
trait ToBytes[A] {

  def bytes(a: A): Array[Byte]
}

/**
 * A companion object of trait {@link ToBytes}.
 * @author ueshin
 */
object ToBytes {

  /**
   * Represents a convert from String.
   * @author ueshin
   */
  implicit object StringToBytes extends ToBytes[String] {
    def bytes(a: String) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from Boolean.
   * @author ueshin
   */
  implicit object BooleanToBytes extends ToBytes[Boolean] {
    def bytes(a: Boolean) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from Long.
   * @author ueshin
   */
  implicit object LongToBytes extends ToBytes[Long] {
    def bytes(a: Long) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from Float.
   * @author ueshin
   */
  implicit object FloatToBytes extends ToBytes[Float] {
    def bytes(a: Float) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from Double.
   * @author ueshin
   */
  implicit object DoubleToBytes extends ToBytes[Double] {
    def bytes(a: Double) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from Int.
   * @author ueshin
   */
  implicit object IntToBytes extends ToBytes[Int] {
    def bytes(a: Int) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from Short.
   * @author ueshin
   */
  implicit object ShortToBytes extends ToBytes[Short] {
    def bytes(a: Short) = Bytes.toBytes(a)
  }

  /**
   * Represents a convert from BigDecimal.
   * @author ueshin
   */
  implicit object BigDecimalToBytes extends ToBytes[BigDecimal] {
    def bytes(a: BigDecimal) = Bytes.toBytes(a.underlying)
  }
}
