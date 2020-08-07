# SpringCloud
This is a [GRPC](https://grpc.io/) example project with spring boot

# Artifacts    
* **Protos**: This is a library for all the `.proto` files. [reference](https://developers.google.com/protocol-buffers/docs/proto3)
    * Uses: 
        * `io.grpc libraries`
        * `protobuf-maven-plugin`
* **GRPC-server**: Spingboot GRPC server with a HelloService registered.
    * Framework: `Springboot 2.3.2.RELEASE`
    * Uses:
        * `io.github.lognet`: spring boot grpc starter library
* **GRPC-client**: Springboot GRPC client + rest API running in root context path.
    * Framework: `Springboot 2.3.2.RELEASE`
    * Uses:
        * `io.grpc libraries`           
#How to run
* Requirements:
    * JDK 11
    * Maven 3
* Steps
    * build & install the `Proto` artifact into local maven repository 
        ```
            cd proto
            mvn install
        ```
    * build & run the `server` and `client` artifacts 
        ```
            cd grpcclient
            mvn spring-boot:run
      ```
      ```
            cd grpcserver
            mvn spring-boot:run
        ```  
    * Test in the browser with the following URL: [Greeting](http://localhost:8081/?firstName=John&lastName=Doe)