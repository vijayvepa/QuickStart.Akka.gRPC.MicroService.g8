# CONTRIBUTION

## Prerequisites

- Download and Install [g8](http://www.foundweekends.org/giter8/)
- Download and Install [sbt](http://www.scala-sbt.org/) 
- IntelliJ Idea (recommended)

## Quick Guide

- Add relevant source files and config files in [src/main/g8](src/main/g8) folder.
- Before adding variables, replace all `$` with `\$` in your source code. Otherwise template generation will fail.
- Add variables and defaults in [src/main/g8/default.properties](src/main/g8/default.properties)
- Use the variables in your source code (example:  replace original project name with`$name$`)
- Update the file and folder names with variables (example: `$package$` folder)

## Test Locally

Go to the parent folder of this project

```shell
cd ..
```

Generate project

```shell
 g8 file://Quickstart.Akka.gRPC.MicroService.g8/
```

This will ask for different variables and generate the project with those substituted. Below are some examples.

```shell

name [akka-grpc-service]: airline-reservation-service
akka_version [2.8.0]: 
sbt_version [1.8.2]: 
group [com.example]: com.expedia
package [shopping]: airline.reservation
domain_package [cart]: reservation
projection_package [popularity]: analytics
domain [ShoppingCart]: AirlineReservation
projection [ItemPopularity]: AirlineStatistics
database [shopping-cart]: airline-reservation

```


- Ensure that the folder generation is successful.
- Ensure that the generated project builds with correct expected substitutions.

