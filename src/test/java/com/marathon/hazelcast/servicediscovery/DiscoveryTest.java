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

import client.model.v2.App;
import client.model.v2.GetAppResponse;
import client.model.v2.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.Lists;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spi.properties.ClusterProperty;
import org.junit.Rule;
import org.junit.Test;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collections;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertTrue;


public class DiscoveryTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSingleMemberDiscovery() throws IOException, InterruptedException {
        GetAppResponse response = new GetAppResponse();
        App app = new App();
        app.setId("test_app");
        Task task = new Task();
        task.setHost("127.0.0.1");
        task.setPorts(Collections.singletonList(5701));
        app.setTasks(Collections.singletonList(task));
        response.setApp(app);
        stubFor(get(urlEqualTo("/v2/apps/test_app"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mapper.writeValueAsBytes(response))));
        HazelcastInstance hazelcast = getHazelcastInstanceWithoutBasicAuth(5701);
        assertTrue(hazelcast.getCluster().getMembers().size() > 0);
        hazelcast.shutdown();
    }

    @Test
    public void testMultiMemberDiscovery() throws UnknownHostException, InterruptedException, JsonProcessingException {
        GetAppResponse response = new GetAppResponse();
        App app = new App();
        app.setId("test_app");
        Task task1 = new Task();
        task1.setHost("127.0.0.1");
        task1.setPorts(Collections.singletonList(5701));
        Task task2 = new Task();
        task2.setHost("127.0.0.1");
        task2.setPorts(Collections.singletonList(5702));
        Task task3 = new Task();
        task3.setHost("127.0.0.1");
        task3.setPorts(Collections.singletonList(5703));
        app.setTasks(Lists.asList(task1, task2, new Task[]{task3}));
        response.setApp(app);
        stubFor(get(urlEqualTo("/v2/apps/test_app"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mapper.writeValueAsBytes(response))));

        HazelcastInstance hazelcast1 = getHazelcastInstanceWithoutBasicAuth(5701);
        HazelcastInstance hazelcast2 = getHazelcastInstanceWithoutBasicAuth(5702);
        HazelcastInstance hazelcast3 = getHazelcastInstanceWithBasicAuth(5703);
        assertTrue(hazelcast3.getCluster().getMembers().size() > 0);
        assertTrue(hazelcast3.getCluster().getMembers().size() == 3);
        hazelcast1.shutdown();
        hazelcast2.shutdown();
        hazelcast3.shutdown();
    }

    private HazelcastInstance getHazelcastInstanceWithoutBasicAuth(int port)
            throws UnknownHostException, InterruptedException {
        return getHazelcastInstance(port, false);
    }

    private HazelcastInstance getHazelcastInstanceWithBasicAuth(int port)
            throws UnknownHostException, InterruptedException {
        return getHazelcastInstance(port, true);
    }

    private HazelcastInstance getHazelcastInstance(int port, boolean withBasicAuthMarathon)
            throws UnknownHostException, InterruptedException {
        Config config = new Config();
        config.setProperty(ClusterProperty.DISCOVERY_SPI_ENABLED.getName(), "true");
        config.setProperty(ClusterProperty.DISCOVERY_SPI_PUBLIC_IP_ENABLED.getName(), "true");
        config.setProperty(ClusterProperty.SOCKET_CLIENT_BIND_ANY.getName(), "false");
        config.setProperty(ClusterProperty.SOCKET_BIND_ANY.getName(), "false");
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getInterfaces().addInterface("127.0.0.1").setEnabled(true);
        networkConfig.setPort(port);
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getTcpIpConfig().setEnabled(false);
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getAwsConfig().setEnabled(false);
        DiscoveryConfig discoveryConfig = joinConfig.getDiscoveryConfig();
        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new MarathonDiscoveryStrategyFactory());
        discoveryStrategyConfig.addProperty("marathon-endpoint", "http://localhost:8080");
        discoveryStrategyConfig.addProperty("app-id", "test_app");
        discoveryStrategyConfig.addProperty("port-index", "0");
        if (withBasicAuthMarathon) {
            discoveryStrategyConfig.addProperty("marathon-username", "username");
            discoveryStrategyConfig.addProperty("marathon-password", "password");
        }
        discoveryConfig.addDiscoveryStrategyConfig(discoveryStrategyConfig);
        Thread.sleep(2000);
        return Hazelcast.newHazelcastInstance(config);
    }
}
