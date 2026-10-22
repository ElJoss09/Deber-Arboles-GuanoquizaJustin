package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoLibro;

/** Casos de prueba obligatorios del enunciado. Muestra "X/N pruebas OK". */
public class PruebasBiblioteca {
    private static int total = 0;
    private static int correctas = 0;

    public static void main(String[] args) {
        System.out.println("=== PruebasBiblioteca ===");

        // 1. Biblioteca solo con la raíz
        ArbolBiblioteca solo = new ArbolBiblioteca(new UnidadBiblioteca("BIB", "Biblioteca FISEI", TipoUnidad.BIBLIOTECA));
        verificar("Solo raíz: altura 0 y 1 hoja", solo.altura() == 0 && solo.contarHojas() == 1);

        // 2. Biblioteca inicial
        ArbolBiblioteca arbol = DatosPrueba.crearBibliotecaInicial();
        verificar("Inicial: 7 unidades, altura 2, 4 hojas y 13 libros",
                arbol.contarUnidades() == 7 && arbol.altura() == 2
                && arbol.contarHojas() == 4 && arbol.contarLibrosTotal() == 13);

        // 3. Niveles
        verificar("Niveles de BIB, sw, SW-E2 y XYZ: 0, 1, 2 y -1",
                arbol.nivel("BIB") == 0 && arbol.nivel("sw") == 1
                && arbol.nivel("SW-E2") == 2 && arbol.nivel("XYZ") == -1);

        // 4. Agregar ESTANTE a BIBLIOTECA
        verificar("Agregar un ESTANTE como hijo de BIBLIOTECA: false",
                !arbol.agregar("BIB", new UnidadBiblioteca("E-X", "Estante suelto", TipoUnidad.ESTANTE)));

        // 5. Código de unidad repetido / padre inexistente / agregar válido
        verificar("Agregar unidad con código repetido o padre inexistente: false",
                !arbol.agregar("BIB", new UnidadBiblioteca("sw", "Otra", TipoUnidad.AREA))
                && !arbol.agregar("NO-EXISTE", new UnidadBiblioteca("N1", "Nueva", TipoUnidad.AREA)));
        verificar("Agregar AREA a BIBLIOTECA y ESTANTE a AREA: true",
                arbol.agregar("BIB", new UnidadBiblioteca("ADM", "Administración", TipoUnidad.AREA))
                && arbol.agregar("adm", new UnidadBiblioteca("ADM-E1", "Estante 1 de ADM", TipoUnidad.ESTANTE)));

        // 6. Jerarquía
        ArbolBiblioteca inicial = DatosPrueba.crearBibliotecaInicial();
        String esperadaJerarquia = "BIB Biblioteca FISEI\n"
                + "  SW Software\n"
                + "    SW-E1 Estante 1 de Software\n"
                + "    SW-E2 Estante 2 de Software\n"
                + "  TI Tecnologías de la Información\n"
                + "    TI-E1 Estante 1 de TI\n"
                + "  TEL Telecomunicaciones\n";
        verificar("mostrarJerarquia en preorden con 2 espacios por nivel",
                inicial.mostrarJerarquia().equals(esperadaJerarquia));

        // 7-9. Recorridos de SW-E1
        CatalogoABB sw1 = inicial.buscar("SW-E1").getCatalogo();
        verificar("Preorden SW-E1: 60 35 20 45 40 80 70 75 90",
                codigos(sw1.recorrer(TipoRecorrido.PREORDEN)).equals(Arrays.asList(60, 35, 20, 45, 40, 80, 70, 75, 90)));
        verificar("Inorden SW-E1: 20 35 40 45 60 70 75 80 90",
                codigos(sw1.recorrer(TipoRecorrido.INORDEN)).equals(Arrays.asList(20, 35, 40, 45, 60, 70, 75, 80, 90)));
        verificar("Posorden SW-E1: 20 40 45 35 75 70 90 80 60",
                codigos(sw1.recorrer(TipoRecorrido.POSORDEN)).equals(Arrays.asList(20, 40, 45, 35, 75, 70, 90, 80, 60)));

        // 10. Traza
        String traza = sw1.trazaRecorrido(TipoRecorrido.PREORDEN);
        verificar("Traza: contiene entra/VISITA/null y termina en la secuencia",
                traza.startsWith("entra 60\nVISITA 60\n  entra 35")
                && traza.contains("null -> regresa")
                && traza.endsWith("Secuencia PREORDEN: 60 35 20 45 40 80 70 75 90"));

        // 11. Código repetido
        verificar("Insertar un código repetido (45): false",
                !sw1.insertar(new Libro(45, "Repetido", "Alguien", 2020)) && sw1.contar() == 9);

        // 12-13. Retos
        verificar("caminoBusqueda(42): [60, 35, 45, 40]",
                sw1.caminoBusqueda(42).equals(Arrays.asList(60, 35, 45, 40)));
        verificar("contarEnRango(40, 75): 5", sw1.contarEnRango(40, 75) == 5);

        // 14. Insertar 42 y eliminar 20, 70, 60
        sw1.insertar(new Libro(42, "Libro 42", "Autor 42", 2001));
        boolean elim = sw1.eliminar(20) && sw1.eliminar(70) && sw1.eliminar(60);
        verificar("Insertar 42 y eliminar 20, 70 y 60: preorden 75 35 45 40 42 80 90",
                elim && codigos(sw1.recorrer(TipoRecorrido.PREORDEN)).equals(Arrays.asList(75, 35, 45, 40, 42, 80, 90))
                && sw1.esABBValido());

        // 15. Inexistentes
        verificar("Buscar, modificar y eliminar código inexistente: null / false / false",
                sw1.buscar(999) == null && !sw1.modificar(999, "T", "A", 2000) && !sw1.eliminar(999));

        // 16. Modificar
        verificar("Modificar un libro existente cambia sus datos pero no el código",
                sw1.modificar(80, "Nuevo título", "Nuevo autor", 2020)
                && sw1.buscar(80).getTitulo().equals("Nuevo título")
                && sw1.buscar(80).getCodigo() == 80);

        // 17. Libros inválidos
        verificar("Libro con datos inválidos: IllegalArgumentException",
                lanza(() -> new Libro(0, "T", "A", 2000))
                && lanza(() -> new Libro(-5, "T", "A", 2000))
                && lanza(() -> new Libro(1, "", "A", 2000))
                && lanza(() -> new Libro(1, "T", null, 2000))
                && lanza(() -> new Libro(1, "T", "A", 1899))
                && lanza(() -> new Libro(1, "T", "A", 2027)));

        // 18. ABB inválido (nieto mal ubicado)
        CatalogoABB malo = new CatalogoABB();
        NodoLibro r = nodo(50);
        r.setIzquierdo(nodo(30));
        r.getIzquierdo().setDerecho(nodo(60)); // 60 está a la izquierda de 50: inválido
        r.setDerecho(nodo(70));
        malo.setRaiz(r);
        verificar("esABBValido() con un nieto mal ubicado: false (y true en árbol correcto)",
                !malo.esABBValido() && inicial.buscar("SW-E2").getCatalogo().esABBValido());

        // 19. Extra: desdePreorden
        CatalogoABB rec = CatalogoABB.desdePreorden(new int[]{60, 35, 20, 45, 40, 80, 70, 75, 90});
        verificar("Extra desdePreorden: reconstruye el mismo ABB",
                rec.esABBValido() && rec.contar() == 9
                && codigos(rec.recorrer(TipoRecorrido.INORDEN)).equals(Arrays.asList(20, 35, 40, 45, 60, 70, 75, 80, 90))
                && codigos(rec.recorrer(TipoRecorrido.POSORDEN)).equals(Arrays.asList(20, 40, 45, 35, 75, 70, 90, 80, 60)));

        System.out.println("\n" + correctas + "/" + total + " pruebas OK");
    }

    // ------------------------------------------------------------------ ayudas
    private static void verificar(String nombre, boolean condicion) {
        total++;
        if (condicion) {
            correctas++;
        }
        System.out.println((condicion ? "[OK]    " : "[FALLA] ") + nombre);
    }

    private static List<Integer> codigos(List<Libro> libros) {
        List<Integer> lista = new ArrayList<>();
        for (Libro l : libros) {
            lista.add(l.getCodigo());
        }
        return lista;
    }

    private static NodoLibro nodo(int codigo) {
        return new NodoLibro(new Libro(codigo, "Libro " + codigo, "Autor", 2000));
    }

    private static boolean lanza(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
