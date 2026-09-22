# RoutinEXE

**RoutinEXE** — Aplicación de gestión de rutinas de ejercicio multiplataforma y offline.

## Descripción

RoutinEXE es una aplicación de escritorio JavaFX que permite gestionar rutinas de ejercicios, sesiones de entrenamiento, categorías de ejercicios y usuarios. Funciona completamente offline con una base de datos SQLite local.

## Funcionalidades

- **Usuarios**: Crear, editar y eliminar usuarios (nombre obligatorio, edad, altura y peso opcionales).
- **Categorías**: Gestionar categorías de ejercicios (ej. Cardio, Fuerza, Flexibilidad...).
- **Ejercicios**: CRUD de ejercicios asignados a categorías, con descripción y marca de tiempo.
- **Rutinas**: Crear rutinas por usuario con días de la semana, fecha inicio/fin, y ejercicios asignados (series, repeticiones).
- **Sesiones**: Registro de sesiones de entrenamiento por fecha, con seguimiento de ejercicios completados (done/undo) y marca de día completado.
- **Dashboard**: Vista resumida por usuario con estadísticas y navegación a todas las secciones.
- **Calendario**: Vista de calendario para gestionar sesiones por día y mes.

## Arquitectura

```
src/main/java/com/routineexe/
├── view/           # Interfaces de usuario (JavaFX)
│   ├── Main.java              # Punto de entrada principal
│   ├── DashboardLayout.java   # Layout con sidebar y dashboard
│   ├── DashboardView.java
│   ├── users/                 # UserView, UserListView, UserFormDialog...
│   ├── categories/            # CategoryView, CategoryListView...
│   ├── exercises/             # ExerciseView, ExerciseListView...
│   ├── routines/              # RoutineView, RoutineListView...
│   └── sessions/              # SessionView, SessionListView...
├── model/          # Modelos de datos
│   ├── User.java
│   ├── Category.java
│   ├── Exercise.java
│   ├── Routine.java
│   ├── RoutineExercise.java
│   ├── Session.java
│   └── SessionExercise.java
├── database/       # Acceso a datos (DAO + helper)
│   ├── DatabaseHelper.java    # Conexión SQLite + esquema
│   ├── UserDAO.java
│   ├── CategoryDAO.java
│   ├── ExerciseDAO.java
│   ├── RoutineDAO.java
│   ├── RoutineExerciseDAO.java
│   ├── SessionDAO.java
│   └── SessionExerciseDAO.java
└── util/           # Utilidades
    └── FontAwesomeIcons.java

src/main/resources/
├── styles.css              # Estilos CSS (tema oscuro)
├── bootstrap-icons.css     # Iconos Bootstrap
└── fonts/                  # Font Awesome + Bootstrap Icons (.ttf/.woff2)

data/
└── routin-exe.db           # Base de datos SQLite (generada automáticamente)
```

## Tecnologías

| Tecnología | Versión |
|------------|---------|
| Java | 25 |
| JavaFX | 25.0.4 |
| SQLite JDBC | 3.49.1.0 |
| Build | Gradle + Maven |

## Requisitos previos

- **JDK 25** o superior
- Linux (la aplicación se ejecuta en escritorio); también soporta Windows y macOS (perfiles en pom.xml)

## Construcción

### Con Gradle
```bash
./gradlew build
```

### Con Maven
```bash
mvn clean package
```

## Ejecución

### AppImage (recomendado para Linux)
```bash
./dist/RoutinEXE-x86_64.AppImage
```

### JAR
```bash
java -jar dist/routin-exe.jar
```

### Desde IDE
Ejecutar la clase `com.routineexe.view.Main`.

## Base de datos

La base de datos SQLite se crea automáticamente en `data/routin-exe.db` al primer arranque. Contiene las siguientes tablas:

| Tabla | Descripción |
|-------|-------------|
| users | Usuarios (id, username, age, height, weight) |
| categories | Categorías de ejercicios |
| exercises | Ejercicios (nombre, categoría, descripción, time_based) |
| routines | Rutinas (usuario, nombre, fecha inicio/fin) |
| routine_days | Días de la semana asignados a rutinas |
| routine_exercises | Ejercicios asignados a rutinas (día, series, repeticiones) |
| sessions | Sesiones de entrenamiento (rutina, fecha, día, completado) |
| session_exercises | Ejercicios en sesiones (series, repeticiones, peso, hecho) |

## Estructura de distribución

```
dist/
├── RoutinEXE-x86_64.AppImage    # Ejecutable Linux (83MB)
└── routin-exe.jar               # JAR ejecutable

AppDir/                           # Estructura para AppImage
├── AppRun
├── RoutinEXE.desktop
├── RoutinEXE.png
└── usr/
    ├── lib/
    │   ├── runtime/             # JRE personalizado (jlink)
    │   ├── app/routin-exe.jar
    │   ├── javafx/              # Módulos JavaFX
    │   └── sqlite-jdbc-*.jar
```

## Licencia

Proyecto privado de desarrollo.
