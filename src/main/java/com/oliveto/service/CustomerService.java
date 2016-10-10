package com.oliveto.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oliveto.dto.CustomerDto;
import com.oliveto.entity.Customer;
import com.oliveto.exception.DataAPIException;
import com.oliveto.repository.ICustomerRepository;

@Service("customerService")
@Transactional
public class CustomerService {

	private static Logger logger = LoggerFactory.getLogger(CustomerService.class);
	@Autowired
	ICustomerRepository customerDao;

	@PersistenceContext
	private EntityManager entityManager;

	public List<Customer> getAllCustomers() {
		return customerDao.findAll();
	}

	/**
	 * @param id
	 * @return Customer with Id, otherwise an error
	 * @throws DataAPIException
	 */
	public Customer getCustomer(Integer id) throws DataAPIException {

		Customer customer = customerDao.findOne(id);
		if (customer == null) {
			logger.error("Customer does not exist. Id: {}", id);
			throw new DataAPIException("Customer does not exist.");
		}
		return customer;
	}

	
	/**
	 * Create a customer
	 * 
	 * @param customerDto
	 * @return the new customer created
	 * @throws DataAPIException
	 */
	public Customer addCustomer(CustomerDto customerDto) throws DataAPIException {

		if (customerDao.exists(customerDto.getId())) {
			logger.error("Customer already exist. Id: {}", customerDto.getId());
			throw new DataAPIException("Customer already exist.");
		}
		if (!isValidString(customerDto.getAddress()) || !isValidString(customerDto.getName())) {
			logger.error("Field name or address have wrong characters. Name: {}, Address: {}", customerDto.getName(),
					customerDto.getAddress());
			throw new DataAPIException("Field name or address have wrong characters.");
		}
		Customer newCustomer = new Customer(customerDto.getId(), customerDto.getName(),
				customerDto.getAddress());

		return customerDao.save(newCustomer);
	}

	/**
	 * Update a customer
	 * 
	 * @param customerDto
	 * @return customer updated
	 * @throws DataAPIException
	 */
	public Customer updateCustomer(CustomerDto customerDto) throws DataAPIException {

		
		Customer customer = customerDao.findOne(customerDto.getId());
		customer.setAddress(customerDto.getAddress());
		customer.setName(customerDto.getName());

		if (!isValidString(customerDto.getAddress()) || !isValidString(customerDto.getName())) {
			logger.error("Field name or address have wrong characters. Name: {}, Address: {}", customerDto.getName(),
					customerDto.getAddress());
			throw new DataAPIException("Field name or address have wrong characters.");
		}
		customerDao.save(customer);
		return customer;
	}

	/**
	 * @param id
	 * @throws DataAPIException
	 */
	public void deleteCustomer(Integer id) throws DataAPIException {
		if (!customerDao.exists(id)) {
			logger.error("Customer does not exist. Id: {}", id);
			throw new DataAPIException("Customer does not exist.");
		}
		customerDao.delete(id);
	}

	public List<Customer> filterByName(String name) throws DataAPIException {
		if (!isValidString(name)) {
			logger.error("Field name has wrong characters. Name: {}", name);
			throw new DataAPIException("Field name has wrong characters.");
		}
		return customerDao.findByNameContaining(name);
	}

	public List<Customer> filterByAddress(String address) throws DataAPIException {
		if (!isValidString(address)) {
			logger.error("Field address has wrong characters. Address: {}", address);
			throw new DataAPIException("Field address has wrong characters.");
		}
		return customerDao.findByAddressContaining(address);
	}

	public List<Customer> findByName(String name) throws DataAPIException {
		if (!isValidString(name)) {
			logger.error("Field name has wrong characters. Name: {}", name);
			throw new DataAPIException("Field name has wrong characters.");
		}
		return customerDao.findByName(name);
	}

	public List<Customer> findByAddress(String address) throws DataAPIException {
		if (!isValidString(address)) {
			logger.error("Field address has wrong characters. Address: {}", address);
			throw new DataAPIException("Field address has wrong characters.");
		}
		return customerDao.findByAddress(address);
	}

	public List<Customer> filterByNameAndAddress(String name, String address) throws DataAPIException {
		if (!isValidString(name) || !isValidString(address)) {
			logger.error("Field name or address have wrong characters. Name: {0}, Address: {1}", name, address);
			throw new DataAPIException("Field name or address have wrong characters.");
		}
		return customerDao.findByNameContainingAndAddressContaining(name, address);
	}
	
	public List<Customer> findByNameAndAddress(String name, String address) throws DataAPIException {
		if (!isValidString(name) || !isValidString(address)) {
			logger.error("Field name or address have wrong characters. Name: {}, Address: {}", name, address);
			throw new DataAPIException("Field name or address have wrong characters.");
		}
		return customerDao.findByNameAndAddress(name, address);
	}
	

	private boolean isValidString(String toValidate) {
		return toValidate.isEmpty() || toValidate.matches("[A-Za-z0-9\\s]+");
	}

}
