# Hazelcast Marathon Discovery [![Travis build status](https://travis-ci.org/phaneesh/hazelcast-marathon-discovery.svg?branch=master)](https://travis-ci.org/phaneesh/hazelcast-marathon-discovery)

This is a discovery strategy extension for Hazelcast to make discovery work on [Marathon](https://mesosphere.github.io/marathon/).
This library compiles only on Java 8.

## Dependencies
* [Hazelcast](https://hazelcast.com/)
  

## Usage
Hazelcast Marathon Discovery provides a easy way to enable member discovery with elastic applications on docker & DCOS
like environment where using a static host list or using multicast based discovery is not possible.

### Build instructions
  - Clone the source:

        git clone github.com/phaneesh/hazelcast-marathon-discovery

  - Build

        mvn install

### Maven Dependency
Add clojars repository to project repositories:
* Maven
```xml
<repository>
    <id>clojars</id>
    <name>Clojars repository</name>
    <url>https://clojars.org/repo</url>
</repository>
```
* SBT
```sbt
resolvers ++= Seq(
  "clojars" at "https://clojars.org/repo"
)
```

* Gradle
```gradle
repositories {
    maven {
        url 'https://clojars.org/repo'
    }
}
```

Use the following dependency:
* Maven
```xml
<dependency>
    <groupId>com.marathon.hazelcast.servicediscovery</groupId>
    <artifactId>hazelcast-marathon-discovery</artifactId>
    <version>0.0.7</version>
</dependency>
```
* SBT
```sbt
libraryDependencies += "com.marathon.hazelcast.servicediscovery" % "hazelcast-marathon-discovery" % "0.0.6"
```
* Gradle
```gradle
compile 'com.marathon.hazelcast.servicediscovery:hazelcast-marathon-discovery:0.0.6'
```

### Using Hazelcast Marathon Discovery
```java
Config config = new Config();
//This is important to enable the discovery strategy
config.setProperty("hazelcast.discovery.enabled", "true");
config.setProperty("hazelcast.discovery.public.ip.enabled", "true");
config.setProperty("hazelcast.socket.client.bind.any", "false");
config.setProperty("hazelcast.socket.bind.any", "false");
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
