/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author terra
 */
package com.miempresa.vistas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.miempresa.reportes.Reportes; // Asegúrate de importar la clase Reportes

public class Login extends JFrame {
    private JTextField usuarioField;
    private JPasswordField passwordField;

    public Login() {
        // Inicializa la UI
        setTitle("Inicio de Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);
        
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setBounds(10, 20, 80, 25);
        panel.add(usuarioLabel);
        
        usuarioField = new JTextField(20);
        usuarioField.setBounds(100, 20, 165, 25);
        panel.add(usuarioField);
        
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);
        
        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);
        
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBounds(10, 80, 150, 25);
        panel.add(loginButton);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verificar credenciales desde la base de datos
                String usuario = usuarioField.getText();
                String contraseña = new String(passwordField.getPassword());
                
                if (verificarCredenciales(usuario, contraseña)) {
                    // Si la verificación es correcta, mostrar el menú del sistema
                    mostrarMenuSistema();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                }
            }
        });
    }

    private boolean verificarCredenciales(String usuario, String contraseña) {
        // Lógica de verificación de credenciales en la base de datos
        String url = "jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;";
        String dbUser = "sa"; // Cambia esto según sea necesario
        String dbPassword = "admin123"; // Cambia esto según sea necesario
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            String sql = "SELECT COUNT(*) FROM Usuarios WHERE usuario = ? AND contraseña = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);
            pstmt.setString(2, contraseña); // Se debe usar "contraseña" aquí
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Devuelve true si existe al menos un usuario que coincida
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.");
        }
        return false; // Retorna false si no se encontró coincidencia
    }

    private void mostrarMenuSistema() {
        // Crear una nueva ventana para el menú del sistema
        JFrame menuFrame = new JFrame("Menú del Sistema");
        menuFrame.setSize(400, 350);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        
        JPanel menuPanel = new JPanel();
        menuFrame.add(menuPanel);
        colocarMenuComponents(menuPanel);
        menuFrame.setVisible(true);
    }

    private void colocarMenuComponents(JPanel menuPanel) {
        menuPanel.setLayout(null);
        
        // Botón para Artículos
        JButton articulosButton = new JButton("Artículos");
        articulosButton.setBounds(50, 30, 150, 25);
        menuPanel.add(articulosButton);
        articulosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la interfaz de artículos
                new ArticuloForm();
            }
        });

        // Botón para clientes
        JButton clientesButton = new JButton("Clientes");
        clientesButton.setBounds(50, 70, 150, 25);
        menuPanel.add(clientesButton);
        clientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la interfaz de clientes
                new ClienteForm("alta", 0);
            }
        });

        // Botón para proveedores
        JButton proveedoresButton = new JButton("Proveedores");
        proveedoresButton.setBounds(50, 110, 150, 25);
        menuPanel.add(proveedoresButton);
        proveedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la interfaz de proveedores
                new ProveedorForm("alta", 0);
            }
        });

        // Botón para Facturas de Compras
        JButton facturasComprasButton = new JButton("Facturas de Compras");
        facturasComprasButton.setBounds(50, 150, 150, 25);
        menuPanel.add(facturasComprasButton);
        facturasComprasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la interfaz de facturas de compras
                new FacturaCompraForm();
            }
        });

        // Botón para Facturas de Ventas
        JButton facturasVentasButton = new JButton("Facturas de Ventas");
        facturasVentasButton.setBounds(50, 190, 150, 25);
        menuPanel.add(facturasVentasButton);
        facturasVentasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abrir la interfaz de facturas de ventas
                new FacturaVentaForm();
            }
        });

        // Botón para Reportería
        JButton reporteriaButton = new JButton("Reportería");
        reporteriaButton.setBounds(50, 230, 150, 25);
        menuPanel.add(reporteriaButton);
        reporteriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarMenuReporteria();
            }
        });
    }

    private void mostrarMenuReporteria() {
        JFrame reporteriaFrame = new JFrame("Menú de Reporterías");
        reporteriaFrame.setSize(300, 300);
        reporteriaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reporteriaFrame.setLocationRelativeTo(null);
        
        JPanel reporteriaPanel = new JPanel();
        reporteriaFrame.add(reporteriaPanel);
        placeReporteriaComponents(reporteriaPanel);
        reporteriaFrame.setVisible(true);
    }

    private void placeReporteriaComponents(JPanel reporteriaPanel) {
        reporteriaPanel.setLayout(null);
        
        // Botón para Reportería de Ventas del Día
        JButton ventasDiaButton = new JButton("Reportería de Ventas del Día");
        ventasDiaButton.setBounds(30, 30, 230, 25);
        reporteriaPanel.add(ventasDiaButton);
        ventasDiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una instancia de Reportes y obtener las ventas del día
                Reportes reportes = new Reportes("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;");
                ArrayList<String[]> resultados = reportes.reporteriaVentasDelDia();
                
                // Si no hay resultados, mostrar un mensaje
                if (resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay ventas registradas para el día de hoy.");
                } else {
                    // Mostrar los resultados en una tabla
                    mostrarResultadosEnTabla(resultados);
                }
            }
        });

        // Botón para Reportería de Ventas por Cliente
        JButton ventasClienteButton = new JButton("Reportería de Ventas por Cliente");
        ventasClienteButton.setBounds(30, 70, 230, 25);
        reporteriaPanel.add(ventasClienteButton);
        ventasClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para mostrar la reportería de ventas por cliente
                JOptionPane.showMessageDialog(null, "Reportería de Ventas por Cliente");
            }
        });

        // Botón para Reportería de Compras por Proveedores
        JButton comprasProveedoresButton = new JButton("Reportería de Compras por Proveedores");
        comprasProveedoresButton.setBounds(30, 110, 230, 25);
        reporteriaPanel.add(comprasProveedoresButton);
        comprasProveedoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para mostrar la reportería de compras por proveedores
                JOptionPane.showMessageDialog(null, "Reportería de Compras por Proveedores");
            }
        });

        // Botón para Reportería de Inventario
        JButton inventarioButton = new JButton("Reportería de Inventario");
        inventarioButton.setBounds(30, 150, 230, 25);
        reporteriaPanel.add(inventarioButton);
        inventarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para mostrar la reportería de inventario
                JOptionPane.showMessageDialog(null, "Reportería de Inventario");
            }
        });
    }

    private void mostrarResultadosEnTabla(ArrayList<String[]> resultados) {
        String[] columnNames = {"ID Venta", "Total Venta", "Fecha Venta"};
        String[][] data = resultados.toArray(new String[0][0]);

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JFrame tableFrame = new JFrame("Resultados de Ventas del Día");
        tableFrame.setSize(400, 300);
        tableFrame.add(scrollPane);
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Login();
    }
}







