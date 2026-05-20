# Documentación Técnica - WhatsPoke MVVM

## 1. Arquitectura General

La aplicación sigue estrictamente la arquitectura **MVVM (Model-View-ViewModel)** con separación de responsabilidades en capas:

- **UI Layer:** Screens (Compose) + ViewModels (StateFlow/SharedFlow).
- **Domain Layer:** Modelos de dominio (`Pokemon`, mappers DTO/Entity).
- **Data Layer:** Repositories + Data Sources (Remote API, Local Database, DataStore).
- **DI Layer:** Inyección de dependencias con Hilt.

---

## 2. Diagrama de Clases (Descripción para dibujar)

### 2.1 Paquete `data.local`
```
AppDatabase (abstract class, RoomDatabase)
  + pokemonDao(): PokemonDao
  + commentDao(): CommentDao

PokemonDao (interface, @Dao)
  + getAllFavorites(): Flow<List<PokemonEntity>>
  + getFavoriteById(id: Int): PokemonEntity?
  + insertFavorite(pokemon: PokemonEntity): Long
  + deleteFavoriteById(id: Int)
  + isFavoriteFlow(id: Int): Flow<Boolean>
  + getPokemonWithComments(id: Int): Flow<PokemonWithComments?>

CommentDao (interface, @Dao)
  + getCommentsForPokemon(pokemonId: Int): Flow<List<CommentEntity>>
  + insertComment(comment: CommentEntity)
  + deleteCommentById(commentId: Long)

PokemonEntity (@Entity tableName="pokemon_favorites")
  - id: Int (@PrimaryKey)
  - name: String
  - description: String
  - imageUrl: String
  - type: String
  - height: Int
  - weight: Int
  - abilities: String
  - stats: String

CommentEntity (@Entity tableName="comments")
  - commentId: Long (@PrimaryKey autoGenerate)
  - pokemonId: Int (ForeignKey -> PokemonEntity.id, CASCADE)
  - authorName: String
  - content: String
  - timestamp: Long

PokemonWithComments
  - pokemon: PokemonEntity (@Embedded)
  - comments: List<CommentEntity> (@Relation parentColumn="id", entityColumn="pokemonId")
```

### 2.2 Paquete `data.local.datastore`
```
SettingsDataStore (@Singleton)
  - context: Context
  + userNameFlow: Flow<String>
  + themeModeFlow: Flow<String>
  + saveUserName(name: String): suspend
  + saveThemeMode(mode: String): suspend
```

### 2.3 Paquete `data.remote`
```
PokeApiService (interface, Retrofit)
  + getPokemonList(limit, offset): PokemonListResponseDto
  + getPokemonDetail(id): PokemonDetailResponseDto

PokemonListResponseDto
  - count: Int
  - next: String?
  - previous: String?
  - results: List<PokemonResultDto>

PokemonResultDto
  - name: String
  - url: String

PokemonDetailResponseDto
  - id: Int
  - name: String
  - height: Int
  - weight: Int
  - sprites: SpritesDto
  - types: List<TypeSlotDto>
  - abilities: List<AbilitySlotDto>
  - stats: List<StatSlotDto>
```

### 2.4 Paquete `data.repository`
```
PokemonApiRepository (@Singleton)
  - pokeApiService: PokeApiService
  + getPokemonList(...): PokemonListResponseDto
  + getPokemonDetail(...): PokemonDetailResponseDto

FavoriteRepository (@Singleton)
  - pokemonDao: PokemonDao
  + getAllFavorites(): Flow<List<PokemonEntity>>
  + addFavorite(pokemon): Boolean
  + removeFavorite(id)
  + isFavoriteFlow(id): Flow<Boolean>
  + getPokemonWithComments(id): Flow<PokemonWithComments?>

CommentRepository (@Singleton)
  - commentDao: CommentDao
  + getCommentsForPokemon(id): Flow<List<CommentEntity>>
  + addComment(comment)
  + deleteComment(id)

SettingsRepository (@Singleton)
  - settingsDataStore: SettingsDataStore
  + userNameFlow: Flow<String>
  + themeModeFlow: Flow<String>
  + saveUserName(name)
  + saveThemeMode(mode)
```

