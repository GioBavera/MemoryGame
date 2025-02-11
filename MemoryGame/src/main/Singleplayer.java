package main;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Singleplayer extends JFrame {

    private static final long serialVersionUID = 1L;
    private JButton[] buttons;
    private ImageIcon[] imagenesCartas;
    private ImageIcon reverso; // Se podría poner una personalizada, pero por estética queda así
    private int[] valoresCartas; // Cartas en numeros, para logica
    private int posicionCartaAbierta = -1;
    private boolean estaVerificando = false;
    
    private String nombre;		// Nombre del jugador
    private JLabel tiempoLabel; 
    private int tamTablero;
    
    private Timer turnTimer;
    private int tiempoTranscurrido = 0;

    public Singleplayer(int tablero, String nombre) {
        this.tamTablero = tablero;
        this.nombre = nombre;

        setTitle("Memory Game");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        IconWindows.setWindowIcon(this);

        iniciar();
        panelEstado();
        startTurnTimer();
    }

    private void iniciar() {
        Random rand = new Random();
        int cartasTotales = tamTablero * tamTablero;
        
        JPanel panel = new JPanel(new GridLayout(tamTablero, tamTablero, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        for (int i = 0; i < cartasTotales; i++) {
        	valoresCartas[i] = values.get(i);
            buttons[i] = botonCarta(i);
            panel.add(buttons[i]);
        }

        add(panel, BorderLayout.CENTER);
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

    private JButton botonCarta(int index) {
        JButton button = new JButton(reverso);
        button.setFocusPainted(false);
        button.setBackground(Color.decode("#D3D3D3"));
        button.setPreferredSize(new Dimension(100, 100));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        button.addActionListener(new CardClickListener(index));
        return button;
    }

    private void panelEstado() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(Box.createVerticalGlue());

        tiempoLabel = new JLabel("Tiempo transcurrido: " + tiempoTranscurrido + "s", SwingConstants.CENTER);
        tiempoLabel.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        tiempoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tiempoLabel);

        panel.add(Box.createVerticalStrut(10));

        JButton volverButton = new JButton("Volver");
        volverButton.setFont(new Font("Sans-serif", Font.PLAIN, 16));
        volverButton.setPreferredSize(new Dimension(100, 25));
        volverButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	SoundEffect.playSound("back.wav");
                stopTurnTimer();
                dispose();
                new MenuSingle();
            }
        });
        panel.add(volverButton);

        add(panel, BorderLayout.NORTH);
    }

    private class CardClickListener implements ActionListener {
        private int index;

        public CardClickListener(int index) {
            this.index = index;	// Carta clickeada se guarda
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
            	estaVerificando = true;		// Evita que se hagan mas clicks
                int primeraCarta = posicionCartaAbierta;
                posicionCartaAbierta = -1;

                // Antes de verificar si son o no iguales, se le muestran por 300 milisegundos las cartas al jugador
                Timer delayTimer = new Timer(300, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (valoresCartas[primeraCarta] == valoresCartas[index]) {
                            match(primeraCarta, index); // Si son iguales, se deshabilitan
                        } else {
                            esconder(primeraCarta, index); // Caso contrario, se vuelven a esconder
                        }
                        estaVerificando = false;
                        estaTerminado(); // Verifica si terminó el juego
                        ((Timer) e.getSource()).stop();
                    }
                });

                delayTimer.setRepeats(false);
                delayTimer.start();
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
        buttons[primeraCarta].setIcon(reverso);
        buttons[segundaCarta].setIcon(reverso);
    }

    private void match(int primeraCarta, int segundaCarta) {
        SoundEffect.playSound("correct.wav");
        
        buttons[primeraCarta].setEnabled(false);
        buttons[segundaCarta].setEnabled(false);
        // Sacar el icono de la bandera
        buttons[primeraCarta].setIcon(null);
        buttons[segundaCarta].setIcon(null);
        // Establecer en blanco
        buttons[primeraCarta].setBackground(Color.decode("#FFFFFF"));
        buttons[segundaCarta].setBackground(Color.decode("#FFFFFF"));
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
            stopTurnTimer();
            JOptionPane.showMessageDialog(this, "Terminado. Tardaste " + tiempoTranscurrido + " s.");
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            String formattedDateTime = currentDateTime.format(formatter);
            
            guardarPuntaje(formattedDateTime, tiempoTranscurrido);
            dispose();
            new MenuSingle();
        }
    }

    private void guardarPuntaje(String dateTime, int timeElapsed) {
        String archivoPuntajes = obtenerArchivo();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoPuntajes, true))) {
            writer.write(timeElapsed + "s - " + nombre + "     [" + dateTime + "] ");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar el historial.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerArchivo() {
        switch (tamTablero) {
            case 4: 
            	return "puntajes4.txt";
            case 6: 
            	return "puntajes6.txt";
            case 8: 
            	return "puntajes8.txt";
            default: 
            	return "puntajes.txt";
        }
    }

    private void startTurnTimer() {
    	turnTimer = new Timer(1000, new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        tiempoTranscurrido++;
    	        tiempoLabel.setText("Tiempo transcurrido: " + tiempoTranscurrido + "s");
    	    }
    	});

        turnTimer.start();
    }

    private void stopTurnTimer() {
        if (turnTimer != null) {
            turnTimer.stop();
        }
    }
}
