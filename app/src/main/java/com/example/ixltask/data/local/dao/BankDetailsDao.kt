package com.example.ixltask.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ixltask.data.local.model.BankDetailsEntity
import com.example.ixltask.data.local.model.EmployeeEntity


@Dao
interface BankDetailsDao : IDao<BankDetailsEntity> {

    @Query("select * from bankdetailsentity where userId=:userId")
    suspend fun getBankDetailsByUserId(userId: Int):BankDetailsEntity?

    @Query("delete from bankdetailsentity where userId=:userId")
    suspend fun deleteById(userId: Int)
}