### 2.5 Paquete `ui.viewmodel`
```
UiState<out T> (sealed class)
  - Loading
  - Success<T>(data: T)
  - Error(message: String)

ElemListViewModel (@HiltViewModel)
  - pokemonApiRepository, favoriteRepository
  + pokemonListState: StateFlow<UiState<List<Pokemon>>>
  + favoriteIds: StateFlow<Set<Int>>
  + eventFlow: SharedFlow<String>
  + loadPokemonList()
  + addToFavorites(pokemon)

DetailItemViewModel (@HiltViewModel)
  - pokemonApiRepository, favoriteRepository
  + pokemonDetailState: StateFlow<UiState<Pokemon>>
  + isFavorite: StateFlow<Boolean>
  + eventFlow: SharedFlow<String>
  + loadPokemonDetail(id)
  + toggleFavorite()

FavListViewModel (@HiltViewModel)
  - favoriteRepository
  + favoritesState: StateFlow<UiState<List<Pokemon>>>
  + loadFavorites()
  + deleteFavorite(id)

DetailFavViewModel (@HiltViewModel)
  - favoriteRepository, commentRepository
  + pokemonWithComments: StateFlow<PokemonWithComments?>
  + comments: StateFlow<List<CommentEntity>>
  + loadPokemonWithComments(id)
  + addComment(pokemonId, authorName, content)
  + deleteComment(commentId)

ProfileViewModel (@HiltViewModel)
  - settingsRepository
  + userName: StateFlow<String>
  + themeMode: StateFlow<String>
  + saveUserName(name)
  + saveThemeMode(mode)
```

### 2.6 Paquete `model`
```
Pokemon (data class)
  - id, name, description, imageUrl, type, height, weight, abilities, stats, isFavorite

Extension Functions:
  + PokemonResultDto.toPokemon(): Pokemon
  + PokemonDetailResponseDto.toPokemon(): Pokemon
  + PokemonEntity.toPokemon(): Pokemon
  + Pokemon.toEntity(): PokemonEntity
```

---

## 3. Diagrama de Casos de Uso (Descripción para dibujar)

### Actores:
- **Usuario:** Interactúa con la aplicación.

### Casos de Uso:
1. **CU-01: Ver Lista de Pokemon**
   - Actor: Usuario
   - Descripción: El usuario abre la app y ve una lista de Pokemon cargada desde la API.
   - Flujo: Home -> Carga API -> Muestra lista con Coil.

2. **CU-02: Ver Detalle de Pokemon (API)**
   - Actor: Usuario
   - Descripción: Al pulsar un Pokemon, se consulta la API para obtener información extendida.
   - Flujo: Pulsar tarjeta -> Llamada Retrofit GET /pokemon/{id} -> Muestra detalle completo.

3. **CU-03: Guardar Favorito**
   - Actor: Usuario
   - Descripción: Desde la lista o el detalle, el usuario guarda un Pokemon en Room.
   - Flujo: Pulsar corazón/botón -> ViewModel -> Repository -> DAO insert -> Icono cambia a amarillo.

4. **CU-04: Detectar Duplicado de Favorito**
   - Actor: Sistema
   - Descripción: Si el Pokemon ya existe en Room, se muestra un Toast y no se elimina.
   - Flujo: Pulsar guardar -> Repository verifica existencia -> Emite evento SharedFlow -> Toast "ya es favorito".

5. **CU-05: Ver Favoritos**
   - Actor: Usuario
   - Descripción: Navega a la pestaña Favorites y ve los Pokemon almacenados localmente.
   - Flujo: Tab Favorites -> Repository -> DAO Flow -> UI actualiza en vivo.

6. **CU-06: Eliminar Favorito**
   - Actor: Usuario
   - Descripción: El usuario elimina un favorito tras confirmar un diálogo.
   - Flujo: Pulsar corazón amarillo -> AlertDialog -> Confirmar -> DAO delete -> Flow emite nueva lista.

