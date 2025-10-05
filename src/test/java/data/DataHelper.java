package data;

import lombok.Value;

public class DataHelper {

    private DataHelper() {
    }

    // --- Авторизация ---
    public static AuthInfo getAuthInfo () {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo){
        return new VerificationCode("12345");
    }

    // --- Карты ---
    public static CardInfo getFirstCardInfo() {
        return new CardInfo("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0001", "0001");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0002", "0002");
    }

    // --- Вспомогательные классы ---
    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class CardInfo {
        String id;          // data-test-id карты на странице
        String number;      // полный номер карты для ввода перевода
        String lastDigits;  // последние 4 цифры карты для выбора
    }

    // Метод для получения информации о карте по индексу
    public static CardInfo getCardInfo(int index) {
        if (index == 0) {
            return getFirstCardInfo();
        } else if (index == 1) {
            return getSecondCardInfo();
        }
        throw new IllegalArgumentException("Неверный индекс карты: " + index);
    }
}
