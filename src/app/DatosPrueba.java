package app;

import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;

/** Construye la estructura inicial indicada en el enunciado. */
public final class DatosPrueba {

    private DatosPrueba() { }

    /** BIB -> (SW -> SW-E1, SW-E2), (TI -> TI-E1), TEL; con 13 libros. */
    public static ArbolBiblioteca crearBibliotecaInicial() {
        ArbolBiblioteca arbol = new ArbolBiblioteca(
                new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));

        arbol.agregar("BIB", new UnidadBiblioteca("SW", "Software", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TI", "Tecnologías de la Información", TipoUnidad.AREA));
        arbol.agregar("BIB", new UnidadBiblioteca("TEL", "Telecomunicaciones", TipoUnidad.AREA));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E1", "Estante 1 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("SW", new UnidadBiblioteca("SW-E2", "Estante 2 de Software", TipoUnidad.ESTANTE));
        arbol.agregar("TI", new UnidadBiblioteca("TI-E1", "Estante 1 de TI", TipoUnidad.ESTANTE));

        CatalogoABB sw1 = arbol.buscar("SW-E1").getCatalogo();
        sw1.insertar(new Libro(60, "Código Limpio", "Robert C. Martin", 2008));
        sw1.insertar(new Libro(35, "Estructuras de Datos en Java", "Mark Allen Weiss", 2012));
        sw1.insertar(new Libro(80, "Ingeniería de Software", "Ian Sommerville", 2011));
        sw1.insertar(new Libro(20, "Patrones de Diseño", "Erich Gamma", 1994));
        sw1.insertar(new Libro(45, "Refactorización", "Martin Fowler", 1999));
        sw1.insertar(new Libro(70, "El Programador Pragmático", "Andrew Hunt", 1999));
        sw1.insertar(new Libro(90, "Arquitectura Limpia", "Robert C. Martin", 2017));
        sw1.insertar(new Libro(40, "Java: Cómo Programar", "Paul Deitel", 2016));
        sw1.insertar(new Libro(75, "Código Completo", "Steve McConnell", 2004));

        CatalogoABB sw2 = arbol.buscar("SW-E2").getCatalogo();
        sw2.insertar(new Libro(15, "UML Distilled", "Martin Fowler", 2003));
        sw2.insertar(new Libro(10, "Pruebas de Software", "Glenford Myers", 2011));
        sw2.insertar(new Libro(25, "Scrum: El Arte de Hacer el Doble", "Jeff Sutherland", 2014));

        CatalogoABB ti1 = arbol.buscar("TI-E1").getCatalogo();
        ti1.insertar(new Libro(50, "Redes de Computadoras", "Andrew Tanenbaum", 2012));

        return arbol;
    }
}
