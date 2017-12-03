package cucumbersteps;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.en.And;
import static org.junit.Assert.assertEquals;
import io.teamz.course.Account;
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
	
}
