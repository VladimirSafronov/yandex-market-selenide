package pages;

import static com.codeborne.selenide.Selenide.*;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import helpers.Assertions;
import io.qameta.allure.Step;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Page Object раздела Смартфоны
 */
public class SmartphonesPage implements BasePageObject {

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
    System.out.println("Size of products: " + allProductsTitle.size());
    System.out.println("The first product is: " + allProductsTitle.get(0));
    System.out.println("The last product is: " + allProductsTitle.get(allProductsTitle.size() - 1));

    Assertions.assertTrue(isResultCorrespond(allProductsTitle, correctSearchResults),
        "");
  }

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
  private SmartphonesPage goLastPage() {
    while (isNextPageExists()) {
      $x(SHOW_MORE_BUTTON_XPATH).click();
    }
    return this;
  }

  private boolean isNextPageExists() {
    return $x("//div[@data-zone-name='SearchPager']").has(Condition.editable) &&
        $x(SHOW_MORE_BUTTON_XPATH).exists();
  }
}
