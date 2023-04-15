# Akka Quickstart with Java

Adapted from  https://github.com/akka/akka-quickstart-java.g8

## Quickstart guide

This example is described in the [Lightbend Tutorial for Microservices](https://developer.lightbend.com/docs/akka-guide/microservices-tutorial/index.html)

The  tutorial illustrates how Microservices can interact to implement a shopping cart system using the Akka gRPC and Kafka frameworks.

## Giter8 template

It can also be used as a [Giter8][g8] template for Akka with Java.

Prerequisites:
- JDK 8
- [sbt][sbt] 1.2.8 or higher ([download here][sbt_download])

Open a console and run the following command to apply this template:
 ```
sbt new https://github.com/vijayvepa/QuickStart.Akka.gRPC.MicroService.g8
 ```

This template will prompt for the name of the project. Press `Enter` if the default values suit you.

Once inside the project folder, run the application with:
```
sbt run
```

This template also provides build descriptors for maven and gradle. You can use any of the following commands to run 
the application:
```
mvn compile exec:exec
```
or
```
gradle run
```

## Template license

Based on templates and examples Written by Lightbend, Inc.

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.

[g8]: http://www.foundweekends.org/giter8/
[sbt]: http://www.scala-sbt.org/
[sbt_download]: http://www.scala-sbt.org/download.html
