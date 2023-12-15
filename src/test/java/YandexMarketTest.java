import static com.codeborne.selenide.Selenide.open;

import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YandexMarketMainPage;

public class YandexMarketTest extends BaseTest {

  @Feature("Проверка результатов поиска")
  @DisplayName("Проверка результатов поиска c помощью PO")
  @ParameterizedTest(name = "{displayName} {arguments}")
  @MethodSource("helpers.DataProvider#providerCheckingSmartphones")
  public void checkProducts(String url, String title,
      List<String> brands, List<String> correctSearchResults) {
    open(url, YandexMarketMainPage.class)
        .goCatalog()
        .moveToElectronica()
        .goToSmartphones()
        .checkPage(title)
        .chooseBrand(brands)
        .waitSearchResult()
        .checkFindResult(correctSearchResults);
  }
}
