package pruebaEjercicio;


public class Carril {

    private int posicion;
    private boolean finalizo;
    private final Carro carro;

    public Carril (Carro carro){
        this.carro = carro;
        this.posicion = 0;
        this.finalizo = false;
    }

    public boolean isFinalizo() {
        return finalizo;
    }

    public void setFinalizo(boolean finalizo) {
        this.finalizo = finalizo;
    }

    public int getPosicion() {
        return posicion;
    }

    public Carro getCarro() {
        return carro;
    }

    public void avanzar (int metros){
        this.posicion += metros;
    }

}