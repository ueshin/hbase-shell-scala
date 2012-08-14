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
package st.happy_camper.hbase

import org.apache.hadoop.hbase.util.Bytes

/**
 * A shell package object.
 * @author ueshin
 */
package object shell {

  /**
   * Represents an augmented byte array.
   * @author ueshin
   */
  class AugmentByteArray(bytes: Array[Byte]) {

    /**
     * Returns the value as String.
     * @return the String value
     */
    def asString() = Bytes.toString(bytes)

    /**
     * Returns the value as Boolean.
     * @return the Boolean value
     */
    def asBoolean() = Bytes.toBoolean(bytes)

    /**
     * Returns the value as Long.
     * @return the Long value
     */
    def asLong() = Bytes.toLong(bytes)

    /**
     * Returns the value as Float.
     * @return the Float value
     */
    def asFloat() = Bytes.toFloat(bytes)

    /**
     * Returns the value as Double.
     * @return the Double value
     */
    def asDouble() = Bytes.toDouble(bytes)

    /**
     * Returns the value as Int.
     * @return the Int value
     */
    def asInt() = Bytes.toInt(bytes)

    /**
     * Returns the value as Short.
     * @return the Short value
     */
    def asShort() = Bytes.toShort(bytes)

    /**
     * Returns the value as BigDecimal.
     * @return the BigDecimal value
     */
    def asBigDecimal() = BigDecimal(Bytes.toBigDecimal(bytes))
  }

  /**
   * Returns AugmentByteArray instance.
   * @param bytes the byte array
   * @return the instance
   */
  implicit def toAugmentByteArray(bytes: Array[Byte]) = new AugmentByteArray(bytes)

  /**
   * Returns byte array of the String value.
   * @param s the String value
   * @return the byte array
   */
  implicit def toBytes(s: String) = Bytes.toBytes(s)

  /**
   * Returns byte array of the Boolean value.
   * @param b the Boolean value
   * @return the byte array
   */
  implicit def toBytes(b: Boolean) = Bytes.toBytes(b)

  /**
   * Returns byte array of the Long value.
   * @param l the Long value
   * @return the byte array
   */
  implicit def toBytes(l: Long) = Bytes.toBytes(l)

  /**
   * Returns byte array of the Float value.
   * @param f the Float value
   * @return the byte array
   */
  implicit def toBytes(f: Float) = Bytes.toBytes(f)

  /**
   * Returns byte array of the Double value.
   * @param d the Double value
   * @return the byte array
   */
  implicit def toBytes(d: Double) = Bytes.toBytes(d)

  /**
   * Returns byte array of the Int value.
   * @param i the Int value
   * @return the byte array
   */
  implicit def toBytes(i: Int) = Bytes.toBytes(i)

  /**
   * Returns byte array of the Short value.
   * @param s the Short value
   * @return the byte array
   */
  implicit def toBytes(s: Short) = Bytes.toBytes(s)

  /**
   * Returns byte array of the BigDecimal value.
   * @param b the BigDecimal value
   * @return the byte array
   */
  implicit def toBytes(b: BigDecimal) = Bytes.toBytes(b.underlying)

  /**
   * Represents entry point of loan pattern.
   * @param r the resource
   * @param block the code block to be run
   * @return something returned by the block
   */
  def using[R <: { def close() }, A](r: R)(block: R => A): A = {
    try {
      block(r)
    } finally {
      r.close()
    }
  }
}
