package ru.netology;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.Condition;


import java.time.Duration;

public class PaymentPage {


    private SelenideElement buyButton = $(".button.button_size_m");
    private SelenideElement buyOnCreditButton = $$(".button_view_extra").first();
    private SelenideElement continueButton = $$(".button_view_extra").last();
    private SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("input[placeholder='08']");
    private SelenideElement yearField = $("input[placeholder='22']");
    private SelenideElement ownerField = $x("//span[text()='Владелец']/following-sibling::span/input");
    private SelenideElement cvcField = $("input[placeholder='999']");
    private SelenideElement statusOkNotification = $(".notification.notification_status_ok");
    private SelenideElement statusErrorNotification = $(".notification_status_error");
    private SelenideElement inputErrorMessages = $(".input__sub");
    private SelenideElement StatusOkNotificationDisplayed = $(By.cssSelector(".notification.notification_status_ok"));
    private SelenideElement StatusErrorNotificationDisplayed = $(By.cssSelector(".notification.notification_status_error"));

    public void clickBuyButton() {
        buyButton.click();
    }

    public void clickBuyOnCreditButton() {
        buyOnCreditButton.click();
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void enterCardNumber(String cardNumber) {
        cardNumberField.setValue(cardNumber);
    }

    public void enterMonth(String month) {
        monthField.setValue(month);
    }

    public void enterYear(String year) {
        yearField.setValue(year);
    }

    public void enterOwner(String owner) {
        ownerField.setValue(owner);
    }

    public void enterCvc(String cvc) {
        cvcField.setValue(cvc);
    }

    public boolean isStatusOkNotificationDisplayed() {
        return statusOkNotification.isDisplayed();
    }

    public boolean isStatusErrorNotificationDisplayed() {
        return statusErrorNotification.isDisplayed();
    }

    public String getInputErrorMessage() {
        return inputErrorMessages.getText();
    }

    public void waitForStatusOkNotification() {
        statusOkNotification.should(appear, Duration.ofSeconds(15));
    }

    public void waitForStatusErrorNotification() {
        statusErrorNotification.should(appear, Duration.ofSeconds(15));
    }
}