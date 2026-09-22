# Manual de Usuario — RoutinEXE

## Índice
1. Introducción
2. Primera ejecución
3. Gestión de usuarios
4. Gestión de categorías
5. Gestión de ejercicios
6. Gestión de rutinas
7. Sesiones de entrenamiento
8. Dashboard
9. Calendario
10. Resolución de problemas

---

## 1. Introducción

RoutinEXE es una aplicación de escritorio para gestionar rutinas de ejercicio de forma offline. Permite crear y organizar usuarios, categorías de ejercicios, rutinas personalizadas y registrar sesiones de entrenamiento con seguimiento detallado.

## 2. Primera ejecución

Al abrir RoutinEXE por primera vez:
- Se crea automáticamente la base de datos SQLite en la carpeta `data/` (dentro del directorio de la aplicación o del directorio de trabajo actual).
- Aparece la pantalla de **gestión de usuarios**.
- No se requiere configuración adicional.

**Requisito**: JDK 25+ instalado en el sistema.

## 3. Gestión de usuarios

Desde la pantalla inicial se pueden gestionar los usuarios:

### Crear usuario
1. Haz clic en el botón **Crear** (botón azul/primario).
2. Rellena el campo **nombre de usuario** (obligatorio).
3. Opcionalmente, indica **edad**, **altura** y **peso**.
4. Haz clic en **Guardar**.

### Editar usuario
1. Selecciona un usuario en la tabla.
2. Haz clic en el botón **editar** (icono de lápiz) en la fila.
3. Modifica los campos y guarda.

### Eliminar usuario
1. Selecciona un usuario en la tabla.
2. Haz clic en el botón **eliminar** (icono de papelera).
3. Confirma la eliminación en el diálogo.

> **Nota**: Al seleccionar un usuario, se accede a su dashboard con todas las funcionalidades.

## 4. Gestión de categorías

Las categorías permiten agrupar ejercicios (ej. Cardio, Fuerza, Flexibilidad).

### Crear categoría
1. En la vista de categorías, haz clic en **Crear**.
2. Introduce el **nombre** de la categoría.
3. Guarda.

### Editar/Eliminar
- Usa los botones de acción (lápiz / papelera) en la tabla.

## 5. Gestión de ejercicios

Los ejercicios pertenecen a una categoría y pueden tener descripción y marca de tiempo.

### Crear ejercicio
1. En la vista de ejercicios, haz clic en **Crear**.
2. Rellena:
   - **Nombre** (obligatorio)
   - **Categoría** (seleccionar de la lista)
   - **Descripción** (opcional)
   - **Basado en tiempo** (checkbox, si el ejercicio se mide por duración)
3. Guarda.

### Editar/Eliminar
- Usa los botones de acción en la tabla.

## 6. Gestión de rutinas

Las rutinas se asocian a un usuario y contienen ejercicios programados por días de la semana.

### Crear rutina
1. En la vista de rutinas, haz clic en **Crear**.
2. Selecciona el **usuario** al que pertenece la rutina.
3. Introduce el **nombre** de la rutina.
4. Establece las **fechas de inicio y fin** (opcionales).
5. Selecciona los **días de la semana** en los que se entrena.
6. Asigna ejercicios a cada día con sus **series** y **repeticiones**:
   - Añade ejercicios desde la lista.
   - Para cada ejercicio asignado, indica series y repeticiones.
7. Guarda.

### Editar/Eliminar
- Usa los botones de acción en la tabla.

## 7. Sesiones de entrenamiento

Las sesiones registran las prácticas realizadas, con seguimiento de cada ejercicio.

### Ver sesiones
1. Haz clic en la pestaña **Sesiones** del sidebar del dashboard.
2. Usa el **calendario** para navegar por meses.
3. Haz clic en una fecha para abrir una sesión.

### Crear sesión
1. Selecciona una fecha en el calendario o haz clic en **Iniciar sesión** para la fecha actual.
2. Selecciona la rutina asociada.
3. La vista muestra los ejercicios programados para ese día.
4. Para cada ejercicio, registra:
   - **Series**
   - **Repeticiones**
   - **Peso** (opcional)
   - **Hecho** (checkbox para marcar como completado)
5. Guarda.

### Marcar/desmarcar como completado
- Usa el checkbox **Done** en cada ejercicio de la sesión para marcar o desmarcar su completado.
- Puedes marcar el día completo como hecho con el interruptor (switch) de **Session Day Done**.

> **Importante**: Las sesiones de días pasados son de solo lectura. Solo se pueden editar las sesiones del día actual.

## 8. Dashboard

Al seleccionar un usuario desde la pantalla principal, se accede al dashboard:

- **Barra lateral (sidebar)**: Navegación a Usuarios, Categorías, Ejercicios, Rutinas y Sesiones.
- **Estadísticas**: Tarjetas con información resumida del usuario.
- **Botón de cerrar sesión**: Volver a la pantalla de selección de usuario.

### Navegación
- Haz clic en los botones de la barra lateral para ir a cada sección.
- Las vistas del dashboard están pre-filtradas por el usuario seleccionado.

## 9. Calendario

El calendario en la vista de sesiones permite:

- **Navegación**: Usa las flechas para ir al mes anterior/siguiente.
- **Crear sesión**: Haz clic en una celda de fecha disponible para crear una sesión.
- **Reset**: Al seleccionar una rutina, el calendario se resetea al mes actual.

## 10. Resolución de problemas

### La aplicación no arranca
- Asegúrate de que tienes **JDK 25** o superior instalado.
- Verifica que `java -version` devuelve la versión correcta.

### Error de base de datos
- El archivo `data/routin-exe.db` se crea automáticamente. No lo elimines manualmente.
- Si hay corrupción, realiza una copia de seguridad y elimina el archivo para que se cree uno nuevo.

### La AppImage no ejecuta
- Asegúrate de que el archivo tiene permisos de ejecución:
  ```bash
  chmod +x RoutinEXE-x86_64.AppImage
  ```
- Verifica que tu sistema sea x86_64 (64-bit).

### No se muestran datos en el dashboard
- Asegúrate de que has creado al menos un usuario y una rutina.
- El dashboard solo muestra datos asociados al usuario seleccionado.

### Advertencias en la consola
- Las advertencias sobre "restricted method" y "native access" son normales en Java 25 y no afectan el funcionamiento.
- "xapp-gtk3-module" es un módulo GTK opcional no esencial.

---

**RoutinEXE v1.0.0**
