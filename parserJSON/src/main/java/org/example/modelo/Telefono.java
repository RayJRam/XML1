package org.example.modelo;

public class Telefono {
    private String tipo;
    private String numero;

    public Telefono() {
    }

    public Telefono(String tipo, String numero) {
        this.tipo = tipo;
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "\nTelefono:" +
                "\ntipo='" + tipo + '\'' +
                ", numero='" + numero + '\'';
    }
}