7. **CU-07: Añadir Comentario**
   - Actor: Usuario
   - Descripción: En el detalle de un favorito, añade un comentario firmado por su nombre.
   - Flujo: Pulsar FAB -> Escribir comentario -> Send -> ViewModel obtiene userName de DataStore -> Insert CommentEntity -> Lista actualizada.

8. **CU-08: Configurar Perfil**
   - Actor: Usuario
   - Descripción: Cambia el nombre de usuario y el tema de la aplicación.
   - Flujo: Profile -> Editar nombre -> Save -> DataStore persiste -> StateFlow actualiza UI. Seleccionar tema -> DataStore persiste -> Theme cambia globalmente.

---

## 4. Diagrama de Secuencia Detallado: Guardar en API hasta Actualización de UI

**Escenario:** Usuario pulsa "Guardar en Favoritos" en DetailItemScreen para un Pokemon cargado desde la API.

### Participantes:
- **Usuario (Actor)**
- **DetailItemScreen (UI / View)**
- **DetailItemViewModel (ViewModel)**
- **FavoriteRepository (Repository)**
- **PokemonDao (DAO / Data Source)**
- **Room Database (SQLite)**

### Flujo de Secuencia (paso a paso):

```
1. Usuario -> DetailItemScreen: pulsa botón "Save to Favorites"

2. DetailItemScreen -> DetailItemViewModel: invoca toggleFavorite()

3. DetailItemViewModel -> FavoriteRepository: llama a getFavoriteById(id)
   Nota: Verificación de duplicado.

4. FavoriteRepository -> PokemonDao: invoca getFavoriteById(id)

5. PokemonDao -> Room Database: ejecuta SELECT * FROM pokemon_favorites WHERE id = ?

6. Room Database -> PokemonDao: retorna PokemonEntity? (null si no existe)

7. PokemonDao -> FavoriteRepository: retorna null

8. FavoriteRepository -> DetailItemViewModel: retorna null (no existe)

9. DetailItemViewModel -> FavoriteRepository: llama a addFavorite(pokemon.toEntity())

10. FavoriteRepository -> PokemonDao: invoca insertFavorite(pokemonEntity)

11. PokemonDao -> Room Database: ejecuta INSERT INTO pokemon_favorites (...)

12. Room Database -> PokemonDao: retorna rowId (éxito)

13. PokemonDao -> FavoriteRepository: retorna rowId

14. FavoriteRepository -> DetailItemViewModel: retorna true (éxito)

15. DetailItemViewModel -> DetailItemViewModel: actualiza _isFavorite.value = true
    Nota: Esto emite un nuevo valor al StateFlow, y la UI lo observa.

16. DetailItemViewModel -> DetailItemScreen: StateFlow<Boolean> emite true
    Nota: El botón se deshabilita porque isFavorite = true.

17. DetailItemViewModel -> DetailItemViewModel: _eventFlow.emit("Pokemon guardado en favoritos")

18. DetailItemViewModel -> DetailItemScreen: SharedFlow<String> emite el mensaje

19. DetailItemScreen: muestra Toast con el mensaje recibido.

20. DetailItemScreen -> ElemListScreen (indirecto): al volver, favoriteIds StateFlow 
    en ElemListViewModel también se actualiza porque observa getAllFavorites() de Room.
    Nota: El icono del corazón en la lista cambia a amarillo automáticamente.
```

### Notas del flujo:
- **Unidireccionalidad:** La UI solo observa StateFlow/SharedFlow. Nunca modifica datos directamente.
- **Reactivo:** Room emite Flows que actualizan múltiples observadores (listas, iconos, estados) sin intervención manual.
- **Manejo de errores:** Si el insert fallara (por ejemplo, conflicto), OnConflictStrategy.IGNORE devuelve -1, y el ViewModel puede manejarlo.

---

## 5. Patrones y Decisiones Técnicas

