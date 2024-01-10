import allure.CustomAllureSelenide;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

  @BeforeAll
  public static void setup() {
    SelenideLogger.addListener("AllureSelenide",
        new CustomAllureSelenide().screenshots(true).savePageSource(true));
  }

  @BeforeEach
  public void option() {
    Configuration.timeout = 12000;
    Configuration.browser = "chrome";
  }
}
