/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miempresa.reportes;

/**
 *
 * @author terra
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Reportes {
    private String connectionString;

    // Constructor que usa la cadena de conexión proporcionada
    public Reportes(String connectionString) {
        this.connectionString = connectionString;
    }

    // Constructor que usa la cadena de conexión predeterminada
    public Reportes() {
        this.connectionString = "jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;";
    }

    public ArrayList<String[]> reporteriaVentasDelDia() {
        String sql = "SELECT id_venta, total_venta, fecha_venta FROM Ventas WHERE CAST(fecha_venta AS DATE) = CAST(GETDATE() AS DATE)";
        ArrayList<String[]> resultados = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(connectionString, "sa", "admin123"); // Se añaden las credenciales aquí
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String[] fila = new String[3];
                fila[0] = String.valueOf(rs.getInt("id_venta"));
                fila[1] = String.valueOf(rs.getDouble("total_venta"));
                fila[2] = rs.getDate("fecha_venta").toString();
                resultados.add(fila);
            }
        } catch (SQLException e) {
            // Manejo de errores
            System.err.println("Error al acceder a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return resultados; // Retorna la lista de resultados, puede estar vacía
    }
}


