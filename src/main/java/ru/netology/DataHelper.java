package ru.netology;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

public class DataHelper {
    private static Faker faker = new Faker();

    @Data
    @AllArgsConstructor
    public static class CardInfo {
        private String number;
        private String status;

    }

    public static CardInfo getApprovedCard() {
        CardInfo approved = new CardInfo("4444 4444 4444 4441", "APPROVED");
        return approved;
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo("4444 4444 4444 4442", "DECLINED");
    }

    public static String generateMonth() {
        return String.format("%02d", faker.number().numberBetween(1, 13));
    }

    public static String generateYear() {
        int currentYear = LocalDate.now().getYear() % 100;
        int randomOffset = faker.number().numberBetween(1, 4);
        return String.format("%02d", currentYear + randomOffset);
    }

    public static String generateOwnerName() {
        return faker.name().fullName();
    }

    public static String generateCvc() {
        return String.format("%03d", faker.number().numberBetween(100, 1000));
    }
}
