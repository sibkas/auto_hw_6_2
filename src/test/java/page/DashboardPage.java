package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.Condition;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        $("[data-test-id=dashboard]").shouldBe(visible);
    }

    public DashboardPage refresh() {
        // Нажимаем кнопку Обновить
        SelenideElement refreshButton = $$("span.button__text")
                .findBy(Condition.text("Обновить"));
        refreshButton.click();


        $("[data-test-id=dashboard]").shouldBe(visible);

        return this;
    }

    public int getCardBalance(String cardId) {
        SelenideElement card = cards.findBy(Condition.attribute("data-test-id", cardId))
                .shouldBe(visible);
        return extractBalance(card.text());
    }


    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).replaceAll("[^\\d]", "");
        return Integer.parseInt(value);
    }

    public TransferPage selectCardToTransferByLastDigits(String lastDigits) {
        SelenideElement card = cards.findBy(text(lastDigits));
        if (card == null) {
            throw new IllegalArgumentException("Карта с последними цифрами " + lastDigits + " не найдена");
        }
        card.$("[data-test-id='action-deposit']").click();
        return page(TransferPage.class);
    }

    // Индекс 0 для первой карты, 1 для второй
    public int getCardBalanceByIndex(int index) {
        SelenideElement card = cards.get(index).shouldBe(visible);
        return extractBalance(card.text());
    }

    public TransferPage selectCardToTransferByIndex(int index) {
        // Получаем элемент карты по индексу
        SelenideElement card = cards.get(index).shouldBe(visible);
        card.$("[data-test-id='action-deposit']").click();
        return page(TransferPage.class);
    }
}
