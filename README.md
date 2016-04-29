# Hazelcast Marathon Discovery [![Travis build status](https://travis-ci.org/phaneesh/hazelcast-marathon-discovery.svg?branch=master)](https://travis-ci.org/phaneesh/hazelcast-marathon-discovery)

This is a discovery strategy extension for Hazelcast to make discovery work on [Marathon](https://mesosphere.github.io/marathon/).
This library compiles only on Java 8.

## Dependencies
* [Hazelcast](https://hazelcast.com/)
* [marathon-client](https://github.com/mohitsoni/marathon-client) 0.4.2  

## Usage
Hazelcast Marathon Discovery provides a easy way to enable member discovery with elastic applications on docker & DCOS
like environment where using a static host list or using multicast based discovery is not possible.

### Build instructions
  - Clone the source:

        git clone github.com/phaneesh/hazelcast-marathon-discovery

  - Build

        mvn install

### Maven Dependency
Add jcenter repository to project repositories:
* Maven
```xml
<repository>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
    <id>central</id>
    <name>bintray</name>
    <url>http://jcenter.bintray.com</url>
</repository>
```
* SBT
```sbt
resolvers ++= Seq(
  "bintray" at "http://jcenter.bintray.com"
)
```

* Gradle
```gradle
repositories {
    maven {
        url 'http://jcenter.bintray.com'
    }
}
```

Use the following dependency:
* Maven
```xml
<dependency>
    <groupId>com.marathon.hazelcast.servicediscovery</groupId>
    <artifactId>hazelcast-marathon-discovery</artifactId>
    <version>0.0.3</version>
</dependency>
```
* SBT
```sbt
libraryDependencies += "com.marathon.hazelcast.servicediscovery" % "hazelcast-marathon-discovery" % "0.0.3"
```
* Gradle
```gradle
compile 'com.marathon.hazelcast.servicediscovery:hazelcast-marathon-discovery:0.0.3'
```

### Using Hazelcast Marathon Discovery
```java
Config config = new Config();
//This is important to enable the discovery strategy
config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED, "true");
config.setProperty(GroupProperty.DISCOVERY_SPI_PUBLIC_IP_ENABLED, "true");
config.setProperty(GroupProperty.SOCKET_CLIENT_BIND_ANY, "false");
config.setProperty(GroupProperty.SOCKET_BIND_ANY, "false");
NetworkConfig networkConfig = config.getNetworkConfig();
JoinConfig joinConfig = networkConfig.getJoin();
joinConfig.getTcpIpConfig().setEnabled(false);
joinConfig.getMulticastConfig().setEnabled(false);
joinConfig.getAwsConfig().setEnabled(false);
DiscoveryConfig discoveryConfig = joinConfig.getDiscoveryConfig();
//Set the discovery strategy to RangerDiscoveryStrategy
DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new MarathonDiscoveryStrategyFactory());
//Marathon master endpoint that can provide app metadata
discoveryStrategyConfig.addProperty("marathon-endpoint", "http://my-marathon-master:8080");
//Namespace that needs to be used by ranger for this service
discoveryStrategyConfig.addProperty("app-id", "my_app");
//Hazelcast port index that is used in marathon configuration
discoveryStrategyConfig.addProperty("port-index", "1");
discoveryConfig.addDiscoveryStrategyConfig(discoveryStrategyConfig);
//Create the hazelcast instance
HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);
```

### Note
If you are using hazelcast in applications deployed on DCOS; then you should set the following configuration for discovery to work

```java
NetworkConfig networkConfig = config.getNetworkConfig();
networkConfig.setPublicAddress("<public ip address/host name>" +":" +"<public port>");
```
Example: (On DCOS + Marathon assuming hazelcast container port is set to 5701)
```java
NetworkConfig networkConfig = config.getNetworkConfig();
networkConfig.setPublicAddress(System.getenv("HOST") +":" +System.getenv("PORT_5701"))
```

LICENSE
-------

Copyright 2016 Phaneesh Nagaraja <phaneesh.n@gmail.com>.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
