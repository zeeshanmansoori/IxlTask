package com.example.ixltask.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.ixltask.data.local.model.EmployeeEntity


@Dao
interface EmployeeDetailsDao : IDao<EmployeeEntity> {

    @Query("select * from employeeentity where userId=:userId")
    suspend fun getEmployeeDetailsByUserId(userId: Int): EmployeeEntity?
}
