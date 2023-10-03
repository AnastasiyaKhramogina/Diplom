package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DebitCardPaymentTest {
    private PaymentPage payment;

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
    public void testDebitCardPaymentDeclined() {

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

    @Test
    public void testDebitCardNumberNotFilled() {
        payment.clickBuyButton();
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testDebitCardNumberLessThan16Digits() {
        payment.clickBuyButton();
        payment.enterCardNumber("759456321567859");
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testSpecifyTheYearFrom0To9ForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear("5");
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testTheSpecifiedYearIsLessThanTheCurrentForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear("21");
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Истёк срок действия карты", errorMessage);
    }

    @Test
    public void testTheYearIs6YearsOlderThanTheCurrentOneForDebitCard() {
        payment.clickBuyButton();
        ;
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear("30");
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверно указан срок действия карты", errorMessage);
    }

    @Test
    public void testYearIsEmptyForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testSpecifyTheMonthFrom0To9ForDebitCard() {
        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth("8");
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testSpecifiedMonthIsGreaterThan12ForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth("14");
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверно указан срок действия карты", errorMessage);
    }

    @Test
    public void testTheSpecifiedMonthIsLessThanTheCurrentForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth("05");
        payment.enterYear("23");
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверно указан срок действия карты", errorMessage);
    }

    @Test
    public void testMonthIsEmptyForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testSpecifyTheCvcFrom0To9ForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc("4");

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

    @Test
    public void testCvcHas2DigitsForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc("74");

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }


    @Test
    public void testSpecialCharactersInTheCardholderFieldForDebitCard() {

        payment.clickBuyButton();
        DataHelper.CardInfo cardInfo = DataHelper.getApprovedCard();
        payment.enterCardNumber(cardInfo.getNumber());
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner("!%?*");
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }

}

