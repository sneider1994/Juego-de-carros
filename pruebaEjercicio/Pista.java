package pruebaEjercicio;

public class Pista {

    private final int limiteDistancia;
    private final Carril[] carriles;

    public Pista(int limiteDistancia, int carriles) {
        this.limiteDistancia = limiteDistancia * 1000;
        this.carriles = new Carril[carriles];
    }

    public int getLimiteDistancia() {
        return limiteDistancia;
    }

    public Carril[] getVias() {
        return carriles;
    }

    public void agregarVia(Carril carril, int numeroVia) {
        this.carriles[numeroVia] = carril;
    }

    public void avanzarVia(int numeroVia, int metros) {
        this.carriles[numeroVia].avanzar(metros);
    }
}