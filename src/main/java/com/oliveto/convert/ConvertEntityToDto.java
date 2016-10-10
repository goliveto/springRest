package com.oliveto.convert;

import org.modelmapper.ModelMapper;

import com.oliveto.dto.CustomerDto;
import com.oliveto.entity.Customer;

public class ConvertEntityToDto {

	/**
	 * Convert from Customer entity to CustomerDto
	 * 
	 * @param customer
	 *            entity
	 * @return customerDto
	 */
	public CustomerDto customerToDto(Customer customer) {
		ModelMapper modelMapper = new ModelMapper();
		CustomerDto postDto = modelMapper.map(customer, CustomerDto.class);
		return postDto;
	}
}
