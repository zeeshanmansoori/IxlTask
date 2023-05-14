package com.example.ixltask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ixltask.data.local.dao.BankDetailsDao
import com.example.ixltask.data.local.dao.EmployeeDetailsDao
import com.example.ixltask.data.local.dao.UserDao
import com.example.ixltask.data.local.model.BankDetailsEntity
import com.example.ixltask.data.local.model.EmployeeEntity
import com.example.ixltask.data.local.model.UserEntity


@Database(
    entities = [
        UserEntity::class,
        EmployeeEntity::class,
        BankDetailsEntity::class,
    ], version = 5
)
abstract class IxlDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getEmployeeDao(): EmployeeDetailsDao
    abstract fun getBankDetailsDao(): BankDetailsDao

    companion object {
        private lateinit var db: IxlDatabase
        fun getInstance(context: Context): IxlDatabase {
            if (!Companion::db.isInitialized) {
                db = Room.databaseBuilder(context, IxlDatabase::class.java, "sfsdf")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return db
        }
    }
}