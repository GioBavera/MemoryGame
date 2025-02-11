/* JUEGO DE LA MEMORIA
 * GIOVANI BAVERA - 2do AÑO - AUS 
 * 
 * Juego de la memoria: sacar dos cartas iguales para ganar el par.
 * Hay tres tamaños de tableros: 4x4, 6x6, 8x8.
 * Hay dos modos de juegos:
 * 		- Modo Solitario: Resolver un tablero con un contador. Se permite ver la tabla de
 * 		  puntajes de cada tamaño del tablero.
 * 
 * 		- Modo 1vs1: se turnan para resolver el tablero, el que mas pares tenga, gana la 
 * 		  partida. Antes de comenzar, se puede ver el resultado de las ultimas 5 partidas.
 * 
 * Juego hecho con interface grafica (.swing) y utiliza archivos para guardar resultados. 
*/

package main;

public class main {
	public static void main(String[] args){
		new MenuInicial();
	}
}
