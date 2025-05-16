package com.example.bankapp.controller;

import com.example.bankapp.dao.ClientDAO;
import com.example.bankapp.model.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddClientController {

    @FXML private TextField fullNameField;
    @FXML private TextField passportSeriesField;
    @FXML private TextField passportNumberField;
    @FXML private DatePicker passportIssuedDatePicker;
    @FXML private TextField passportIssuedByField;

    @FXML
    private void onSave() {
        String fullName   = fullNameField.getText().trim();
        String series     = passportSeriesField.getText().trim();
        String number     = passportNumberField.getText().trim();
        LocalDate issuedDate = passportIssuedDatePicker.getValue();
        String issuedBy   = passportIssuedByField.getText().trim();

        // Валидация
        if (fullName.isEmpty() || series.isEmpty() || number.isEmpty() || issuedDate == null || issuedBy.isEmpty()) {
            showError("Заполните все поля, включая дату и орган, выдавший паспорт.");
            return;
        }
        if (!series.matches("\\d{4}")) {
            showError("Серия паспорта должна состоять из 4 цифр.");
            return;
        }
        if (!number.matches("\\d{6}")) {
            showError("Номер паспорта должен состоять из 6 цифр.");
            return;
        }

        // Разбиваем ФИО
        String[] parts = fullName.split("\\s+");
        if (parts.length < 2) {
            showError("Введите фамилию и имя (и при необходимости отчество).");
            return;
        }
        String lastName   = parts[0];
        String firstName  = parts[1];
        String middleName = parts.length >= 3 ? parts[2] : "";

        try {
            Client client = new Client();
            client.setLastName(lastName);
            client.setFirstName(firstName);
            client.setMiddleName(middleName);
            client.setPassportSeries(series);
            client.setPassportNumber(number);
            client.setPassportIssuedDate(issuedDate.format(DateTimeFormatter.ISO_DATE));
            client.setPassportIssuedBy(issuedBy);

            ClientDAO.save(client);
            showInfo("Клиент успешно добавлен.");
            closeWindow();
        } catch (Exception e) {
            showError("Ошибка при сохранении клиента: " + e.getMessage());
        }
    }

    private void closeWindow() {
        ((Stage) fullNameField.getScene().getWindow()).close();
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }
}
