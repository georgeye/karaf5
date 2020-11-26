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
package org.apache.karaf.core;

import lombok.extern.java.Log;
import org.apache.karaf.core.model.Module;
import org.junit.jupiter.api.*;
import org.osgi.framework.Bundle;

import java.io.File;
import java.net.URI;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Log
public class KarafTest {

    @Test
    @Order(1)
    public void testKarafRunWithResolvedBundleModule() throws Exception {
        Karaf karaf = Karaf.build(KarafConfig.builder()
                .homeDirectory("target/karaf")
                .cacheDirectory("target/karaf/cache/1")
                .clearCache(true)
                .build());
        karaf.init();
        karaf.addModule("https://repo1.maven.org/maven2/org/ops4j/pax/url/pax-url-mvn/1.3.7/pax-url-mvn-1.3.7.jar");
        karaf.start();

        Map<String, Module> modules = karaf.getModules();
        Module module = modules.get("1");
        Assertions.assertEquals(Bundle.ACTIVE, module.getMetadata().get("State"));
    }

    @Test
    public void testAddRemoveBundleModule() throws Exception {
        Karaf karaf = Karaf.build(KarafConfig.builder()
                .homeDirectory("target/karaf")
                .cacheDirectory("target/karaf/cache/addremove")
                .clearCache(true)
                .build());
        karaf.init();
        karaf.addModule("https://repo1.maven.org/maven2/org/ops4j/pax/url/pax-url-mvn/1.3.7/pax-url-mvn-1.3.7.jar");
        Assertions.assertEquals(1, karaf.getModules().size());
        Module module = karaf.getModules().get("1");
        Assertions.assertEquals(Bundle.ACTIVE, module.getMetadata().get("State"));
        karaf.removeModule("1");
        Assertions.assertEquals(0, karaf.getModules().size());
        karaf.start();
    }

    @Test
    @Order(2)
    public void testKarafRunWithUnresolvedBundleModule() throws Exception {
        Karaf karaf = Karaf.build(KarafConfig.builder()
                .homeDirectory("target/karaf")
                .cacheDirectory("target/karaf/cache/2")
                .clearCache(true)
                .build());
        karaf.init();
        try {
            karaf.addModule("https://repo1.maven.org/maven2/org/ops4j/pax/url/pax-url-aether/2.6.3/pax-url-aether-2.6.3.jar");
            Assertions.fail("Bundle exception expected");
            karaf.start();
        } catch (Exception e) {
            // no-op
        }
    }

    @Test
    @Disabled("TODO: fix TomcatURLStreamHandler disable")
    public void testKarafRunWithSpringBootModule() throws Exception {
        Karaf karaf = Karaf.build(KarafConfig.builder()
                .homeDirectory("target/karaf")
                .cacheDirectory("target/karaf/cache/spring")
                .clearCache(true)
                .build());
        karaf.init();

        karaf.addModule("file:src/test/resources/rest-service-0.0.1-SNAPSHOT.jar");

        karaf.start();
    }

    @Test
    public void testKarafGet() throws Exception {
        Karaf karaf = Karaf.build(KarafConfig.builder()
                .homeDirectory("target/karaf")
                .cacheDirectory("target/karaf/cache/3")
                .clearCache(true)
                .build());
        karaf.init();
        karaf.start();

        Karaf instance = Karaf.get();
        Assertions.assertEquals(karaf, instance);
    }

    @Test
    public void testKarafService() throws Exception {
        Karaf karaf = Karaf.build(KarafConfig.builder()
                .homeDirectory("target/karaf")
                .cacheDirectory("target/karaf/cache/4")
                .clearCache(true)
                .build());
        karaf.init();
        karaf.start();

        Assertions.assertNotNull(karaf.getService(Karaf.class));
    }

    @Test
    public void test() throws Exception {
        ProtectionDomain protectionDomain = getClass().getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URI location = (codeSource != null) ? codeSource.getLocation().toURI() : null;
        String path = (location != null) ? location.getSchemeSpecificPart() : null;
        if (path == null) {
            throw new IllegalStateException("Unable to determine code source archive");
        }
        File root = new File(path);
        if (!root.exists()) {
            throw new IllegalStateException("Unable to determine code source archive from " + root);
        }
        System.out.println(root.getAbsolutePath());
        if (root.isDirectory()) {
            System.out.println(root.getAbsolutePath() + " is a directory");
        } else {
            System.out.println(root.getAbsolutePath() + " is a jar");
        }
    }

}
