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
import com.hazelcast.config.properties.PropertyTypeConverter;
import com.hazelcast.config.properties.SimplePropertyDefinition;

public interface MarathonDiscoveryConfiguration {

    PropertyDefinition MARATHON_ENDPOINT = new SimplePropertyDefinition("marathon-endpoint", PropertyTypeConverter.STRING);

    PropertyDefinition APP_ID = new SimplePropertyDefinition("app-id", PropertyTypeConverter.STRING);

    PropertyDefinition PORT_INDEX = new SimplePropertyDefinition("port-index", PropertyTypeConverter.STRING);

    PropertyDefinition MARATHON_USERNAME = new SimplePropertyDefinition("marathon-username", true, PropertyTypeConverter.STRING);

    PropertyDefinition MARATHON_PASSWORD = new SimplePropertyDefinition("marathon-password", true, PropertyTypeConverter.STRING);

}
