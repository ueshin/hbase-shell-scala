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
trait BytesTo[A] {

  /**
   * Converts byte array to some type.
   * @param bytes the byte array
   * @return the converted value
   */
  def to(bytes: Array[Byte]): A
}

/**
 * A companion object of trait {@link BytesTo}.
 * @author ueshin
 */
object BytesTo {

  /**
   * Represents a converter to String.
   * @author ueshin
   */
  implicit object BytesToString extends BytesTo[String] {
    override def to(bytes: Array[Byte]) = Bytes.toString(bytes)
  }

  /**
   * Represents a converter to Boolean.
   * @author ueshin
   */
  implicit object BytesToBoolean extends BytesTo[Boolean] {
    override def to(bytes: Array[Byte]) = Bytes.toBoolean(bytes)
  }

  /**
   * Represents a converter to Long.
   * @author ueshin
   */
  implicit object BytesToLong extends BytesTo[Long] {
    override def to(bytes: Array[Byte]) = Bytes.toLong(bytes)
  }

  /**
   * Represents a converter to Float.
   * @author ueshin
   */
  implicit object BytesToFloat extends BytesTo[Float] {
    override def to(bytes: Array[Byte]) = Bytes.toFloat(bytes)
  }

  /**
   * Represents a converter to Double.
   * @author ueshin
   */
  implicit object BytesToDouble extends BytesTo[Double] {
    override def to(bytes: Array[Byte]) = Bytes.toDouble(bytes)
  }

  /**
   * Represents a converter to Int.
   * @author ueshin
   */
  implicit object BytesToInt extends BytesTo[Int] {
    override def to(bytes: Array[Byte]) = Bytes.toInt(bytes)
  }

  /**
   * Represents a converter to Short.
   * @author ueshin
   */
  implicit object BytesToShort extends BytesTo[Short] {
    override def to(bytes: Array[Byte]) = Bytes.toShort(bytes)
  }

  /**
   * Represents a converter to BigDecimal.
   * @author ueshin
   */
  implicit object BytesToBigDecimal extends BytesTo[BigDecimal] {
    override def to(bytes: Array[Byte]) = BigDecimal(Bytes.toBigDecimal(bytes))
  }
}
