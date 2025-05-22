package com.example.bankapp.dao;

import com.example.bankapp.model.Loan;
import com.example.bankapp.util.DBUtil;
import java.sql.*;

public class LoanDAO {
    public static void save(Loan loan) {
        String sql = "INSERT INTO loans(client_id, currency_code, principal_amount, loan_date, due_date, interest_rate, amount_due, monthly_payment) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, loan.getClientId());
            ps.setString(2, loan.getCurrencyCode());
            ps.setDouble(3, loan.getPrincipalAmount());
            ps.setDate(4, loan.getLoanDate());
            ps.setDate(5, loan.getDueDate());
            ps.setFloat(6, loan.getInterestRate());
            ps.setDouble(7, loan.getAmountDue());
            ps.setDouble(8, loan.getMonthlyPayment());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}