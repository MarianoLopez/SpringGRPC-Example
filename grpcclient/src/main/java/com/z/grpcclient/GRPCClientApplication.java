package com.z.grpcclient;

import com.z.grpc.HelloRequest;
import com.z.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigurationProperties(GRPCProperties.class)
public class GRPCClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GRPCClientApplication.class, args);
	}

}

@RestController
@RequiredArgsConstructor
class HelloController {
	private final HelloService helloService;

	@GetMapping
	HelloDTOResponse hello(@RequestParam String firstName,
						@RequestParam(required = false, defaultValue = "") String lastName) {
		return helloService.hello(firstName, lastName);
	}
}

@Service
@RequiredArgsConstructor
class HelloService {
	private final HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub;

	HelloDTOResponse hello(String firstName, String lastName) {
		var helloResponse = helloServiceBlockingStub.hello(
				HelloRequest
						.newBuilder()
						.setFirstName(firstName)
						.setLastName(lastName)
						.build()
		);

		return new HelloDTOResponse(helloResponse.getGreeting());
	}
}

@Configuration
class GRPCConfiguration {
	@Bean
	ManagedChannel managedChannel(GRPCProperties properties) {
		return ManagedChannelBuilder
				.forAddress(properties.getHost(), properties.getPort())
				.usePlaintext()
				.build();
	}

	@Bean
	HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub(ManagedChannel managedChannel) {
		return HelloServiceGrpc.newBlockingStub(managedChannel);
	}
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "grpc")
class GRPCProperties {
	private String host = "localhost";
	private int port = 8080;
}

@RequiredArgsConstructor
@Data
class HelloDTOResponse {
	final private String message;
}
