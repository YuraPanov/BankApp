package com.example.bankapp.dao;

import com.example.bankapp.model.Currency;
import com.example.bankapp.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO {
    public static List<Currency> findAll() {
        List<Currency> list = new ArrayList<>();
        String sql = "SELECT c.currency_code, c.currency_name, cr.price_perRub " +
                "FROM currencies c JOIN conversion_rate cr ON c.currency_code = cr.code";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Currency(
                        rs.getString("currency_code"),
                        rs.getString("currency_name"),
                        rs.getDouble("price_perRub")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static void save(Currency currency) {
        String insertCurrencySQL = "INSERT INTO currencies (currency_code, currency_name) VALUES (?, ?)";
        String insertRateSQL = "INSERT INTO conversion_rate (code, price_perRub) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Отключаем авто-коммит для управления транзакцией вручную

            // Вставляем в таблицу currencies
            try (PreparedStatement currencyStmt = conn.prepareStatement(insertCurrencySQL)) {
                currencyStmt.setString(1, currency.getCurrencyCode());
                currencyStmt.setString(2, currency.getCurrencyName());
                currencyStmt.executeUpdate();
            }

            // Вставляем в таблицу conversion_rate
            try (PreparedStatement rateStmt = conn.prepareStatement(insertRateSQL)) {
                rateStmt.setString(1, currency.getCurrencyCode());
                rateStmt.setDouble(2, currency.getPricePerRub());
                rateStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void update(Currency currency) {
        String updateRateSQL = "UPDATE conversion_rate SET price_perRub = ? WHERE code = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateRateSQL)) {

            stmt.setDouble(1, currency.getPricePerRub());
            stmt.setString(2, currency.getCurrencyCode());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Обновление курса не удалось, возможно, валюта не найдена.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при обновлении курса валюты", e);
        }
    }
}