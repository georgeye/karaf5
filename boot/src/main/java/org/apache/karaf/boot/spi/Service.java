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
package org.apache.karaf.boot.spi;

import org.apache.karaf.boot.service.ServiceRegistry;

import java.util.Locale;
import java.util.Properties;

/**
 * Generic Karaf Service.
 */
public interface Service {

    int DEFAULT_PRIORITY = 1000;

    /**
     * Register a service in the Karaf registry.
     * @param serviceRegistry the Karaf service registry.
     * @throws Exception if register fails.
     */
    default void onRegister(final ServiceRegistry serviceRegistry) throws Exception {
        // no-op, service
    }

    /**
     * Retrieve the service priority (allow services sorting).
     * Default is 1000.
     * @return the service priority.
     */
    default int priority() {
        return DEFAULT_PRIORITY;
    }

    /**
     * Retrieve the service name.
     * Default is the service simple class name.
     * @return the service name.
     */
    default String name() {
        return getClass().getSimpleName().toLowerCase(Locale.ROOT).replaceAll("Service", "");
    }

    /**
     * Add properties specific to a service that could be used by other services during lookup.
     * Default is empty properties.
     *
     * @return the service properties.
     */
    default Properties properties() {
        return new Properties();
    }

}
