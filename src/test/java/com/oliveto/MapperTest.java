package com.oliveto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oliveto.convert.ConvertEntityToDto;
import com.oliveto.dto.CustomerDto;
import com.oliveto.entity.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {

	@Autowired
	private ConvertEntityToDto convert;

	@Test
	public void dtoToEntity() {
		Customer entity = new Customer();
		entity.setAddress("123 Victoria Street");
		entity.setName("Gerardo Oliveto");
		entity.setId(123456);
		
		CustomerDto dto = convert.customerToDto(entity);
	
		assertEquals(entity.getName(), dto.getName());
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getAddress(), dto.getAddress());
	}
		
}
