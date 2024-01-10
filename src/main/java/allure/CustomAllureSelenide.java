package allure;

import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLog;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Данный класс переопределяет метод AllureSelenide чтобы задавать поведение скриншотрования
 */
public class CustomAllureSelenide extends AllureSelenide {

  private static final Logger LOGGER = LoggerFactory.getLogger(AllureSelenide.class);

  private final boolean saveScreenshots = true;
  private final boolean savePageHtml = true;
  private boolean includeSelenideLocatorsSteps = true;
  private final Map<LogType, Level> logTypesToSave = new HashMap<>();
  private final AllureLifecycle lifecycle;

  public CustomAllureSelenide() {
    this(Allure.getLifecycle());
  }

  public CustomAllureSelenide(final AllureLifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }

  private static Optional<byte[]> getScreenshotBytes() {
    try {
      return WebDriverRunner.hasWebDriverStarted()
          ? Optional.of(((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(
          OutputType.BYTES))
          : Optional.empty();
    } catch (WebDriverException e) {
      LOGGER.warn("Could not get screen shot", e);
      return Optional.empty();
    }
  }

  private static Optional<byte[]> getPageSourceBytes() {
    try {
      return WebDriverRunner.hasWebDriverStarted()
          ? Optional.of(WebDriverRunner.getWebDriver().getPageSource().getBytes(UTF_8))
          : Optional.empty();
    } catch (WebDriverException e) {
      LOGGER.warn("Could not get page source", e);
      return Optional.empty();
    }
  }

  private static String getBrowserLogs(final LogType logType, final Level level) {
    return String.join("\n\n", Selenide.getWebDriverLogs(logType.toString(), level));
  }

  @Override
  public void afterEvent(final LogEvent event) {
      lifecycle.getCurrentTestCaseOrStep().ifPresent(parentUuid -> {
        if (saveScreenshots) {
          getScreenshotBytes()
              .ifPresent(bytes -> lifecycle.addAttachment("Screenshot", "image/png", "png", bytes));
        }
        if (savePageHtml) {
          getPageSourceBytes()
              .ifPresent(
                  bytes -> lifecycle.addAttachment("Page source", "text/html", "html", bytes));
        }
        if (!logTypesToSave.isEmpty()) {
          logTypesToSave
              .forEach((logType, level) -> {
                final byte[] content = getBrowserLogs(logType, level).getBytes(UTF_8);
                lifecycle.addAttachment("Logs from: " + logType, "application/json", ".txt",
                    content);
              });
        }
      });

    if (stepsShouldBeLogged(event)) {
      lifecycle.getCurrentTestCaseOrStep().ifPresent(parentUuid -> {
        switch (event.getStatus()) {
          case PASS:
            lifecycle.updateStep(step -> step.setStatus(Status.PASSED));
            break;
          case FAIL:
            lifecycle.updateStep(stepResult -> {
              stepResult.setStatus(getStatus(event.getError()).orElse(Status.BROKEN));
              stepResult.setStatusDetails(
                  getStatusDetails(event.getError()).orElse(new StatusDetails()));
            });
            break;
          default:
            LOGGER.warn("Step finished with unsupported status {}", event.getStatus());
            break;
        }
        lifecycle.stopStep();
      });
    }
  }

  private boolean stepsShouldBeLogged(final LogEvent event) {
    return includeSelenideLocatorsSteps || !(event instanceof SelenideLog);
  }
}
