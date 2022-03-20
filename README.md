# Roman numerals

## How to build
Simply run:
```
mvn clean install 
```
This will create a fat jar executable (spring-boot-maven-plugin), docker image (jib-maven-plugin) and Asciidoc documentation (asciidoctor-maven-plugin).

## How to run
```
java -jar target/RomanNumerals-0.0.1-SNAPSHOT.jar
```
OR
```
mvn spring-boot:run
```
OR
```
docker run -p 8080:8080 jakub/roman
```
## AsciiDoc 
AsciiDoc documentation is hosted under: 

http://localhost:8080/docs/roman-api.html

## Performance
```
Server Hostname:        localhost
Server Port:            8080

Document Path:          /romannumeral?query=255
Document Length:        31 bytes

Concurrency Level:      100
Time taken for tests:   2.162 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      1360000 bytes
HTML transferred:       310000 bytes
Requests per second:    4624.31 [#/sec] (mean)
Time per request:       21.625 [ms] (mean)
Time per request:       0.216 [ms] (mean, across all concurrent requests)
Transfer rate:          614.17 [Kbytes/sec] received
```

## Algorithm
The algorithm detects closest lower or equal number, puts it into result 
and recursively calls the same algorithm for remained value. The result is locally cached
for better performance.

I was inspired by Medium article: 

https://medium.com/programming-essentials/how-to-convert-to-a-roman-numeral-825245ac762b

## Extension 3
### Logging
SLF4J with Mapped Diagnostic Context for easy troubleshooting
### Tracing
On every exception traceId is propagated to the client for further investigation,
each roman conversion creates new spanId
### Metrics
Standard Spring Actuator metrics + GuavaCacheMetrics extension.

All metrics can be found here:

http://localhost:8080/actuator/metrics

On top of that, there is one custom counter metric which is incremented on every roman numeral conversion 
(so for example you may create top10 numbers statistic)

i.e. http://localhost:8080/actuator/metrics/roman.count?tag=number:255

I hope you like it,

Jakub