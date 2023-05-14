package com.example.ixltask.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ixltask.data.local.model.BankDetailsEntity


@Dao
interface BankDetailsDao : IDao<BankDetailsEntity> {

    @Query("select * from bankdetailsentity where userId=:userId")
    suspend fun getBankDetailsByUserId(userId: Int):BankDetailsEntity?
}
