package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PopularEventsTests {

    final static SelenideElement popularEvents = $(byText("Популярные")).parent();
    enum SPORTS {
        FOOTBALL("Футбол", false),
        BASKETBALL("Баскетбол", false),
        TENNIS("Теннис", false),
        EUROLIGA("Евролига", true),
        KHL("КХЛ", true);

        private final String name;
        private final boolean state;

        SPORTS(String name, boolean state) {
            this.name = name;
            this.state = state;
        }
    }

    @BeforeAll
    static void setUp() {
        Configuration.browserSize = "1920*1080";
        Configuration.browser = "chrome";
        Configuration.baseUrl = "https://www.marathonbet.ru/su/";
        Configuration.pageLoadStrategy = "eager";
        //Configuration.holdBrowserOpen = true;
    }

    @ParameterizedTest(name = "Проверка отображения {0} в разделе Популярных событий")
    @ValueSource( strings = {"КХЛ", "Евролига", "Лига ВТБ"})
    void sportShouldBeInPopularByValueSource(String sport) {
        open("");

        sportShouldBeInPopular(sport, true);
    }

    @ParameterizedTest(name = "Проверка отображения {0} в разделе Популярных событий (state={1})")
    @CsvSource({
            "КХЛ, true",
            "Евролига, true",
            "Лига ВТБ, true",
            "Футбол, false",
            "Хоккей, false"
    })
    void sportShouldBeInPopularByCsvSource(String sport, boolean state) {
        open("");

        sportShouldBeInPopular(sport, state);
    }

    @ParameterizedTest(name = "Проверка отображения {0} в разделе Популярных событий (state={1})")
    @CsvFileSource(files = "src/test/resources/popular.csv", useHeadersInDisplayName = false)
    void sportShouldBeInPopularByCsvFileSource(String sport, boolean state) {
        open("");

        sportShouldBeInPopular(sport, state);
    }

    @ParameterizedTest(name = "Проверка отображения {0} в разделе Популярных событий")
    @EnumSource(value = SPORTS.class)
    void sportShouldBeInPopularByEnumSource(SPORTS sport) {
        open("");

        sportShouldBeInPopular(sport.name, sport.state);
    }

    @ParameterizedTest(name = "Проверка отображения {0} в разделе Популярных событий")
    @MethodSource
    void sportShouldBeInPopularByMethodSource(String sport) {
        open("");

        sportShouldBeInPopular(sport, true);
    }

    static Stream<String> sportShouldBeInPopularByMethodSource() {
        return Stream.of("КХЛ", "Евролига", "Лига ВТБ");
    }

    void sportShouldBeInPopular(String sport, boolean state) {
        if (state) {
            popularEvents.shouldHave(text(sport));
        } else {
            popularEvents.shouldNotHave(text(sport));
        }
    }

    @AfterEach
    void afterEach() {
        Selenide.closeWebDriver();
    }
}
