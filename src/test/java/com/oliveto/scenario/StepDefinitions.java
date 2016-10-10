package com.oliveto.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.oliveto.dto.CustomerDto;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitions {
	
	private static String SERVER_LOCATION = "http://localhost:8080"; 
	private String lastResponse;
	
	//*******************GIVEN*******************//
	
	@Given("^Having at customer with phone \"(.+)\" name \"(.+)\" and address \"(.+)\"$" )
	public void having_at_customer_with(Integer id, String name, String address) throws Throwable {
	
		CustomerDto customerDto = new CustomerDto(id,name,address);
		if(!customerExist(customerDto))
		{
			addCustomerThatNeed(customerDto);
		}
	}


	@Given("^Do not exist customer with phone \"(.+)\" name \"(.+)\" and address \"(.+)\"$")
	public void do_not_exist_customer_with(Integer id, String name, String address) throws ClientProtocolException, IOException {
	
		CustomerDto dto = new CustomerDto(id,name,address);
		if (customerExist(dto))
		{
			deleteCustomer(dto.getId());
		}
	}

	//*******************WHEN*******************//
	@When("^Find by name \"(.+)\" and delete customer$")
	public void find_by_name_and_delete_customer(String name) throws HttpResponseException, IOException {
		
		find_customer_by("name",name);
		List<CustomerDto> actualList = parseToCustomerDTO();
		assertNotEquals(0, actualList.size());

		deleteCustomer(actualList.get(0).getId());
	}
	
	@When ("^Delete customer \"(.+)\"$")
	public void deleteCustomer(Integer id) throws ClientProtocolException, IOException
	{
		String url = encodeSpace(SERVER_LOCATION+"/testLPS/customers/"+id);
		HttpDelete request = new HttpDelete(url);
		request.addHeader("content-type", "application/json");

		executeUrl(request);
	}

	@When("^I get all of Customer$")
	public void getCustomers() throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(SERVER_LOCATION+"/testLPS/customers");
		request.addHeader("content-type", "application/json");

		executeUrl(request);
	}

	@When("^Filter customer by \"(.+)\" with \"(.+)\"$")
	public void filter_customer_by_entity_with(String entity, String value) throws ClientProtocolException, IOException
	{
		String url = encodeSpace(SERVER_LOCATION+"/testLPS/customers/query?"+entity+"="+value);
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");
	
		executeUrl(request);
	}


	@When("^I add a new customer$")
	public void addNewCustomer(String jsonStringCustomer) throws ClientProtocolException, IOException {

		HttpPost request = new HttpPost(SERVER_LOCATION+"/testLPS/customers");
		request.addHeader("content-type", "application/json");
		HttpEntity entity = new ByteArrayEntity(jsonStringCustomer.getBytes("UTF-8"));
		request.setEntity(entity);

		executeUrl(request);
	}

	@When("^Update customer$")
	public void update_customer_with_telephone(String jsonStringCustomer) throws ClientProtocolException, IOException {

		HttpPut request = new HttpPut(SERVER_LOCATION+"/testLPS/customers");
		request.addHeader("content-type", "application/json");
		HttpEntity entity = new ByteArrayEntity(jsonStringCustomer.getBytes("UTF-8"));
		request.setEntity(entity);

		executeUrl(request);

	}

	@When("^Filter customer with name \"(.+)\" and address \"(.+)\"$")
	public void filter_customer_with_name_and_address(String name, String address)
			throws ClientProtocolException, IOException {
		String url = encodeSpace(SERVER_LOCATION+"/testLPS/customers/filterByName/" + name + "/filterByAddress/" + address);
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");

		executeUrl(request);
	}

	@When("^Find customer by \"(.+)\" \"(.+)\" $")
	public void find_customer_by(String entity, String name)
			throws ClientProtocolException, IOException {
		String url = encodeSpace(SERVER_LOCATION+"/testLPS/customers/query?"+entity+"=" + name);
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");

		executeUrl(request);
	}
	
	
	//*******************THEN*******************//
	@Then("^Response should contain:$")
	public void checkResponse(String expectedJsonString) throws HttpResponseException, IOException {
		
		List<CustomerDto> actualList = parseToCustomerDTO();

		CustomerDto expected = new Gson().fromJson(expectedJsonString, CustomerDto.class);

		assertTrue(actualList.contains(expected));
	}

	@Then("^Response has error \"(.+)\"$")
	public void response_has_error(String errorNumber) throws HttpResponseException, IOException {
		assertTrue(lastResponse.contains("\"status\":"+errorNumber));
	}

	
	
	/**
	 * Parse a string json value to List<CustomerDto> class
	 * 
	 * @return List<CustomerDto> 
	 * @throws HttpResponseException
	 * @throws IOException
	 */
	private List<CustomerDto> parseToCustomerDTO() throws HttpResponseException, IOException {

		Type listType = new TypeToken<List<CustomerDto>>() {
		}.getType();
		List<CustomerDto> customerList = new Gson().fromJson(lastResponse, listType);

		return customerList;
	}

	/**
	 * Create a customer with specific dto values 
	 * @param dto
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private void addCustomerThatNeed(CustomerDto dto) throws ClientProtocolException, IOException {
	
		HttpPost request = new HttpPost(SERVER_LOCATION+"/testLPS/customers");
		request.addHeader("content-type", "application/json");
		
		HttpEntity httpEntity = new ByteArrayEntity(new ObjectMapper().writeValueAsBytes(dto));
		request.setEntity(httpEntity);
		
		executeUrl(request);
	}


	private void executeUrl(HttpRequestBase request)
			throws IOException, ClientProtocolException, HttpResponseException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
	
		HttpResponse response = httpClient.execute(request);
		assertEquals(200, response.getStatusLine().getStatusCode());
		lastResponse = new BasicResponseHandler().handleResponse(response);
	}

	/**
	 * Check if exist a customer with value in the entity
	 * @param dto
	 * @return true if the customer exist in the system
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private boolean customerExist(CustomerDto dto) throws ClientProtocolException, IOException {
		String url = encodeSpace(SERVER_LOCATION+"/testLPS/customers/" + dto.getId());
		HttpGet request = new HttpGet(url);
		request.addHeader("content-type", "application/json");
		
		executeUrl(request);

		if (lastResponse.contains("status"))
		{
			return false;
		}
		
		CustomerDto res = new Gson().fromJson(lastResponse, CustomerDto.class);
		
		//exist id
		if( res.getId().equals(dto.getId()) )
		{ //update with other parameters
			update_customer_with_telephone(new ObjectMapper().writeValueAsString(dto));
			return true;
		}
		return false;
	}


	private String encodeSpace(String string) {
		return string.replaceAll("%", "%25").replaceAll(" ", "%20");
	}
}
