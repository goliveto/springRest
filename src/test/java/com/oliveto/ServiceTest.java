package com.oliveto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.oliveto.dto.CustomerDto;
import com.oliveto.entity.Customer;
import com.oliveto.exception.DataAPIException;
import com.oliveto.repository.ICustomerRepository;
import com.oliveto.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ServiceTest  {
		
	@Autowired
	CustomerService customerService;
	@Autowired
	ICustomerRepository customerDao;

	
	
	@Test
	public void addCustomerTest() {

		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";

		CustomerDto dto = new CustomerDto();
		dto.setId(id);
		dto.setAddress(address);
		dto.setName(name);
		
		assertEquals(0, customerDao.findAll().size());
		assertEquals(true, !customerDao.exists(id));
		try {
			customerService.addCustomer(dto);
		} catch (DataAPIException e) {
			assertThat(false);
		}
		assertEquals(true, customerDao.exists(id));
		assertEquals(1, customerDao.findAll().size());

		Integer id2 = 33333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";

		CustomerDto dto2 = new CustomerDto();
		dto2.setId(id2);
		dto2.setAddress(address2);
		dto2.setName(name2);

		try {
			customerService.addCustomer(dto2);
		} catch (DataAPIException e) {
			assertThat(false);
		}
		assertEquals(2, customerDao.findAll().size());
		assertEquals(true, customerDao.exists(33333333));
	}
		
	@Test (expected = DataAPIException.class)
	public void addCustomerAddressBadParameterTest() throws DataAPIException {

		Integer id = 22222222;
		String address = "32 Victoria Street #";
		String name = "Lebron James ";

		CustomerDto dto = new CustomerDto();
		dto.setId(id);
		dto.setAddress(address);
		dto.setName(name);
		
		assertEquals(0, customerDao.findAll().size());
		customerService.addCustomer(dto);
	}
	
	@Test (expected = DataAPIException.class)
	public void addCustomerNameBadParameterTest() throws DataAPIException {

		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James %";

		CustomerDto dto = new CustomerDto();
		dto.setId(id);
		dto.setAddress(address);
		dto.setName(name);
		
		assertEquals(0, customerDao.findAll().size());
		customerService.addCustomer(dto);
	}
	
	
	@Test
	public void getCustomerTest() throws DataAPIException {
		Integer id  = 022222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);

		Customer actual = customerService.getCustomer(id);
		assertEquals(id, actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(address, actual.getAddress());

	}
	@Test
	public void updateCustomerTest() throws DataAPIException {
		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);


		Customer actual = customerDao.findOne(id);
		assertEquals(id, actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(address, actual.getAddress());

		String newName = "Michael Jordan";
		String newAddress = "54 Remuera";
		
		CustomerDto dto1 = new CustomerDto(id, newName, newAddress);

		customerService.updateCustomer(dto1);

		actual = customerDao.findOne(id);
		assertEquals(id, actual.getId());
		assertEquals(newName, actual.getName());
		assertEquals(newAddress, actual.getAddress());
	}
	@Test
	public void deleteCustomerTest() throws DataAPIException {
		Integer id = 236502222;
		String address = "32 Victoria Street";
		String name = "Lebron James";

		Integer id2 = 035433333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";

		Customer entity = new Customer(id, name, address);
		Customer entity2 = new Customer(id2, name2, address2);

		customerDao.save(entity);
		customerDao.save(entity2);

		customerService.deleteCustomer(id);

		assertEquals(1, customerDao.findAll().size());
		assertEquals(true, customerDao.exists(entity2.getId()));

	}
	@Test
	public void filterByNameTest() throws DataAPIException {
		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);
		
		Integer id3 = 444444444;
		String address3 = "342 Parnell Road";
		String name3 = "Kun Aguero";
		addCustomerBD(id3, name3,address3 );
		
		List<Customer> listCustomer = customerService.filterByName("nel");
		
		assertEquals(1,listCustomer.size());		
		assertEquals(id2, listCustomer.get(0).getId()); 
		assertEquals(name2,listCustomer.get(0).getName());
		assertEquals(address2,listCustomer.get(0).getAddress());
		
		listCustomer = customerService.filterByName("Jose");
		
		assertEquals(0,listCustomer.size());		
	}
	@Test (expected = DataAPIException.class)
	public void filterNameBadParameterTest() throws DataAPIException {
		customerService.filterByName("$");
	}

	
	@Test
	public void filterByAddressTest() throws DataAPIException {
		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);
		
		Integer id3 = 444444444;
		String address3 = "342 Parnell Road";
		String name3 = "Kun Aguero";
		addCustomerBD(id3, name3,address3 );
		
		List<Customer> listCustomer = customerService.filterByAddress("Roa");
		
		assertEquals(1,listCustomer.size());		
		assertEquals(id3, listCustomer.get(0).getId()); 
		assertEquals(name3,listCustomer.get(0).getName());
		assertEquals(address3,listCustomer.get(0).getAddress());
		
		listCustomer = customerService.filterByAddress("Street");
		assertEquals(2,listCustomer.size());		
	}
	@Test (expected = DataAPIException.class)
	public void filterAddressBadParameterTest() throws DataAPIException {
		customerService.filterByAddress(" @ ");
	}

	@Test
	public void findByNameTest() throws DataAPIException {
		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);
		
		Integer id3 = 444444444;
		String address3 = "342 Parnell Road";
		String name3 = "Kun Aguero";
		addCustomerBD(id3, name3,address3 );
		
		List<Customer> listCustomer = customerService.findByName(name2);
		
		assertEquals(1,listCustomer.size());		
		assertEquals(id2, listCustomer.get(0).getId()); 
		assertEquals(name2,listCustomer.get(0).getName());
		assertEquals(address2,listCustomer.get(0).getAddress());
		
		listCustomer = customerService.findByName("Lionel");
		assertEquals(0,listCustomer.size());	
	}
	
	@Test (expected = DataAPIException.class)
	public void findNameBadParameterTest() throws DataAPIException {
		customerService.findByName(" ' ");
	}

	@Test
	public void findByAddressTest() throws DataAPIException {
		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);
		
		Integer id3 = 444444444;
		String address3 = "342 Parnell Road";
		String name3 = "Kun Aguero";
		addCustomerBD(id3, name3,address3 );
		
		List<Customer> listCustomer = customerService.findByAddress(address);
		
		assertEquals(1,listCustomer.size());		
		assertEquals(id, listCustomer.get(0).getId()); 
		assertEquals(name,listCustomer.get(0).getName());
		assertEquals(address,listCustomer.get(0).getAddress());
		
		listCustomer = customerService.findByAddress("Street");
		assertEquals(0,listCustomer.size());	
	}

	@Test (expected = DataAPIException.class)
	public void findAddressBadParameterTest() throws DataAPIException {
		customerService.findByName(" } ");
	}

	@Test
	public void filterByNameAndAddressTest() throws DataAPIException {
		Integer id = 22222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";
		addCustomerBD(id,name,address);
		
		Integer id2 = 333333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";
		addCustomerBD(id2, name2, address2);
		
		Integer id3 = 444444444;
		String address3 = "342 Parnell Road";
		String name3 = "Kun Aguero";
		addCustomerBD(id3, name3,address3 );
		
		List<Customer> listCustomer = customerService.filterByNameAndAddress("Leonel", "Street");
		
		assertEquals(1,listCustomer.size());		
		assertEquals(id2, listCustomer.get(0).getId()); 
		assertEquals(name2,listCustomer.get(0).getName());
		assertEquals(address2,listCustomer.get(0).getAddress());
		
		listCustomer = customerService.filterByNameAndAddress("Kun", "Street");
		assertEquals(0,listCustomer.size());
	}
	
	@Test (expected = DataAPIException.class)
	public void filterByNameAndAddressNameBadParameterTest() throws DataAPIException {
		customerService.filterByNameAndAddress(" } ", "Remuera");
	}

	@Test (expected = DataAPIException.class)
	public void filterByNameAndAddressAddressBadParameterTest() throws DataAPIException {
		customerService.filterByNameAndAddress("Lebron"," {");
	}

	@Test
	public void getAllCustomersTest() {
		Integer id = 054222222;
		String address = "32 Victoria Street";
		String name = "Lebron James";

		Integer id2 = 218333333;
		String address2 = "32 Hobson Street";
		String name2 = "Leonel Messi";

		Customer entity = new Customer(id, name, address);
		Customer entity2 = new Customer(id2, name2, address2);

		customerDao.save(entity);
		customerDao.save(entity2);

		List<Customer> listCustomer = customerService.getAllCustomers();
		assertEquals(2, listCustomer.size());

		for (Customer customTemp : listCustomer) {
			if ((customTemp != entity) && (customTemp != entity2)) {
				assertThat(false);
			}
		}
	}
	
	/**
	 * Create a Customer in the database
	 * @param telephone
	 * @param name
	 * @param address
	 */
	private void addCustomerBD(Integer telephone, String name, String address) {
		Customer entity = new Customer(telephone, name, address);
		customerDao.save(entity);
	}
}
