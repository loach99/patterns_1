import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestData {

        private static Faker faker;
        private WebDriver driver;

        @BeforeAll
        static void setUpAll() {
            WebDriverManager.chromedriver().browserVersion("122").setup();
        }
    
        @BeforeEach
        void setUp() {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            options.addArguments("disable-infobars");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--headless");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-gpu");
            driver = new ChromeDriver(options);
        }
    
        @AfterEach
        void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }
        @Test

        void positiveTest() {

                DataGenerate.UserInfo validUser = DataGenerate.Registration.generateUser("ru");
                int daysToAddForFirstMeeting = 4;
                String firstMeetingDate = DataGenerate.generateDate(daysToAddForFirstMeeting);
                int daysToAddForSecondMeeting = 7;
                String secondMeetingDate = DataGenerate.generateDate(daysToAddForSecondMeeting);

                $("[data-test-id=city] input").setValue(validUser.getCity());
                $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
                $("[data-test-id=date] input").setValue(firstMeetingDate);
                $("[data-test-id=name] input").setValue(validUser.getName());
                $("[data-test-id=phone] input").setValue(validUser.getPhone());
                $("[data-test-id=agreement]").click();
                $("button.button").click();

                $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
                $("[data-test-id='success-notification'] .notification__content")
                                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                                .shouldBe(visible);

                $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
                $("[data-test-id=date] input").setValue(secondMeetingDate);
                $("button.button").click();

                $("[data-test-id='replan-notification'] .notification__content")
                                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                                .shouldBe(visible);

                $("[data-test-id='replan-notification'] button").click();
                $("[data-test-id='success-notification'] .notification__content")
                                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate))
                                .shouldBe(visible);

        }

}
