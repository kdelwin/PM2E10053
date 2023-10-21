package com.example.pm2e10053.Models;

public class ContactoModel {
    private Integer id;
    private String nombres;
    private String telefonos;
    private String notas;
    private String paises;
    private byte[] imagen; // Agregamos un arreglo de bytes para la imagen

    // Constructor
    public ContactoModel(Integer id, String nombres, String telefonos, String notas, String paises, byte[] imagen) {
        this.id = id;
        this.nombres = nombres;
        this.telefonos = telefonos;
        this.notas = notas;
        this.paises = paises;
        this.imagen = imagen;
    }

    public ContactoModel() {
    }

    // Métodos Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getPaises() {
        return paises;
    }

    public void setPaises(String paises) {
        this.paises = paises;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
