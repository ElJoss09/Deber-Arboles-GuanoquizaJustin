package negocio;

import java.util.ArrayList;
import java.util.List;

import enums.TipoUnidad;
import modelo.UnidadBiblioteca;

/** Nodo del árbol general n-ario. Solo los ESTANTE tienen catálogo (ABB). */
public class NodoUnidad {
    private final UnidadBiblioteca dato;
    private final List<NodoUnidad> hijos;
    private final CatalogoABB catalogo;

    public NodoUnidad(UnidadBiblioteca dato) {
        this.dato = dato;
        this.hijos = new ArrayList<>();
        this.catalogo = (dato.getTipo() == TipoUnidad.ESTANTE) ? new CatalogoABB() : null;
    }

    public UnidadBiblioteca getDato() { return dato; }
    public List<NodoUnidad> getHijos() { return hijos; }
    /** @return el ABB del estante, o null si la unidad no es un ESTANTE. */
    public CatalogoABB getCatalogo() { return catalogo; }
    public boolean esHoja() { return hijos.isEmpty(); }
}
