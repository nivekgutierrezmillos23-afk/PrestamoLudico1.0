package org.example.dao;

import org.example.model.ObjetoCatalogo;
import org.example.model.Prestamo;
import org.example.util.ConexionDB;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrestamosDAOImpl implements PrestamosDAO {

    @Override
    public List<ObjetoCatalogo> listarCatalogo() {
        List<ObjetoCatalogo> lista = new ArrayList<>();
        String query = "SELECT nombre, cantidad FROM objeto_catalogo";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lista.add(new ObjetoCatalogo(rs.getString("nombre"), rs.getInt("cantidad")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Prestamo> listarPrestamosActivos() {
        List<Prestamo> lista = new ArrayList<>();
        String query = "SELECT estudiante, documento, objeto, hora_salida FROM prestamo";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                lista.add(new Prestamo(
                        rs.getString("estudiante"),
                        rs.getString("documento"),
                        rs.getString("objeto"),
                        rs.getString("hora_salida")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean registrarPrestamo(String estudiante, String documento, String objeto) {
        try (Connection conn = ConexionDB.getConnection()) {
            String queryCheck = "SELECT cantidad FROM objeto_catalogo WHERE nombre = ?";
            int stock = 0;
            try (PreparedStatement ps = conn.prepareStatement(queryCheck)) {
                ps.setString(1, objeto);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) stock = rs.getInt("cantidad");
            }

            if (stock <= 0) return false;

            conn.setAutoCommit(false);

            String queryUpdate = "UPDATE objeto_catalogo SET cantidad = cantidad - 1 WHERE nombre = ?";
            try (PreparedStatement ps = conn.prepareStatement(queryUpdate)) {
                ps.setString(1, objeto);
                ps.executeUpdate();
            }

            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String queryInsert = "INSERT INTO prestamo (estudiante, documento, objeto, hora_salida) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(queryInsert)) {
                ps.setString(1, estudiante);
                ps.setString(2, documento);
                ps.setString(3, objeto);
                ps.setString(4, hora);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean devolverObjeto(String documento, String objeto) {
        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            String queryStock = "UPDATE objeto_catalogo SET cantidad = cantidad + 1 WHERE nombre = ?";
            try (PreparedStatement ps = conn.prepareStatement(queryStock)) {
                ps.setString(1, objeto);
                ps.executeUpdate();
            }

            String queryDel = "DELETE FROM prestamo WHERE documento = ? AND objeto = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(queryDel)) {
                ps.setString(1, documento);
                ps.setString(2, objeto);
                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
