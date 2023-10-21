package com.example.pm2e10053.Connection;

public class Contactos {

    public static final String namedb = "PMI0053";
    public static final String Tabla = "contactos";
    public static final String id = "id";
    public static final String nombres = "nombres";
    public static final String notas = "notas";
    public static final String paises = "paises";
    public static final String telefonos = "telefonos";
    public static final String imagen = "imagen";

    public static final String CreateTableContactos = "CREATE TABLE contactos " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, nombres TEXT, telefonos TEXT, notas TEXT, " +
            "paises TEXT, imagen BLOB )";

    public static final String DropTableContactos = "DROP TABLE IF EXISTS contactos";

    //dml
    public static final String SelectTableContactos = "SELECT * FROM " + Contactos.Tabla;
}
