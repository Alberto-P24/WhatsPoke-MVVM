package com.example.whatspoke.data.repository

import com.example.whatspoke.data.local.dao.CommentDao
import com.example.whatspoke.data.local.entity.CommentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepository @Inject constructor(
    private val commentDao: CommentDao
) {

    fun getCommentsForPokemon(pokemonId: Int): Flow<List<CommentEntity>> {
        return commentDao.getCommentsForPokemon(pokemonId)
    }

    suspend fun addComment(comment: CommentEntity) {
        commentDao.insertComment(comment)
    }

    suspend fun deleteComment(commentId: Long) {
        commentDao.deleteCommentById(commentId)
    }
}
