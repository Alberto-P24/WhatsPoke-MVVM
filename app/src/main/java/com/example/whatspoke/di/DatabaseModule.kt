package com.example.whatspoke.di

import android.content.Context
import androidx.room.Room
import com.example.whatspoke.data.local.AppDatabase
import com.example.whatspoke.data.local.dao.CommentDao
import com.example.whatspoke.data.local.dao.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun providePokemonDao(database: AppDatabase): PokemonDao = database.pokemonDao()

    @Provides
    fun provideCommentDao(database: AppDatabase): CommentDao = database.commentDao()
}
