package com.apteka24.bdd.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;

public class AdminSteps {

    @Дано("Администратор авторизован в системе")
    public void adminLoggedIn() {
        // Без UI: шаг-заглушка
    }

    @И("Администратор находится в панели управления")
    public void openDashboard() {
        // Без UI: шаг-заглушка
    }

    @И("Переходит в раздел {string}")
    public void goToSection(String section) {
        // Без UI: шаг-заглушка
    }

    @Когда("Администратор переходит в раздел {string}")
    public void adminGoes(String section) {
        // Без UI: шаг-заглушка
    }

    @И("Администратор вводит {string}, {string}, {string}, {string} и {string}")
    public void fillProduct(String name, String category, String price, String desc, String stock) {
        // Без UI: шаг-заглушка
    }

    @Тогда("Отображается сообщение {string}")
    public void showsMessage(String message) {
        // Без UI: шаг-заглушка
    }

    @Дано("Товар {string} существует в системе")
    public void productExists(String name) {
        // Без UI: шаг-заглушка
    }

    @Когда("Администратор находит товар {string} в списке")
    public void findProduct(String name) {
        // Без UI: шаг-заглушка
    }

    @И("Нажимает на кнопку {string}")
    public void clickNamed(String text) {
        // Без UI: шаг-заглушка
    }

    @И("Меняет цену на {string}")
    public void changePrice(String price) {
        // Без UI: шаг-заглушка
    }

    @И("Подтверждает удаление в диалоговом окне")
    public void confirmDialog() {
        // Без UI: шаг-заглушка
    }

    @И("Администратор заполняет {string} и {string}")
    public void fillPharmacy(String address, String phone) {
        // Без UI: шаг-заглушка
    }

    @Дано("Аптека по адресу {string} существует в системе")
    public void pharmacyExists(String addr) {
        // Без UI: шаг-заглушка
    }

    @И("Меняет номер телефона на {string}")
    public void changePharmacyPhone(String phone) {
        // Без UI: шаг-заглушка
    }

    @И("Заполняет {string} и {string}")
    public void fillUser(String phone, String role) {
        // Без UI: шаг-заглушка
    }

    @Дано("В списке существует пользователь с номером телефона {string} и ролью {string}")
    public void userWithRole(String phone, String role) {
        // Без UI: шаг-заглушка
    }

    @Когда("Администратор находит пользователя {string}")
    public void findUser(String phone) {
        // Без UI: шаг-заглушка
    }

    @И("Меняет роль на {string}")
    public void changeRole(String role) {
        // Без UI: шаг-заглушка
    }

    @Дано("В списке существует пользователь с номером {string}")
    public void userInList(String phone) {
        // Без UI: шаг-заглушка
    }

    @И("Пользователь с номером {string} может авторизоваться в панели управления")
    public void userCanLogin(String phone) {
        // Без UI: шаг-заглушка
    }
}


