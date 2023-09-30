package ru.netology;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.SneakyThrows;

public class SqlHelper {

    private final static String DB_URL = System.getProperty("DB_URL");
    private final static String DB_USER = System.getProperty("DB_USER");
    private final static String DB_PASSWORD = System.getProperty("DB_PASSWORD");

    public static boolean isPaymentStatusApproved() {
        return isStatusEquals("payment_entity", "APPROVED");
    }

    public static boolean isPaymentStatusDeclined() {
        return isStatusEquals("payment_entity", "DECLINED");
    }

    public static boolean isCreditRequestStatusApproved() {
        return isStatusEquals("credit_request_entity", "APPROVED");
    }

    public static boolean isCreditRequestStatusDeclined() {
        return isStatusEquals("credit_request_entity", "DECLINED");
    }

    @SneakyThrows
    private static boolean isStatusEquals(String tableName, String expectedStatus) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM " + tableName + " WHERE status = ?")) {
            statement.setString(1, expectedStatus);

            return statement.executeQuery().next();
        }
    }

    public static void clearPaymentTable() {
        executeUpdate("DELETE FROM payment_entity;");
    }

    public static void clearCreditRequestTable() {
        executeUpdate("DELETE FROM credit_request_entity;");
    }

    public static void clearOrderTable() {
        executeUpdate("DELETE FROM order_entity;");
    }

    @SneakyThrows
    private static void executeUpdate(String query) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        }
    }
}
