package steps;

import data.DataHelper;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import page.DashboardPage;
import page.LoginPage;
import page.TransferPage;
import page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferSteps {

    private DashboardPage dashboardPage;
    private TransferPage transferPage;


    // Сохраняем начальные балансы карт
    private int balanceFirstCardBefore;
    private int balanceSecondCardBefore;

    @Допустим("пользователь залогинен с именем {string} и паролем {string}")
    public void userLoggedIn(String login, String password) {
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(login, password);
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());

        // Сохраняем исходные балансы
        balanceFirstCardBefore = dashboardPage.getCardBalanceByLastDigits("0001");
        balanceSecondCardBefore = dashboardPage.getCardBalanceByLastDigits("0002");
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на карту с последними цифрами {string}")
    public void userTransfersMoney(int amount, String fromCardLastDigits, String toCardLastDigits) {
        String fromCardNumber;
        if (fromCardLastDigits.equals(DataHelper.getFirstCardInfo().getLastDigits())) {
            fromCardNumber = DataHelper.getFirstCardInfo().getNumber();
        } else {
            fromCardNumber = DataHelper.getSecondCardInfo().getNumber();
        }

        // Открываем страницу перевода на карту получателя
        transferPage = dashboardPage.selectCardToTransferByLastDigits(toCardLastDigits);

        // Совершаем перевод
        dashboardPage = transferPage.transferFromCard(fromCardNumber, amount);

        // Ждём обновления баланса карты получателя
        dashboardPage = dashboardPage.refresh();
    }

    @Тогда("баланс карты с последними цифрами {string} увеличился на {int} рублей")
    public void checkBalanceIncrease(String cardLastDigits, int expectedIncrease) {
        int initialBalance;
        if (cardLastDigits.equals(DataHelper.getFirstCardInfo().getLastDigits())) {
            initialBalance = balanceFirstCardBefore;
        } else {
            initialBalance = balanceSecondCardBefore;
        }
        int actualBalance = dashboardPage.getCardBalanceByLastDigits(cardLastDigits);
        int expectedBalance = initialBalance + expectedIncrease; // вычисляем конечный баланс
        assertEquals(expectedBalance, actualBalance,
                "Баланс карты с последними цифрами " + cardLastDigits + " не совпадает");
    }

}
