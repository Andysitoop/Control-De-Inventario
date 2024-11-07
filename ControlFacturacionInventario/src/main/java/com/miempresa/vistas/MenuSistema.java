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

public class MenuSistema extends JFrame {
    public MenuSistema() {
        setTitle("Menú del Sistema");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);
        
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel menuLabel = new JLabel("Bienvenido al Menú del Sistema");
        menuLabel.setBounds(100, 20, 200, 25);
        panel.add(menuLabel);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(150, 100, 80, 25);
        panel.add(btnSalir);

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });

        // Aquí puedes agregar más botones o funcionalidades según sea necesario
    }

    public static void main(String[] args) {
        new MenuSistema();
    }
}

