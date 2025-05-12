package com.example.bankapp.dao;

import com.example.bankapp.model.Client;
import com.example.bankapp.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    public static List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT client_id, first_name, last_name, passport_series, passport_number, passport_issued_date FROM clients";
        try (Connection c = DBUtil.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                Client cl = new Client();
                cl.setClientId(rs.getInt("client_id"));
                cl.setFirstName(rs.getString("first_name"));
                cl.setLastName(rs.getString("last_name"));
                cl.setPassportSeries(rs.getString("passport_series"));
                cl.setPassportNumber(rs.getString("passport_number"));
                cl.setPhotoPath(rs.getString("passport_issued_date"));
                list.add(cl);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static void save(Client cl) {
        String sql = "INSERT INTO clients (first_name, last_name, passport_series, passport_number, passport_issued_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.getFirstName());
            ps.setString(2, cl.getLastName());
            ps.setString(3, cl.getPassportSeries());
            ps.setString(4, cl.getPassportNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}