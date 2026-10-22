package modelo;

import enums.TipoUnidad;

/** Unidad de la jerarquía: biblioteca, área o estante. El código se guarda en mayúsculas. */
public class UnidadBiblioteca {
    private final String codigo;
    private String nombre;
    private final TipoUnidad tipo;

    public UnidadBiblioteca(String codigo, String nombre, TipoUnidad tipo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la unidad no puede ser nulo ni vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la unidad no puede ser nulo ni vacío.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de la unidad no puede ser nulo.");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.tipo = tipo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public TipoUnidad getTipo() { return tipo; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la unidad no puede ser nulo ni vacío.");
        }
        this.nombre = nombre.trim();
    }

    @Override
    public String toString() {
        return codigo + " " + nombre;
    }
}
