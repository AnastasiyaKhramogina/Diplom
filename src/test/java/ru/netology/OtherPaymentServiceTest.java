package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OtherPaymentServiceTest {
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
        SqlHelper.clearOrderTable();
    }
    @Test
    public void testCardNumberNotFilled() {
        payment.clickBuyOnCreditButton();
        payment.enterMonth(DataHelper.generateMonth());
        payment.enterYear(DataHelper.generateYear());
        payment.enterOwner(DataHelper.generateOwnerName());
        payment.enterCvc(DataHelper.generateCvc());

        payment.clickContinueButton();

        String errorMessage = payment.getInputErrorMessage();
        assertEquals("Неверный формат", errorMessage);
    }
    @Test
    public void testCardNumberLessThan16Digits() {
        payment.clickBuyOnCreditButton();
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
    public void testSpecifyTheYearFrom0To9() {

        payment.clickBuyOnCreditButton();
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
    public void testTheSpecifiedYearIsLessThanTheCurrent() {

        payment.clickBuyOnCreditButton();
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
    public void testTheYearIs6YearsOlderThanTheCurrentOne() {
        payment.clickBuyOnCreditButton();
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
    public void testYearIsEmpty() {

        payment.clickBuyOnCreditButton();
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
    public void testSpecifyTheMonthFrom0To9() {
        payment.clickBuyOnCreditButton();
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
    public void testSpecifiedMonthIsGreaterThan12() {

        payment.clickBuyOnCreditButton();
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
    public void testTheSpecifiedMonthIsLessThanTheCurrent() {

        payment.clickBuyOnCreditButton();
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
    public void testMonthIsEmpty() {

        payment.clickBuyOnCreditButton();
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
    public void testSpecifyTheCvcFrom0To9() {

        payment.clickBuyOnCreditButton();
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
    public void testCvcHas2Digits() {

        payment.clickBuyOnCreditButton();
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
    public void testSpecialCharactersInTheCardholderField() {

        payment.clickBuyOnCreditButton();
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
