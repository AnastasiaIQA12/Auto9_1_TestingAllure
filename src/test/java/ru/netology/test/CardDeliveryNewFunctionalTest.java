package ru.netology.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import io.qameta.allure.selenide.AllureSelenide;

public class CardDeliveryNewFunctionalTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUpEach() {
        open("http://localhost:9999/");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldCardDeliveryOnOneDate() {
        $("[data-test-id=city] input").setValue("Абакан");
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(3));
        $("[data-test-id=name] input").setValue("Елифанова Анастасия");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(withText("Успешно!")).waitUntil(visible,15000);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(3)));
        $("button.button").click();
        $(withText("У Вас уже запланирована встрача на эту дату. Хотите перепланировать!")).waitUntil(visible,15000);
    }

    @Test
    void shouldCardDeliveryOnDiffrentDate() {
        $("[data-test-id=city] input").setValue(DataGenerator.Registration.generateByCard().getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(3));
        $("[data-test-id=name] input").setValue(DataGenerator.Registration.generateByCard().getName());
        $("[data-test-id=phone] input").setValue(DataGenerator.Registration.generateByCard().getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(withText("Успешно!")).shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(3)));
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(7));
        $("button.button").click();
        $(withText("Необходимо подтверждение")).waitUntil(visible,15000);
        $("[data-test-id=replan-notification] .button").click();
        $(withText("Успешно!")).waitUntil(visible,15000);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(7)));
    }

    @Test
    void shouldCardDeliveryOnDiffrentDateWrongName() {
        $("[data-test-id=city] input").setValue(DataGenerator.Registration.generateByCard().getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(3));
        $("[data-test-id=name] input").setValue("Elifanova");
        $("[data-test-id=phone] input").setValue(DataGenerator.Registration.generateByCard().getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldCardDeliveryOnDiffrentDateWrongPhone() {
        $("[data-test-id=city] input").setValue(DataGenerator.Registration.generateByCard().getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(3));
        $("[data-test-id=name] input").setValue(DataGenerator.Registration.generateByCard().getName());
        $("[data-test-id=phone] input").setValue("89270");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldCardDeliveryOnDiffrentDateNameAndPhone() {
        $("[data-test-id=city] input").setValue(DataGenerator.Registration.generateByCard().getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(3));
        $("[data-test-id=name] input").setValue("Elifanova");
        $("[data-test-id=phone] input").setValue("89270");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldCardDeliveryWithDropDownList() {
        $("[data-test-id=city] input").setValue("Ка");
        $$(".menu-item").find(exactText("Казань")).click();
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(3));
        $("[data-test-id=name] input").setValue(DataGenerator.Registration.generateByCard().getName());
        $("[data-test-id=phone] input").setValue(DataGenerator.Registration.generateByCard().getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(withText("Успешно!")).shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(3)));
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(7));
        $("button.button").click();
        $(withText("Необходимо подтверждение")).waitUntil(visible,15000);
        $("[data-test-id=replan-notification] .button").click();
        $(withText("Успешно!")).waitUntil(visible,15000);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(7)));
    }

    @Test
    void shouldSubmitFormWithCalendar() {
        $("[data-test-id=city] input").setValue(DataGenerator.Registration.generateByCard().getCity());
        String year= DataGenerator.Registration.getDayVisit(3).substring(6);
        int numberMonth= Integer.parseInt(DataGenerator.Registration.getDayVisit(3).substring(3,5));
        String[] month={"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        String day= DataGenerator.Registration.getDayVisit(3).substring(0,2);
        $("[data-test-id=date] button").click();
        String  calendarText=$(".calendar__name").innerText();

        while (calendarText.contains(year)==false)
        {
            $("[data-step='12']").click();
            calendarText =$(".calendar__name").innerText();
        }

        while (calendarText.contains(month[numberMonth-1])==false)
        {
            $("[data-step='1']").click();
            calendarText =$(".calendar__name").innerText();
        }

        ElementsCollection dayInCalendar=$$(".calendar__day");
        for (int i=0; i<dayInCalendar.size(); i++){
            if (dayInCalendar.get(i).innerText().equals(day)){
                dayInCalendar.get(i).click();
                break;
            }
        }

        $("[data-test-id=name] input").setValue(DataGenerator.Registration.generateByCard().getName());
        $("[data-test-id=phone] input").setValue(DataGenerator.Registration.generateByCard().getPhone());
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(withText("Успешно!")).shouldBe(visible);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(3)));
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL+"A"+Keys.DELETE);
        $("[data-test-id=date] input").setValue(DataGenerator.Registration.getDayVisit(7));
        $("button.button").click();
        $(withText("Необходимо подтверждение")).waitUntil(visible,15000);
        $("[data-test-id=replan-notification] .button").click();
        $(withText("Успешно!")).waitUntil(visible,15000);
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText("Встреча успешно запланирована на "+DataGenerator.Registration.getDayVisit(7)));
    }


}
