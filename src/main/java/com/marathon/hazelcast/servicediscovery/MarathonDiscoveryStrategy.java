/*
 * Copyright 2016 Phaneesh Nagaraja <phaneesh.n@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marathon.hazelcast.servicediscovery;

import com.hazelcast.logging.ILogger;
import com.hazelcast.nio.Address;
import com.hazelcast.spi.discovery.AbstractDiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.SimpleDiscoveryNode;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author phaneesh
 */
public class MarathonDiscoveryStrategy extends AbstractDiscoveryStrategy {

    private String marathonEndpoint;

    private String appId;

    private int port;

    private ILogger logger;

    public MarathonDiscoveryStrategy(final ILogger logger, Map<String, Comparable> properties) {
        super(logger, properties);
        this.marathonEndpoint = getOrNull("discovery.marathon", MarathonDiscoveryConfiguration.MARATHON_ENDPOINT);
        this.appId = getOrNull("discovery.marathon", MarathonDiscoveryConfiguration.APP_ID);
        this.port = Integer.parseInt(getOrNull("discovery.marathon", MarathonDiscoveryConfiguration.PORT_INDEX));
        this.logger = logger;
        try {
            MarathonServiceDiscoveryHelper.start(marathonEndpoint, appId, port, logger);
        } catch (Exception e) {
           logger.severe("Failed to start service discovery!", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    MarathonServiceDiscoveryHelper.stop();
                } catch (Exception e) {
                    logger.severe("Error adding shutdown hook!", e);
                }
            }
        });
    }

    public Iterable<DiscoveryNode> discoverNodes() {
        return MarathonServiceDiscoveryHelper.getAllNodes().stream().map(n -> {
            Map<String, Object> attributes = Collections.<String, Object>singletonMap("hostname", n.getHost());
            try {
                return new SimpleDiscoveryNode(new Address(n.getHost(), n.getPort()), attributes);
            } catch (UnknownHostException e) {
                logger.severe("Error adding discovered member", e);
                return null;
            }
        }).collect(Collectors.toList());
    }
}
