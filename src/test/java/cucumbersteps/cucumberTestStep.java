package cucumbersteps;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import static org.junit.Assert.assertEquals;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.teamz.course.Account;


public class cucumberTestStep {
 String web_driver_msg="";
 String login_msg="";

 ///scenario simple test example to see whether cucumber integrate to STS successfully
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
	   
  /////scenario test for loading web driver
  io.restassured.response.Response response;
 
  @Given("^the api is running on localhost8080")
  public void the_api_is_running_on_localhost8080() throws Throwable{
	  RestAssured.baseURI="http://localhost:8080/";
  }
 
  @When("^the user perform a get request for loadDriver$")
   public void the_user_perform_a_get_request_for_loadDriver() throws Throwable{
	
	  RequestSpecification httpRequest = RestAssured.given();
	  String g="/loadDriver";
	  //response =httpRequest.get("/loadDriver/");
	  response =httpRequest.get(g);
	System.out.println("Response Body is =>  " + response.asString());
	System.out.println(response.getStatusLine());
	web_driver_msg=response.asString();
   }
   
  @Then("^user should see the response from the webpage (.*)$")
   public void user_should_see_the_response_from_the__webpage(String msg) {
	   
	   assertEquals(response.asString(),msg);
	   
	}

  //////scenario for testing the credential login to my.sc.edu
  private String userName;
  private String passWord;
  String postbody;
  
  @Given("^the web driver is loaded successfully$")
  public void the_web_driver_is_loaded_successfully()throws Throwable {
	 // RestAssured.baseURI="http://localhost:8080/";
	  
	//  assertEquals(response.asString(),web_driver_msg);
  }
  
  @And("^the user set the username to be (.*) and password to be (.*)$")
  public void the_user_set_the_username_and_password_to_be(String usrname, String password)throws Throwable {
	  userName=usrname;
	  passWord=password;
	  postbody="{\"username\":\""+userName+"\",\"pass\":\""+passWord+"\"}";
   }
  
  @When("^the user send the post request$")
  public void the_user_send_a_post_request()throws Throwable{
	  //RequestSpecification httpRequest=RestAssured.given().contentType("application/json").body("{\"username\":\"00372484\",\"pass\":\"Li*****\"}");
	  RequestSpecification httpRequest=RestAssured.given().contentType("application/json").body(postbody);
	  response=httpRequest.post("/login/");
	  System.out.println("Response Body is =>  " + response.asString()); 
	  System.out.println(response.getStatusLine());
	  login_msg=response.asString();
  }
  @Then("^user should see the pass duo request on their phone and will see (.*)$")
  public void user_shoud_see_the_webpage_response(String msg) throws Throwable{
	  assertEquals(response.asString(),msg);
	  
  }
  
  
  //////scenario for testing save all courses 
  @Given("^the user successfully login to my.sc.edu$")
  public void the_user_successfully_login_to_myscedu() throws Throwable{
	 
	  //assertEquals(response.asString(),login_msg);
  }
 
  @And("the user send a get request to localhost /saveAllCourses$")
  public void the_user_send_request_to_saveCourse() throws Throwable {
	 // RestAssured.baseURI="http://localhost:8080";

	  RequestSpecification httpRequest = RestAssured.given();
	  
	  response =httpRequest.get("/saveAllCourses");
//	  response =httpRequest.get("/saveCourses/Spring 2018/CSCE$");
	 
	  System.out.println("Response Body is =>  " + response.asString());
  }
  
   @Then("^all of the courses should be saved$")
   public void the_user_should_see_courses_satrt_saving()throws Throwable{
	  String msg= "All courses in my.sc.edu saved to database";
	  assertEquals(response.asString(),msg);
   }
 
  
   
   //////scenario for testing save certain courses 
   @Given("^the user login to my.sc.edu$")
   public void the_user_login_to_myscedu() throws Throwable{
 	 
 	  //assertEquals(response.asString(),login_msg);
   }
   
   @And("the user send a get request to localhost /saveCourses/(.*)/(.*)$")
   public void the_user_send_request_to_saveCourse(String msg1, String msg2) throws Throwable {
 	 
     RequestSpecification httpRequest = RestAssured.given();
 	 String getrequest="/saveCourses/"+msg1+"/"+msg2;
 	//response =httpRequest.get("/saveCourses/Spring 2018/CSCE");
 	response =httpRequest.get(getrequest);
 	System.out.println("Response Body is =>  " + response.asString());
   }
   
    @Then("^chosen courses should be saved$")
    public void chosen_courses_satrt_saving()throws Throwable{
 	  String msg= "Courses successfully saved to database";
 	  assertEquals(response.asString(),msg);
    }
  

}
