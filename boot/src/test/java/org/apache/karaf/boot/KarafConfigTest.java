/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.boot;

import org.apache.karaf.boot.config.KarafConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KarafConfigTest {

    @Test
    public void defaultTest() {
        System.setProperty("foo.test", "bar.test");

        KarafConfig config = new KarafConfig();
        config.getProperties().put("foo", "bar");

        Assertions.assertEquals("world", config.getProperty("hello", "world"));
        Assertions.assertEquals("bar", config.getProperty("foo"));
        Assertions.assertEquals("bar.test", config.getProperty("foo.test"));
        Assertions.assertNull(config.getProperty("not.defined"));
    }

}
