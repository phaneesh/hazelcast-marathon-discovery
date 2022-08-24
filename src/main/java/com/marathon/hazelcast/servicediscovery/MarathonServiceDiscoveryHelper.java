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

import com.hazelcast.internal.util.CollectionUtil;
import com.hazelcast.logging.ILogger;
import mesosphere.marathon.client.Marathon;
import mesosphere.marathon.client.MarathonClient;
import mesosphere.marathon.client.model.v2.GetAppResponse;
import mesosphere.marathon.client.utils.MarathonException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class MarathonServiceDiscoveryHelper {

    private static Marathon marathon;

    private static final AtomicReference<List<ServiceNode>> serviceNodes = new AtomicReference<>(Collections.emptyList());

    private static ILogger log;

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static class MarathonPoller implements Runnable {

        private final Marathon marathon;

        private final String app;

        private final int portIndex;

        public MarathonPoller(final Marathon marathon, final String app, final int portIndex) {
            this.marathon = marathon;
            this.app = app;
            this.portIndex = portIndex;
        }

        @Override
        public void run() {
            try {
                GetAppResponse response = marathon.getApp(app);
                List<ServiceNode> nodes = response.getApp().getTasks().stream()
                        .map( task -> new ServiceNode(task.getHost(),
                                                      CollectionUtil.<Integer>getItemAtPositionOrNull(task.getPorts(), portIndex)))
                        .collect(Collectors.toList());
                serviceNodes.getAndSet(nodes);
            } catch (MarathonException e) {
                log.severe("Error getting app metadata from marathon", e);
            }
        }
    }

    public static class ServiceNode {

        private String host;

        private int port;

        public ServiceNode(final String host, final int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

    public static void start(final String marathonEndpoint, final String appId, final int portIndex, final ILogger logger) throws Exception {
        marathon = MarathonClient.getInstance(marathonEndpoint);
        log = logger;
        MarathonPoller marathonPoller = new MarathonPoller(marathon, appId, portIndex);
        scheduledExecutorService.scheduleAtFixedRate(marathonPoller, 0, 10, TimeUnit.SECONDS);
    }

    public static List<ServiceNode> getAllNodes() {
        return serviceNodes.get();
    }

    public static void stop() throws Exception {
        scheduledExecutorService.shutdown();
    }
}
