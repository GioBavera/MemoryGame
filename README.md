El clasico 'Juego de la Memoria' o 'Memory Game' desarrollado en Java. El objetivo es encontrar pares de cartas iguales: si acertas, te llevas el par y sumas un punto; de lo contrario, las cartas se vuelven a voltear y debes intentarlo nuevamente.

En esta implementacion del juego, se incluyeron dos modos de juego:
  -  SINGLEPLAYER: El jugador debe encontrar todos los pares en el menor tiempo posible.
  -  MULTIPLAYER: Dos jugadores compiten por obtener la mayor cantidad de pares. Cuando el tablero queda vacío, gana quien haya reunido más pares.

Caracteristicas del Programa: 
  - Las fichas son banderas de paises, habiendo disponible 46 banderas.
  - Eleccion de tamaño de tablero: 4x4, 6x6, 8x8.
  - Interfaz Grafica (.swing).
  - Efectos de sonidos.
  - Registro de puntajes y tiempos en archivos .txt. Aca se diferencias en los modos de juego:
      - Singleplayer utiliza 3 archivos de texto (puntajes4.txt, puntajes6.txt, puntajes8.txt). Los puntajes se guardan de acuerdo al tamaño del tablero ue se eligio.
      - Multiplayer: aca se guarda todo en un mismo archivo: historial.txt.
  - Tabla de puntajes/resultados visible antes de que inicie la partida. 
