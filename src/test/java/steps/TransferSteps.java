package steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import data.DataHelper;
import page.DashboardPage;
import page.LoginPage;
import page.TransferPage;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferSteps {
    private DashboardPage dashboardPage;
    private TransferPage transferPage;
    private int balanceFirstCardBefore;
    private int balanceSecondCardBefore;


    public TransferSteps() {}

    @Допустим("пользователь залогинен с именем {string} и паролем {string}")
    public void userIsLoggedIn(String login, String password) {
        Selenide.open("http://localhost:9999");
        LoginPage loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(login, password);
        var authInfo = new DataHelper.AuthInfo(login, password);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        verificationPage.validVerify(verificationCode.getCode());
        dashboardPage = new DashboardPage();

        balanceFirstCardBefore = dashboardPage.getCardBalanceByIndex(0);
        balanceSecondCardBefore = dashboardPage.getCardBalanceByIndex(1);

    }


    @Когда("пользователь переводит {int} рублей с карты с индексом {int} на карту с индексом {int}")
    public void userTransfersMoney(int amount, int fromCardIndex, int toCardIndex) {
        // Получаем полный номер карты-источника по индексу из DataHelper
        String fromCardNumber = DataHelper.getCardInfo(fromCardIndex).getNumber();

        // Выбираем карту получателя по индексу
        transferPage = dashboardPage.selectCardToTransferByIndex(toCardIndex);

        // Совершаем перевод
        dashboardPage = transferPage.transferFromCard(fromCardNumber, amount);

        // Обновляем дашборд для получения актуальных балансов
        dashboardPage = dashboardPage.refresh();
    }

    @Тогда("баланс карты с индексом {int} увеличился на {int} рублей")
    public void checkBalanceIncrease(int cardIndex, int expectedIncrease) {
        int initialBalance;
        if (cardIndex == 0) {
            initialBalance = balanceFirstCardBefore;
        } else if (cardIndex == 1) {
            initialBalance = balanceSecondCardBefore;
        } else {
            throw new IllegalArgumentException("Неверный индекс карты: " + cardIndex);
        }
        // Получаем текущий баланс карты по индексу
        int actualBalance = dashboardPage.getCardBalanceByIndex(cardIndex);
        int expectedBalance = initialBalance + expectedIncrease;
        assertEquals(expectedBalance, actualBalance,
                "Баланс карты с индексом " + cardIndex + " не совпадает");
    }
}