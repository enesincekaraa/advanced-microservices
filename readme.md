# Spring Boot Microservice Architecture

Advanced Microservice is a cloud-native microservice architecture developed with Spring Boot 3.x and Java 17.  
It features service discovery, centralized configuration, distributed tracing, fault tolerance, and gateway routing.

---

## 🧩 Microservices

| Service             | Description |
|---------------------|-------------|
| `employee-service`  | Resilience4j Retry, CircuitBreaker, Fallback |
| `department-service`| Lightweight service used by Employee |
| `config-server`     | Spring Cloud centralized configuration |
| `discovery-server`  | Eureka Discovery Server |
| `gateway-service`   | Spring Cloud Gateway for routing |

---

## 🚀 Getting Started
* Startup order:
* git https://github.com/enesincekaraa/advanced-microservices.git
* 1 - discovery-server
* 2 - discovery-server
* 3 - discovery-server
* 4 - discovery-server
* 5 - discovery-server


```bash



*  RabbitMQ Setup

      
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>

```yaml
      rabbitmq:
      host: localhost
      port: 5672
      username: guest
      password: guest
```

*  🐳 Run in Docker
   docker run --rm -it -p 5672:5672 rabbitmq:4.1.0
```bash



* Settings for Zipkin and steps to run it via Docker

      <!-- Gözlem ve ölçüm toplamak için -->
      <dependency>
          <groupId>io.micrometer</groupId>
          <artifactId>micrometer-observation</artifactId>
      </dependency>

        <!-- Brave altyapısıyla trace köprüsü kurar -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-tracing-bridge-brave</artifactId>
        </dependency>

        <!-- Zipkin'e trace bilgilerini göndermek için -->
        <dependency>
            <groupId>io.zipkin.reporter2</groupId>
            <artifactId>zipkin-reporter-brave</artifactId>
        </dependency>

        <!-- Eğer Feign Client kullanıyorsan (Opsiyonel) -->
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-micrometer</artifactId>
        </dependency>


```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    bus:
      refresh:
        enabled: true
  tracing:
    sampling:
      probability: 1.0

logging:
  level:
    org:
      springframework:
        cloud:
          gateway:
            handler:
              RoutePredicateHandlerMapping: DEBUG
        web: DEBUG
  pattern:
    level: "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]"
```

docker run --rm -it --name zipkin -p 9411:9411 openzipkin/zipkin


* Settings for Resilience4J and steps


        <!-- Circuit breaker -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

````
 @FeignClient(
        name = "DEPARTMENT-SERVICE",
        fallback = DepartmentClientFallback.class
)



@Component
public class DepartmentClientFallback implements DepartmentClient {



    @Override
    public ApiResponse<DepartmentResponse> getDepartment(String departmentCode) {
        DepartmentResponse dummy = new DepartmentResponse(
                "Department not found",
                "Department not found",
                departmentCode
        );

        return new ApiResponse<>(
                "Department service is unavailable",
                false,
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                dummy
        );
    }
}
````
````

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentClient client;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentClient client) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.client = client;
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {
       boolean exists = employeeRepository.existsByEmail((request.email()));
       if (exists) {
              throw new EmployeeAlreadyExistsException("Employee already exists with email: " + request.email());
         }
          try {
                var employee = employeeMapper.toEmployee(request);
                var savedEmployee = employeeRepository.save(employee);
                return employeeMapper.toEmployeeResponse(savedEmployee);
          } catch (Exception e) {
                throw new IllegalArgumentException("Error occurred while creating employee: " + e.getMessage());
       }


    }

    @CircuitBreaker(name = "${spring.application.name}",fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public ClientResponse getEmployeeByEmail(String email) {
        logger.info("Fetching employee with email: {}", email);
        var employee = employeeRepository.getEmployeeByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with email: " + email));

        var employeeResponse = employeeMapper.toEmployeeResponse(employee);

        if (employee.getDepartmentCode() == null){
            throw new IllegalArgumentException("Employee not found with email: " + email);
        }
        var department = client.getDepartment(employee.getDepartmentCode());

      return new ClientResponse(
              employeeResponse,
                new ApiResponse<>(
                        department.message(),
                        department.success(),
                        department.timestamp(),
                        department.statusCode(),
                        department.data()
                )
      );


    }



    public ClientResponse getDefaultDepartment(String email,Exception e) {
        logger.error("Error occurred while fetching department for employee with email: {}. Error: {}", email, e.getMessage());
        var employee = employeeRepository.getEmployeeByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with email: " + email));

        var employeeResponse = employeeMapper.toEmployeeResponse(employee);

        if (employee.getDepartmentCode() == null){
            throw new IllegalArgumentException("Employee not found with email: " + email);
        }
//        var department = client.getDepartment(employee.getDepartmentCode());

        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.toString();
        DepartmentResponse department = new DepartmentResponse(
                "Department not found",
                "Department not found",
                formattedDateTime
        );

        return new ClientResponse(
                employeeResponse,
                new ApiResponse<>(
                        "Department not found",
                        false,
                        now,
                        HttpStatus.NOT_FOUND.value(),
                        department
                )
        );


    }
````

```yaml

resilience4j:
  retry:
    instances:
      employee-service:                         # İkisi de aynı isimde olmalı
        maxAttempts: 3                          # 1 orijinal + 2 retry
        waitDuration: 2s                        # Her deneme arasında 2 saniye bekle
        retryExceptions:
          - java.io.IOException
          - org.springframework.web.client.HttpServerErrorException
        ignoreExceptions:
          - java.lang.IllegalArgumentException
        exponentialBackoffMultiplier: 2.0       # Her retry arasında süreyi katla
        enableExponentialBackoff: true
        failAfterMaxAttempts: true              # Max retry sonrası exception fırlat

  circuitbreaker:
    instances:
      employee-service:                         # Aynı isimle tanımlanmalı
        registerHealthIndicator: true           # Health endpoint'e yansıtır
        failureRateThreshold: 50                # %50 hata oranında devre açılır
        minimumNumberOfCalls: 5                 # 5 çağrıdan sonra istatistik başlar
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 5s             # Devre açıldıktan sonra 5s bekle
        permittedNumberOfCallsInHalfOpenState: 3
        slidingWindowSize: 10                   # Son 10 çağrıya göre karar verir
        slidingWindowType: COUNT_BASED

```





