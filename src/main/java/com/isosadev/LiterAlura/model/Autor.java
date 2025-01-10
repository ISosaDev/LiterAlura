package com.isosadev.LiterAlura.model;


import jakarta.persistence.*;

/**
 * Entidad que representa a un autor de un libro.
 * Esta clase se mapea a la tabla "autores" en la base de datos.
 */
@Entity // Anotación que marca esta clase como una entidad JPA (se mapea a una tabla en la base de datos)
@Table(name = "autores") // Anotación que especifica el nombre de la tabla en la base de datos
public class Autor {

    /**
     * Identificador único del autor (clave primaria).
     * Se genera automáticamente por la base de datos.
     */
    @Id // Anotación que marca este campo como la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anotación que indica que la clave primaria se genera automáticamente por la base de datos (autoincremental)
    private Long id;

    /**
     * Nombre del autor. Debe ser único en la base de datos.
     */
    @Column(unique = true) // Anotación que asegura que el nombre del autor sea único en la base de datos
    private String nombre;

    /**
     * Año de nacimiento del autor (puede ser nulo).
     */
    private Integer añoDeNacimiento;

    /**
     * Año de muerte del autor (puede ser nulo).
     */
    private Integer añoDeMuerte;

    // Constructores (opcional) y getters y setters (necesarios para JPA)

    public Autor() {} // Constructor vacío requerido por JPA

    public Autor(String nombre, Integer añoDeNacimiento, Integer añoDeMuerte) {
        this.nombre = nombre;
        this.añoDeNacimiento = añoDeNacimiento;
        this.añoDeMuerte = añoDeMuerte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAñoDeNacimiento() {
        return añoDeNacimiento;
    }

    public void setAñoDeNacimiento(Integer añoDeNacimiento) {
        this.añoDeNacimiento = añoDeNacimiento;
    }

    public Integer getAñoDeMuerte() {
        return añoDeMuerte;
    }

    public void setAñoDeMuerte(Integer añoDeMuerte) {
        this.añoDeMuerte = añoDeMuerte;
    }
}