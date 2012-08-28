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
   * Represents a converter from byte array to some value.
   * @author ueshin
   */
  private[shell] sealed class BytesToA(bytes: Array[Byte]) {

    /**
     * Converts byte array to some value.
     * @return the converted value
     */
    def to[A: BytesTo] = implicitly[BytesTo[A]].to(bytes)
  }

  /**
   * Returns BytesToA instance.
   * @param bytes the byte array
   * @return the instance
   */
  implicit def toBytesToA(bytes: Array[Byte]) = new BytesToA(bytes)

  /**
   * Represents a converter from some value to byte array.
   * @param <A> the type of the value
   * @author ueshin
   */
  private[shell] sealed class AToBytes[A: ToBytes](a: A) {

    /**
     * Converts some value to byte array.
     * @return the converted byte array
     */
    def bytes = implicitly[ToBytes[A]].bytes(a)
  }

  /**
   * Returns AToBytes instance.
   * @param a the value
   * @return the instance
   */
  implicit def toAToBytes[A: ToBytes](a: A) = new AToBytes(a)

  /**
   * Converts some value to byte array.
   * @param a the some value
   * @return the instance
   */
  implicit def toBytes[A: ToBytes](a: A) = a.bytes

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
