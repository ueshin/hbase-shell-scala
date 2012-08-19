/*
 * Copyright 2012 Happy-Camper Street.
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
package st.happy_camper.hbase.shell.coprocessor.protocol

import java.io.IOException

import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.ipc.CoprocessorProtocol

/**
 * A trait representing count row command protocol.
 * @author ueshin
 */
trait RowCountProtocol extends CoprocessorProtocol {

  /**
   * Returns row count of each region.
   * @param filter the filter to filter rows
   * @return row count
   */
  def count(implicit scan: Scan = new Scan()): Long
}
