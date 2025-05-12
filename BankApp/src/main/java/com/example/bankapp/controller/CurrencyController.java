package com.example.bankapp.controller;

import com.example.bankapp.dao.CurrencyDAO;
import com.example.bankapp.model.Currency;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CurrencyController {
    @FXML private TableView<Currency> table;
    @FXML private TableColumn<Currency, String> codeCol;
    @FXML private TableColumn<Currency, String> nameCol;
    @FXML private TableColumn<Currency, Double> rateCol;
    @FXML private TextField codeField, nameField, rateField;
    @FXML private Button addBtn, updateBtn;

    private Runnable onCurrencyUpdated;

    public void setOnCurrencyUpdated(Runnable onCurrencyUpdated) {
        this.onCurrencyUpdated = onCurrencyUpdated;
    }

    @FXML
    public void initialize() {
        codeCol.setCellValueFactory(new PropertyValueFactory<>("currencyCode"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("currencyName"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("pricePerRub"));
        refreshTable();

        addBtn.setOnAction(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String rateText = rateField.getText().trim();

            if (code.isEmpty() || name.isEmpty() || rateText.isEmpty()) {
                showError("Пожалуйста, заполните все поля.");
                return;
            }
            if (code.length() != 3) {
                showError("Код валюты должен состоять из 3 символов");
                return;
            }
            try {
                double rate = Double.parseDouble(rateText);

                if (rate == 0) {
                    showError("Курс валюты не может быть равен 0.");
                    return;
                }

                Currency c = new Currency(code, name, rate);
                CurrencyDAO.save(c);
                refreshTable();
                clearFields();
                if (onCurrencyUpdated != null) onCurrencyUpdated.run();
            } catch (NumberFormatException ex) {
                showError("Некорректный формат курса. Введите число.");
            }
        });

        updateBtn.setOnAction(e -> {
            Currency selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Выберите валюту для обновления.");
                return;
            }

            String rateText = rateField.getText().trim();
            if (rateText.isEmpty() || rateText.equals("0")) {
                showError("Введите курс и он не должен быть равен 0.");
                return;
            }

            try {
                double newRate = Double.parseDouble(rateText);
                Currency updatedCurrency = new Currency(
                        selected.getCurrencyCode(),
                        selected.getCurrencyName(),
                        newRate
                );
                CurrencyDAO.update(updatedCurrency);
                refreshTable();
                rateField.clear();
                new Alert(Alert.AlertType.INFORMATION, "Курс валюты успешно обновлен!").showAndWait();
                if (onCurrencyUpdated != null) onCurrencyUpdated.run();
            } catch (NumberFormatException ex) {
                showError("Некорректный формат курса. Введите число.");
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        codeField.clear();
        nameField.clear();
        rateField.clear();
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(CurrencyDAO.findAll()));
    }
}