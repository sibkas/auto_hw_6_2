package page;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class LoginPage {

    public VerificationPage validLogin(String login, String password) {
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();
        // Ожидаем переход к вводу кода верификации
        $("[data-test-id=code] input").shouldBe(visible);
        return page(VerificationPage.class);
    }
}