| Aspecto | Decisión | Justificación |
|---------|----------|---------------|
| **Inyección de Dependencias** | Hilt | Estándar oficial de Google, reduce boilerplate y facilita testing. |
| **Comunicación UI-VM** | StateFlow + SharedFlow | StateFlow para estados de UI reactivos. SharedFlow para eventos puntuales (Toasts). |
| **Imágenes** | Coil | Carga eficiente de imágenes de red con caché, integración nativa con Compose. |
| **Serialización JSON** | Gson + Retrofit | Simple y compatible con la estructura de PokeAPI. |
| **Relación Room** | 1:N Pokemon-Comments | ForeignKey con CASCADE permite borrar comentarios al eliminar el Pokemon padre. |
| **Conflictos Room** | OnConflictStrategy.IGNORE | Evita excepciones al insertar duplicados; el ViewModel interpreta el resultado. |
| **Tema Dinámico** | DataStore + WhatsPokeTheme | El tema se lee de DataStore en MainActivity y se propaga a toda la app. |

---

## 6. Estructura de Paquetes

```
com.example.whatspoke
├── WhatsPokeApplication.kt          # Application class con @HiltAndroidApp
├── MainActivity.kt                  # Entry point, observe themeMode
├── model
│   └── Pokemon.kt                   # Modelo de dominio + Mappers
├── data
│   ├── local
│   │   ├── AppDatabase.kt           # Room Database
│   │   ├── dao
│   │   │   ├── PokemonDao.kt
│   │   │   └── CommentDao.kt
│   │   ├── entity
│   │   │   ├── PokemonEntity.kt
│   │   │   ├── CommentEntity.kt
│   │   │   └── PokemonWithComments.kt
│   │   └── datastore
│   │       └── SettingsDataStore.kt
│   ├── remote
│   │   ├── PokeApiService.kt
│   │   └── dto
│   │       ├── PokemonListResponseDto.kt
│   │       └── PokemonDetailResponseDto.kt
│   └── repository
│       ├── PokemonApiRepository.kt
│       ├── FavoriteRepository.kt
│       ├── CommentRepository.kt
│       └── SettingsRepository.kt
├── di
│   ├── AppModule.kt
│   ├── DatabaseModule.kt
│   └── NetworkModule.kt
├── ui
│   ├── components
│   │   └── PokemonCard.kt
│   ├── navigation
│   │   └── NavGraph.kt
│   ├── screens
│   │   ├── ElemListScreen.kt
│   │   ├── DetailItemScreen.kt
│   │   ├── FavListScreen.kt
│   │   ├── DetailFavScreen.kt
│   │   ├── ProfileScreen.kt
│   │   └── AboutScreen.kt
│   ├── theme
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel
│       ├── UiState.kt
│       ├── ElemListViewModel.kt
│       ├── DetailItemViewModel.kt
│       ├── FavListViewModel.kt
│       ├── DetailFavViewModel.kt
│       └── ProfileViewModel.kt
```

---

## 7. Requisitos Cumplidos de la Rúbrica

- [x] **MVVM:** Separación clara View-ViewModel-Repository-Model.
- [x] **DataStore:** Persistencia de nombre de usuario y preferencia de tema.
- [x] **API Externa:** Retrofit para lista y detalle extendido.
- [x] **StateFlow/SharedFlow:** Comunicación reactiva entre ViewModel y View.
- [x] **Room:** Favoritos y comentarios con entidades, DAOs, relaciones y database.
- [x] **Lógica de Duplicados:** Icono distintivo + Toast de "ya es favorito".
- [x] **Borrado con Confirmación:** AlertDialog + actualización en vivo mediante Flow.
- [x] **Comentarios Firmados:** Cada comentario muestra el autor obtenido de DataStore.
- [x] **Sin Literales Hardcoded:** Todo texto extraído a `strings.xml`.
- [x] **Coil:** Carga de imágenes desde URLs.
- [x] **Hilt:** Inyección de dependencias en todas las capas.
