package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Multiplayer extends JFrame {

	private static final long serialVersionUID = 1L;
    private JButton[] buttons; 
    private ImageIcon[] imagenesCartas;  
    private ImageIcon reverso;  
    private int[] valoresCartas;  
    private int posicionCartaAbierta = -1;
    private boolean estaVerificando = false;

    private int actualJugador = 1;
    private int[] puntaje = {0, 0};
    
    private JLabel turnoLabel;
    private JLabel puntajeLabel;
    private JLabel tiempoLabel;
    
    private int tamTablero;
    private String nombre1;
    private String nombre2;

    private Timer turnTimer;
    private int turnTimeLeft = 10;

    public Multiplayer (int tablero, String nombre1, String nombre2) {
        Random rand = new Random();
        this.tamTablero = tablero;
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;

        setTitle("Memory Game");
        setSize(700, 700); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20)); // Margen alrededor del contenido
        setLocationRelativeTo(null);    // Centra ventana en pantalla
        setResizable(false); // No permite redimensionar
        IconWindows.setWindowIcon(this);

        int cartasTotales = tamTablero * tamTablero;
        buttons = new JButton[cartasTotales];
        valoresCartas = new int[cartasTotales];
        imagenesCartas = new ImageIcon[cartasTotales / 2];
        reverso = new ImageIcon("images/back.png");

        int max = valorMinimoReparto();
        int j = rand.nextInt(max);
        imagenXcarta(j);

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < valoresCartas.length / 2; i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);
        
        // Interfaz
        JPanel boardPanel = new JPanel(new GridLayout(tamTablero, tamTablero, 5, 5));
        boardPanel.setBackground(Color.WHITE);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Crear los botones para las cartas
        for (int i = 0; i < cartasTotales; i++) {
        	valoresCartas[i] = values.get(i);
            buttons[i] = new JButton(reverso);
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.decode("#D3D3D3"));
            buttons[i].setPreferredSize(new Dimension(100, 100));
            buttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            buttons[i].addActionListener(new CardClickListener(i));
            boardPanel.add(buttons[i]);
        }

        // Panel superior para mostrar el estado del juego
        JPanel estatus = new JPanel();
        estatus.setLayout(new BoxLayout(estatus, BoxLayout.Y_AXIS));
        estatus.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        turnoLabel = new JLabel("Turno de: " + nombre1, SwingConstants.CENTER);
        turnoLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));
        turnoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        puntajeLabel = new JLabel("Puntuación: " + nombre1 + " - " + puntaje[0] + " | " + nombre2 + " - " + puntaje[1], SwingConstants.CENTER);
        puntajeLabel.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        puntajeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tiempoLabel = new JLabel("Tiempo restante: " + turnTimeLeft + "s", SwingConstants.CENTER);
        tiempoLabel.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        tiempoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton volverButton = new JButton("Volver");
        volverButton.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        volverButton.setPreferredSize(new Dimension(100, 25));
        volverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	SoundEffect.playSound("back.wav");
            	new MenuMulti();
                dispose();
            }
        });

        estatus.add(turnoLabel);
        estatus.add(Box.createRigidArea(new Dimension(0, 10)));
        estatus.add(puntajeLabel);
        estatus.add(Box.createRigidArea(new Dimension(0, 10)));
        estatus.add(tiempoLabel);
        estatus.add(Box.createRigidArea(new Dimension(0, 10)));
        estatus.add(volverButton);

        add(estatus, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        startTurnTimer();	// Timer por turno
        setVisible(true);
    }
    
    private int valorMinimoReparto() {
        switch (tamTablero) {
            case 4: return 38;
            case 6: return 30;
            default: return 14;
        }
    }

    private void imagenXcarta(int inicio) {
        for (int i = 0; i < imagenesCartas.length; i++) {
            imagenesCartas[i] = new ImageIcon("images/card" + (inicio + i) + ".png");
        }
    }
    
    private class CardClickListener implements ActionListener {
        private int index;

        public CardClickListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (estaVerificando || buttons[index].getIcon() != reverso || index == posicionCartaAbierta) {
                return;
            }

            revelar(index);

            if (posicionCartaAbierta == -1) {
            	posicionCartaAbierta = index;
            } else {
            	estaVerificando = true;
                int primeraCarta = posicionCartaAbierta;
                posicionCartaAbierta = -1;

                SwingUtilities.invokeLater(() -> {
                    if (valoresCartas[primeraCarta] == valoresCartas[index]) {
                        match(primeraCarta, index);
                        puntaje[actualJugador - 1]++;
                    } else {
                        esconder(primeraCarta, index);
                        actualJugador = (actualJugador == 1) ? 2 : 1;
                    }
                    estaVerificando = false;
                    turnTimeLeft = 10; // Reiniciar tiempo del turno
                    actualizar();
                    estaTerminado();
                });
            }
        }
    }

    private void revelar(int index) {
        if (buttons[index].getIcon() == reverso) {
        	SoundEffect.playSound("card.wav");
            Image img = imagenesCartas[valoresCartas[index]].getImage().getScaledInstance(buttons[index].getWidth(), buttons[index].getHeight(), Image.SCALE_SMOOTH);
            buttons[index].setIcon(new ImageIcon(img));
        }
    }

    private void esconder(int primeraCarta, int segundaCarta) {
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons[primeraCarta].setIcon(reverso);
                buttons[segundaCarta].setIcon(reverso);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void match(int primeraCarta, int segundaCarta) {
    	SoundEffect.playSound("correct.wav");
        buttons[primeraCarta].setEnabled(false);
        buttons[segundaCarta].setEnabled(false);
        buttons[primeraCarta].setIcon(null);
        buttons[segundaCarta].setIcon(null);
        
        buttons[primeraCarta].setBackground(Color.decode("#FFFFFF"));
        buttons[segundaCarta].setBackground(Color.decode("#FFFFFF"));
    }

    private void actualizar() {
        turnoLabel.setText("Turno de: " + (actualJugador == 1 ? nombre1 : nombre2));
        puntajeLabel.setText("Puntuación: " + nombre1 + " - " + puntaje[0] + " | " + nombre2 + " - " + puntaje[1]);
        tiempoLabel.setText("Tiempo restante: " + turnTimeLeft + "s");
    }

    private void estaTerminado() {
        boolean terminado = true;
        for (JButton button : buttons) {
            if (button.isEnabled()) {
                terminado = false;
                break;
            }
        }

        if (terminado) {
        	SoundEffect.playSound("victory.wav");
            String ganador;
            if (puntaje[0] > puntaje[1]) {
                ganador = "¡" + nombre1 + " gana con " + puntaje[0] + " puntos!";
            } else if (puntaje[1] > puntaje[0]) {
                ganador = "¡" + nombre2 + " gana con " + puntaje[1] + " puntos!";
            } else {
                ganador = "¡Es un empate! Ambos jugadores tienen " + puntaje[0] + " puntos.";
            }

            JOptionPane.showMessageDialog(this, ganador);
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            guardarResultado("[" + formattedDateTime + "] " + nombre1 + " vs " + nombre2 + ": " + puntaje[0] + " - " + puntaje[1]);
            new MenuMulti();
            dispose();
        }
    }

    private void guardarResultado(String linea) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("historial.txt", true))) {
            writer.write(linea);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el historial.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startTurnTimer() {
        turnTimeLeft = 10;
        if (turnTimer != null) {
            turnTimer.stop();
        }
        turnTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnTimeLeft--;
                actualizar();
                if (turnTimeLeft == 0) {
                    actualJugador = (actualJugador == 1) ? 2 : 1;
                    turnTimeLeft = 10;
                    actualizar();
                }
            }
        });
        turnTimer.start();
    }

}
