package pages;

import static com.codeborne.selenide.Condition.editable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfElementsToBeLessThan;

import com.codeborne.selenide.Selenide;
import helpers.Assertions;
import io.qameta.allure.Step;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;

/**
 * Page Object раздела
 */
public class SectionPage implements BasePageObject {

  /**
   * Xpath кнопки Вперёд
   */
  private final String nextButtonXpath = "//span[text()='Вперёд']";
  /**
   * Xpath блока с пагинацией
   */
  private final String pagerBlockXpath = "//div[@data-zone-name='SearchPager']";
  /**
   * Xpath названия продукта
   */
  private final String productNameXpath = "//h3[@data-auto='snippet-title-header']";
  /**
   * Xpath изображения продукта
   */
  private final String contentPicture = "//div[@data-zone-name='picture']";
  /**
   * Xpath прелоадера
   */
  private final String preloaderXpath = "//div[@data-auto='preloader']";
  /**
   * Количество прелодеров на странице во время загрузки контента
   */
  private final int preloaderCountWhenLoad = 3;
  /**
   * Список, содержащий весь искомый контент
   */
  private List<String> allProducts = new ArrayList<>();

  @Step("Убеждаемся, что перешли в раздел с заголовком {title}")
  public SectionPage checkPage(String title) {
    String currentTittle = title();
    assert currentTittle != null;
    Assertions.assertTrue(currentTittle.equals(title),
        "Текущий заголовок страницы: " + currentTittle + ", отличается от ожидаемого: " + title);
    return this;
  }

  @Step("Задаем параметр по {brands}")
  public SectionPage chooseBrand(List<String> brands) {
    for (String brand : brands) {
      $x("//span[text()='" + brand + "']").click();
      waitNumberOfElements(preloaderXpath, preloaderCountWhenLoad);
    }
    return this;
  }

  @Step("Дожидаемся результатов поиска")
  public SectionPage waitSearchResult() {
    $x(contentPicture).should(visible, editable);
    return this;
  }

  @Step("Проверяем результаты выборки")
  public void checkFindResult(List<String> correctSearchResults) {
    goLastPage();
    isResultsCorrespond(correctSearchResults);
  }

  @Step("Проходим на последнюю страницу выборки")
  private void goLastPage() {
    while (isNextPageExists()) {
      savePageContent();
      $x(nextButtonXpath).click();
      waitNumberOfElements(preloaderXpath, preloaderCountWhenLoad);
    }
    savePageContent();
  }

  /**
   * Метод проверяет соответствие всех результатов поиска с ожидаемым списком брендов
   *
   * @param correctSearchResults - всевозможные варианты названия брендов
   */
  private void isResultsCorrespond(List<String> correctSearchResults) {
    List<String> notCorrespondProducts = new ArrayList<>();
    for (String product : allProducts) {
      if (!isProductCorrespond(product, correctSearchResults, " ")) {
        notCorrespondProducts.add(product);
      }
    }
    Assertions.assertTrue(notCorrespondProducts.isEmpty(),
        "Список товаров не соответствующих фильтру: " + notCorrespondProducts);
  }

  /**
   * Метод ищет всевозможные соответствия названия продукта, разделяя данные одного продукта по
   * параметру
   *
   * @param product              - данные одного продукта
   * @param correctSearchResults - всевозможные варианты названия брендов
   * @return - найдено ли соответствие товара
   */
  private boolean isProductCorrespond(String product, List<String> correctSearchResults,
      String splitter) {
    String[] productData = product.split(splitter);
    for (String data : productData) {
      if (correctSearchResults.contains(data)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Метод ожидает необходимого количества элементов на странице, и дальнейшего уменьшения элементов
   * (например, появления, и исчезновения прелоадера)
   *
   * @param xPath - xpath элемента
   * @param count - количество элементов
   */
  private void waitNumberOfElements(String xPath, int count) {
    Selenide.Wait()
        .until(numberOfElementsToBe(By.xpath(xPath), count));
    Selenide.Wait()
        .until(numberOfElementsToBeLessThan(By.xpath(xPath), count));
  }

  /**
   * Метод сохраняет весь контент со страницы в результирующий список
   */
  private void savePageContent() {
    $x(pagerBlockXpath).scrollTo();
    addProducts($$x(productNameXpath).texts());
  }

  /**
   * Метод сохраняет список товаров в итоговый список
   *
   * @param products - список товаров
   */
  private void addProducts(List<String> products) {
    allProducts.addAll(products);
  }

  /**
   * Метод проверяет имеется ли следующая страница с товарами
   *
   * @return имеется ли на странице кнопка Вперёд
   */
  private boolean isNextPageExists() {
    return $x(pagerBlockXpath).is(editable) && $x(nextButtonXpath).isDisplayed();
  }
}
