package com.apteka24.bdd.steps;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;

public class CommonSteps {

    @Когда("Пользователь нажимает на кнопку {string}")
    public void clickNamedButton(String buttonText) {
        // Без UI: шаг-заглушка
    }

    @И("Нажимает кнопку {string}")
    public void clickButton(String buttonText) {
        // Без UI: шаг-заглушка
    }

    @И("Нажимает кнопку {string} в модальном окне")
    public void clickButtonInModal(String buttonText) {
        // Без UI: шаг-заглушка
    }

    @Тогда("Отображается сообщение об ошибке валидации")
    public void validationErrorShown() {
        // Без UI: шаг-заглушка
    }
}