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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArticuloForm extends JFrame {
    private JTextField idField;
    private JTextField nombreField;
    private JTextArea descripcionArea;
    private JTextField precioField;
    private JTextField cantidadField;
    private JTextField fechaRegistroField;
    private int articuloId; // ID del artículo para modificaciones

    public ArticuloForm() {
        setTitle("Formulario de Artículo - ABC");
        setSize(400, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel articuloPanel = new JPanel();
        add(articuloPanel);
        placeArticuloComponents(articuloPanel);

        setVisible(true);
    }

    private void placeArticuloComponents(JPanel articuloPanel) {
        articuloPanel.setLayout(null);

        JLabel idLabel = new JLabel("ID Artículo:");
        idLabel.setBounds(10, 20, 120, 25);
        articuloPanel.add(idLabel);

        idField = new JTextField(20);
        idField.setBounds(150, 20, 200, 25);
        idField.setEditable(false); // No editable porque se autogenera
        articuloPanel.add(idField);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(10, 60, 80, 25);
        articuloPanel.add(nombreLabel);

        nombreField = new JTextField(20);
        nombreField.setBounds(150, 60, 200, 25);
        articuloPanel.add(nombreField);

        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionLabel.setBounds(10, 100, 80, 25);
        articuloPanel.add(descripcionLabel);

        descripcionArea = new JTextArea();
        descripcionArea.setBounds(150, 100, 200, 60);
        articuloPanel.add(descripcionArea);

        JLabel precioLabel = new JLabel("Precio Unitario:");
        precioLabel.setBounds(10, 170, 120, 25);
        articuloPanel.add(precioLabel);

        precioField = new JTextField(20);
        ((AbstractDocument) precioField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        precioField.setBounds(150, 170, 200, 25);
        articuloPanel.add(precioField);

        JLabel cantidadLabel = new JLabel("Cantidad Existente:");
        cantidadLabel.setBounds(10, 210, 120, 25);
        articuloPanel.add(cantidadLabel);

        cantidadField = new JTextField(20);
        ((AbstractDocument) cantidadField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        cantidadField.setBounds(150, 210, 200, 25);
        articuloPanel.add(cantidadField);

        JLabel fechaRegistroLabel = new JLabel("Fecha Registro:");
        fechaRegistroLabel.setBounds(10, 250, 120, 25);
        articuloPanel.add(fechaRegistroLabel);

        fechaRegistroField = new JTextField();
        fechaRegistroField.setBounds(150, 250, 200, 25);
        fechaRegistroField.setEditable(false); // No editable
        articuloPanel.add(fechaRegistroField);

        // Establecer la fecha de registro automáticamente
        fechaRegistroField.setText(LocalDateTime.now().toString());

        // Botones para operaciones ABC
        JButton altaButton = new JButton("Alta");
        altaButton.setBounds(30, 290, 100, 25);
        articuloPanel.add(altaButton);
        altaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarArticulo();
            }
        });

        JButton bajaButton = new JButton("Baja");
        bajaButton.setBounds(140, 290, 100, 25);
        articuloPanel.add(bajaButton);
        bajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarArticulo();
            }
        });

        JButton cambioButton = new JButton("Cambio");
        cambioButton.setBounds(250, 290, 100, 25);
        articuloPanel.add(cambioButton);
        cambioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarArticulo();
            }
        });

        // Botón para cargar datos de un artículo existente
        JButton cargarButton = new JButton("Cargar");
        cargarButton.setBounds(150, 330, 100, 25);
        articuloPanel.add(cargarButton);
        cargarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                articuloId = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del artículo a cargar:"));
                loadArticuloData();
            }
        });
    }

    private void loadArticuloData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "SELECT * FROM Articulos WHERE id_articulo = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, articuloId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                idField.setText(String.valueOf(rs.getInt("id_articulo")));
                nombreField.setText(rs.getString("nombre_articulo"));
                descripcionArea.setText(rs.getString("descripcion"));
                precioField.setText(String.valueOf(rs.getDouble("precio_unitario")));
                cantidadField.setText(String.valueOf(rs.getInt("cantidad_existente")));
                fechaRegistroField.setText(rs.getString("fecha_registro"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos del artículo.");
        }
    }

    private void guardarArticulo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "INSERT INTO Articulos (nombre_articulo, descripcion, precio_unitario, cantidad_existente, fecha_registro) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreField.getText());
            pstmt.setString(2, descripcionArea.getText());
            pstmt.setDouble(3, Double.parseDouble(precioField.getText()));
            pstmt.setInt(4, Integer.parseInt(cantidadField.getText()));
            pstmt.setString(5, formattedDate);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Artículo guardado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el artículo: " + e.getMessage());
        }
    }

    private void cambiarArticulo() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "UPDATE Articulos SET nombre_articulo = ?, descripcion = ?, precio_unitario = ?, cantidad_existente = ? WHERE id_articulo = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreField.getText());
            pstmt.setString(2, descripcionArea.getText());
            pstmt.setDouble(3, Double.parseDouble(precioField.getText()));
            pstmt.setInt(4, Integer.parseInt(cantidadField.getText()));
            pstmt.setInt(5, articuloId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Artículo actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el artículo: " + e.getMessage());
        }
    }

    private void eliminarArticulo() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ControlFacturacionInventario;encrypt=true;trustServerCertificate=true;", "sa", "admin123")) {
            String sql = "DELETE FROM Articulos WHERE id_articulo = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, articuloId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Artículo eliminado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al eliminar el artículo: " + e.getMessage());
        }
    }

    private class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("[0-9.]*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("[0-9.]*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}





