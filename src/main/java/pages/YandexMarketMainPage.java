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
  @Step("Перехожу в Каталог")
  public YandexMarketCatalogPage goCatalog() {
    $x("//div[@data-zone-name='catalog']").click();
    return page(YandexMarketCatalogPage.class);
  }
}
