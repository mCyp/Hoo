package com.joe.jetpackdemo.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.joe.jetpackdemo.db.data.StorageData
import com.joe.jetpackdemo.db.data.User

/**
 * StorageData的Dao
 */
@Dao
interface StorageDataDao {
    @Query("SELECT * FROM storage_data WHERE id=:key")
    fun findStorageDataByKey(key: String): StorageData?

    @Query("SELECT * FROM storage_data")
    fun getAllStorageData(): List<StorageData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStorageData(data: StorageData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStorageData(data: List<StorageData>)

    @Delete
    fun deleteStorageData(data: StorageData)

    @Update
    fun updateStorageData(data: StorageData)
}