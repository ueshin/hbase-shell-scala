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
package coprocessor
package handler

import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.coprocessor.BaseEndpointCoprocessor

import st.happy_camper.hbase.shell.coprocessor.protocol.RowCountProtocol

/**
 * A handler for {@link RowCountProtocol}.
 * @author ueshin
 */
trait RowCountProtocolHandler {
  self: BaseEndpointCoprocessor with RowCountProtocol =>

  override def count(implicit scan: Scan): Long = {
    using(getEnvironment().getRegion().getScanner(scan)) { scanner =>
      (0L /: scanner.toIterator) { (count, kvs) =>
        count + 1L
      }
    }
  }
}
