package com.senati.restappstore;

public class Software {
    private int id;
    private String nombre;
    private int espaciomb;
    private String versionsoft;
    private double precio;

    public Software(int id, String nombre, int espaciomb, String versionsoft, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.espaciomb = espaciomb;
        this.versionsoft = versionsoft;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEspaciomb() {
        return espaciomb;
    }

    public void setEspaciomb(int espaciomb) {
        this.espaciomb = espaciomb;
    }

    public String getVersionsoft() {
        return versionsoft;
    }

    public void setVersionsoft(String versionsoft) {
        this.versionsoft = versionsoft;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}