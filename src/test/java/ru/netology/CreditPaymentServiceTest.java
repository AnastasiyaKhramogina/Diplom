package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditPaymentServiceTest {
    private PaymentPage paymentPage;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        paymentPage = open("http://localhost:8080/", PaymentPage.class);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
        SqlHelper.clearCreditRequestTable();
        SqlHelper.clearOrderTable();
    }

    @Test
    public void testCreditCardPaymentDeclined() {
        paymentPage.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isCreditRequestStatusDeclined(), "Статус заявки на кредит не является 'DECLINED'");
    }

    @Test
    public void testCreditCardPaymentApproved() {
        paymentPage.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isCreditRequestStatusApproved(), "Статус заявки на кредит не является 'APPROVED'");
    }

    @Test
    public void testBugCreditCardPaymentDeclined() {

        paymentPage.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        paymentPage.enterCardNumber(cardInfo.getNumber());
        paymentPage.enterMonth(DataHelper.generateMonth());
        paymentPage.enterYear(DataHelper.generateYear());
        paymentPage.enterOwner(DataHelper.generateOwnerName());
        paymentPage.enterCvc(DataHelper.generateCvc());

        paymentPage.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(paymentPage.isStatusErrorNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isCreditRequestStatusDeclined(), "Статус заявки на кредит не является 'DECLINED'");
    }
}
