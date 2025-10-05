package page;


import static com.codeborne.selenide.Selenide.*;

public class TransferPage {

    public DashboardPage transferFromCard(String fromCardNumber, int amount) {
        $("[data-test-id='from'] input").setValue(fromCardNumber);
        $("[data-test-id='amount'] input").setValue(String.valueOf(amount));
        $("[data-test-id='action-transfer']").click();

        return page(DashboardPage.class);
    }
}
