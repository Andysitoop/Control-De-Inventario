/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.miempresa.vistas;

/**
 *
 * @author terra
 */
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime; // Importar LocalDateTime
import java.time.format.DateTimeFormatter; // Importar DateTimeFormatter

public class FacturaVentaForm extends JFrame {
    private JComboBox<String> clienteComboBox;
    private JComboBox<String> articuloComboBox; // ComboBox para seleccionar el artículo
    private JTextField totalField;
    private JTextField ciudadField; // Campo para la ciudad
    private JTextField nitField; // Campo para el NIT
    private JTextField fechaVentaField;
    private JButton guardarButton;
    private List<Integer> clientesIds; // Para almacenar los IDs de clientes
    private List<Integer> articulosIds; // Para almacenar los IDs de artículos

    public FacturaVentaForm() {
        setTitle("Formulario de Factura de Venta");
        setSize(400, 350); // Aumentar el tamaño para incluir nuevos campos
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel facturaPanel = new JPanel();
        add(facturaPanel);
        placeFacturaComponents(facturaPanel);

        cargarClientes(); // Cargar los clientes al iniciar el formulario
        cargarArticulos(); // Cargar los artículos al iniciar el formulario

        setVisible(true);
    }

    private void placeFacturaComponents(JPanel facturaPanel) {
        facturaPanel.setLayout(null);

        JLabel clienteLabel = new JLabel("Cliente:");
        clienteLabel.setBounds(10, 20, 80, 25);
        facturaPanel.add(clienteLabel);

        clienteComboBox = new JComboBox<>();
        clienteComboBox.setBounds(150, 20, 200, 25);
        facturaPanel.add(clienteComboBox);

        JLabel articuloLabel = new JLabel("Artículo:");
        articuloLabel.setBounds(10, 60, 80, 25);
        facturaPanel.add(articuloLabel);

        articuloComboBox = new JComboBox<>();
        articuloComboBox.setBounds(150, 60, 200, 25);
        facturaPanel.add(articuloComboBox);

        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setBounds(10, 100, 80, 25);
        facturaPanel.add(totalLabel);

        totalField = new JTextField(20);
        ((AbstractDocument) totalField.getDocument()).setDocumentFilter(new NumericDecimalFilter());
        totalField.setBounds(150, 100, 200, 25);
        facturaPanel.add(totalField);

        JLabel ciudadLabel = new JLabel("Ciudad:");
        ciudadLabel.setBounds(10, 140, 80, 25);
        facturaPanel.add(ciudadLabel);

        ciudadField = new JTextField(20);
        ciudadField.setBounds(150, 140, 200, 25);
        facturaPanel.add(ciudadField);

        JLabel nitLabel = new JLabel("NIT:");
        nitLabel.setBounds(10, 180, 80, 25);
        facturaPanel.add(nitLabel);

        nitField = new JTextField(20);
        nitField.setBounds(150, 180, 200, 25);
        facturaPanel.add(nitField);

        JLabel fechaVentaLabel = new JLabel("Fecha Venta:");
        fechaVentaLabel.setBounds(10, 220, 120, 25);
        facturaPanel.add(fechaVentaLabel);

        fechaVentaField = new JTextField();
        fechaVentaField.setBounds(150, 220, 200, 25);
        fechaVentaField.setEditable(false); // No editable
        facturaPanel.add(fechaVentaField);

        // Establecer la fecha de venta automáticamente en formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        fechaVentaField.setText(LocalDateTime.now().format(formatter));

        guardarButton = new JButton("Guardar");
        guardarButton.setBounds(150, 260, 100, 25);
        facturaPanel.add(guardarButton);
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFacturaVenta();
            }
        });
    }

    private void cargarClientes() {
        clientesIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "SELECT id_cliente, nombre_cliente FROM Clientes";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_cliente");
                String nombre = rs.getString("nombre_cliente");
                clientesIds.add(id);
                clienteComboBox.addItem(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los clientes.");
        }
    }

    private void cargarArticulos() {
        articulosIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "SELECT id_articulo, nombre_articulo FROM Articulos"; // Asegúrate de que la tabla Articulos exista
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_articulo");
                String nombre = rs.getString("nombre_articulo");
                articulosIds.add(id);
                articuloComboBox.addItem(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los artículos.");
        }
    }

    private void guardarFacturaVenta() {
        int selectedClienteIndex = clienteComboBox.getSelectedIndex();
        if (selectedClienteIndex < 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un cliente.");
            return;
        }

        int clienteId = clientesIds.get(selectedClienteIndex);
        int selectedArticuloIndex = articuloComboBox.getSelectedIndex();
        int articuloId = articulosIds.get(selectedArticuloIndex);
        String total = totalField.getText();
        String ciudad = ciudadField.getText();
        String nit = nitField.getText();
        String fechaVenta = fechaVentaField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "INSERT INTO FacturasVenta (id_cliente, id_articulo, fecha_venta, total, ciudad, nit) VALUES (?, ?, ?, ?, ?, ?)"; // Asegúrate de que estos campos existan en la tabla
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clienteId);
            pstmt.setInt(2, articuloId);
            pstmt.setString(3, fechaVenta);
            pstmt.setBigDecimal(4, new java.math.BigDecimal(total));
            pstmt.setString(5, ciudad);
            pstmt.setString(6, nit);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Factura de venta guardada con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar la factura de venta.");
        }
    }

    // Clase para permitir solo números decimales en el campo de total
    private class NumericDecimalFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) {
            if (string == null) return;
            if (string.matches("\\d*(\\.\\d{0,2})?")) {
                try {
                    super.insertString(fb, offset, string, attr);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) {
            if (text == null) return;
            if (text.matches("\\d*(\\.\\d{0,2})?")) {
                try {
                    super.replace(fb, offset, length, text, attrs);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) {
            try {
                super.remove(fb, offset, length);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}


