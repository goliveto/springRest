Feature: Customers API 

Scenario: Get all of customers 

	Given Having at customer with phone "123456" name "Michael Jordan" and address "23 Chicago"
	When I get all of Customer 
	Then Response should contain: 
		"""
      {
      "id": "123456",
      "name": "Michael Jordan",
      "address": "23 Chicago"
      }
"""
		
Scenario: Add Customers already exist 
	Given Having at customer with phone "123456" name "Michael Jordan" and address "23 Chicago"
	When I add a new customer 
		"""
      {
      "id": 123456,
      "name": "Other Name",
      "address": "Other Address"
      }
"""
	Then Response has error "500" 
	
Scenario: Try add customer with bad parameter name 
	Given Do not exist customer with phone "123456" name "Michael Jordan" and address "23 Chicago"
	When I add a new customer 
		"""
      {
      "id": 123456,
      "name": "Leo M#essi",
      "address": "12 Victoria"
      }
"""
	Then Response has error "500" 
	
Scenario: Try add customer with bad parameter address 
	Given Do not exist customer with phone "123456" name "Michael Jordan" and address "23 Chicago"
	When I add a new customer 
		"""
      {
      "id": 123456,
      "name": "Leo Messi",
      "address": "12 @Victoria"
      }
"""
	Then Response has error "500" 
	
	
Scenario: Add Customer, update the name and get with the filter address and name 
	Given Do not exist customer with phone "123456" name "Michael Jordan" and address "23 Chicago"
	When I add a new customer 
		"""
      {
      "id": 123456,
      "name": "Emanuel Ginobili",
      "address": "54 White bay"
      }
"""
	When Update customer 
		"""
      {
      "id": 123456,
      "name": "Sebastian Ginobili",
      "address": "24 Hobson"
      }
"""
	When Filter customer with name "Sebastian Gi" and address "Hobson" 
	Then Response should contain: 
		"""
      {
      "id": "123456",
      "name": "Sebastian Ginobili",
      "address": "24 Hobson"
      }
"""
		
Scenario: Find user by Name get the telephone number and delete customer 
	Given Having at customer with phone "123456" name "Sebastian Ginobili" and address "Bahia Blanca"
	When Find by name "Sebastian Ginobili" and delete customer
	Then Do not exist customer with phone "123456" name "Sebastian Ginobili" and address "Bahia Blanca"
		
Scenario: Get customer by filter name 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Filter customer by "name" with "Leo" 
	Then Response should contain: 
		"""
      {
      "id": "123456",
      "name": "Leonel Messi",
      "address": "10 Barcelona"
      }
"""
		
Scenario: Get customer by filter address 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Filter customer by "address" with "Barcelona" 
	Then Response should contain: 
		"""
      {
      "id": "123456",
      "name": "Leonel Messi",
      "address": "10 Barcelona"
      }
"""

Scenario: Find user by name 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Find customer by "name" "Leonel Messi" 
	Then Response should contain: 
		"""
      {
      "id": "123456",
      "name": "Leonel Messi",
      "address": "10 Barcelona"
      }
"""

Scenario: Find user by address 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Find customer by "address" "10 Barcelona" 
	Then Response should contain: 
		"""
      {
      "id": "123456",
      "name": "Leonel Messi",
      "address": "10 Barcelona"
      }
"""
Scenario: Find user by name not succesful 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Find customer by "name" "Leonel" 
	Then Response should contain: 
		"""
"""

Scenario: Find user by address not succesful 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Find customer by "address" "Barcelona" 
	Then Response should contain: 
		"""
"""


		
Scenario: Set a parameter no alphanumeric in filter address 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Filter customer by "address" with "Bar*celona" 
	Then Response has error "500" 
	
	
Scenario: Set a parameter no alphanumeric in filter name 
	Given Having at customer with phone "123456" name "Leonel Messi" and address "10 Barcelona"
	When Filter customer by "name" with "Leo%Messi" 
	Then Response has error "500"