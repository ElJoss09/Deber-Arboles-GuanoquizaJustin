package app;

import java.util.NoSuchElementException;
import java.util.Scanner;

import enums.TipoRecorrido;
import enums.TipoUnidad;
import modelo.Libro;
import modelo.UnidadBiblioteca;
import negocio.ArbolBiblioteca;
import negocio.CatalogoABB;
import negocio.NodoUnidad;

/** Menú interactivo del catálogo de la Biblioteca FISEI. */
public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static ArbolBiblioteca arbol;

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Opción: ");
            try {
                ejecutar(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n===== CATÁLOGO BIBLIOTECA FISEI =====");
        System.out.println(" 1. Cargar estructura inicial");
        System.out.println(" 2. Mostrar jerarquía");
        System.out.println(" 3. Agregar unidad");
        System.out.println(" 4. Buscar unidad");
        System.out.println(" 5. Consultar altura, hojas, nivel y total de libros");
        System.out.println(" 6. Insertar libro en un estante");
        System.out.println(" 7. Buscar libro en un estante");
        System.out.println(" 8. Modificar libro de un estante");
        System.out.println(" 9. Eliminar libro de un estante");
        System.out.println("10. Mostrar recorridos con su traza");
        System.out.println("11. Ejecutar retos");
        System.out.println(" 0. Salir");
    }

    private static void ejecutar(int opcion) {
        if (opcion == 0) {
            return;
        }
        if (opcion == 1) {
            arbol = DatosPrueba.crearBibliotecaInicial();
            System.out.println("Estructura inicial cargada (7 unidades, 13 libros).");
            return;
        }
        if (opcion < 0 || opcion > 11) {
            System.out.println("Opción inválida.");
            return;
        }
        if (arbol == null) {
            System.out.println("Primero cargue la estructura inicial (opción 1).");
            return;
        }
        switch (opcion) {
            case 2:  System.out.print(arbol.mostrarJerarquia()); break;
            case 3:  agregarUnidad(); break;
            case 4:  buscarUnidad(); break;
            case 5:  consultas(); break;
            case 6:  insertarLibro(); break;
            case 7:  buscarLibro(); break;
            case 8:  modificarLibro(); break;
            case 9:  eliminarLibro(); break;
            case 10: recorridos(); break;
            case 11: retos(); break;
            default: break;
        }
    }

    // ------------------------------------------------------------------ unidades
    private static void agregarUnidad() {
        String padre = leerLinea("Código de la unidad padre: ");
        String codigo = leerLinea("Código de la nueva unidad: ");
        String nombre = leerLinea("Nombre: ");
        int t = leerEntero("Tipo (1=BIBLIOTECA, 2=AREA, 3=ESTANTE): ");
        if (t < 1 || t > 3) {
            System.out.println("Tipo inválido.");
            return;
        }
        TipoUnidad tipo = TipoUnidad.values()[t - 1];
        boolean ok = arbol.agregar(padre, new UnidadBiblioteca(codigo, nombre, tipo));
        System.out.println(ok ? "Unidad agregada."
                : "No se pudo agregar (padre inexistente, código repetido o jerarquía inválida).");
    }

    private static void buscarUnidad() {
        String codigo = leerLinea("Código de la unidad: ");
        NodoUnidad n = arbol.buscar(codigo);
        if (n == null) {
            System.out.println("La unidad no existe.");
        } else {
            System.out.println("Encontrada: " + n.getDato() + " [" + n.getDato().getTipo() + "]"
                    + " | hijos: " + n.getHijos().size()
                    + (n.getCatalogo() != null ? " | libros: " + n.getCatalogo().contar() : ""));
        }
    }

    private static void consultas() {
        System.out.println("Altura (aristas): " + arbol.altura());
        System.out.println("Hojas: " + arbol.contarHojas());
        System.out.println("Unidades: " + arbol.contarUnidades());
        System.out.println("Total de libros: " + arbol.contarLibrosTotal());
        String codigo = leerLinea("Código de unidad para consultar su nivel: ");
        System.out.println("Nivel de " + codigo + ": " + arbol.nivel(codigo));
    }

    // -------------------------------------------------------------------- libros
    private static CatalogoABB pedirCatalogo() {
        String codigo = leerLinea("Código del estante (ej. SW-E1): ");
        NodoUnidad n = arbol.buscar(codigo);
        if (n == null) {
            System.out.println("La unidad no existe.");
            return null;
        }
        if (n.getCatalogo() == null) {
            System.out.println("La unidad no es un ESTANTE.");
            return null;
        }
        return n.getCatalogo();
    }

    private static void insertarLibro() {
        CatalogoABB c = pedirCatalogo();
        if (c == null) {
            return;
        }
        int codigo = leerEntero("Código del libro (entero > 0): ");
        String titulo = leerLinea("Título: ");
        String autor = leerLinea("Autor: ");
        int anio = leerEntero("Año (1900-2026): ");
        Libro libro = new Libro(codigo, titulo, autor, anio);
        System.out.println(c.insertar(libro) ? "Libro insertado." : "El código ya existe: no se insertó.");
    }

    private static void buscarLibro() {
        CatalogoABB c = pedirCatalogo();
        if (c == null) {
            return;
        }
        Libro l = c.buscar(leerEntero("Código del libro: "));
        System.out.println(l == null ? "El libro no existe." : "Encontrado: " + l);
    }

    private static void modificarLibro() {
        CatalogoABB c = pedirCatalogo();
        if (c == null) {
            return;
        }
        int codigo = leerEntero("Código del libro a modificar: ");
        String titulo = leerLinea("Nuevo título: ");
        String autor = leerLinea("Nuevo autor: ");
        int anio = leerEntero("Nuevo año (1900-2026): ");
        System.out.println(c.modificar(codigo, titulo, autor, anio) ? "Libro modificado." : "El libro no existe.");
    }

    private static void eliminarLibro() {
        CatalogoABB c = pedirCatalogo();
        if (c == null) {
            return;
        }
        System.out.println(c.eliminar(leerEntero("Código del libro a eliminar: ")) ? "Libro eliminado." : "El libro no existe.");
    }

    private static void recorridos() {
        CatalogoABB c = pedirCatalogo();
        if (c == null) {
            return;
        }
        for (TipoRecorrido tipo : TipoRecorrido.values()) {
            System.out.println("\n--- " + tipo + " ---");
            System.out.println(c.trazaRecorrido(tipo));
        }
    }

    private static void retos() {
        CatalogoABB c = pedirCatalogo();
        if (c == null) {
            return;
        }
        System.out.println("a) contarEnRango  b) caminoBusqueda  c) esABBValido  d) desdePreorden (extra)");
        String op = leerLinea("Reto: ").toLowerCase();
        switch (op) {
            case "a":
                int a = leerEntero("Límite inferior a: ");
                int b = leerEntero("Límite superior b: ");
                System.out.println("Códigos en [" + a + ", " + b + "]: " + c.contarEnRango(a, b));
                break;
            case "b":
                int cod = leerEntero("Código a buscar: ");
                System.out.println("Camino: " + c.caminoBusqueda(cod));
                break;
            case "c":
                System.out.println("¿Es un ABB válido? " + c.esABBValido());
                break;
            case "d":
                String linea = leerLinea("Preorden (enteros separados por espacios): ");
                String[] partes = linea.isEmpty() ? new String[0] : linea.split("\\s+");
                int[] pre = new int[partes.length];
                try {
                    for (int i = 0; i < partes.length; i++) {
                        pre[i] = Integer.parseInt(partes[i]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Solo se permiten enteros.");
                    return;
                }
                CatalogoABB r = CatalogoABB.desdePreorden(pre);
                System.out.println(r.trazaRecorrido(TipoRecorrido.INORDEN).replaceAll("(?s).*(Secuencia.*)", "$1"));
                System.out.println(r.trazaRecorrido(TipoRecorrido.PREORDEN).replaceAll("(?s).*(Secuencia.*)", "$1"));
                System.out.println("¿Es ABB válido? " + r.esABBValido());
                break;
            default:
                System.out.println("Reto inválido.");
        }
    }

    // ------------------------------------------------------------------- entrada
    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        try {
            return SC.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("\nFin de la entrada.");
            System.exit(0);
            return "";
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            String s = leerLinea(mensaje);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }
}
