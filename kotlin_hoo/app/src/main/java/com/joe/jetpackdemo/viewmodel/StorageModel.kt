package com.joe.jetpackdemo.viewmodel

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.jetpackdemo.db.data.StorageData
import com.joe.jetpackdemo.db.repository.StorageDataRepository
import com.joe.jetpackdemo.ui.activity.TEST_NAME
import com.joe.jetpackdemo.utils.AppPrefsUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

class StorageModel(private val storageDataRepository: StorageDataRepository) : ViewModel() {

    /**
     * SP 数据写入
     */
    @ExperimentalTime
    fun writeDataOnSp(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = TimeSource.Monotonic.markNow()
            when (type) {
                "int" -> { writeDataOnSpDetail(fileSize, dataSize) { j -> AppPrefsUtils.putIntWithNotCommit("int${j}", j) } }
                "long" -> { writeDataOnSpDetail(fileSize, dataSize) { j -> AppPrefsUtils.putLongWithNotCommit("long${j}", j.toLong()) } }
                "float" -> { writeDataOnSpDetail(fileSize, dataSize) { j -> AppPrefsUtils.putFloatWithNotCommit("float${j}", j.toFloat()) } }
                "Boolean" -> { writeDataOnSpDetail(fileSize, dataSize) { j -> AppPrefsUtils.putBooleanWithNotCommit("Boolean${j}", true) } }
                "String" -> { writeDataOnSpDetail(fileSize, dataSize) { j -> AppPrefsUtils.putStringWithNotCommit("String${j}", j.toString()) } }
            }
            Log.e("StorageModel", "sp write time: " + beginTime.elapsedNow().inMilliseconds)
        }
    }

    private fun writeDataOnSpDetail(fileSize: Int, dataSize: Int, block: (Int) -> Unit) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            AppPrefsUtils.changeName(fileName)
            for (j in 0 until dataSize) {
                block(j)
                AppPrefsUtils.commit()
            }
        }
    }

    /**
     * SP数据清空
     */
    fun clearDataOnSp(fileSize: Int) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            AppPrefsUtils.changeName(fileName)
            AppPrefsUtils.clearAll()
        }
    }

    @ExperimentalTime
    fun checkDataOnSp(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch {
            val beginTime = TimeSource.Monotonic.markNow()
            when (type) {
                "int" -> { checkDataOnSpDetail(fileSize, dataSize) { j -> return@checkDataOnSpDetail AppPrefsUtils.getInt("int${j}") != j } }
                "long" -> { checkDataOnSpDetail(fileSize, dataSize) { j -> return@checkDataOnSpDetail AppPrefsUtils.getLong("long${j}") != j.toLong() } }
                "float" -> { checkDataOnSpDetail(fileSize, dataSize) { j -> return@checkDataOnSpDetail AppPrefsUtils.getFloat("float${j}") != j.toFloat() } }
                "Boolean" -> { checkDataOnSpDetail(fileSize, dataSize) { j -> return@checkDataOnSpDetail !AppPrefsUtils.getBoolean("Boolean${j}")} }
                "String" -> { checkDataOnSpDetail(fileSize, dataSize) { j -> return@checkDataOnSpDetail AppPrefsUtils.getString("Boolean${j}") != j.toString()} }
            }
            Log.e("StorageModel", "sp checkTime: " + beginTime.elapsedNow().inMilliseconds)
        }
    }

    private fun checkDataOnSpDetail(fileSize: Int, dataSize: Int, block: (Int) -> Boolean) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            AppPrefsUtils.changeName(fileName)
            for (j in 0 until dataSize) {
                if (block(j)) {
                    Log.e("StorageModel", "Sp check is false")
                    return
                }
            }
        }
    }

    @ExperimentalTime
    fun writeDataOnMMKV(fileSize: Int, dataSize: Int, type: String) {
        val beginTime = TimeSource.Monotonic.markNow()
        when (type) {
            "int" -> { writeDataOnMMKVOnDetail(fileSize, dataSize) {j,mmkv-> mmkv?.encode("int${j}", j) } }
            "long" -> { writeDataOnMMKVOnDetail(fileSize, dataSize) {j,mmkv-> mmkv?.encode("long${j}", j.toLong()) } }
            "float" -> { writeDataOnMMKVOnDetail(fileSize, dataSize) {j,mmkv-> mmkv?.encode("float${j}", j.toLong()) } }
            "Boolean" -> { writeDataOnMMKVOnDetail(fileSize, dataSize) {j,mmkv-> mmkv?.encode("Boolean${j}", true) } }
            "String" -> { writeDataOnMMKVOnDetail(fileSize, dataSize) {j,mmkv-> mmkv?.encode("String${j}", j.toString()) } }
        }
        Log.e("StorageModel", "MMKV write time: " + beginTime.elapsedNow().inMilliseconds)
    }

    private fun writeDataOnMMKVOnDetail(fileSize: Int, dataSize: Int, block: (Int, MMKV?) -> Unit) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            val mmkv = MMKV.mmkvWithID(fileName)
            for (j in 0 until dataSize) {
                block(j, mmkv)
            }
        }
    }

    fun clearDataOnMMKV(fileSize: Int) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            val mmkv = MMKV.mmkvWithID(fileName)
            mmkv?.clearAll()
        }
    }

    @ExperimentalTime
    fun checkDataOnMMKV(fileSize: Int, dataSize: Int, type: String) {
        val beginTime = TimeSource.Monotonic.markNow()
        when (type) {
            "int" -> { checkDataOnMMKVOnDetail(fileSize, dataSize){j,mmkv-> return@checkDataOnMMKVOnDetail mmkv?.decodeInt("int${j}") != j} }
            "long" -> { checkDataOnMMKVOnDetail(fileSize, dataSize){j,mmkv-> return@checkDataOnMMKVOnDetail mmkv?.decodeLong("long${j}") != j.toLong()} }
            "float" -> { checkDataOnMMKVOnDetail(fileSize, dataSize){j,mmkv-> return@checkDataOnMMKVOnDetail mmkv?.decodeFloat("float${j}") != j.toFloat()} }
            "Boolean" -> { checkDataOnMMKVOnDetail(fileSize, dataSize){j,mmkv-> return@checkDataOnMMKVOnDetail mmkv?.decodeBool("Boolean${j}") != true} }
            "String" -> { checkDataOnMMKVOnDetail(fileSize, dataSize){j,mmkv-> return@checkDataOnMMKVOnDetail mmkv?.decodeString("String${j}") != j.toString()} }
        }
        Log.e("StorageModel", "MMKV check time: " + beginTime.elapsedNow().inMilliseconds)
    }

    private fun checkDataOnMMKVOnDetail(fileSize: Int, dataSize: Int, block: (Int, MMKV?) -> Boolean) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            val mmkv = MMKV.mmkvWithID(fileName)
            for (j in 0 until dataSize) {
                if (block(j, mmkv)) {
                    Log.e("StorageModel", "MMKV check is false")
                    return
                }
            }
        }
    }

    @ExperimentalTime
    fun writeDataOnDataStore(fileSize: Int, dataSize: Int, type: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = TimeSource.Monotonic.markNow()
            when (type) {
                "int" -> { writeDataOnDataStoreOnDetail(fileSize, dataSize, context){j, it-> it[intPreferencesKey("int${j}")] = j } }
                "long" -> { writeDataOnDataStoreOnDetail(fileSize, dataSize, context){j, it-> it[longPreferencesKey("long${j}")] = j.toLong() } }
                "float" -> { writeDataOnDataStoreOnDetail(fileSize, dataSize, context){j, it-> it[floatPreferencesKey("float${j}")] = j.toFloat() } }
                "Boolean" -> { writeDataOnDataStoreOnDetail(fileSize, dataSize, context){j, it-> it[booleanPreferencesKey("Boolean${j}")] = true } }
                "String" -> { writeDataOnDataStoreOnDetail(fileSize, dataSize, context){j, it-> it[stringPreferencesKey("String${j}")] = j.toString() } }
            }
            Log.e("StorageModel", "DataStore write time: " + beginTime.elapsedNow().inMilliseconds)
        }
    }

    private suspend fun writeDataOnDataStoreOnDetail(fileSize: Int, dataSize: Int,context: Context, block: (Int, MutablePreferences) -> Unit) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            val dataStore = context.createDataStore(fileName)
            for (j in 0 until dataSize) {
                dataStore.edit { block(j, it) }
            }
        }
    }

    @ExperimentalTime
    @InternalCoroutinesApi
    fun checkDataOnDataStoreOutSide(fileSize: Int, dataSize: Int, type: String, context: Context) {
        val dataStoreChannel = Channel<Boolean>(capacity = Channel.UNLIMITED) { }
        checkDataOnDataStore(fileSize, dataSize, type, context, dataStoreChannel)
        var count = 0
        val beginTime = TimeSource.Monotonic.markNow()
        viewModelScope.launch {
            dataStoreChannel.consumeAsFlow().collect(object : FlowCollector<Boolean> {
                override suspend fun emit(value: Boolean) {
                    if (value) {
                        count++
                        if (count == fileSize) {
                            Log.e("StorageModel", "DataStore check time: " + beginTime.elapsedNow().inMilliseconds)
                        }
                    }
                }
            })
        }
    }

    @InternalCoroutinesApi
    private fun checkDataOnDataStore(
        fileSize: Int,
        dataSize: Int,
        type: String,
        context: Context,
        channel: Channel<Boolean>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                "int" -> { checkDataOnDataStoreOnDetail(fileSize, dataSize, context, channel){j,p -> return@checkDataOnDataStoreOnDetail p[intPreferencesKey("int${j}")] != j } }
                "long" -> { checkDataOnDataStoreOnDetail(fileSize, dataSize, context, channel){j,p -> return@checkDataOnDataStoreOnDetail p[longPreferencesKey("long${j}")] != j.toLong() } }
                "float" -> { checkDataOnDataStoreOnDetail(fileSize, dataSize, context, channel){j,p -> return@checkDataOnDataStoreOnDetail p[floatPreferencesKey("float${j}")] != j.toFloat() } }
                "Boolean" -> { checkDataOnDataStoreOnDetail(fileSize, dataSize, context, channel){j,p -> return@checkDataOnDataStoreOnDetail p[booleanPreferencesKey("Boolean${j}")] != true } }
                "String" -> { checkDataOnDataStoreOnDetail(fileSize, dataSize, context, channel){j,p -> return@checkDataOnDataStoreOnDetail p[stringPreferencesKey("String${j}")] != j.toString() } }
            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun checkDataOnDataStoreOnDetail(
        fileSize: Int,
        dataSize: Int,
        context: Context,
        channel: Channel<Boolean>,
        block: (Int, Preferences) -> Boolean
    ){
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            val dataStore = context.createDataStore(fileName)
            dataStore.data.map { p ->
                for (j in 0 until dataSize) {
                    if(block(j, p)){
                        Log.e("StorageModel", "dataStore check is false")
                        return@map false
                    }
                }
                return@map true
            }.collect(object : FlowCollector<Boolean> {
                override suspend fun emit(value: Boolean) {
                    channel.send(value)
                }
            })
        }
    }

    fun clearDataOnDataStore(fileSize: Int, context: Context) {
        viewModelScope.launch {
            for (i in 0 until fileSize) {
                val fileName = "$TEST_NAME-${i}"
                val dataStore = context.createDataStore(fileName)
                dataStore.edit {
                    it.clear()
                }
            }
        }
    }


    @ExperimentalTime
    fun writeDataOnSQL(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = TimeSource.Monotonic.markNow()
            when (type) {
                "int" -> { writeDataOnSQLOnDetail(fileSize, dataSize){j-> storageDataRepository.insertOneStorageData(StorageData("int${j}", j.toString())) } }
                "long" -> { writeDataOnSQLOnDetail(fileSize, dataSize){j-> storageDataRepository.insertOneStorageData(StorageData("long${j}", j.toString())) } }
                "float" -> { writeDataOnSQLOnDetail(fileSize, dataSize){j-> storageDataRepository.insertOneStorageData(StorageData("float${j}", j.toString())) } }
                "Boolean" -> { writeDataOnSQLOnDetail(fileSize, dataSize){j-> storageDataRepository.insertOneStorageData(StorageData("Boolean${j}", j.toString())) } }
                "String" -> { writeDataOnSQLOnDetail(fileSize, dataSize){j-> storageDataRepository.insertOneStorageData(StorageData("String\$${j}", j.toString())) } }
            }
            Log.e("StorageModel", "SQL write time: " + beginTime.elapsedNow().inMilliseconds)
        }
    }

    private fun writeDataOnSQLOnDetail(fileSize: Int, dataSize: Int, block: (Int) -> Unit){
        for (i in 0 until fileSize) {
            for (j in 0 until dataSize) {
                block(j)
            }
        }
    }

    @ExperimentalTime
    fun checkDataOnSQL(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = TimeSource.Monotonic.markNow()
            when (type) {
                "int" -> {
                    checkDataOnSQLOnDetail(fileSize, dataSize) { j ->
                        val data = storageDataRepository.findStorageDataById("int${j}")
                        return@checkDataOnSQLOnDetail (data == null || data.value != j.toString())
                    }
                }
                "long" -> {
                    checkDataOnSQLOnDetail(fileSize, dataSize) { j ->
                        val data = storageDataRepository.findStorageDataById("long${j}")
                        return@checkDataOnSQLOnDetail (data == null || data.value != j.toString())
                    }
                }
                "float" -> {
                    checkDataOnSQLOnDetail(fileSize, dataSize) { j ->
                        val data = storageDataRepository.findStorageDataById("float${j}")
                        return@checkDataOnSQLOnDetail (data == null || data.value != j.toString())
                    }
                }
                "Boolean" -> {
                    checkDataOnSQLOnDetail(fileSize, dataSize) { j ->
                        val data = storageDataRepository.findStorageDataById("Boolean${j}")
                        return@checkDataOnSQLOnDetail (data == null || data.value != j.toString())
                    }
                }
                "String" -> {
                    checkDataOnSQLOnDetail(fileSize, dataSize) { j ->
                        val data = storageDataRepository.findStorageDataById("String${j}")
                        return@checkDataOnSQLOnDetail (data == null || data.value != j.toString())
                    }
                }
            }
            Log.e("StorageModel", "SQL check time: " + beginTime.elapsedNow().inMilliseconds)
        }
    }

    private fun checkDataOnSQLOnDetail(fileSize: Int, dataSize: Int, block: (Int) -> Boolean) {
        for (i in 0 until fileSize) {
            for (j in 0 until dataSize) {
                if (block(j)) {
                    Log.e("StorageModel", "SQL check is false")
                    return
                }
            }
        }
    }

    fun clearDataOnSQL() {
        // 没必要删除
    }


}