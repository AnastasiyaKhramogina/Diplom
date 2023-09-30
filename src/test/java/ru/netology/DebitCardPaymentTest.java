package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitCardPaymentTest {
    private PaymentPage payment;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        System.setProperty("DB_URL", "jdbc:postgresql://localhost:5432/app");
        System.setProperty("DB_USER", "app");
        System.setProperty("DB_PASSWORD", "pass");
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        payment = open("http://localhost:8080/", PaymentPage.class);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
        SqlHelper.clearPaymentTable();
        SqlHelper.clearOrderTable();
    }

    @Test
    public void testDebitCardPaymentApproved() {
        payment.clickBuyButton();

        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        payment.waitForStatusOkNotification();

        assertTrue(payment.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isPaymentStatusApproved(), "Статус платежа не является 'APPROVED'");
    }

    @Test
    public void testDebitCardPaymentDeclined() {
        payment.clickBuyButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        payment.waitForStatusOkNotification();

        assertTrue(payment.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isPaymentStatusDeclined(), "Статус платежа не является 'DECLINED'");
    }

    @Test
    public void testIncorrectDebitCardNumber() {
        payment.clickBuyButton();

        payment.enterCardNumber("8888888888888888");
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        payment.waitForStatusErrorNotification();

        assertTrue(payment.isStatusErrorNotificationDisplayed(), "Всплывающее окно с ошибкой");
    }


    @Test
    public void testDecliningDebitCardPayment() {

        payment.clickBuyButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        payment.waitForStatusErrorNotification();

        assertTrue(payment.isStatusErrorNotificationDisplayed(), "Всплывающее окно с ошибкой не отобразилось");

        assertTrue(SqlHelper.isPaymentStatusDeclined(), "Статус платежа не является 'DECLINED'");
    }

}