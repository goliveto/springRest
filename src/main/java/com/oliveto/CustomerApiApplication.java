package com.oliveto;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.oliveto.convert.ConvertEntityToDto;

@SpringBootApplication
@EntityScan("com.oliveto.entity")
public class CustomerApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public ConvertEntityToDto convert()
	{
		return new ConvertEntityToDto();
	}


	public static void main(String[] args) {
		SpringApplication.run(CustomerApiApplication.class, args);
	}
}
