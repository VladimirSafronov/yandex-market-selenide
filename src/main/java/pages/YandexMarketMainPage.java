package pages;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.page;

import io.qameta.allure.Step;

/**
 * Page Object главной страницы Яндекс Маркет
 */
public class YandexMarketMainPage implements BasePageObject {

  @Step("Перехожу в {buttonName}")
  public <T extends BasePageObject> T clickButton(String buttonName, Class<T> typeNextPage) {
    $x(String.format("//span[contains(text(), '%s')]", buttonName)).click();
    return typeNextPage.cast(page(typeNextPage));
  }
}
