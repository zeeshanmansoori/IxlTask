package com.example.ixltask.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ixltask.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao : IDao<UserEntity> {

    @Query("select * from userentity where id=:userId")
    suspend fun getUserById(userId: Int): UserEntity

    @Query("select * from userentity ")
    fun getUsersAsFlow(): Flow<List<UserEntity>>
}
