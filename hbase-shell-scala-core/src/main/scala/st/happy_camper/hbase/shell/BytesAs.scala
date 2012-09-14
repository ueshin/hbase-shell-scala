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
 * A trait of a converter from bytes to some value.
 * @author ueshin
 */
trait BytesAs[A] {

  /**
   * Treats byte array as some type.
   * @param bytes the byte array
   * @return the converted value
   */
  def as(bytes: Array[Byte]): A
}

/**
 * A companion object of trait {@link BytesAs}.
 * @author ueshin
 */
object BytesAs {

  /**
   * Represents a converter to String.
   * @author ueshin
   */
  implicit object BytesAsString extends BytesAs[String] {
    override def as(bytes: Array[Byte]) = Bytes.toString(bytes)
  }

  /**
   * Represents a converter to Boolean.
   * @author ueshin
   */
  implicit object BytesAsBoolean extends BytesAs[Boolean] {
    override def as(bytes: Array[Byte]) = Bytes.toBoolean(bytes)
  }

  /**
   * Represents a converter to Long.
   * @author ueshin
   */
  implicit object BytesAsLong extends BytesAs[Long] {
    override def as(bytes: Array[Byte]) = Bytes.toLong(bytes)
  }

  /**
   * Represents a converter to Float.
   * @author ueshin
   */
  implicit object BytesAsFloat extends BytesAs[Float] {
    override def as(bytes: Array[Byte]) = Bytes.toFloat(bytes)
  }

  /**
   * Represents a converter to Double.
   * @author ueshin
   */
  implicit object BytesAsDouble extends BytesAs[Double] {
    override def as(bytes: Array[Byte]) = Bytes.toDouble(bytes)
  }

  /**
   * Represents a converter to Int.
   * @author ueshin
   */
  implicit object BytesAsInt extends BytesAs[Int] {
    override def as(bytes: Array[Byte]) = Bytes.toInt(bytes)
  }

  /**
   * Represents a converter to Byte.
   * @author ueshin
   */
  implicit object BytesAsByte extends BytesAs[Byte] {
    override def as(bytes: Array[Byte]) = bytes(0)
  }

  /**
   * Represents a converter to Short.
   * @author ueshin
   */
  implicit object BytesAsShort extends BytesAs[Short] {
    override def as(bytes: Array[Byte]) = Bytes.toShort(bytes)
  }

  /**
   * Represents a converter to BigDecimal.
   * @author ueshin
   */
  implicit object BytesAsBigDecimal extends BytesAs[BigDecimal] {
    override def as(bytes: Array[Byte]) = BigDecimal(Bytes.toBigDecimal(bytes))
  }
}
