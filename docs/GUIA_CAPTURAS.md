# Guía de Capturas de Pantalla - WhatsPoke MVVM

## 1. Pantalla Principal (ElemListScreen)
- **Descripción:** Lista de Pokemon cargada desde la API (PokeAPI).
- **Contenido visual:**
  - AppBar/BottomNavigation con ítems: Home, Favorites, Profile, About.
  - Lista scrolleable de tarjetas (PokemonCard) con imagen, nombre y tipo.
  - Icono de corazón vacío en cada tarjeta (Add to favorites).
  - Si algún Pokemon ya está en favoritos, mostrar corazón amarillo relleno (icono distintivo).
  - Estado de carga: CircularProgressIndicator centrado.

## 2. Pantalla de Detalle desde API (DetailItemScreen)
- **Descripción:** Información extendida de un Pokemon obtenida de la API.
- **Contenido visual:**
  - Imagen grande del Pokemon (Coil AsyncImage).
  - Nombre del Pokemon en headlineLarge.
  - Type, Height, Weight, Abilities, Stats (mucho más contenido que en la lista).
  - Botón "Save to Favorites" habilitado/deshabilitado según estado.

## 3. Pantalla de Perfil (ProfileScreen)
- **Descripción:** Configuración de usuario y tema.
- **Contenido visual:**
  - Icono grande de perfil.
  - Texto mostrando el nombre actual del usuario (DataStore).
  - Campo de texto (OutlinedTextField) para modificar nombre.
  - Botón "Save" para persistir nombre.
  - Grupo de RadioButtons: Light, Dark, System.
  - Tema cambiando en tiempo real al seleccionar una opción.

## 4. Pantalla de Favoritos (FavListScreen)
- **Descripción:** Lista de Pokemon guardados en Room.
- **Contenido visual:**
  - Lista scrolleable de tarjetas favoritas.
  - Icono de corazón amarillo relleno en cada tarjeta.
  - Al pulsar el corazón: AlertDialog de confirmación (título, mensaje, Confirm/Cancel).
  - Lista actualizándose "en vivo" tras borrar un elemento.

## 5. Pantalla de Detalle de Favorito (DetailFavScreen)
- **Descripción:** Detalle del Pokemon local con comentarios.
- **Contenido visual:**
  - Imagen, nombre, tipo, descripción, habilidades, stats.
  - Lista de comentarios mostrando contenido y autor ("by Trainer").
  - FloatingActionButton (FAB) con icono "+" para añadir comentario.
  - Campo de texto y botón Send para publicar comentario.
  - Icono de eliminar en cada comentario.

## 6. Pantalla About (AboutScreen)
- **Descripción:** Información de la aplicación.
- **Contenido visual:**
  - Nombre de la app, temática, versión.
  - Icono de email funcional para abrir selector de correo.
  - Sin literales hardcoded, todo desde strings.xml.

## 7. App Inspection - Base de Datos Room
- **Descripción:** Captura de la herramienta App Inspection de Android Studio.
- **Contenido visual:**
  - Tabla `pokemon_favorites` con columnas: id, name, description, imageUrl, type, height, weight, abilities, stats.
  - Tabla `comments` con columnas: commentId, pokemonId, authorName, content, timestamp.
  - Relación visible: comentarios vinculados a pokemonId.

## 8. DataStore - Fichero de Configuración Local
- **Descripción:** Captura del archivo generado por Preferences DataStore.
- **Contenido visual:**
  - Ruta: `/data/data/com.example.whatspoke/files/datastore/user_settings.preferences_pb` (o mediante Device File Explorer).
  - Contenido legible mostrando claves: `user_name` con valor personalizado, `theme_mode` con valor (light/dark/system).

## 9. Network Inspector - Tráfico de Red
- **Descripción:** Captura de la herramienta Network Inspector durante la llamada a la API.
- **Contenido visual:**
  - Petición GET a `https://pokeapi.co/api/v2/pokemon?limit=20`.
  - Petición GET a `https://pokeapi.co/api/v2/pokemon/{id}`.
  - Código de respuesta 200 OK.
  - Payload JSON visible con campos count, results, name, url, sprites, types, etc.
  - Tiempo de respuesta y encabezados HTTP.
