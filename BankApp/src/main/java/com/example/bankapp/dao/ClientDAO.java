package com.example.bankapp.dao;

import com.example.bankapp.model.Client;
import com.example.bankapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    public static List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT client_id, first_name, last_name, middle_name, passport_series, " +
                "passport_number, passport_issued_date, passport_issued_by FROM clients";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Client cl = new Client();
                cl.setClientId(rs.getInt("client_id"));
                cl.setFirstName(rs.getString("first_name"));
                cl.setLastName(rs.getString("last_name"));
                cl.setMiddleName(rs.getString("middle_name"));
                cl.setPassportSeries(rs.getString("passport_series"));
                cl.setPassportNumber(rs.getString("passport_number"));
                cl.setPassportIssuedDate(rs.getString("passport_issued_date"));
                cl.setPassportIssuedBy(rs.getString("passport_issued_by"));
                list.add(cl);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static void save(Client cl) {
        String sql = "INSERT INTO clients (first_name, last_name, middle_name, passport_series, passport_number, passport_issued_date, passport_issued_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.getFirstName());
            ps.setString(2, cl.getLastName());
            ps.setString(3, cl.getMiddleName());
            ps.setString(4, cl.getPassportSeries());
            ps.setString(5, cl.getPassportNumber());
            ps.setString(6, cl.getPassportIssuedDate());
            ps.setString(7, cl.getPassportIssuedBy());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void update(Client cl) {
        String sql = "UPDATE clients SET first_name=?, last_name=?, middle_name=?, "
                + "passport_series=?, passport_number=?, passport_issued_date=?, passport_issued_by=? "
                + "WHERE client_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.getFirstName());
            ps.setString(2, cl.getLastName());
            ps.setString(3, cl.getMiddleName());
            ps.setString(4, cl.getPassportSeries());
            ps.setString(5, cl.getPassportNumber());
            ps.setString(6, cl.getPassportIssuedDate());
            ps.setString(7, cl.getPassportIssuedBy());
            ps.setInt   (8, cl.getClientId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int clientId) {
        String sql = "DELETE FROM clients WHERE client_id=?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Client findByPassport(String series, String number) {
        String sql = "SELECT client_id, first_name, last_name, middle_name, passport_series, " +
                "passport_number, passport_issued_date, passport_issued_by " +
                "FROM clients WHERE passport_series = ? AND passport_number = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, series);
            ps.setString(2, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Client cl = new Client();
                cl.setClientId(rs.getInt("client_id"));
                cl.setFirstName(rs.getString("first_name"));
                cl.setLastName(rs.getString("last_name"));
                cl.setMiddleName(rs.getString("middle_name"));
                cl.setPassportSeries(rs.getString("passport_series"));
                cl.setPassportNumber(rs.getString("passport_number"));
                cl.setPassportIssuedDate(rs.getString("passport_issued_date"));
                cl.setPassportIssuedBy(rs.getString("passport_issued_by"));
                return cl;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // не найден
    }
}