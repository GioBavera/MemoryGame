package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MenuSingle extends JFrame {
	
    private static final long serialVersionUID = 1L;
    private JTextArea puntajes;

    public MenuSingle() {
        setTitle("Modo Singleplayer");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        IconWindows.setWindowIcon(this);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel conf = panelConfiguracion();
        JPanel punt = panelPuntajes();

        panel.add(conf, BorderLayout.WEST);
        panel.add(punt, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    private JPanel panelConfiguracion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Modo Singleplayer"));
        panel.setPreferredSize(new Dimension(200, 0));

        JLabel nombreLabel = new JLabel("Ingrese su nombre:");
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField campo = new JTextField(10);
        campo.setMaximumSize(new Dimension(150, 25));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tableroLabel = new JLabel("Seleccione el tamaño del tablero:");
        tableroLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] tamaños = {"4x4", "6x6", "8x8"};
        JComboBox<String> selector = new JComboBox<>(tamaños);
        selector.setMaximumSize(new Dimension(55, 25));
        selector.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jugarButton = new JButton("JUGAR");
        jugarButton.setMaximumSize(new Dimension(100, 30));
        jugarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton volverButton = new JButton("Volver");
        volverButton.setPreferredSize(new Dimension(50, 15));
        volverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuInicial();
                dispose();
            }
        });

        jugarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = campo.getText().trim();
                String boardSizeString = (String) selector.getSelectedItem();
                int boardSize = Character.getNumericValue(boardSizeString.charAt(0));

                if (playerName.isEmpty()) {
                    JOptionPane.showMessageDialog(MenuSingle.this, "Por favor, ingrese su nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    new Singleplayer(boardSize, playerName);
                    dispose();
                }
            }
        });

        panel.add(Box.createVerticalStrut(60));
        panel.add(nombreLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(tableroLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(selector);
        panel.add(Box.createVerticalStrut(30));
        panel.add(jugarButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(volverButton);

        return panel;
    }

    private JPanel panelPuntajes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tabla de Puntajes"));

        JPanel buttonPanel = botones();

        puntajes = new JTextArea();
        puntajes.setEditable(false);
        puntajes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scoreScrollPane = new JScrollPane(puntajes);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scoreScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel botones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton button4x4 = new JButton("4x4");
        JButton button6x6 = new JButton("6x6");
        JButton button8x8 = new JButton("8x8");

        button4x4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPuntajes("puntajes4.txt");
            }
        });
        button6x6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPuntajes("puntajes6.txt");
            }
        });
        button8x8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPuntajes("puntajes8.txt"); 
            }
        });

        panel.add(button4x4);
        panel.add(button6x6);
        panel.add(button8x8);

        return panel;
    }

    private void mostrarPuntajes(String nombre) {
        puntajes.setText("");
        try (BufferedReader reader = new BufferedReader(new FileReader(nombre))) {
            ArrayList<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }

            lineas.sort((a, b) -> {
                int numA = Integer.parseInt(a.split("s")[0].trim());
                int numB = Integer.parseInt(b.split("s")[0].trim());
                return Integer.compare(numA, numB);
            });

            for (String puntaje : lineas) {
                puntajes.append(puntaje + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el archivo: " + nombre, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

