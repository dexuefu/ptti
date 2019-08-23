package cn.ptti.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Configuration
@SpringBootApplication
public class PortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
	}

	@RequestMapping("/test")
	public String test() {
		System.out.println("sdfsfsfds");
		return "sdfeioijv";
	}
}
