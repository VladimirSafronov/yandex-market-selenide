package pages;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.page;

import io.qameta.allure.Step;

/**
 * Page Object каталога Яндекс Маркет
 */
public class YandexMarketCatalogPage implements BasePageObject {

  @Step("Наводим курсор на раздел {elementName}")
  public YandexMarketCatalogPage moveToElement(String elementName) {
    $x(String.format("//a[img]/span[text()='%s']", elementName)).hover();
    return this;
  }

  @Step("Переходим в раздел {sectionName}")
  public <T extends BasePageObject> T goToSection(String sectionName, Class<T> typeNextPage) {
    $x(String.format("//a[text()='%s']", sectionName)).click();
    return typeNextPage.cast(page(typeNextPage));
  }
}
