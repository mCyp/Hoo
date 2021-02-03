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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.logging.Logger

class StorageModel(private val storageDataRepository: StorageDataRepository) : ViewModel() {

    /**
     * SP 数据写入
     */
    fun writeDataOnSp(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = System.currentTimeMillis()
            when (type) {
                "int" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "int${j}"
                            AppPrefsUtils.putIntWithNotCommit(key, j)
                            AppPrefsUtils.commit()
                        }
                    }
                }
                "long" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "long${j}"
                            AppPrefsUtils.putLongWithNotCommit(key, j.toLong())
                            AppPrefsUtils.commit()
                        }
                    }
                }
                "float" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "float${j}"
                            AppPrefsUtils.putFloatWithNotCommit(key, j.toFloat())
                            AppPrefsUtils.commit()
                        }
                    }
                }
                "Boolean" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "Boolean${j}"
                            AppPrefsUtils.putBooleanWithNotCommit(key, true)
                            AppPrefsUtils.commit()
                        }
                    }
                }
                "String" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "String${j}"
                            AppPrefsUtils.putStringWithNotCommit(key, j.toString())
                            AppPrefsUtils.commit()
                        }
                    }
                }
            }
            val totalTime = System.currentTimeMillis() - beginTime
            Log.e("StorageModel", "sp write time: " + totalTime)
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

    fun checkDataOnSp(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch {
            val beginTime = System.currentTimeMillis()
            when (type) {
                "int" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "int${j}"
                            if (AppPrefsUtils.getInt(key) != j) {
                                Log.e("StorageModel", "Sp check is false")
                                return@launch
                            }
                        }
                    }
                }
                "long" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "long${j}"
                            if (AppPrefsUtils.getLong(key) != j.toLong()) {
                                Log.e("StorageModel", "Sp check is false")
                                return@launch
                            }
                        }
                    }
                }
                "float" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "float${j}"
                            if (AppPrefsUtils.getFloat(key) != j.toFloat()) {
                                Log.e("StorageModel", "Sp check is false")
                                return@launch
                            }
                        }
                    }
                }
                "Boolean" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "Boolean${j}"
                            if (!AppPrefsUtils.getBoolean(key)) {
                                Log.e("StorageModel", "Sp check is false")
                                return@launch
                            }
                        }
                    }
                }
                "String" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        AppPrefsUtils.changeName(fileName)
                        for (j in 0 until dataSize) {
                            val key = "String${j}"
                            if (AppPrefsUtils.getString(key) != j.toString()) {
                                Log.e("StorageModel", "Sp check is false")
                                return@launch
                            }
                        }
                    }
                }
            }
            val totalTime = System.currentTimeMillis() - beginTime
            Log.e("StorageModel", "sp checkTime: " + totalTime)
        }
    }

    fun writeDataOnMMKV(fileSize: Int, dataSize: Int, type: String) {
        val beginTime = System.currentTimeMillis()
        when (type) {
            "int" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "int${j}"
                        mmkv?.encode(key, j)
                    }
                }
            }
            "long" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "long${j}"
                        mmkv?.encode(key, j.toLong())
                    }
                }
            }
            "float" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "float${j}"
                        mmkv?.encode(key, j.toLong())
                    }
                }
            }
            "Boolean" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "Boolean${j}"
                        mmkv?.encode(key, true)
                    }
                }
            }
            "String" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "String${j}"
                        mmkv?.encode(key, j.toString())
                    }
                }
            }
        }
        val totalTime = System.currentTimeMillis() - beginTime
        Log.e("StorageModel", "MMKV write time: " + totalTime)
    }

    fun clearDataOnMMKV(fileSize: Int) {
        for (i in 0 until fileSize) {
            val fileName = "$TEST_NAME-${i}"
            val mmkv = MMKV.mmkvWithID(fileName)
            mmkv?.clearAll()
        }
    }

    fun checkDataOnMMKV(fileSize: Int, dataSize: Int, type: String) {
        val beginTime = System.currentTimeMillis()
        when (type) {
            "int" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "int${j}"
                        if (mmkv?.decodeInt(key) != j) {
                            Log.e("StorageModel", "MMKV check is false")
                            return
                        }
                    }
                }
            }
            "long" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "long${j}"
                        if (mmkv?.decodeLong(key) != j.toLong()) {
                            Log.e("StorageModel", "MMKV check is false")
                            return
                        }
                    }
                }
            }
            "float" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "float${j}"
                        if (mmkv?.decodeFloat(key) != j.toFloat()) {
                            Log.e("StorageModel", "MMKV check is false")
                            return
                        }
                    }
                }
            }
            "Boolean" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "Boolean${j}"
                        if (mmkv?.decodeBool(key) != true) {
                            Log.e("StorageModel", "MMKV check is false")
                            return
                        }
                    }
                }
            }
            "String" -> {
                for (i in 0 until fileSize) {
                    val fileName = "$TEST_NAME-${i}"
                    val mmkv = MMKV.mmkvWithID(fileName)
                    for (j in 0 until dataSize) {
                        val key = "String${j}"
                        if (mmkv?.decodeString(key) != j.toString()) {
                            Log.e("StorageModel", "MMKV check is false")
                            return
                        }
                    }
                }
            }
        }
        val totalTime = System.currentTimeMillis() - beginTime
        Log.e("StorageModel", "MMKV check time: " + totalTime)
    }

    fun writeDataOnDataStore(fileSize: Int, dataSize: Int, type: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = System.currentTimeMillis()
            when (type) {
                "int" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        for (j in 0 until dataSize) {
                            dataStore.edit {
                                val key = intPreferencesKey("int${j}")
                                it[key] = j
                            }
                        }
                    }
                }
                "long" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        for (j in 0 until dataSize) {
                            dataStore.edit {
                                val key = longPreferencesKey("long${j}")
                                it[key] = j.toLong()
                            }
                        }
                    }
                }
                "float" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        for (j in 0 until dataSize) {
                            dataStore.edit {
                                val key = floatPreferencesKey("float${j}")
                                it[key] = j.toFloat()
                            }
                        }
                    }
                }
                "Boolean" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        for (j in 0 until dataSize) {
                            dataStore.edit {
                                val key = booleanPreferencesKey("Boolean${j}")
                                it[key] = true
                            }
                        }
                    }
                }
                "String" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val t = System.currentTimeMillis() - beginTime;
                        val dataStore = context.createDataStore(fileName)
                        //Log.e("StorageModel", "DataStore begin - " + i + " -time: " + t)
                        for (j in 0 until dataSize) {
                            dataStore.edit {
                                val key = stringPreferencesKey("String${j}")
                                it[key] = j.toString()
                            }
                        }
                        val t1 = System.currentTimeMillis() - beginTime;
                        //Log.e("StorageModel", "DataStore end - " + i + " -time: " + t1)
                    }
                }
            }
            val totalTime = System.currentTimeMillis() - beginTime
            Log.e("StorageModel", "DataStore write time: " + totalTime)
        }
    }

    @InternalCoroutinesApi
    fun checkDataOnDataStoreOutSide(fileSize: Int, dataSize: Int, type: String, context: Context){
        val dataStoreChannel = Channel<Boolean>(capacity = Channel.UNLIMITED) { }
        checkDataOnDataStore(fileSize, dataSize, type, context, dataStoreChannel)
        var count = 0
        val beginTime = System.currentTimeMillis()
        viewModelScope.launch {
            dataStoreChannel.consumeAsFlow().collect(object :FlowCollector<Boolean>{
                override suspend fun emit(value: Boolean) {
                    if(!value){

                    }else{
                        count++
                        if(count == fileSize){
                            val totalTime = System.currentTimeMillis() - beginTime
                            Log.e("StorageModel", "DataStore check time: " + totalTime)
                        }
                    }
                }
            })
        }
    }


    @InternalCoroutinesApi
    private fun checkDataOnDataStore(fileSize: Int, dataSize: Int, type: String, context: Context, channel: Channel<Boolean>) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                "int" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        dataStore.data.map { p ->
                            for (j in 0 until dataSize) {
                                val key = intPreferencesKey("int${j}")
                                if (p[key] != j) {
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
                "long" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        dataStore.data.map { p ->
                            for (j in 0 until dataSize) {
                                val key = longPreferencesKey("long${j}")
                                if (p[key] != j.toLong()) {
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
                "float" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        dataStore.data.map { p ->
                            for (j in 0 until dataSize) {
                                val key = floatPreferencesKey("float${j}")
                                if (p[key] != j.toFloat()) {
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
                "Boolean" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        dataStore.data.map { p ->
                            for (j in 0 until dataSize) {
                                val key = booleanPreferencesKey("Boolean${j}")
                                if (p[key] != true) {
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
                "String" -> {
                    for (i in 0 until fileSize) {
                        val fileName = "$TEST_NAME-${i}"
                        val dataStore = context.createDataStore(fileName)
                        dataStore.data.map { p ->
                            for (j in 0 until dataSize) {
                                val key = stringPreferencesKey("String${j}")
                                if (p[key] != j.toString()) {
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
            }
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


    fun writeDataOnSQL(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = System.currentTimeMillis()
            when (type) {
                "int" -> {
                    val list = mutableListOf<StorageData>()
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            //list.add(StorageData("int${j}",j.toString()))
                            storageDataRepository.insertOneStorageData(
                                StorageData(
                                    "int${j}",
                                    j.toString()
                                )
                            )
                        }
                    }
                    // storageDataRepository.insertAllStorageData(list)
                }
                "long" -> {
                    val list = mutableListOf<StorageData>()
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            //list.add(StorageData("long${j}",j.toString()))
                            storageDataRepository.insertOneStorageData(
                                StorageData(
                                    "long${j}",
                                    j.toString()
                                )
                            )
                        }
                    }
                    //storageDataRepository.insertAllStorageData(list)
                }
                "float" -> {
                    val list = mutableListOf<StorageData>()
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            storageDataRepository.insertOneStorageData(
                                StorageData(
                                    "float${j}",
                                    j.toString()
                                )
                            )
                            // list.add(StorageData("float${j}",j.toString()))
                        }
                    }
                    // storageDataRepository.insertAllStorageData(list)
                }
                "Boolean" -> {
                    val list = mutableListOf<StorageData>()
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            storageDataRepository.insertOneStorageData(
                                StorageData(
                                    "Boolean\${j}",
                                    1.toString()
                                )
                            )
                            //list.add(StorageData("Boolean${j}","1"))
                        }
                    }
                    // storageDataRepository.insertAllStorageData(list)
                }
                "String" -> {
                    val list = mutableListOf<StorageData>()
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            storageDataRepository.insertOneStorageData(
                                StorageData(
                                    "String${j}",
                                    j.toString()
                                )
                            )
                            // list.add(StorageData("String${j}", "1"))
                        }
                    }
                    //storageDataRepository.insertAllStorageData(list)
                }
            }
            val totalTime = System.currentTimeMillis() - beginTime
            Log.e("StorageModel", "SQL write time: " + totalTime)
        }
    }

    fun checkDataOnSQL(fileSize: Int, dataSize: Int, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val beginTime = System.currentTimeMillis()
            when (type) {
                "int" -> {
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            val data = storageDataRepository.findStorageDataById("int${j}")
                            if (data == null || data.value != j.toString()) {
                                Log.e("StorageModel", "SQL check is false")
                                return@launch
                            }
                        }
                    }
                }
                "long" -> {
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            val data = storageDataRepository.findStorageDataById("long${j}")
                            if (data == null || data.value != j.toString()) {
                                Log.e("StorageModel", "SQL check is false")
                                return@launch
                            }
                        }
                    }
                }
                "float" -> {
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            val data = storageDataRepository.findStorageDataById("float${j}")
                            if (data == null || data.value != j.toString()) {
                                Log.e("StorageModel", "SQL check is false")
                                return@launch
                            }
                        }
                    }
                }
                "Boolean" -> {
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            val data = storageDataRepository.findStorageDataById("Boolean${j}")
                            if (data == null || data.value != 1.toString()) {
                                Log.e("StorageModel", "SQL check is false")
                                return@launch
                            }
                        }
                    }
                }
                "String" -> {
                    for (i in 0 until fileSize) {
                        for (j in 0 until dataSize) {
                            val data = storageDataRepository.findStorageDataById("String${j}")
                            if (data == null || data.value != j.toString()) {
                                Log.e("StorageModel", "SQL check is false")
                                return@launch
                            }
                        }
                    }
                }
            }
            val totalTime = System.currentTimeMillis() - beginTime
            Log.e("StorageModel", "SQL check time: " + totalTime)
        }
    }

    fun clearDataOnSQL() {
        // 没必要删除
    }


}