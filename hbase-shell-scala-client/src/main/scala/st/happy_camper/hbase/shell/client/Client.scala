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
package st.happy_camper.hbase.shell.client

import org.apache.hadoop.conf.Configuration

/**
 * A super trait of HBase client.
 * @author ueshin
 */
trait Client {

  implicit val conf: Configuration

  /**
   * Creates new table instance.
   * @param tablename the table name
   * @return the new instance
   */
  def table(tablename: Array[Byte]) = new Table(tablename)
}
