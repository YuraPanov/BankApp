package com.example.bankapp.controller;

import java.time.LocalDate;
import com.example.bankapp.dao.ClientDAO;
import com.example.bankapp.dao.CurrencyDAO;
import com.example.bankapp.dao.LoanDAO;
import com.example.bankapp.model.Client;
import com.example.bankapp.model.Currency;
import com.example.bankapp.model.Loan;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;


public class LoanController {
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ImageView photoView;
    @FXML private ComboBox<Currency> currencyCombo;
    @FXML private TextField amountField;
    @FXML private DatePicker dueDatePicker;

    @FXML public void initialize() {
        try {
            clientCombo.setItems(FXCollections.observableArrayList(ClientDAO.findAll()));
            updateCurrencyCombo();

            clientCombo.setOnAction(e -> {
                Client c = clientCombo.getValue();
                if (c != null && c.getPhotoPath() != null) {
                    try {
                        Image img = new Image(new File(c.getPhotoPath()).toURI().toString());
                        photoView.setImage(img);
                    } catch (Exception ex) {
                        showError("Ошибка загрузки изображения: " + ex.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            showError("Ошибка инициализации: " + e.getMessage());
        }
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
