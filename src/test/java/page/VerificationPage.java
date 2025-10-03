package page;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class VerificationPage {

    public DashboardPage validVerify(String code) {
        $("[data-test-id=code] input").setValue(code);
        $("[data-test-id=action-verify]").click();
        // Ожидаем переход на дашборд
        $("[data-test-id=dashboard]").shouldBe(visible);
        return page(DashboardPage.class);
    }
}
