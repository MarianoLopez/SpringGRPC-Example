package com.z.grpcserver;

import com.z.grpc.HelloRequest;
import com.z.grpc.HelloResponse;
import com.z.grpc.HelloServiceGrpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class GRPCServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GRPCServerApplication.class, args);
	}

}

@GRpcService(interceptors = LogInterceptor.class)
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

@Slf4j
@Component
class LogInterceptor implements ServerInterceptor {

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
																 Metadata headers,
																 ServerCallHandler<ReqT, RespT> next) {
		log.info("Full method name: {} - headers: {}", call.getMethodDescriptor().getFullMethodName(), headers.toString());
		return next.startCall(call, headers);
	}
}
