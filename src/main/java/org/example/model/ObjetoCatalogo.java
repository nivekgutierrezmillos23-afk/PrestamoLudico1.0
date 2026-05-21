package org.example.model;

public class ObjetoCatalogo {
    private String nombre;
    private int cantidad;

    public ObjetoCatalogo(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
}
