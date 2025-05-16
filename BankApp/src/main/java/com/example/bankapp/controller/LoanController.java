package com.example.bankapp.controller;

import java.time.LocalDate;
import com.example.bankapp.dao.ClientDAO;
import com.example.bankapp.dao.CurrencyDAO;
import com.example.bankapp.dao.LoanDAO;
import com.example.bankapp.model.Client;
import com.example.bankapp.model.Currency;
import com.example.bankapp.model.Loan;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;


public class LoanController {
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<Currency> currencyCombo;
    @FXML private TextField amountField;
    @FXML private DatePicker dueDatePicker;

    @FXML public void initialize() {
        // Загружаем всех клиентов в ComboBox
        clientCombo.setItems(FXCollections.observableArrayList(ClientDAO.findAll()));

        // Делаем ComboBox редактируемым
        clientCombo.setEditable(true);

        // Устанавливаем StringConverter: как отображать Client и как искать по строке
        clientCombo.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client != null
                        ? client.getPassportSeries() + " " + client.getPassportNumber()
                        + " — " + client.getFirstName() + " " + client.getLastName()
                        : "";
            }

            @Override
            public Client fromString(String input) {
                if (input == null || input.isBlank()) return null;
                // Ожидаем формат "SERIES NUMBER" (например "1234 567890")
                String[] parts = input.trim().split("\\s+");
                if (parts.length < 2) return null;
                String series = parts[0];
                String number = parts[1];
                return ClientDAO.findByPassport(series, number);
            }
        });

        // При потере фокуса или нажатии Enter — автоматически подтягиваем полное значение
        Platform.runLater(() -> {
            clientCombo.getEditor().setOnAction(evt -> {
                Client c = clientCombo.getConverter().fromString(clientCombo.getEditor().getText());
                if (c != null) {
                    clientCombo.setValue(c);
                } else {
                    showError("Клиент с такими паспортными данными не найден.");
                }
            });
        });

        // Остальная инициализация валют и окон...
        updateCurrencyCombo();
    }


    @FXML
    private void openAddClientWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Добавление клиента");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(e -> updateClientCombo());

            stage.show();
        } catch (Exception e) {
            showError("Ошибка открытия окна добавления клиента: " + e.getMessage());
        }
    }

    private void updateClientCombo() {
        clientCombo.setItems(FXCollections.observableArrayList(ClientDAO.findAll()));
    }

    @FXML private void onIssueLoan() {
        try {
            Loan loan = new Loan();
            Client selectedClient = clientCombo.getValue();
            Currency selectedCurrency = currencyCombo.getValue();

            if (selectedClient == null || selectedCurrency == null) {
                showError("Пожалуйста, выберите клиента и валюту.");
                return;
            }

            loan.setClientId(selectedClient.getClientId());
            loan.setCurrencyCode(selectedCurrency.getCurrencyCode());

            String amountText = amountField.getText();
            if (amountText.isEmpty() || amountText.equals("0")) {
                showError("Введите сумму кредита, больше 0.");
                return;
            }

            try {
                loan.setPrincipalAmount(Double.parseDouble(amountText));
            } catch (NumberFormatException e) {
                showError("Некорректная сумма кредита. Пожалуйста, введите число.");
                return;
            }

            LocalDate dueDate = dueDatePicker.getValue();
            if (dueDate == null) {
                showError("Выберите дату погашения.");
                return;
            }
            if (dueDate.isBefore(LocalDate.now())) {
                showError("Дата погашения не может быть в прошлом.");
                return;
            }

            loan.setDueDate(java.sql.Date.valueOf(dueDate));
            LoanDAO.save(loan);
            new Alert(Alert.AlertType.INFORMATION, "Кредит выдан!").showAndWait();
        } catch (Exception e) {
            showError("Ошибка при выдаче кредита: " + e.getMessage());
        }
    }

    @FXML private void openCurrencyWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/currencies.fxml"));
            Parent root = loader.load();

            CurrencyController currencyController = loader.getController();
            currencyController.setOnCurrencyUpdated(this::updateCurrencyCombo);

            Stage stage = new Stage();
            stage.setTitle("Управление валютами");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            showError("Ошибка открытия окна управления валютами: " + e.getMessage());
        }
    }

    private void updateCurrencyCombo() {
        currencyCombo.setItems(FXCollections.observableArrayList(CurrencyDAO.findAll()));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
