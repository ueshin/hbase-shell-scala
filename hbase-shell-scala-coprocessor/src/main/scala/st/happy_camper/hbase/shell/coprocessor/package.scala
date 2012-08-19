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

import java.util.ArrayList

import scala.collection.JavaConversions.asScalaBuffer

import org.apache.hadoop.hbase.CoprocessorEnvironment
import org.apache.hadoop.hbase.KeyValue
import org.apache.hadoop.hbase.coprocessor.MasterCoprocessorEnvironment
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment
import org.apache.hadoop.hbase.coprocessor.WALCoprocessorEnvironment
import org.apache.hadoop.hbase.regionserver.RegionScanner

/**
 * A coprocessor package object.
 * @author ueshin
 */
package object coprocessor {

  /**
   * Represents an augmented RegionScanner.
   * @author ueshin
   */
  class AugmentRegionScanner(scanner: RegionScanner) {

    /**
     * Returns an iterator to traverse the region.
     * @return the iterator
     */
    def toIterator(): Iterator[List[KeyValue]] = toIterator(-1)

    /**
     * Returns an iterator to traverse the region.
     * @param limit the limit of the retrieving KeyValue's for each iteration
     * @return the iterator
     */
    def toIterator(limit: Int) = new Iterator[List[KeyValue]] {

      private val nextResult = new ArrayList[KeyValue]
      scanner.next(nextResult)

      def hasNext: Boolean = nextResult.size > 0

      def next(): List[KeyValue] = {
        val next = nextResult.toList
        nextResult.clear()
        scanner.next(nextResult, limit)
        next
      }
    }
  }

  /**
   * Returns AugmentRegionScanner instance
   * @param scanner the scanner
   * @return the instance
   */
  implicit def toAugmentRegionScanner(scanner: RegionScanner) = new AugmentRegionScanner(scanner)

  /**
   * Casts env from CoprocessorEnvironment to CoprocessorEnvironment.
   * @param env the env
   * @return the casted env
   */
  implicit def asRegionCoprocessorEnvironment(env: CoprocessorEnvironment) = {
    env.asInstanceOf[RegionCoprocessorEnvironment]
  }

  /**
   * Casts env from CoprocessorEnvironment to WALCoprocessorEnvironment.
   * @param env the env
   * @return the casted env
   */
  implicit def asMasterCoprocessorEnvironment(env: CoprocessorEnvironment) = {
    env.asInstanceOf[MasterCoprocessorEnvironment]
  }

  /**
   * Casts env from CoprocessorEnvironment to WALCoprocessorEnvironment.
   * @param env the env
   * @return the casted env
   */
  implicit def asWALCoprocessorEnvironment(env: CoprocessorEnvironment) = {
    env.asInstanceOf[WALCoprocessorEnvironment]
  }
}
