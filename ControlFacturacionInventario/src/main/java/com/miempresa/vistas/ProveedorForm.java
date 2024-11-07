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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProveedorForm extends JFrame {
    private JTextField nombreField;
    private JTextArea direccionArea;
    private JTextField telefonoField;
    private JTextField emailField;
    private JTextField fechaRegistroField;
    private JTextField productoField; // Nuevo campo para el producto
    private String operacion; // "alta", "baja", "cambio"
    private int proveedorId; // ID del proveedor para modificaciones
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ProveedorForm(String operacion, int proveedorId) {
        this.operacion = operacion;
        this.proveedorId = proveedorId;

        setTitle("Formulario de Proveedor");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel proveedorPanel = new JPanel();
        add(proveedorPanel);
        placeProveedorComponents(proveedorPanel);

        if (operacion.equals("cambio")) {
            loadProveedorData();
        }

        setVisible(true);
    }

    private void placeProveedorComponents(JPanel proveedorPanel) {
        proveedorPanel.setLayout(null);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(10, 20, 80, 25);
        proveedorPanel.add(nombreLabel);

        nombreField = new JTextField(20);
        nombreField.setBounds(150, 20, 200, 25);
        proveedorPanel.add(nombreField);

        JLabel direccionLabel = new JLabel("Dirección:");
        direccionLabel.setBounds(10, 60, 80, 25);
        proveedorPanel.add(direccionLabel);

        direccionArea = new JTextArea();
        direccionArea.setBounds(150, 60, 200, 60);
        proveedorPanel.add(direccionArea);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setBounds(10, 130, 80, 25);
        proveedorPanel.add(telefonoLabel);

        telefonoField = new JTextField(20);
        ((AbstractDocument) telefonoField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        telefonoField.setBounds(150, 130, 200, 25);
        proveedorPanel.add(telefonoField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 170, 80, 25);
        proveedorPanel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(150, 170, 200, 25);
        proveedorPanel.add(emailField);

        JLabel fechaRegistroLabel = new JLabel("Fecha Registro:");
        fechaRegistroLabel.setBounds(10, 210, 120, 25);
        proveedorPanel.add(fechaRegistroLabel);

        fechaRegistroField = new JTextField();
        fechaRegistroField.setBounds(150, 210, 200, 25);
        fechaRegistroField.setEditable(false); // No editable
        proveedorPanel.add(fechaRegistroField);

        // Establecer la fecha de registro automáticamente
        fechaRegistroField.setText(LocalDateTime.now().format(formatter));

        // Nuevo campo para el producto
        JLabel productoLabel = new JLabel("Producto:");
        productoLabel.setBounds(10, 250, 80, 25);
        proveedorPanel.add(productoLabel);

        productoField = new JTextField(20);
        productoField.setBounds(150, 250, 200, 25);
        proveedorPanel.add(productoField);

        JButton guardarButton = new JButton(operacion.equals("cambio") ? "Actualizar" : "Guardar");
        guardarButton.setBounds(50, 290, 100, 25);
        proveedorPanel.add(guardarButton);
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (operacion.equals("alta")) {
                    guardarProveedor();
                } else if (operacion.equals("cambio")) {
                    cambiarProveedor();
                } else if (operacion.equals("baja")) {
                    eliminarProveedor();
                }
                dispose(); // Cerrar el formulario después de la operación
            }
        });

        JButton eliminarButton = new JButton("Eliminar");
        eliminarButton.setBounds(200, 290, 100, 25);
        proveedorPanel.add(eliminarButton);
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProveedor();
            }
        });
    }

    private void loadProveedorData() {
        // Aquí debes implementar la lógica para cargar los datos del proveedor
        // por ejemplo, realizar una consulta a la base de datos con el proveedorId
    }

    private void guardarProveedor() {
        // Implementa la lógica para guardar el proveedor en la base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "INSERT INTO Proveedores (nombre_proveedor, direccion, telefono, email, fecha_registro, producto) VALUES (?, ?, ?, ?, ?, ?)"; // Agregado producto
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreField.getText());
            pstmt.setString(2, direccionArea.getText());
            pstmt.setString(3, telefonoField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, fechaRegistroField.getText());
            pstmt.setString(6, productoField.getText()); // Agregado producto
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Proveedor guardado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el proveedor.");
        }
    }

    private void cambiarProveedor() {
        // Implementa la lógica para actualizar el proveedor en la base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "UPDATE Proveedores SET nombre_proveedor = ?, direccion = ?, telefono = ?, email = ?, producto = ? WHERE id_proveedor = ?"; // Agregado producto
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreField.getText());
            pstmt.setString(2, direccionArea.getText());
            pstmt.setString(3, telefonoField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, productoField.getText()); // Agregado producto
            pstmt.setInt(6, proveedorId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Proveedor actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el proveedor.");
        }
    }

    private void eliminarProveedor() {
        // Solicitar el ID del proveedor a eliminar
        String input = JOptionPane.showInputDialog(null, "Ingrese el ID del proveedor a eliminar:");
        
        if (input != null && !input.trim().isEmpty()) { // Verifica que el usuario no cancele y que el input no esté vacío
            try {
                int proveedorId = Integer.parseInt(input); // Convertir el input a un entero
                try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
                    String sql = "DELETE FROM Proveedores WHERE id_proveedor = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, proveedorId);
                    int rowsAffected = pstmt.executeUpdate(); // Obtener cuántas filas se eliminaron
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Proveedor eliminado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un proveedor con ese ID.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al eliminar el proveedor.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un ID válido.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
        }
    }

    // Clase para restringir la entrada a solo números
    private class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) {
            if (string == null) return;
            if (string.matches("\\d*")) {
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
            if (text.matches("\\d*")) {
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


