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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClienteForm extends JFrame {
    private JTextField nombreField;
    private JTextField direccionField;
    private JTextField telefonoField;
    private JTextField emailField;
    private JTextField fechaRegistroField;
    private String operacion; // "alta", "baja", "cambio"
    private int clienteId; // ID del cliente para modificaciones

    public ClienteForm(String operacion, int clienteId) {
        this.operacion = operacion;
        this.clienteId = clienteId;

        setTitle("Formulario de Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel clientePanel = new JPanel();
        add(clientePanel);
        placeClienteComponents(clientePanel);

        if (operacion.equals("cambio")) {
            loadClienteData();
        }

        setVisible(true);
    }

    private void placeClienteComponents(JPanel clientePanel) {
        clientePanel.setLayout(null);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(10, 20, 80, 25);
        clientePanel.add(nombreLabel);

        nombreField = new JTextField(20);
        nombreField.setBounds(150, 20, 200, 25);
        clientePanel.add(nombreField);

        JLabel direccionLabel = new JLabel("Dirección:");
        direccionLabel.setBounds(10, 60, 80, 25);
        clientePanel.add(direccionLabel);

        direccionField = new JTextField(20);
        direccionField.setBounds(150, 60, 200, 25);
        clientePanel.add(direccionField);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setBounds(10, 100, 80, 25);
        clientePanel.add(telefonoLabel);

        telefonoField = new JTextField(15);
        telefonoField.setBounds(150, 100, 200, 25);
        clientePanel.add(telefonoField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 140, 80, 25);
        clientePanel.add(emailLabel);

        emailField = new JTextField(50);
        emailField.setBounds(150, 140, 200, 25);
        clientePanel.add(emailField);

        JLabel fechaRegistroLabel = new JLabel("Fecha Registro:");
        fechaRegistroLabel.setBounds(10, 180, 120, 25);
        clientePanel.add(fechaRegistroLabel);

        fechaRegistroField = new JTextField();
        fechaRegistroField.setBounds(150, 180, 200, 25);
        fechaRegistroField.setEditable(false); // No editable
        clientePanel.add(fechaRegistroField);

        // Establecer la fecha de registro automáticamente
        fechaRegistroField.setText(getCurrentDateTime());

        JButton guardarButton = new JButton(operacion.equals("cambio") ? "Actualizar" : "Guardar");
        guardarButton.setBounds(150, 220, 100, 25);
        clientePanel.add(guardarButton);
        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validarCampos()) {
                    return; // Salir si hay errores de validación
                }
                if (operacion.equals("alta")) {
                    guardarCliente();
                } else if (operacion.equals("cambio")) {
                    cambiarCliente();
                } else if (operacion.equals("baja")) {
                    eliminarCliente();
                }
                dispose(); // Cerrar el formulario después de guardar
            }
        });
    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private void loadClienteData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "SELECT nombre_cliente, direccion, telefono, email, fecha_registro FROM Clientes WHERE id_cliente = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                nombreField.setText(rs.getString("nombre_cliente"));
                direccionField.setText(rs.getString("direccion"));
                telefonoField.setText(rs.getString("telefono"));
                emailField.setText(rs.getString("email"));
                fechaRegistroField.setText(rs.getString("fecha_registro"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void guardarCliente() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "INSERT INTO Clientes (nombre_cliente, direccion, telefono, email, fecha_registro) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreField.getText());
            pstmt.setString(2, direccionField.getText());
            pstmt.setString(3, telefonoField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, fechaRegistroField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente guardado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el cliente.");
        }
    }

    private void cambiarCliente() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "UPDATE Clientes SET nombre_cliente = ?, direccion = ?, telefono = ?, email = ? WHERE id_cliente = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreField.getText());
            pstmt.setString(2, direccionField.getText());
            pstmt.setString(3, telefonoField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setInt(5, clienteId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el cliente.");
        }
    }

    private void eliminarCliente() {
        int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este cliente?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
                String sql = "DELETE FROM Clientes WHERE id_cliente = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, clienteId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cliente eliminado con éxito.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al eliminar el cliente.");
            }
        }
    }

    private boolean validarCampos() {
        if (nombreField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (direccionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Agrega validaciones similares para los demás campos...
        return true;
    }

    public static void main(String[] args) {
        // Crear el marco principal para los botones
        JFrame mainFrame = new JFrame("Gestión de Clientes");
        mainFrame.setSize(600, 200); // Aumentar el tamaño a 600 de ancho y 200 de alto
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Usa FlowLayout para los botones
        mainFrame.add(buttonPanel);

        // Botón Altas
        JButton altaButton = new JButton("Altas");
        altaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClienteForm("alta", -1); // -1 indica nuevo cliente
            }
        });
        buttonPanel.add(altaButton);

        // Botón Bajas
        JButton bajaButton = new JButton("Bajas");
        bajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idCliente = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del cliente a eliminar:"));
                new ClienteForm("baja", idCliente);
            }
        });
        buttonPanel.add(bajaButton);

        // Botón Cambios
        JButton cambioButton = new JButton("Cambios");
        cambioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idCliente = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del cliente a modificar:"));
                new ClienteForm("cambio", idCliente);
            }
        });
        buttonPanel.add(cambioButton);

        mainFrame.setVisible(true); // Mostrar el marco principal
    }
}


