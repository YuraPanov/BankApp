package com.example.bankapp.controller;

import com.example.bankapp.dao.ClientDAO;
import com.example.bankapp.model.Client;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ClientsController {


    @FXML private TableView<Client> table;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colLastName;
    @FXML private TableColumn<Client, String> colFirstName;
    @FXML private TableColumn<Client, String> colMiddleName;
    @FXML private TableColumn<Client, String> colSeries;
    @FXML private TableColumn<Client, String> colNumber;
    @FXML private TableColumn<Client, String> colDate;
    @FXML private TableColumn<Client, String> colBy;
//    @FXML private TableColumn<Client, String> colTelephone;

    @FXML private TextField fullNameField;
    @FXML private TextField seriesField;
    @FXML private TextField numberField;
    @FXML private DatePicker datePicker;
    @FXML private TextField issuedByField;
//    @FXML private TextField telephoneField;

    private final ObservableList<Client> data = FXCollections.observableArrayList();
    private Client selectedClient = null;

    @FXML
    public void initialize() {
        // Настраиваем колонки
        colId.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getClientId()));
        colLastName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getLastName()));
        colFirstName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getFirstName()));
        colMiddleName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getMiddleName()));
        colSeries.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPassportSeries()));
        colNumber.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPassportNumber()));
        colDate.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPassportIssuedDate()));
        colBy.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPassportIssuedBy()));
//        colTelephone.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getTelephoneNumber()));

        table.setItems(data);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            onSelectClient(newV);
        });

        loadData();
    }

    @FXML
    public void loadData() {
        data.setAll(ClientDAO.findAll());
        clearForm();
    }

    private void onSelectClient(Client client) {
        selectedClient = client;
        if (client == null) {
            clearForm();
            return;
        }
        // Заполняем форму из выбранного
        fullNameField.setText(
                client.getLastName() + " " +
                        client.getFirstName() +
                        (client.getMiddleName().isEmpty() ? "" : " " + client.getMiddleName())
        );
        seriesField.setText(client.getPassportSeries());
        numberField.setText(client.getPassportNumber());
        datePicker.setValue(LocalDate.parse(client.getPassportIssuedDate(), DateTimeFormatter.ISO_DATE));
        issuedByField.setText(client.getPassportIssuedBy());
    }

    @FXML
    public void onAdd() {
        try {
            Client cl = buildClientFromForm();
            ClientDAO.save(cl);
            loadData();
            showInfo("Клиент добавлен");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    public void onUpdate() {
        if (selectedClient == null) {
            showWarning("Выберите клиента в таблице");
            return;
        }
        try {
            Client cl = buildClientFromForm();
            cl.setClientId(selectedClient.getClientId());
            ClientDAO.update(cl);
            loadData();
            showInfo("Изменения сохранены");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    @FXML
    public void onDelete() {
        if (selectedClient == null) {
            showWarning("Выберите клиента для удаления");
            return;
        }

        // Проверим, есть ли у клиента займы
        boolean hasLoans = ClientDAO.hasLoans(selectedClient.getClientId());
        if (hasLoans) {
            showWarning("Невозможно удалить клиента: у него есть связанные займы");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить клиента " + selectedClient.getLastName() + "?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.orElse(ButtonType.NO) == ButtonType.YES) {
            ClientDAO.delete(selectedClient.getClientId());
            loadData();
        }
    }

    private Client buildClientFromForm() {
        String fullName = fullNameField.getText().trim();
        String series   = seriesField.getText().trim();
        String number   = numberField.getText().trim();
        LocalDate date  = datePicker.getValue();
        String by       = issuedByField.getText().trim();
//        String telephone = telephoneField.getText().trim();

        if (fullName.isEmpty() || series.isEmpty() || number.isEmpty() || date == null || by.isEmpty()) {
            throw new IllegalArgumentException("Заполните все поля");
        }
        if (!series.matches("\\d{4}")) {
            throw new IllegalArgumentException("Серия — 4 цифры");
        }
        if (!number.matches("\\d{6}")) {
            throw new IllegalArgumentException("Номер — 6 цифр");
        }
//        if (!number.matches("\\d{12}")) {
//            throw new IllegalArgumentException("Номер — 12 цифр c учётом +7");
//        }
        String[] parts = fullName.split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Укажите минимум фамилию и имя");
        }

        Client cl = new Client();
        cl.setLastName(parts[0]);
        cl.setFirstName(parts[1]);
        cl.setMiddleName(parts.length > 2 ? parts[2] : "");
        cl.setPassportSeries(series);
        cl.setPassportNumber(number);
        cl.setPassportIssuedDate(date.format(DateTimeFormatter.ISO_DATE));
        cl.setPassportIssuedBy(by);
        return cl;
    }

    private void clearForm() {
        selectedClient = null;
        fullNameField.clear();
        seriesField.clear();
        numberField.clear();
        datePicker.setValue(null);
        issuedByField.clear();
    }

    private void showWarning(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR,   msg).showAndWait();
    }
    private void showInfo(String msg)  {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
