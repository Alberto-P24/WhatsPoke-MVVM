package com.example.whatspoke.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.whatspoke.data.local.dao.CommentDao
import com.example.whatspoke.data.local.dao.PokemonDao
import com.example.whatspoke.data.local.entity.CommentEntity
import com.example.whatspoke.data.local.entity.PokemonEntity

@Database(
    entities = [PokemonEntity::class, CommentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun commentDao(): CommentDao

    companion object {
        const val DATABASE_NAME = "whatspoke_database"
    }
}
