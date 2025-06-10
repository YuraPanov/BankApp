package com.example.bankapp.controller;

import com.example.bankapp.dao.LoanDAO;
import com.example.bankapp.model.Loan;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class LoansController {

    @FXML private TextField searchClientIdField;  // Поле для ввода ID клиента
    @FXML private TableView<Loan> table;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, Integer> colClientId;
    @FXML private TableColumn<Loan, String>  colCurrency;
    @FXML private TableColumn<Loan, Double>  colPrincipal;
    @FXML private TableColumn<Loan, Date>    colLoanDate;
    @FXML private TableColumn<Loan, Date>    colDueDate;
    @FXML private TableColumn<Loan, Double>  colInterest;
    @FXML private TableColumn<Loan, Double>  colAmountDue;
    @FXML private TableColumn<Loan, Date>    colActualReturn;
    @FXML private TableColumn<Loan, Double>  colPenalty;
    @FXML private TableColumn<Loan, Double> colMonthlyPayment;
    @FXML private TableColumn<Loan, String>  colStatus;
    @FXML private TableColumn<Loan, Loan>    colAction; // сюда поместим кнопку «Закрыть»

    private final ObservableList<Loan> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Настройка колонок
        colLoanId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLoanId()));
        colClientId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getClientId()));
        colCurrency.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getCurrencyCode()));
        colPrincipal.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPrincipalAmount()));
        colLoanDate.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLoanDate()));
        colDueDate.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDueDate()));
        colInterest.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getInterestRate()));
        colAmountDue.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getAmountDue()));
        colActualReturn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getActualReturnDate()));
        colMonthlyPayment.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getMonthlyPayment()));
        // Колонка penaltyAmount — редактируемая через TextFieldTableCell
        colPenalty.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getPenaltyAmount()));
        colPenalty.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                return value == null ? "" : String.format("%.2f", value);
            }
            @Override
            public Double fromString(String s) {
                if (s == null || s.trim().isEmpty()) return 0.0;
                try {
                    return Double.parseDouble(s.replace(',', '.'));
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }));
        colPenalty.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            Double newPenalty = event.getNewValue();
            loan.setPenaltyAmount(newPenalty);
            updateLoan(loan);
        });

        // Колонка loanStatus — редактируемая через ComboBoxTableCell
        colStatus.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getLoanStatus()));
        colStatus.setCellFactory(ComboBoxTableCell.forTableColumn("Открыть", "Закрыть", "Просроченный"));
        colStatus.setOnEditCommit(event -> {
            Loan loan = event.getRowValue();
            String newStatus = event.getNewValue();
            loan.setLoanStatus(newStatus);
            updateLoan(loan);
        });

        // Колонка «Действие»: кнопка «Закрыть»
        colAction.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colAction.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Loan, Loan> call(TableColumn<Loan, Loan> param) {
                return new TableCell<>() {
                    private final Button btnClose = new Button("Закрыть");

                    @Override
                    protected void updateItem(Loan loan, boolean empty) {
                        super.updateItem(loan, empty);
                        if (loan == null || empty) {
                            setGraphic(null);
                            return;
                        }
                        // Если займ уже закрыт (статус = "Closed" и actualReturnDate != null), скрываем кнопку
                        if ("Closed".equalsIgnoreCase(loan.getLoanStatus()) && loan.getActualReturnDate() != null) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnClose);
                            btnClose.setOnAction(evt -> closeLoan(loan));
                        }
                    }
                };
            }
        });

        table.setItems(data);
        table.setEditable(true);

        loadData();
    }

    /** Загружает все займы из БД */
    private void loadData() {
        data.clear();
        data.addAll(LoanDAO.findAll());
    }

    /** Обновляет один Loan в БД (penalty, status, actualReturnDate) и обновляет таблицу */
    private void updateLoan(Loan loan) {
        LoanDAO.update(loan);
        table.refresh();
    }

    /** Метод «Закрыть займ»: устанавливаем actualReturnDate = сегодня, loanStatus = "Closed" */
    private void closeLoan(Loan loan) {
        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Вы уверены, что хотите закрыть займ №" + loan.getLoanId() + "?",
                ButtonType.YES, ButtonType.NO
        );
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.orElse(ButtonType.NO) == ButtonType.YES) {
            loan.setActualReturnDate(Date.valueOf(LocalDate.now()));
            loan.setLoanStatus("Closed");
            updateLoan(loan);
        }
    }

    /** Обработчик нажатия кнопки «Найти» — ищем первую строку, где clientId совпадает с введённым */
    @FXML
    private void onSearchByClientId() {
        String text = searchClientIdField.getText().trim();
        if (text.isEmpty()) {
            showError("Введите Client ID для поиска.");
            return;
        }

        int targetId;
        try {
            targetId = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            showError("Client ID должен быть целым числом.");
            return;
        }

        // Ищем первую запись в data с нужным clientId
        for (int i = 0; i < data.size(); i++) {
            Loan loan = data.get(i);
            if (loan.getClientId() == targetId) {
                // Выделяем строку и скроллим к ней
                table.getSelectionModel().select(i);
                table.scrollTo(i);
                return;
            }
        }

        // Если ничего не нашли:
        showError("Займы для клиента с ID=" + targetId + " не найдены.");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
