package com.oliveto.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oliveto.convert.ConvertEntityToDto;
import com.oliveto.dto.CustomerDto;
import com.oliveto.entity.Customer;
import com.oliveto.exception.DataAPIException;
import com.oliveto.service.CustomerService;

@RestController
public class CustomerController {

	// private final Logger logger =
	// LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	CustomerService customerService;

	@Autowired
	private ConvertEntityToDto convert;

	@RequestMapping(value = "/customers", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CustomerDto> getCountries() {
		List<Customer> listCustomers = customerService.getAllCustomers();
		return listCustomers.stream().map(customer -> convert.customerToDto(customer)).collect(Collectors.toList());
	}

	@RequestMapping(value = "/customers/query", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CustomerDto> getCustomerByFilter(
			@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "address", required = false, defaultValue = "") String address)
			throws DataAPIException {
		List<Customer> listCustomers = customerService.filterByNameAndAddress(name, address);
		return listCustomers.stream().map(customer -> convert.customerToDto(customer)).collect(Collectors.toList());
	}

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public CustomerDto getCustomerById(@PathVariable Integer id) throws DataAPIException {
		return convert.customerToDto(customerService.getCustomer(id));
	}

	@RequestMapping(value = "/customers", method = RequestMethod.POST, headers = "Accept=application/json")
	public CustomerDto addCustomer(@RequestBody CustomerDto customer) throws DataAPIException {
		return convert.customerToDto(customerService.addCustomer(customer));
	}

	@RequestMapping(value = "/customers", method = RequestMethod.PUT, headers = "Accept=application/json")
	public CustomerDto updateCustomer(@RequestBody CustomerDto customer) throws DataAPIException {
		return convert.customerToDto(customerService.updateCustomer(customer));
	}

	@RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public void deleteCustomer(@PathVariable("id") Integer id) throws DataAPIException {
		customerService.deleteCustomer(id);
	}

	@RequestMapping(value = "customers/name/{name}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CustomerDto> filterByName(@PathVariable("name") String name) throws DataAPIException {
		List<Customer> listCustomers = customerService.findByName(name);
		return listCustomers.stream().map(customer -> convert.customerToDto(customer)).collect(Collectors.toList());

	}

	@RequestMapping(value = "customers/address/{address}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CustomerDto> filterByAddress(@PathVariable("address") String address) throws DataAPIException {
		List<Customer> listCustomers = customerService.findByAddress(address);
		return listCustomers.stream().map(customer -> convert.customerToDto(customer)).collect(Collectors.toList());
	}

	@RequestMapping(value = "customers/filterByName/{name}/filterByAddress/{address}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CustomerDto> filterByAddress(@PathVariable("name") String name, @PathVariable("address") String address)
			throws DataAPIException {
		List<Customer> listCustomers = customerService.filterByNameAndAddress(name, address);
		return listCustomers.stream().map(customer -> convert.customerToDto(customer)).collect(Collectors.toList());
	}

}