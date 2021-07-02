// Authors: Liplan Lekipising and catherine Muthoni
package com.autoparts.autoparts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutopartsApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AutopartsApplication.class, args);

	}
}
