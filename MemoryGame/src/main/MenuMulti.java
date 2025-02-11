package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

class MenuMulti extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String HISTORIAL_FILE = "historial.txt";
    private JTextField campoJugador1;
    private JTextField campoJugador2;
    private JTextArea ultimosJuegos;
    private JComboBox<String> boardSizeBox;

    public MenuMulti() {
        setTitle("Memory Game");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        setResizable(false);
        setLocationRelativeTo(null);
        IconWindows.setWindowIcon(this);

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(panelConfiguracion());
        panel.add(panelHistorial());

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel panelConfiguracion() {
        JPanel panelConfiguracion = new JPanel();
        panelConfiguracion.setLayout(new BoxLayout(panelConfiguracion, BoxLayout.Y_AXIS));
        panelConfiguracion.setBorder(BorderFactory.createTitledBorder("Configuración del Juego"));

        JLabel bienvenidaLabel = new JLabel("Ingrese sus nombres:", SwingConstants.CENTER);
        bienvenidaLabel.setFont(new Font("Sans-serif", Font.BOLD, 20));
        bienvenidaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        campoJugador1 = new JTextField(10);
        campoJugador2 = new JTextField(10);
        boardSizeBox = new JComboBox<>(new String[]{"4x4", "6x6", "8x8"});

        JButton startButton = new JButton("JUGAR");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new iniciar());

        JButton volverButton = new JButton("Volver");
        volverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MenuInicial();
                dispose();
            }
        });

        
        panelConfiguracion.add(Box.createRigidArea(new Dimension(0, 20)));
        panelConfiguracion.add(bienvenidaLabel);
        panelConfiguracion.add(Box.createRigidArea(new Dimension(0, 20)));
        panelConfiguracion.add(panelJugador("Jugador 1:", campoJugador1));
        panelConfiguracion.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConfiguracion.add(panelJugador("Jugador 2:", campoJugador2));
        panelConfiguracion.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConfiguracion.add(selectorSeccion());
        panelConfiguracion.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConfiguracion.add(startButton);
        panelConfiguracion.add(Box.createRigidArea(new Dimension(0, 10)));
        panelConfiguracion.add(volverButton);
        return panelConfiguracion;
    }

    private JPanel panelJugador(String labelTexto, JTextField textCampo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(labelTexto));
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        textCampo.setMaximumSize(new Dimension(120, 20));
        panel.add(textCampo);
        return panel;
    }

    private JPanel selectorSeccion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel("Tipos de Tablero:"));
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        boardSizeBox.setMaximumSize(new Dimension(80, 50));
        panel.add(boardSizeBox);
        return panel;
    }

    private JPanel panelHistorial() {
        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(BorderFactory.createTitledBorder("Ultimas 5 Partidas"));

        ultimosJuegos = new JTextArea();
        ultimosJuegos.setEditable(false);
        ultimosJuegos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(ultimosJuegos);
        panelHistorial.add(scrollPane, BorderLayout.CENTER);

        cargarHistorial();
        return panelHistorial;
    }

    private void cargarHistorial() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORIAL_FILE))) {
            ArrayList<String> lineas = new ArrayList<>();
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }

            int inicio = Math.max(0, lineas.size() - 5);
            for (int i = inicio; i < lineas.size(); i++) {
            	ultimosJuegos.append(lineas.get(i) + "\n");
            }
        } catch (IOException e) {
        	ultimosJuegos.setText("No hay historial de partidas.");
        }
    }

    private class iniciar implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String jugador1 = campoJugador1.getText().trim();
            String jugador2 = campoJugador2.getText().trim();

            if (jugador1.isEmpty() || jugador2.isEmpty()) {
                JOptionPane.showMessageDialog(MenuMulti.this, "Por favor, ingresa los nombres de ambos jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int tamTablero;
            switch (boardSizeBox.getSelectedIndex()) {
            case 1: 
            	tamTablero = 6;
            	break;
            case 2:
            	tamTablero = 8;
            	break;
            default: 
            	tamTablero = 4;
            	break;
            }
            
            dispose();
            new Multiplayer(tamTablero, jugador1, jugador2);
        }
    }
}
