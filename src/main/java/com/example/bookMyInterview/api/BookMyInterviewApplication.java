package com.example.bookMyInterview.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
 
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@EnableHystrix
public class BookMyInterviewApplication {
	@Autowired
	private RestTemplate template;
	
	
	@HystrixCommand(groupKey="example",commandKey="example",fallbackMethod="bookMyInterviewFallBAck")
	   @GetMapping("/bookNow")
	public String bookInterview() 
	{
		String emailServiceResponce= template.getForObject("http:/localhost:8181/emailService/send", String.class);
		String paymentServiceResponce= template.getForObject("http:/localhost:8282/paymentService/pay", String.class);
		return emailServiceResponce+"\n"+paymentServiceResponce; 
	}

	 @GetMapping("/bookNowWithoutHystrix")
		public String bookInterviewWithoutHystrix() 
		{
			String emailServiceResponce= template.getForObject("http:/localhost:8181/emailService/send", String.class);
			String paymentServiceResponce= template.getForObject("http:/localhost:8282/paymentService/pay", String.class);
			return emailServiceResponce+"\n"+paymentServiceResponce; 
		}
	
	
	
	
	public static void main(String[] args) {

		SpringApplication.run(BookMyInterviewApplication.class, args);
	}
	
	
	public String bookMyInterveiwFallBack() {
		return "service gateway failed.....";
	}
	
	@Bean
	public RestTemplate template() {
    return  new RestTemplate();
}
	}
