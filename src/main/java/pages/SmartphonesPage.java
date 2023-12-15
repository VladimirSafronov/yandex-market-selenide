package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import helpers.Assertions;
import io.qameta.allure.Step;
import java.util.Arrays;
import java.util.List;

/**
 * Page Object раздела Смартфоны
 */
public class SmartphonesPage implements BasePageObject {

  /* Xpath кнопки Показать ещё */
  private static final String SHOW_MORE_BUTTON_XPATH = "//span[text()=\"Показать ещё\"]";

  @Step("Убеждаемся, что перешли в раздел Смартфоны")
  public SmartphonesPage checkPage(String title) {
    String currentTittle = title();
    assert currentTittle != null;
    Assertions.assertTrue(currentTittle.equals(title),
        "Заголовок страницы: " + currentTittle + ", отличается от ожидаемого: " + title);
    return this;
  }

  @Step("Задаем параметр по Производителю")
  public SmartphonesPage chooseBrand(List<String> brands) {
    for (String brand : brands) {
      $x("//span[text()=\"" + brand + "\"]").click();
    }
    return this;
  }

  @Step("Дожидаемся результатов поиска")
  public SmartphonesPage waitSearchResult() {
    $x("//article[@data-autotest-id='product-snippet']").should(
        Condition.editable);
    return this;
  }

  @Step("Проверяем результаты выборки")
  public void checkFindResult(List<String> correctSearchResults) {
    goLastPage();
    List<String> allProductsTitle = $$x("//h3[@data-zone-name='title']").texts();
    Assertions.assertTrue(isResultCorrespond(allProductsTitle, correctSearchResults),
        "Список отфильтрованных товаров содержит производителя отличного от ожидаемого");
  }

  /**
   * Метод проверяет соответствие всех результатов поиска с ожидаемым списком брендов
   *
   * @param products             - список отфильтрованных товаров
   * @param correctSearchResults - все возможные варианты названия брендов
   */
  private boolean isResultCorrespond(List<String> products, List<String> correctSearchResults) {
    for (String product : products) {
      boolean isProductContainsResult = false;
      String[] productData = product.split(" ");
      System.out.println("in isResultCorrespond() product: " + Arrays.toString(productData));
      for (String data : productData) {
        if (correctSearchResults.contains(data)) {
          isProductContainsResult = true;
          System.out.println("Find correspond: " + data + " in " + product);
          break;
        }
      }
      if (!isProductContainsResult) {
        System.out.println("Product " + product + " doesn't correspond");
        return false;
      }
    }
    return true;
  }

  @Step("Проходим на последнюю страницу выборки")
  private void goLastPage() {
    while (isNextPageExists()) {
      $x(SHOW_MORE_BUTTON_XPATH).click();
    }
  }

  /**
   * Метод проверяет имеется ли следующая страница с товарами.
   *
   * @return имеется ли на странице кнопка Показать ещё
   */
  private boolean isNextPageExists() {
    return $x("//div[@data-zone-name='SearchPager']").has(Condition.editable) &&
        $x(SHOW_MORE_BUTTON_XPATH).exists();
  }
}
