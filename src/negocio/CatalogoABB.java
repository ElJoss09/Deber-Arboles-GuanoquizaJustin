package negocio;

import java.util.ArrayList;
import java.util.List;

import enums.TipoRecorrido;
import modelo.Libro;

/** ABB de libros ordenado por código. Todas las operaciones son recursivas. */
public class CatalogoABB {
    private NodoLibro raiz;

    public NodoLibro getRaiz() { return raiz; }
    public void setRaiz(NodoLibro raiz) { this.raiz = raiz; }

    // ---------------------------------------------------------------- insertar
    /** Inserta por código. false si el código ya existe. */
    public boolean insertar(Libro libro) {
        if (libro == null || buscar(libro.getCodigo()) != null) {
            return false;
        }
        raiz = insertar(raiz, libro);
        return true;
    }

    private NodoLibro insertar(NodoLibro nodo, Libro libro) {
        if (nodo == null) {
            return new NodoLibro(libro);
        }
        if (libro.getCodigo() < nodo.getDato().getCodigo()) {
            nodo.setIzquierdo(insertar(nodo.getIzquierdo(), libro));
        } else {
            nodo.setDerecho(insertar(nodo.getDerecho(), libro));
        }
        return nodo;
    }

    // ------------------------------------------------------------------ buscar
    /** Busca un libro. null si no existe. */
    public Libro buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private Libro buscar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo == actual) {
            return nodo.getDato();
        }
        return (codigo < actual) ? buscar(nodo.getIzquierdo(), codigo)
                                 : buscar(nodo.getDerecho(), codigo);
    }

    // ------------------------------------------------------------------ contar
    public int contar() {
        return contar(raiz);
    }

    private int contar(NodoLibro nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contar(nodo.getIzquierdo()) + contar(nodo.getDerecho());
    }

    // --------------------------------------------------------------- recorridos
    public List<Libro> recorrer(TipoRecorrido tipo) {
        List<Libro> resultado = new ArrayList<>();
        recorrer(raiz, tipo, resultado);
        return resultado;
    }

    private void recorrer(NodoLibro nodo, TipoRecorrido tipo, List<Libro> out) {
        if (nodo == null) {
            return;
        }
        if (tipo == TipoRecorrido.PREORDEN) {
            out.add(nodo.getDato());
        }
        recorrer(nodo.getIzquierdo(), tipo, out);
        if (tipo == TipoRecorrido.INORDEN) {
            out.add(nodo.getDato());
        }
        recorrer(nodo.getDerecho(), tipo, out);
        if (tipo == TipoRecorrido.POSORDEN) {
            out.add(nodo.getDato());
        }
    }

    /**
     * Traza del recorrido: "entra X" al entrar al nodo, "VISITA X" al procesarlo,
     * "null -> regresa" en un hijo vacío, 2 espacios por nivel y última línea
     * "Secuencia TIPO: ...".
     */
    public String trazaRecorrido(TipoRecorrido tipo) {
        StringBuilder traza = new StringBuilder();
        StringBuilder secuencia = new StringBuilder();
        traza(raiz, tipo, 0, traza, secuencia);
        traza.append("Secuencia ").append(tipo).append(": ").append(secuencia.toString().trim());
        return traza.toString();
    }

    private void traza(NodoLibro nodo, TipoRecorrido tipo, int nivel, StringBuilder t, StringBuilder sec) {
        String sangria = "  ".repeat(nivel);
        if (nodo == null) {
            t.append(sangria).append("null -> regresa\n");
            return;
        }
        int codigo = nodo.getDato().getCodigo();
        t.append(sangria).append("entra ").append(codigo).append("\n");
        if (tipo == TipoRecorrido.PREORDEN) {
            visitar(codigo, sangria, t, sec);
        }
        traza(nodo.getIzquierdo(), tipo, nivel + 1, t, sec);
        if (tipo == TipoRecorrido.INORDEN) {
            visitar(codigo, sangria, t, sec);
        }
        traza(nodo.getDerecho(), tipo, nivel + 1, t, sec);
        if (tipo == TipoRecorrido.POSORDEN) {
            visitar(codigo, sangria, t, sec);
        }
    }

    private void visitar(int codigo, String sangria, StringBuilder t, StringBuilder sec) {
        t.append(sangria).append("VISITA ").append(codigo).append("\n");
        sec.append(" ").append(codigo);
    }

    // ---------------------------------------------------------------- modificar
    /** Modifica título, autor y año (nunca el código). false si no existe. */
    public boolean modificar(int codigo, String titulo, String autor, int anio) {
        Libro libro = buscar(codigo);
        if (libro == null) {
            return false;
        }
        new Libro(codigo, titulo, autor, anio); // valida los datos antes de modificar (lanza IllegalArgumentException)
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnio(anio);
        return true;
    }

    // ----------------------------------------------------------------- eliminar
    /** Elimina hoja, nodo con un hijo o con dos hijos (sucesor inorden). false si no existe. */
    public boolean eliminar(int codigo) {
        if (buscar(codigo) == null) {
            return false;
        }
        raiz = eliminar(raiz, codigo);
        return true;
    }

    private NodoLibro eliminar(NodoLibro nodo, int codigo) {
        if (nodo == null) {
            return null;
        }
        int actual = nodo.getDato().getCodigo();
        if (codigo < actual) {
            nodo.setIzquierdo(eliminar(nodo.getIzquierdo(), codigo));
        } else if (codigo > actual) {
            nodo.setDerecho(eliminar(nodo.getDerecho(), codigo));
        } else {
            // Caso 1 y 2: hoja o un solo hijo
            if (nodo.getIzquierdo() == null) {
                return nodo.getDerecho();
            }
            if (nodo.getDerecho() == null) {
                return nodo.getIzquierdo();
            }
            // Caso 3: dos hijos -> sucesor inorden (mínimo del subárbol derecho)
            NodoLibro sucesor = minimo(nodo.getDerecho());
            nodo.setDato(sucesor.getDato());
            nodo.setDerecho(eliminar(nodo.getDerecho(), sucesor.getDato().getCodigo()));
        }
        return nodo;
    }

    private NodoLibro minimo(NodoLibro nodo) {
        if (nodo.getIzquierdo() == null) {
            return nodo;
        }
        return minimo(nodo.getIzquierdo());
    }

    // -------------------------------------------------------------------- retos
    /** Cuenta los códigos en [a, b] podando las ramas que no pueden contener valores del rango. */
    public int contarEnRango(int a, int b) {
        return contarEnRango(raiz, a, b);
    }

    private int contarEnRango(NodoLibro nodo, int a, int b) {
        if (nodo == null) {
            return 0;
        }
        int c = nodo.getDato().getCodigo();
        if (c < a) {
            return contarEnRango(nodo.getDerecho(), a, b);   // poda: izquierda toda < a
        }
        if (c > b) {
            return contarEnRango(nodo.getIzquierdo(), a, b); // poda: derecha toda > b
        }
        return 1 + contarEnRango(nodo.getIzquierdo(), a, b) + contarEnRango(nodo.getDerecho(), a, b);
    }

    /** Códigos visitados desde la raíz hasta el libro (o hasta donde estaría si no existe). */
    public List<Integer> caminoBusqueda(int codigo) {
        List<Integer> camino = new ArrayList<>();
        caminoBusqueda(raiz, codigo, camino);
        return camino;
    }

    private void caminoBusqueda(NodoLibro nodo, int codigo, List<Integer> camino) {
        if (nodo == null) {
            return;
        }
        int actual = nodo.getDato().getCodigo();
        camino.add(actual);
        if (codigo < actual) {
            caminoBusqueda(nodo.getIzquierdo(), codigo, camino);
        } else if (codigo > actual) {
            caminoBusqueda(nodo.getDerecho(), codigo, camino);
        }
    }

    /** Verifica que TODOS los nodos respeten los límites mínimo y máximo heredados. */
    public boolean esABBValido() {
        return esABBValido(raiz, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean esABBValido(NodoLibro nodo, long min, long max) {
        if (nodo == null) {
            return true;
        }
        int c = nodo.getDato().getCodigo();
        if (c <= min || c >= max) {
            return false;
        }
        return esABBValido(nodo.getIzquierdo(), min, c)
            && esABBValido(nodo.getDerecho(), c, max);
    }

    // -------------------------------------------------------------------- extra
    /**
     * Reconstruye un ABB a partir de su preorden en O(n), sin insertar uno por uno.
     * Los libros se crean con título/autor genéricos porque el preorden solo trae códigos.
     */
    public static CatalogoABB desdePreorden(int[] preorden) {
        CatalogoABB catalogo = new CatalogoABB();
        if (preorden != null) {
            catalogo.setRaiz(construir(preorden, new int[]{0}, Long.MIN_VALUE, Long.MAX_VALUE));
        }
        return catalogo;
    }

    private static NodoLibro construir(int[] pre, int[] indice, long min, long max) {
        if (indice[0] >= pre.length) {
            return null;
        }
        int valor = pre[indice[0]];
        if (valor <= min || valor >= max) {
            return null;
        }
        indice[0]++;
        NodoLibro nodo = new NodoLibro(new Libro(valor, "Libro " + valor, "Autor desconocido", 2000));
        nodo.setIzquierdo(construir(pre, indice, min, valor));
        nodo.setDerecho(construir(pre, indice, valor, max));
        return nodo;
    }
}
