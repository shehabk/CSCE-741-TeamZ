package cucumberRunner;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/cucumber" },
        glue = "cucumbersteps",
        features = "classpath:cucumber/cucumber.feature"
)

public class RunCucumberTest {

}


