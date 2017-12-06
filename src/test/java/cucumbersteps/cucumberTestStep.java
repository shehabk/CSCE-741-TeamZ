package cucumbersteps;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.gargoylesoftware.htmlunit.javascript.host.fetch.Response;

import ch.qos.logback.classic.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.restassured.response.*;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.teamz.course.Account;
import io.teamz.course.CourseRepository;
import io.teamz.course.CourseService;
import org.springframework.http.HttpStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class cucumberTestStep {
	
	private Account a;
	@Given("^I have an Account$")
	public void i_have_an_Account() throws Throwable{
		a=new Account();
	}
	@And("^I set user name as (.*)$")
	public void i_set_user_name_as(String usrName) throws Throwable{
		a.setUsername(usrName);
	}
	@And("^I set password as (.*)$")
	public void i_set_password_as(String pass) throws Throwable{
		a.setPass(pass);
	}
	@Then("^the account should have the user Name as (.*) and password as (.*)$")
	public void the_account_have_usrName_and_pass_as(String aUsr, String apass) {
		assertEquals(aUsr,a.getUsername());
		assertEquals(apass, a.getPass());
	}
	
  
  io.restassured.response.Response response;
  @Given("^the api is running on localhost8080")
  public void the_api_is_running_on_localhost8080() throws Throwable{
	  RestAssured.baseURI="http://localhost:8080/";
  }
  @When("^the user perform a get request for loadDriver$")
   public void the_user_perform_a_get_request_for_loadDriver() throws Throwable{
	  //RestAssured.baseURI="http://localhost:8080/";
	  RequestSpecification httpRequest = RestAssured.given();
	  response =httpRequest.get("/loadDriver/");
	// System.out.println("Response Body is =>  " + response.asString());
   }
   @Then("^user should see the response from the webpage (.*)$")
   public void user_should_see_the_response_from_the__webpage(String msg) {
	   
	   assertEquals(response.asString(),msg);
	   
	   
   }
   

}
