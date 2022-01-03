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

import com.hazelcast.config.properties.PropertyDefinition;
import com.hazelcast.logging.ILogger;
import com.hazelcast.spi.discovery.DiscoveryNode;
import com.hazelcast.spi.discovery.DiscoveryStrategy;
import com.hazelcast.spi.discovery.DiscoveryStrategyFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by phaneesh on 01/02/16.
 */
public class MarathonDiscoveryStrategyFactory implements DiscoveryStrategyFactory {

    private static Collection<PropertyDefinition> properties;

    public MarathonDiscoveryStrategyFactory() {
        properties = new ArrayList<>();
        properties.add(MarathonDiscoveryConfiguration.APP_ID);
        properties.add(MarathonDiscoveryConfiguration.PORT_INDEX);
        properties.add(MarathonDiscoveryConfiguration.MARATHON_ENDPOINT);
        properties.add(MarathonDiscoveryConfiguration.MARATHON_USERNAME);
        properties.add(MarathonDiscoveryConfiguration.MARATHON_PASSWORD);
    }

    @Override
    public Class<? extends DiscoveryStrategy> getDiscoveryStrategyType() {
        return MarathonDiscoveryStrategy.class;
    }

    @Override
    public DiscoveryStrategy newDiscoveryStrategy(DiscoveryNode discoveryNode, ILogger logger, Map<String, Comparable> properties) {
        return new MarathonDiscoveryStrategy(logger, properties);
    }

    @Override
    public Collection<PropertyDefinition> getConfigurationProperties() {
        return properties;
    }
}
