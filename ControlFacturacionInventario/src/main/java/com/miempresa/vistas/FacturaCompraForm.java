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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FacturaCompraForm extends JFrame {
    private JComboBox<String> proveedorComboBox;
    private JTextField totalField;
    private JTextField fechaCompraField;
    private JTextField ciudadField; // Nuevo campo para ciudad
    private JTextField nitField; // Nuevo campo para NIT
    private JTextField productoField; // Nuevo campo para producto
    private JButton guardarButton;
    private List<Integer> proveedoresIds; // Para almacenar los IDs de proveedores

    public FacturaCompraForm() {
        setTitle("Formulario de Factura de Compra");
        setSize(400, 350); // Ajusta el tamaño para incluir el nuevo campo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel facturaPanel = new JPanel();
        add(facturaPanel);
        placeFacturaComponents(facturaPanel);

        cargarProveedores(); // Cargar los proveedores al iniciar el formulario

        setVisible(true);
    }

    private void placeFacturaComponents(JPanel facturaPanel) {
        facturaPanel.setLayout(null);

        JLabel proveedorLabel = new JLabel("Proveedor:");
        proveedorLabel.setBounds(10, 20, 80, 25);
        facturaPanel.add(proveedorLabel);

        proveedorComboBox = new JComboBox<>();
        proveedorComboBox.setBounds(150, 20, 200, 25);
        facturaPanel.add(proveedorComboBox);

        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setBounds(10, 60, 80, 25);
        facturaPanel.add(totalLabel);

        totalField = new JTextField(20);
        ((AbstractDocument) totalField.getDocument()).setDocumentFilter(new NumericDecimalFilter());
        totalField.setBounds(150, 60, 200, 25);
        facturaPanel.add(totalField);

        JLabel fechaCompraLabel = new JLabel("Fecha Compra:");
        fechaCompraLabel.setBounds(10, 100, 120, 25);
        facturaPanel.add(fechaCompraLabel);

        fechaCompraField = new JTextField();
        fechaCompraField.setBounds(150, 100, 200, 25);
        fechaCompraField.setEditable(false); // No editable
        facturaPanel.add(fechaCompraField);

        // Formatear y establecer la fecha de compra automáticamente
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        fechaCompraField.setText(LocalDateTime.now().format(formatter));

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

        // Nuevo campo para producto
        JLabel productoLabel = new JLabel("Producto:");
        productoLabel.setBounds(10, 220, 80, 25);
        facturaPanel.add(productoLabel);

        productoField = new JTextField(20);
        productoField.setBounds(150, 220, 200, 25);
        facturaPanel.add(productoField);

        guardarButton = new JButton("Guardar");
        guardarButton.setBounds(150, 260, 100, 25);
        facturaPanel.add(guardarButton);
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarFacturaCompra();
            }
        });
    }

    private void cargarProveedores() {
        proveedoresIds = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "SELECT id_proveedor, nombre_proveedor FROM Proveedores";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id_proveedor");
                String nombre = rs.getString("nombre_proveedor");
                proveedoresIds.add(id);
                proveedorComboBox.addItem(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los proveedores.");
        }
    }

    private void guardarFacturaCompra() {
        int selectedProveedorIndex = proveedorComboBox.getSelectedIndex();
        if (selectedProveedorIndex < 0) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un proveedor.");
            return;
        }

        int proveedorId = proveedoresIds.get(selectedProveedorIndex);
        String total = totalField.getText();
        String fechaCompra = fechaCompraField.getText();
        String ciudad = ciudadField.getText();
        String nit = nitField.getText();
        String producto = productoField.getText(); // Obtener el producto

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "INSERT INTO FacturasCompra (id_proveedor, fecha_compra, total, ciudad, nit, producto) VALUES (?, ?, ?, ?, ?, ?)"; // Agregar producto a la consulta
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, proveedorId);
            pstmt.setString(2, fechaCompra);
            pstmt.setBigDecimal(3, new java.math.BigDecimal(total));
            pstmt.setString(4, ciudad);
            pstmt.setString(5, nit);
            pstmt.setString(6, producto); // Establecer producto
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Factura de compra guardada con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar la factura de compra.");
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

