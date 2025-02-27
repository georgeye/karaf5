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

import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;
import org.apache.karaf.boot.service.ServiceRegistry;
import org.apache.karaf.boot.spi.Service;
import org.apache.karaf.boot.spi.ServiceLoader;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Main Karaf runtime.
 */
@Log
@Builder
@Data
public class Karaf implements AutoCloseable {

    private final ServiceLoader loader;
    private final ServiceRegistry serviceRegistry = new ServiceRegistry();

    /**
     * Start the Karaf runtime.
     *
     * @return the Karaf runtime.
     */
    public Karaf start() {
        // log format
        if (System.getProperty("java.util.logging.config.file") == null) {
            if (System.getenv("KARAF_LOG_FORMAT") != null) {
                System.setProperty("java.util.logging.SimpleFormatter.format", System.getenv("KARAF_LOG_FORMAT"));
            }
            if (System.getProperty("java.util.logging.SimpleFormatter.format") == null) {
                System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT.%1$tN %4$s [ %2$s ] : %5$s%6$s%n");
            }
        }
        (this.loader == null ? loadServices() : this.loader.load()).forEach(serviceRegistry::add);
        serviceRegistry.start();
        return this;
    }

    private Stream<Service> loadServices() {
        return java.util.ServiceLoader.load(Service.class).stream().map(java.util.ServiceLoader.Provider::get)
                .sorted(Comparator.comparingInt(service -> Integer.getInteger(service.name() + ".priority", service.priority())));
    }

    /**
     * Close (stop) the Karaf runtime.
     */
    @Override
    public void close() {
        serviceRegistry.close();
    }

}
