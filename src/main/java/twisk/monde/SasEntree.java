package main.java.twisk.monde;

public class SasEntree extends Activite {
    public SasEntree() {
        super("SASENTREE");
    }

    @Override
    public String toString() {
        return getNom();
    }
}
