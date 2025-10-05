package org.example.modelo;

public class Direccion {
    private String calle;
    private String ciudad;
    private String estado;
    private long cp;

    public Direccion() {
    }

    public Direccion(String calle, String ciudad, String estado, long cp) {
        this.calle = calle;
        this.ciudad = ciudad;
        this.estado = estado;
        this.cp = cp;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getCp() {
        return cp;
    }

    public void setCp(long cp) {
        this.cp = cp;
    }

    @Override
    public String toString() {
        return "\nDireccion:" +
                "\ncalle='" + calle + '\'' +
                "\nciudad='" + ciudad + '\'' +
                "\nestado='" + estado + '\'' +
                "\ncp=" + cp;
    }
}
