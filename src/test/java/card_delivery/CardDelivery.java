package card_delivery;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import model.DataGenerator;
import model.OrderFormData;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import static com.codeborne.selenide.Condition.*;
import static org.assertj.core.api.Assertions.assertThat;

import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CardDelivery {

    private OrderFormData formData = DataGenerator.generateData();
    private String orderDate;

    private SelenideElement city = $(By.xpath("//*[@placeholder='Город']"));
    private SelenideElement dropDownListCities = $(By.xpath("//*[@class='menu-item__control']"));
    private SelenideElement dateElement = $("[data-test-id=date] input[class=input__control]");
    private SelenideElement name = $(By.xpath("//*[@data-test-id='name']//*[@type='text']"));
    private SelenideElement phone = $(By.xpath("//*[@data-test-id='phone']//*[@type='tel']"));
    private SelenideElement agreement = $(By.xpath("//*[@data-test-id='agreement']"));

    private SelenideElement planSuccessElement = $(By.xpath("//*[@data-test-id='success-notification']"));
    private ElementsCollection btnElement = $$(By.xpath("//*[@role='button']"));
    private SelenideElement replannBtnElement = $(By.xpath("//*[@data-test-id='replan-notification']"));
    private SelenideElement exitBtnElement = $(By.xpath("//*[contains(@class, 'notification__closer')]"));


    @Test
    @Order(0)
    @DisplayName("Успешный заказ доставки карты")
    public void cardDeliveryPositive() {
        open("http://localhost:9999");

        city.setValue(formData.getCity());
        dropDownListCities.click();

        dateElement.sendKeys(Keys.COMMAND + "a");
        dateElement.sendKeys(Keys.DELETE);
        orderDate = DataGenerator.getOrderDate(3);
        dateElement.setValue(orderDate);

        name.setValue(formData.getName());
        phone.setValue(formData.getPhoneNumber());
        agreement.click();

        btnElement.findBy(exactText("Запланировать")).click();

        planSuccessElement.shouldHave(text("Встреча успешно запланирована на " + orderDate));
        exitBtnElement.click();
    }


    @Test
    @Order(1)
    @DisplayName("Успешный заказ доставки карты - Перепланирование")
    public void cardDeliveryPositiveReplanning() {
        open("http://localhost:9999");

        city.setValue(formData.getCity());
        dropDownListCities.click();

        dateElement.sendKeys(Keys.COMMAND + "a");
        dateElement.sendKeys(Keys.DELETE);
        orderDate = DataGenerator.getOrderDate(4);
        dateElement.setValue(orderDate);

        name.setValue(formData.getName());
        phone.setValue(formData.getPhoneNumber());
        agreement.click();
        btnElement.findBy(exactText("Запланировать")).click();

        planSuccessElement.shouldHave(text("Встреча успешно запланирована на " + orderDate));
        exitBtnElement.click();

        dateElement.sendKeys(Keys.COMMAND + "a");
        dateElement.sendKeys(Keys.DELETE);
        orderDate = DataGenerator.getOrderDate(5);
        dateElement.setValue(orderDate);
        btnElement.findBy(exactText("Запланировать")).click();

        replannBtnElement.shouldHave(text("Необходимо подтверждение"));
        btnElement.find(exactText("Перепланировать")).click();
        planSuccessElement.shouldHave(text("Встреча успешно запланирована на " + orderDate));
        exitBtnElement.click();
    }


    @Test
    @Order(2)
    @DisplayName("Заказ доставки карты - Невалидные данные в поле 'Город'")
    public void cardDeliveryOneNegative() {
        open("http://localhost:9999");

        city.setValue("Moscow");

        dateElement.sendKeys(Keys.COMMAND + "a");
        dateElement.sendKeys(Keys.DELETE);
        orderDate = DataGenerator.getOrderDate(3);
        dateElement.setValue(orderDate);

        name.setValue(formData.getName());
        phone.setValue(formData.getPhoneNumber());
        agreement.click();
        btnElement.findBy(exactText("Запланировать")).click();

        assertThat($(By.xpath("//*[@data-test-id='city']//*[@class='input__sub']")).getText()).isEqualTo("Доставка в выбранный город недоступна");
    }

    @Test
    @Order(3)
    @DisplayName("Заказ доставки карты - Невалидные данные в поле 'Дата встречи'")
    public void cardDeliveryTwoNegative() {
        open("http://localhost:9999");

        city.setValue(formData.getCity());
        dropDownListCities.click();

        dateElement.sendKeys(Keys.COMMAND + "a");
        dateElement.sendKeys(Keys.DELETE);
        dateElement.setValue("22.10.2011");
        dateElement.setValue(orderDate);

        name.setValue(formData.getName());
        phone.setValue(formData.getPhoneNumber());
        agreement.click();
        btnElement.findBy(exactText("Запланировать")).click();

        assertThat($(By.xpath("//*[@data-test-id='date']//*[@class='input__sub']")).getText()).isEqualTo("Заказ на выбранную дату невозможен");
    }


    @Test
    @Order(4)
    @DisplayName("Заказ доставки карты - Невалидные данные в поле 'Фамилия и имя'")
    public void cardDeliveryThreeNegative() {
        open("http://localhost:9999");

        city.setValue(formData.getCity());
        dropDownListCities.click();

        dateElement.sendKeys(Keys.COMMAND + "a");
        dateElement.sendKeys(Keys.DELETE);
        orderDate = DataGenerator.getOrderDate(4);
        dateElement.setValue(orderDate);

        name.setValue("Ivanov");
        phone.setValue(formData.getPhoneNumber());
        agreement.click();
        btnElement.findBy(exactText("Запланировать")).click();

        assertThat($(By.xpath("//*[@data-test-id='name']//*[@class='input__sub']")).getText()).isEqualTo("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.");
    }
}