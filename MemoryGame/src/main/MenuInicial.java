package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuInicial extends JFrame {
	private static final long serialVersionUID = 1L;

	public MenuInicial() {
        setTitle("Juego de Memoria");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setResizable(false); // No permite redimensionar
        setVisible(true);
        IconWindows.setWindowIcon(this);

        setLayout(new FlowLayout());
        
        // Etiqueta de bienvenida
        JLabel bienvenidaLabel = new JLabel("¡Bienvenido al Juego de Memoria!");
        bienvenidaLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Boton de Singleplayer
        JButton jugadorUnoButton = new JButton("Singleplayer");
        jugadorUnoButton.setPreferredSize(new Dimension(200, 40));
        jugadorUnoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MenuSingle();
            	dispose();
            }
        });
        
        // Boton de 1vs1
        JButton dosJugadoresButton = new JButton("Multiplayer");
        dosJugadoresButton.setPreferredSize(new Dimension(200, 40));
        dosJugadoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new MenuMulti();
            	dispose();
            }
        });
        
        add(bienvenidaLabel);
        add(Box.createVerticalStrut(70));
        add(jugadorUnoButton);
        add(dosJugadoresButton);
        setVisible(true);

    }
}


