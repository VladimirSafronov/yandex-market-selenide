package pages;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.page;

import io.qameta.allure.Step;

/**
 * Page Object каталога Яндекс Маркет
 */
public class YandexMarketCatalogPage implements BasePageObject {

  /* Xpath секции Электроника */
  private static final String SECTION_ELECTRONIC_XPATH =
      "//ul[@role='tablist']/li[@role='tab']/a/span[text()=\"Электроника\"]";

  /* Xpath раздела Смартфоны */
  private static final String SECTION_SMARTPHONE_XPATH = "//a[text()=\"Смартфоны\"]";

  @Step("Наводим курсор на раздел Электроника")
  public YandexMarketCatalogPage moveToElectronica() {
    $x(SECTION_ELECTRONIC_XPATH).hover();
    return this;
  }

  @Step("Переходим в раздел \"Смартфоны\"")
  public SmartphonesPage goToSmartphones() {
    $x(SECTION_SMARTPHONE_XPATH).click();
    return page(SmartphonesPage.class);
  }
}
