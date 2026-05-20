package com.example.whatspoke.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.whatspoke.data.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Query("SELECT * FROM comments WHERE pokemonId = :pokemonId ORDER BY timestamp DESC")
    fun getCommentsForPokemon(pokemonId: Int): Flow<List<CommentEntity>>

    @Insert
    suspend fun insertComment(comment: CommentEntity)

    @Query("DELETE FROM comments WHERE commentId = :commentId")
    suspend fun deleteCommentById(commentId: Long)
}
