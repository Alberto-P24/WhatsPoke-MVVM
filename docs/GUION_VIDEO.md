# Guion de Video Demostrativo - WhatsPoke MVVM
**Duración estimada:** 2-3 minutos

---

## Introducción (0:00 - 0:10)
"Hola, en este video demostraré la aplicación WhatsPoke, refactorizada bajo la arquitectura MVVM con DataStore, Retrofit, Room y flujos de estado."

---

## 1. Cambio de Nombre y Tema en DataStore (0:10 - 0:40)
1. Navegar a la pestaña **Profile** desde el BottomNavigation.
2. Mostrar el nombre actual (por defecto: "Trainer").
3. Modificar el campo de texto a un nombre personalizado (ej. "Ash").
4. Pulsar el botón **Save**.
5. Observar que el nombre se actualiza inmediatamente gracias al StateFlow.
6. Seleccionar el tema **Dark** mediante el RadioButton correspondiente.
7. Mostrar cómo toda la aplicación cambia a modo oscuro en tiempo real.
8. Volver a seleccionar **System** para restaurar el tema por defecto.

**Texto guion:** "En el perfil, persistimos el nombre de usuario y la preferencia de tema usando DataStore. Los cambios se reflejan instantáneamente gracias a los StateFlows."

---

## 2. Carga de API y Navegación al Detalle (0:40 - 1:10)
1. Navegar a la pestaña **Home**.
2. Mostrar la lista de Pokemon cargándose desde la PokeAPI (mostrar spinner brevemente).
3. Pulsar sobre un Pokemon (ej. Bulbasaur) para abrir el detalle.
4. En el detalle, mostrar toda la información extendida: imagen grande, tipo, altura, peso, habilidades y estadísticas base.
5. Resaltar que esta información proviene de una segunda llamada a la API (`/pokemon/{id}`).

**Texto guion:** "La lista inicial se carga desde PokeAPI. Al pulsar un elemento, realizamos una consulta adicional para obtener datos detallados como stats, habilidades y medidas."

---

## 3. Guardado de Favorito y Mensaje de "Ya Guardado" (1:10 - 1:35)
1. En la pantalla de detalle, pulsar el botón **Save to Favorites**.
2. Mostrar el Toast: "Pokemon guardado en favoritos".
3. Volver atrás y mostrar que el icono del corazón en la lista ahora está amarillo y relleno (icono distintivo).
4. Intentar volver a guardar el mismo Pokemon desde el detalle o pulsar el corazón en la lista.
5. Mostrar el Toast: "Este Pokemon ya es favorito" (no se elimina, solo se informa).

**Texto guion:** "Al guardar, se inserta en Room. Si el Pokemon ya existe, el sistema detecta el duplicado y muestra un Toast informativo sin eliminar el registro."

---

## 4. Navegación a Favoritos, Detalle y Comentarios (1:35 - 2:15)
1. Navegar a la pestaña **Favorites**.
2. Mostrar la lista de favoritos persistidos en Room.
3. Pulsar sobre el Pokemon favorito para ver su detalle local.
4. Mostrar la sección de comentarios (inicialmente vacía o con existentes).
5. Pulsar el **FAB (+)** para mostrar el campo de nuevo comentario.
6. Escribir un comentario y pulsar **Send**.
7. Mostrar el comentario publicado con la firma del autor: "by Ash" (nombre obtenido del DataStore).
8. Pulsar el icono de **papelera** en un comentario para mostrar la confirmación de borrado.

**Texto guion:** "En favoritos, los datos provienen de Room. Cada comentario se firma automáticamente con el nombre del usuario almacenado en DataStore, demostrando la relación compleja entre entidades."

---

## 5. Borrado de Favorito con Confirmación (2:15 - 2:40)
1. Desde la lista de favoritos, pulsar el icono del corazón amarillo de un Pokemon.
2. Mostrar el AlertDialog: "Delete Favorite - Are you sure you want to delete X from favorites?".
3. Pulsar **Confirm**.
4. Observar cómo el elemento desaparece de la lista inmediatamente (actualización en vivo mediante Flow).
5. Pulsar **Cancel** en otro intento para mostrar que se aborta la operación.

**Texto guion:** "El borrado requiere confirmación explícita. Al confirmar, Room emite el nuevo estado y la UI se actualiza automáticamente gracias a los flujos de estado."

---

## Cierre (2:40 - 2:55)
"WhatsPoke cumple con todos los requisitos de la arquitectura MVVM: separación de capas, unidireccionalidad de datos, uso de StateFlow/SharedFlow, persistencia local con Room, configuración con DataStore y consumo de API con Retrofit."

---

**Notas técnicas para grabación:**
- Usar Android Studio Emulator con API 34+.
- Abrir App Inspection para mostrar tablas de Room en paralelo durante la demo.
- Abrir Network Inspector para capturar el tráfico de la API en vivo.
- Mostrar Device File Explorer para evidenciar el archivo .preferences_pb de DataStore.
