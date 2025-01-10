package com.isosadev.LiterAlura.model;


import jakarta.persistence.*;

import java.util.Objects;

/**
 * Entidad que representa un idioma.
 * Se mapea a la tabla "idiomas" en la base de datos.
 */
@Entity
@Table(name = "idiomas")
public class Idioma {

    /**
     * Identificador único del idioma (clave primaria).
     * Se genera automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del idioma. Debe ser único en la base de datos.
     */
    @Column(unique = true)
    private String nombre;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Idioma(){}

    /**
     * Constructor que crea un nuevo idioma con un nombre específico.
     * @param nombre El nombre del idioma.
     */
    public Idioma(String nombre){this.nombre = nombre;}

    /**
     * Obtiene el ID del idioma.
     * @return El ID del idioma.
     */
    public Long getId() {return id;}

    /**
     * Obtiene el nombre del idioma.
     * @return El nombre del idioma.
     */
    public String getNombre() {return nombre;}

    /**
     * Establece el nombre del idioma.
     * @param nombre El nuevo nombre del idioma.
     */
    public void setNombre(String nombre) {this.nombre = nombre;}

    /**
     * Sobreescritura del método equals para comparar objetos Idioma por su nombre.
     * @param o El objeto a comparar.
     * @return true si los nombres son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Idioma idioma = (Idioma) o;
        return Objects.equals(nombre, idioma.nombre);
    }

    /**
     * Sobreescritura del método hashCode para generar un código hash basado en el nombre.
     * @return El código hash del idioma.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}

