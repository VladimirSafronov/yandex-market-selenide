package helpers;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Данный класс содержит параметры для @MethodSource
 */
public class DataProvider {

  public static Stream<Arguments> providerCheckingSmartphones() {
    final List<String> brands = List.of("Apple");
    final List<String> correctSearchResults = List.of("IPhone", "iphone", "Iphone", "IPHONE",
        "iPhone");

    return Stream.of(Arguments.of(
        "https://market.yandex.ru/", "Каталог", "Электроника", "Смартфоны",
        "Смартфоны — купить по низкой цене на Яндекс Маркете",
        brands, correctSearchResults
    ));
  }
}
