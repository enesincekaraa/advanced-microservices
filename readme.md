# Spring Boot Microservice Architecture

Advanced Microservice is a cloud-native microservice architecture developed with Spring Boot 3.x and Java 17.  
It features service discovery, centralized configuration, distributed tracing, fault tolerance, and gateway routing.

---
Advanced Microservices Architecture with Spring Boot
Bu proje, Spring Boot ve Spring Cloud teknolojileri kullanılarak geliştirilmiş, ölçeklenebilir ve dayanıklı bir mikroservis mimarisi örneğidir. Proje, çeşitli mikroservislerin entegrasyonunu, merkezi yapılandırmayı, servis keşfini, API geçidini ve hata tolerans mekanizmalarını içermektedir.​

🧱 Proje Mimarisi
Proje aşağıdaki mikroservislerden oluşmaktadır:​

Config Server: Tüm mikroservisler için merkezi yapılandırma sağlar.

Discovery Server (Eureka): Servislerin kayıt ve keşfini yönetir.

API Gateway: Tüm gelen istekleri yönlendirir ve filtreler.

Employee Service: Çalışan bilgilerini yönetir.

Department Service: Departman bilgilerini yönetir.​


🚀 Kullanılan Teknolojiler
Java 17

Spring Boot 3.x

Spring Cloud (Config, Eureka, Gateway)

Resilience4j (Circuit Breaker, Retry)

RabbitMQ (Spring Cloud Bus)

Zipkin (Distributed Tracing)


```bash

⚙️ Yapılandırma ve Kurulum
1. RabbitMQ Kurulumu
RabbitMQ, mikroservisler arasında mesajlaşma için kullanılır.​

Docker ile çalıştırma:

bash
Kopyala
Düzenle
docker run --rm -it -p 5672:5672 rabbitmq:4.1.0


(application.yml):
rabbitmq:
  host: localhost
  port: 5672
  username: guest
  password: guest


2. Zipkin Kurulumu
Zipkin, dağıtık izleme ve performans analizi için kullanılır.​

Docker ile çalıştırma:
docker run --rm -it --name zipkin -p 9411:9411 openzipkin/zipkin


Yapılandırma (application.yml):

yaml
Kopyala
Düzenle
management:
  endpoints:
    web:
      exposure:
        include: "*"
  tracing:
    sampling:
      probability: 1.0

logging:
  level:
    org:
      springframework:
        web: DEBUG
  pattern:
    level: "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]"


3. Resilience4j Yapılandırması
Resilience4j, mikroservislerin hata toleransını artırmak için kullanılır.​

Yapılandırma (application.yml):
resilience4j:
  retry:
    instances:
      employee-service:
        maxAttempts: 3
        waitDuration: 2s
        retryExceptions:
          - java.io.IOException
          - org.springframework.web.client.HttpServerErrorException
        ignoreExceptions:
          - java.lang.IllegalArgumentException
        exponentialBackoffMultiplier: 2.0
        enableExponentialBackoff: true
        failAfterMaxAttempts: true

  circuitbreaker:
    instances:
      employee-service:
        registerHealthIndicator: true
        failureRateThreshold: 50
        minimumNumberOfCalls: 5
        automaticTransitionFromOpenToHalfOpenEnabled: true
        waitDurationInOpenState: 5s
        permittedNumberOfCallsInHalfOpenState: 3
        slidingWindowSize: 10
        slidingWindowType: COUNT_BASED



🧪 Servislerin Başlatılması
Her bir mikroservis, bağımsız olarak çalıştırılabilir. Ancak, servislerin birbirleriyle iletişim kurabilmesi için aşağıdaki sırayla başlatılmaları önerilir:​

Config Server

Discovery Server (Eureka)

API Gateway

Department Service

Employee Service​


📁 Örnek Yapılandırma Dosyaları
Her bir mikroservis için application-example.yml dosyaları mevcuttur. Bu dosyalar, servislerin yapılandırmalarını örneklemek için kullanılır ve application.yml dosyaları oluşturulurken referans alınabilir.​

🧑‍💻 Katkıda Bulunanlar
Enes İncekara
GitHub | LinkedIn







