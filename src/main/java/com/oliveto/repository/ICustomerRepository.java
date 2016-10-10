package com.oliveto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oliveto.entity.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> 
{
	public List<Customer> findByNameContaining(String name);
	public List<Customer> findByAddressContaining(String address);
	public List<Customer> findByNameContainingAndAddressContaining(String name, String address);

	public List<Customer> findByName(String name);
	public List<Customer> findByAddress(String address);
	public List<Customer> findByNameAndAddress(String name,String address);

}
