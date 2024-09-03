[<img src="./images/harman.png" width="300" height="150"/>](harman.png)

# ignite-utils
`utils` project provides the below functionalities to a service.

1. Centralized logging.
2. Health checks.
3. Diagnostic data reporting.
4. Application Metrics - counters, gauges and histograms.

# Table of Contents
* [Getting Started](#getting-started)
* [Usage](#usage)
* [How to contribute](#how-to-contribute)
* [Built with Dependencies](#built-with-dependencies)
* [Code of Conduct](#code-of-conduct)
* [Authors](#authors)
* [Security Contact Information](#security-contact-information)
* [Support](#support)
* [Troubleshooting](#troubleshooting)
* [License](#license)
* [Announcements](#announcements)


## Getting Started

To build the project in the local working directory after the project has been cloned/forked, run:

```mvn clean install```

from the command line interface.

### Prerequisites

1. Maven
2. Java 11

### Installation

[How to set up maven](https://maven.apache.org/install.html)

[Install Java](https://stackoverflow.com/questions/52511778/how-to-install-openjdk-11-on-windows)

### Running the tests

```mvn test```

Or run a specific test

```mvn test -Dtest="TheFirstUnitTest"```

To run a method from within a test

```mvn test -Dtest="TheSecondUnitTest#whenTestCase2_thenPrintTest2_1"```

### Deployment

`utils` project serves as a library for the services. It is not meant to be deployed as a service in any cloud environment.

## Usage
Add the following dependency in the target project
```
<dependency>
  <groupId>com.harman.ignite</groupId>
  <artifactId>ignite-utils</artifactId>
  <version>3.X.X</version>
</dependency>
```

### Logging

To implement logging in the services, the services need to get an instance of `IgniteLogger` class using the `IgniteLoggerFactory` factory class.

Example:
```java
private static IgniteLogger logger = IgniteLoggerFactory.getLogger(ProtocolTranslatorPreProcessor.class);
```

The `IgniteLogger` is an extension of slf4j logger. All the concepts related to logging an error message, exceptions, stack traces, etc. alongwith the different log levels remain the same.

### Health Check

`utils` provides the services with Health Check capabilities by implementing a number of different `HealthMonitor` instances. 
`HealthService` listens to the health state changes in all the `HealthMonitor` instances implemented by the service and maintains Gauge data for the healthy and unhealthy health monitors along with the overall health of the service for service health monitoring.


### Diagnostic Reporting

`utils` provides the service with Diagnostic reporting capabilities by implementing a number of `DiagnosticReporter` instances which in turn maintain `DiagnosticData` for the metrics and the corresponding `DiagnosticResult`.
`DiagnosticService` listens to all the enabled diagnostic reporters and do the reporting in Graylog besides publishing the metrics in prometheus about diagnostic data of a service.

### Implementing metrics
`utils` provides the service with metrics - Counters, Gauge and Histogram.
Any service can extend the above metrics for reporting and monitoring purpose.

Examples:

<b> Implementing a Counter </b>

Custom `Counter` classes need to extend the abstract class `AbstractIgniteCounter`

```java
public class GenericIgniteCounter extends AbstractIgniteCounter {
    public GenericIgniteCounter(String name, String help, String... labels) {
        createCounter(name, help, labels);
    }    
}
```

<b> Implementing a Gauge </b>

Custom `Gauge` classes need to extend the abstract class `AbstractIgniteGauge`

```java
public class IgniteDiagnosticGuage extends IgniteGuage {
    public IgniteDiagnosticGuage() {
        createGuage("diagnostic_metric", "node", "diagnostic_reporter_name", "diagnostic_reporter_sublabel");
    }
}
```

<b> Implementing a Histogram </b>

Custom `Histogram` classes need to extend the abstract class `AbstractIgniteHistogram`

```java
public class GenericIgniteHistogram extends AbstractIgniteHistogram {
    public GenericIgniteHistogram(String name, String help, double[] buckets, String... labelNames) {
        createHistogram(name, help, buckets, labelNames);
    }
}
```

## Built With Dependencies

|                           Dependency                            | Purpose                                    |
|:---------------------------------------------------------------:|:-------------------------------------------|
|  [Ignite Entities](https://github.com/HARMANInt/ics/entities)   | The library to implement database entities |
| [Spring Framework](https://spring.io/projects/spring-framework) | The core spring support                    |
|     [Spring Boot](https://spring.io/projects/spring-boot/)      | The web framework used                     |
|               [Maven](https://maven.apache.org/)                | Dependency Management                      |
|               [Junit](https://junit.org/junit5/)                | Testing framework                          |
|              [Mockito](https://site.mockito.org/)               | Test Mocking framework                     |

## How to contribute

Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for details on our contribution guidelines, and the process for submitting pull requests to us.

## Code of Conduct

Please read [CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md) for details on our code of conduct.

## Authors

* **Kaushal Arora** - *Initial work* 
* **Ashish Kumar Singh** - *Coding guidelines*

See also the list of [contributors](https://github.com/HARMANInt/ics/utils/contributors) who participated in this project.

## Security Contact Information

Please read [SECURITY.md](./SECURITY.md) to raise any security related issues.

## Support
Please write to us at [csp@harman.com](mailto:csp@harman.com)

## Troubleshooting

Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for details on how to raise an issue and submit a pull request to us.

## License

This project is licensed under the Apache-2.0 License - see the [LICENSE](./LICENSE) file for details.


## Announcements
All updates to this library are documented in our [Release Notes](./release_notes.txt) and [releases](https://github.com/HARMAN-Automotive/utils/releases).
For the versions available, see the [tags on this repository](https://github.com/HARMAN-Automotive/utils/tags).

