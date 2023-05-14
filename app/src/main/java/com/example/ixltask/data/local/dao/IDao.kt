package com.example.ixltask.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


@Dao
interface IDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T):Long

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)


}