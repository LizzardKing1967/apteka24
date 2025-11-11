package com.apteka24.bdd.steps;

import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;

public class SearchSteps {

    @Когда("Пользователь вводит в строку поиска {string}")
    public void typeInSearch(String query) {
        // Без UI: шаг-заглушка
    }

    @Тогда("Отображается список товаров")
    public void listVisible() {
        // Без UI: шаг-заглушка
    }

    @И("В списке присутствует товар {string}")
    public void itemPresent(String name) {
        // Без UI: шаг-заглушка
    }

    @Когда("Пользователь нажимает на категорию {string}")
    public void clickCategory(String category) {
        // Без UI: шаг-заглушка
    }

    @Тогда("Отображается список товаров, принадлежащих категории {string}")
    public void listForCategory(String category) {
        // Без UI: шаг-заглушка
    }

}


