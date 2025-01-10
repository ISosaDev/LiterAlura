package com.isosadev.LiterAlura.model;


import jakarta.persistence.*;

import java.util.Objects;

/**
 * Entidad que representa un género literario.
 * Se mapea a la tabla "generos" en la base de datos.
 */
@Entity
@Table(name = "generos")
public class Genero {

    /**
     * Identificador único del género (clave primaria).
     * Se genera automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del género. Debe ser único en la base de datos.
     */
    @Column(unique = true)
    private String nombre;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Genero() {}

    /**
     * Constructor que crea un nuevo género con un nombre.
     * @param nombre El nombre del género.
     */
    public Genero(String nombre) {this.nombre = nombre;}

    /**
     * Obtiene el ID del género.
     * @return El ID del género.
     */
    public Long getId() {return id;}

    /**
     * Obtiene el nombre del género.
     * @return El nombre del género.
     */
    public String getNombre() {return nombre;}

    /**
     * Establece el nombre del género.
     * @param nombre El nuevo nombre del género.
     */
    public void setNombre(String nombre) {this.nombre = nombre;}

    /**
     * Sobreescritura del método equals para comparar objetos Genero por su nombre.
     * @param o El objeto a comparar.
     * @return true si los nombres son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genero genero = (Genero) o;
        return Objects.equals(nombre, genero.nombre);
    }

    /**
     * Sobreescritura del método hashCode para generar un código hash basado en el nombre.
     * @return El código hash del género.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}


