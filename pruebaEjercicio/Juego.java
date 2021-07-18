package pruebaEjercicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Juego extends DAO {

	public static void main(String[] args) {
		Scanner leer = new Scanner(System.in);
		int opcion;
		boolean salir = false;
		ArrayList<Podio> historialPodios = new ArrayList<>();

		while (!salir) {
			opcion = bienvenida(leer);
			switch (opcion) {
			case 1: {
				historialPodios.add(iniciarJuego(leer));
				break;
			}
			case 2:{
				imprimirCarrerasActuales(historialPodios);
                  break;
			}
			case 3: {
				imprimirResultados();
				break;
			}
			
			case 4: {
			  	salir = true;
				break;
			}
			default:
				System.out.println("Error escoja un numero entre 1 y 3");
			}
		}

	}

	private static int bienvenida(Scanner leer) {

		System.out.println("Bienvenido al juego de carreras");
		System.out.println("Presiona 1 para jugar");
		System.out.println("Presiona 2 para ver resultados de las carreras actuales");
		System.out.println("Presiona 3 para ver Todo el historial de ganadores");
		System.out.println("Presiona 4 para salir");
		return leer.nextInt();
	}

	private static Podio iniciarJuego(Scanner leer) {
		int limitePista = definirDistancia(leer);
		int numeroJugadores = definirNumeroJugadores(leer);
		Pista pista = new Pista(limitePista, numeroJugadores);
		asignarJugadores(leer, numeroJugadores, pista);

		int turno = 0;
		int ronda = 0;
		int contador = 0;
		String ganadorActual;
		Podio podio = new Podio(numeroJugadores);
		do {
			if (!pista.getVias()[turno].isFinalizo()) {
				System.out.println("Turno del jugador: " + (turno + 1));
				System.out.print("Escriba cualquier letra y presione enter para lanzar dado: ");
				leer.next();
				int resultado = obtenerResultadoDado();
				System.out.println("Resultado al tirar el dado = " + resultado);
				int metros = resultado * 100;
				int posicionActual = pista.getVias()[turno].getPosicion();
				metros = Math.min(metros, pista.getLimiteDistancia() - posicionActual);
				pista.avanzarVia(turno, metros);
				posicionActual = pista.getVias()[turno].getPosicion();
				System.out.println("El jugador " + (turno + 1) + " avanzo: " + metros + " metros");
				if (posicionActual >= pista.getLimiteDistancia()) {
					ganadorActual = pista.getVias()[turno].getCarro().getConductor().getNombre();
					podio.agregarGanador(contador, ganadorActual);
					pista.getVias()[turno].setFinalizo(true);
					contador++;
				}
				for (int i = 0; i < pista.getVias().length; i++) {
					System.out.println("Carro " + (i + 1) + ": " + pista.getVias()[i].getPosicion());
				}
			}
			ronda++;
			turno = ronda % numeroJugadores;
		} while (contador < numeroJugadores);
		System.out.println("El Juego Termino \n");
		insertarJugadores(podio);
		imprimirPodio(podio);
		return podio;
	}

	private static int definirDistancia(Scanner leer) {
		int distanciaPista = 0;
		do {
			System.out.println("Digite la ditancia de la pista en kilometros");
			try {
				distanciaPista = leer.nextInt();
			} catch (Exception e) {
				System.out.println("La distancia de la pista escrita no es valida");
			}
		} while (distanciaPista == 0);

		return distanciaPista;
	}

	private static int definirNumeroJugadores(Scanner leer) {
        int cantidadJugadores = 0;
        try {
        	  System.out.println("Seleccione la cantidad de Jugadores");
              cantidadJugadores = leer.nextInt();
              
		} catch (Exception e) {
			System.out.println("Digite un numero");
			
		}
     
        return cantidadJugadores;
    }
	     
		


	private static void asignarJugadores(Scanner scanner, int numeroJugadores, Pista pista) {
		for (int i = 0; i < numeroJugadores; i++) {
			System.out.println("Ingrese el nombre del jugador " + (i + 1) + ": ");
			String nombreConductor = scanner.next();
			Jugador conductor = new Jugador(nombreConductor);
			Carro carro = new Carro(conductor);
			Carril carril = new Carril(carro);
			pista.agregarVia(carril, i);
		}
	}

	private static void imprimirPodio(Podio podio) {
		System.out.println("Los puestos son: \n");

		for (int i = 0; i < podio.getGanadores().length; i++) {
			System.out.println("Puesto número " + (i + 1) + ": " + podio.getGanadores()[i]);
			if (i == 2) {
				break;
			}
		}
	}

	private static void insertarJugadores(Podio podio) {
		for (int i = 0; i < podio.getGanadores().length; i++) {
			String insertJugador = "INSERT INTO ganadores(puesto, nombreJugador) VALUES (?,?)";
			try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(insertJugador);) {
			
				ps.setInt(1, (i + 1));
				ps.setString(2, podio.getGanadores()[i]);

				int response = ps.executeUpdate();
				if (response > 0) {
					System.out.println("jugador registrado correctamente");
					System.out.println("");
				}

			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}

		}
	}
	
    private static void imprimirCarrerasActuales(ArrayList<Podio> historialPodios) {
        if(historialPodios.isEmpty()){
            System.out.println("Todavia no se ha jugado una carrera actual");
        } else {
            System.out.println("Las carreras actuales los resultados son los siguientes: ");
            for (int i = 0; i < historialPodios.size(); i++) {
                System.out.println("____Carrera" + (i+1) + ":______");
                imprimirPodio(historialPodios.get(i));
                System.out.println("____________________________________________");
            }
        }
    }
	
	private static void imprimirResultados() {
		String plantilla = "puesto: %s, Nombre: %s";

		String SelectJugadores = "SELECT puesto,nombreJugador FROM ganadores";

		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(SelectJugadores);) {

			ResultSet res = ps.executeQuery();

			while (res.next()) {
				int puesto = res.getInt("puesto");
				String nombre = res.getString("nombreJugador");
		

				System.out.println(String.format(plantilla, puesto, nombre));
				

			}
			System.out.println("______________________________________________________");
			System.out.println("");

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	private static int obtenerResultadoDado() {
		return (int) (Math.random() * 6) + 1;
	}

}