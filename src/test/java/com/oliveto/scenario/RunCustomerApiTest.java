package com.oliveto.scenario;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions (
		format = { "pretty", "html:target/cucumber" } ,
		glue={"com.oliveto.scenario"},
		features= "classpath:customer.feature"
)
public class RunCustomerApiTest {
	
	
}	