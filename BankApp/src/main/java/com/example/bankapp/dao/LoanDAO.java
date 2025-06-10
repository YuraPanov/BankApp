package com.example.bankapp.dao;

import com.example.bankapp.model.Loan;
import com.example.bankapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // 1) Метод сохранения нового займа (вы его уже имеете, обновил под 7 параметров)
    public static void save(Loan loan) {
        String sql = "INSERT INTO loans(client_id, currency_code, principal_amount, loan_date, due_date, interest_rate, amount_due) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, loan.getClientId());
            ps.setString(2, loan.getCurrencyCode());
            ps.setDouble(3, loan.getPrincipalAmount());
            ps.setDate(4, loan.getLoanDate());
            ps.setDate(5, loan.getDueDate());
            ps.setDouble(6, loan.getInterestRate());
            ps.setDouble(7, loan.getAmountDue());
            ps.executeUpdate();

            // Получаем сгенерированный loan_id и присваиваем объекту (опционально)
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    loan.setLoanId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2) Метод чтения списка всех займов
    public static List<Loan> findAll() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT loan_id, client_id, currency_code, principal_amount, loan_date, due_date, " +
                "interest_rate, amount_due, actual_return_date, penalty_amount, loan_status, monthly_payment " +
                "FROM loans";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setClientId(rs.getInt("client_id"));
                loan.setCurrencyCode(rs.getString("currency_code"));
                loan.setPrincipalAmount(rs.getDouble("principal_amount"));
                loan.setLoanDate(rs.getDate("loan_date"));
                loan.setDueDate(rs.getDate("due_date"));
                loan.setInterestRate(rs.getDouble("interest_rate"));
                loan.setAmountDue(rs.getDouble("amount_due"));
                loan.setActualReturnDate(rs.getDate("actual_return_date")); // может быть null
                loan.setPenaltyAmount(rs.getDouble("penalty_amount"));
                loan.setLoanStatus(rs.getString("loan_status"));
                loan.setMonthlyPayment(rs.getDouble("monthly_payment"));
                list.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3) Метод обновления (penalty, status, actual_return_date и т. д.)
    public static void update(Loan loan) {
        String sql = "UPDATE loans SET penalty_amount = ?, loan_status = ?, actual_return_date = ? " +
                "WHERE loan_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDouble(1, loan.getPenaltyAmount());
            ps.setString(2, loan.getLoanStatus());
            ps.setDate(3, loan.getActualReturnDate()); // если null, запишется NULL
            ps.setInt(4,    loan.getLoanId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
