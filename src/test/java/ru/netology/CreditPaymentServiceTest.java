package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditPaymentServiceTest {
    private Payment payment;

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
        payment = open("http://localhost:8080/", Payment.class);
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
        SqlHelper.clearCreditRequestTable();
        SqlHelper.clearOrderTable();
    }

    @Test
    public void testCreditCardPaymentApproved() {
        payment.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(payment.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isCreditRequestStatusApproved(), "Статус заявки на кредит не является 'APPROVED'");
    }

    @Test
    public void testCreditCardPaymentDeclined() {
        payment.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(payment.isStatusOkNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isCreditRequestStatusDeclined(), "Статус заявки на кредит не является 'DECLINED'");
    }

    @Test
    public void testDecliningCreditCardPayment() {

        payment.clickBuyOnCreditButton();

        DataHelper.CardInfo cardInfo = DataHelper.getDeclinedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        Selenide.sleep(10000);

        assertTrue(payment.isStatusErrorNotificationDisplayed(), "Всплывающее окно об операции не отобразилось");

        assertTrue(SqlHelper.isCreditRequestStatusDeclined(), "Статус заявки на кредит не является 'DECLINED'");
    }
}
