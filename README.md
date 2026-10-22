# Catálogo de la Biblioteca FISEI — Árbol general y ABB en Java

**APE 3 · Deber individual — Estructura de Datos (Tercero, Ingeniería de Software)**

## 1. Problema

La Biblioteca FISEI se organiza en **Áreas** y cada Área en **Estantes**. Esa jerarquía
(`BIBLIOTECA → AREA → ESTANTE`) se modela con un **árbol general n-ario** (`ArbolBiblioteca`).
Cada Estante guarda sus libros en un **Árbol Binario de Búsqueda** (`CatalogoABB`) ordenado por el
código del libro (entero positivo, sin repetidos).

La aplicación permite construir, consultar, recorrer y modificar ambas estructuras usando
**programación orientada a objetos y recursividad** (sin `TreeSet`, `TreeMap`, `PriorityQueue`,
`NavigableSet`, `NavigableMap`, `Collections.sort` ni `List.sort`).

```
BIB Biblioteca FISEI
├── SW Software
│   ├── SW-E1 Estante 1 de Software → 60, 35, 80, 20, 45, 70, 90, 40, 75
│   └── SW-E2 Estante 2 de Software → 15, 10, 25
├── TI Tecnologías de la Información
│   └── TI-E1 Estante 1 de TI → 50
└── TEL Telecomunicaciones
```

## 2. Estructura del proyecto

```
src/
├── enums/    TipoUnidad.java, TipoRecorrido.java
├── modelo/   Libro.java, UnidadBiblioteca.java
├── negocio/  NodoUnidad.java, NodoLibro.java, ArbolBiblioteca.java, CatalogoABB.java
└── app/      Main.java, DatosPrueba.java, PruebasBiblioteca.java
```

## 3. Requisitos

- JDK 11 o superior (probado con JDK 21).
- Visual Studio Code con el *Extension Pack for Java* (opcional) o la terminal.

## 4. Instrucciones de ejecución

Desde la raíz del repositorio:

```bash
# Compilar
javac -encoding UTF-8 -d out $(find src -name "*.java")      # Linux / macOS / Git Bash
# En PowerShell:
# javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName

# Menú interactivo
java -cp out app.Main

# Pruebas automáticas (muestra "X/N pruebas OK")
java -cp out app.PruebasBiblioteca
```

> Si en Windows se ven mal las tildes, ejecute con `java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out app.Main`.

En VS Code: abrir la carpeta, ejecutar `Main.java` o `PruebasBiblioteca.java` con el botón *Run*.

## 5. Menú principal

1. Cargar estructura inicial · 2. Mostrar jerarquía · 3. Agregar unidad · 4. Buscar unidad ·
5. Altura, hojas, nivel y total de libros · 6. Insertar libro · 7. Buscar libro · 8. Modificar libro ·
9. Eliminar libro · 10. Recorridos con traza · 11. Retos (rango, camino, validez, `desdePreorden`) · 0. Salir

## 6. Captura de `PruebasBiblioteca`

> Reemplace esta línea por su captura: `![Pruebas](docs/captura_pruebas.png)`

Resultado esperado: `20/20 pruebas OK`.

## 7. Documentación adicional

- [`TRAZAS.md`](TRAZAS.md): resolución a mano de inserciones, recorridos, traza y eliminaciones.

## 8. Autor

Justin Guanoquiza  3BSW
