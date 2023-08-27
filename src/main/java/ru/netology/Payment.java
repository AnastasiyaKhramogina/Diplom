package ru.netology;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class Payment {
    private SelenideElement buyButton = $(".button.button_size_m");
    private SelenideElement buyOnCreditButton = $$(".button_view_extra").first(); // Первая кнопка "Купить в кредит"
    private SelenideElement continueButton = $$(".button_view_extra").last(); // Вторая кнопка "Продолжить"
    private SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private SelenideElement monthField = $("input[placeholder='08']");
    private SelenideElement yearField = $("input[placeholder='22']");
    private SelenideElement ownerField = $x("//span[text()='Владелец']/following-sibling::span/input");
    private SelenideElement cvcField = $("input[placeholder='999']");
    private SelenideElement statusOkNotification = $(".notification.notification_status_ok");
    private SelenideElement statusErrorNotification = $(".notification_status_error");
    private SelenideElement inputErrorMessages = $(".input__sub");

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
}
