package com.acid.myapplication

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Entity(tableName = "whatever")
data class Whatever(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String = UUID.randomUUID().toString()
)

@Dao
interface WhateverDao {
    @Query("SELECT * FROM whatever")
    fun getAll(): Flow<List<Whatever>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Whatever>)
}

@Database(entities = [Whatever::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun whateverDao(): WhateverDao
}

object DatabaseProvider {
    private var instance: AppDataBase? = null

    fun getDb(context: Context): AppDataBase {
        kotlin.synchronized(this) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDataBase::class.java, "whatever")
                    .build()
            }
        }
        return instance!!
    }
}


