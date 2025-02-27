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
package org.apache.karaf.camel;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.karaf.boot.Karaf;
import org.apache.karaf.boot.service.KarafConfigService;
import org.apache.karaf.boot.service.KarafLifeCycleService;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class CamelServiceTest {

    @Test
    public void routeBuilderServiceTest() throws Exception {
        CamelService camelService = new CamelService();
        MyRouteBuilder routeBuilder = new MyRouteBuilder();
        Karaf karaf = Karaf.builder().loader(() -> Stream.of(new KarafConfigService(), new KarafLifeCycleService(), routeBuilder, camelService)).build().start();

        MockEndpoint mockEndpoint = camelService.getCamelContext().getEndpoint("mock:test", MockEndpoint.class);
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived("Hello world!");

        ProducerTemplate producerTemplate = camelService.getCamelContext().createProducerTemplate();
        producerTemplate.sendBody("direct:test", "Hello world!");

        mockEndpoint.assertIsSatisfied();

        karaf.close();
    }

}
