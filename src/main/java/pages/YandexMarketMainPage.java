package pages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

/**
 * Page Object главной страницы Яндекс Маркет
 */
public class YandexMarketMainPage implements BasePageObject {

  /* Xpath кнопки Каталог */
  private static final String CATALOG_BUTTON_XPATH = "//div[@data-zone-name='catalog']";

  @Step("Перехожу в Каталог")
  public YandexMarketCatalogPage goCatalog() {
    $x(CATALOG_BUTTON_XPATH).click();
    return page(YandexMarketCatalogPage.class);
  }
}
