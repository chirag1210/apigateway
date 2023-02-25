# apigateway

Spring cloud APi Gateway previously popular gate is Zuul api gateway but now it is not supported now spring cloud api gateway

Steps to create Spring Cloud API Gateway

- ***Step 1***  
```
On Spring Initializr, choose:  
  
Group Id: com.microservices.apigateway  
Artifact Id: api-gateway  
Dependencies  
DevTools  
Actuator  
Config Client  
Eureka Discovery Client  
Gateway (Spring Cloud Routing)  

- ***Step 2***:
pom.xml dependency  

```
<dependency>  
			<groupId>org.springframework.cloud</groupId>  
			<artifactId>spring-cloud-starter-gateway</artifactId>  
</dependency>  
```

- ***Step 3***:
/api-gateway/src/main/resources/application.properties Modified  
  
spring.application.name=api-gateway  
server.port=8765  
  
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka  


- ***Step 4***  Run 
http://localhost:8765/CURRENCY-EXCHANGE/currency-exchange/from/USD/to/INR

It gives error
  
- ***Step 5*** In api gateway 
```
application.properties
spring.cloud.gateway.discovery.locator.enabled=true
```

- ***Step 6*** : try again

http://localhost:8765/CURRENCY-EXCHANGE/currency-exchange/from/USD/to/INR

its works!

- ***Step 7***: but Url is not look like good
```
application.properties

spring.cloud.gateway.discovery.locator.lower-case-service-id=true
```

- ***Step 8***  
now hit url: http://localhost:8765/currency-exchange/currency-exchange/from/USD/to/INR  
  
- ***Step 9***: Exploring Routes with Spring Cloud Gateway  
  
Final URL we want  
  
http://localhost:8765/currency-exchange/from/USD/to/INR  
http://localhost:8765/currency-conversion/from/USD/to/INR/quantity/10  
http://localhost:8765/currency-conversion-feign/from/USD/to/INR/quantity/10  
http://localhost:8765/currency-conversion-new/from/USD/to/INR/quantity/10  
  
- ***Step 9***

/api-gateway/src/main/resources/application.properties Modified   
Commented  
```    
#spring.cloud.gateway.discovery.locator.enabled=true  
#spring.cloud.gateway.discovery.locator.lowerCaseServiceId=true  
```  
  
- ***Step 10***   
Create new configuration file  
```  
ApiGatewayConfiguration.java  
  
import org.springframework.cloud.gateway.route.RouteLocator;  
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
  
@Configuration  
public class ApiGatewayConfiguration {  
	  
	@Bean  
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {  
		return builder.routes()  
				.route(p -> p  
						.path("/get")  
						.filters(f -> f  
								.addRequestHeader("MyHeader", "MyURI")  
								.addRequestParameter("Param", "MyValue"))  
						.uri("http://httpbin.org:80"))  
				.route(p -> p.path("/currency-exchange/**")  
						.uri("lb://currency-exchange"))  
				.route(p -> p.path("/currency-conversion/**")  
						.uri("lb://currency-conversion"))  
				.route(p -> p.path("/currency-conversion-feign/**")  
						.uri("lb://currency-conversion"))  
				.route(p -> p.path("/currency-conversion-new/**")  
						.filters(f -> f.rewritePath(  
								"/currency-conversion-new/(?<segment>.*)",   
								"/currency-conversion-feign/${segment}"))  
						.uri("lb://currency-conversion"))  
				.build();  
	}  
  
}  
  
  
Run the application and hit  
  
http://localhost:8765/currency-exchange/from/USD/to/INR  
http://localhost:8765/currency-conversion/from/USD/to/INR/quantity/10  
http://localhost:8765/currency-conversion-feign/from/USD/to/INR/quantity/10  
http://localhost:8765/currency-conversion-new/from/USD/to/INR/quantity/10  


