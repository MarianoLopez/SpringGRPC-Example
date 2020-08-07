package com.z.grpcserver;

import com.z.grpc.HelloRequest;
import com.z.grpc.HelloResponse;
import com.z.grpc.HelloServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
@RequiredArgsConstructor
public class GRPCServerApplication implements ApplicationRunner {
	private final HelloServiceImpl helloService;

	public static void main(String[] args) {
		SpringApplication.run(GRPCServerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Server server = ServerBuilder
				.forPort(8080)
				.addService(helloService)
				.build();

		server.start();
		System.out.println("Server: "+server.toString());
		server.awaitTermination();
	}
}

@Service
class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
	@Override
	public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
		String greeting = "Hello, " +
				request.getFirstName() +
				" " +
				request.getLastName();

		HelloResponse response = HelloResponse.newBuilder()
				.setGreeting(greeting)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
