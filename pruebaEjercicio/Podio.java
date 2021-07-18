package pruebaEjercicio;

public class Podio {
    private final String[] ganadores;

    public Podio(int numeroJugadores) {
        this.ganadores = new String[numeroJugadores];
    }

    public String[] getGanadores() {
        return ganadores;
    }

    public void agregarGanador(int contador, String conductor) {
        this.ganadores[contador] = conductor;
    }
}