package twisk.outils;

public class MutableBoolean {
    private boolean value;

    public MutableBoolean(boolean value) {
        this.value = value;
    }
    public MutableBoolean() {this(Boolean.FALSE);}

    public boolean getValue() {
        return value;
    }
    public void setValue(boolean value) { this.value = value; }
}
