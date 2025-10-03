package page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class DashboardPage {
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
    }

    public int getCardBalance(String cardId) {
        for (SelenideElement card : cards) {
            String attr = card.getAttribute("data-test-id");
            if (attr != null && attr.equals(cardId)) {
                String text = card.text();
                return extractBalance(text);
            }
        }
        throw new IllegalArgumentException("Карты с id " + cardId + " не найдено");
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish).replaceAll("[^\\d]", "");
        return Integer.parseInt(value);
    }

    // Переход на страницу перевода с карты по последним цифрам
    public TransferPage selectCardToTransferByLastDigits(String lastDigits) {
        for (SelenideElement card : cards) {
            if (card.text().contains(lastDigits)) {
                card.$("[data-test-id='action-deposit']").click(); // Клик по кнопке "Пополнить"
                return page(TransferPage.class);
            }
        }
        throw new IllegalArgumentException("Карта с последними цифрами " + lastDigits + " не найдена");
    }
}
