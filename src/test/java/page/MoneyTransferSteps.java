package page;

import data.DataHelper;
import io.cucumber.java.ru.Допустим;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferSteps {

    private DashboardPage dashboardPage;
    private TransferPage transferPage;
    private final String firstCardId = "92df3f1c-a033-48e6-8390-206f6b1f56c0";
    private final String secondCardId = "0f3f5c2a-249e-4c3d-8287-09f7a039391d";
    private final String secondCardLastDigits = "0002";
    private final String firstCardLastDigits = "0001";


    private int balanceFirstCardBefore;
    private int balanceSecondCardBefore;

    @Допустим("пользователь залогинен с именем {string} и паролем {string}")
    public void userLoggedIn(String login, String password) {
        var authInfo = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);

        LoginPage loginPage = open("http://localhost:9999", LoginPage.class);
        VerificationPage verificationPage = loginPage.validLogin(login, password);
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());

        balanceFirstCardBefore = dashboardPage.getCardBalance(firstCardId);
        balanceSecondCardBefore = dashboardPage.getCardBalance(secondCardId);
    }

    @Когда("пользователь переводит {int} рублей с карты с номером {string} на свою 1 карту с главной страницы")
    public void userTransfersMoney(int amount, String fromCardNumber) throws InterruptedException {
        transferPage = dashboardPage.selectCardToTransferByLastDigits(firstCardLastDigits);
        dashboardPage = transferPage.transferFromCard(fromCardNumber, amount);

        int attempts = 0;
        int balanceFirstCardAfter;
        do {
            balanceFirstCardAfter = dashboardPage.getCardBalance(firstCardId);
            if (balanceFirstCardAfter == balanceFirstCardBefore + amount) {
                break;
            }
            Thread.sleep(500);
            attempts++;
        } while (attempts < 5);
    }

    @Тогда("баланс его 1 карты из списка на главной странице должен стать {int} рублей")
    public void checkBalance(int expectedBalance) {
        int actualBalance = dashboardPage.getCardBalance(firstCardId);
        assertEquals(expectedBalance, actualBalance);
    }

}
