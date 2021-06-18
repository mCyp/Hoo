package com.joe.jetpackdemo.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.joe.jetpackdemo.R
import com.joe.jetpackdemo.viewmodel.CustomViewModelProvider
import com.joe.jetpackdemo.viewmodel.StorageModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.time.ExperimentalTime

const val TEST_NAME = "store"
class StoreLocalDataActivity : AppCompatActivity(),View.OnClickListener, AdapterView.OnItemSelectedListener {

    private val storageModel: StorageModel by viewModels {
        CustomViewModelProvider.provideStorageDataModel(
            this
        )
    }

    private lateinit var spAddType: Spinner
    private lateinit var spFileSize: Spinner
    private lateinit var spDataSize: Spinner
    private lateinit var spDataType: Spinner
    private lateinit var spClearType: Spinner
    private lateinit var spMigrateType: Spinner

    private lateinit var btnWriteData: Button
    private lateinit var btnClearData: Button
    private lateinit var btnMigrateData: Button

    private var writeType: String = "SharedPreferences"
    private var fileSize: Int = 1
    private var dataSize: Int = 1000
    private var type: String = "int"
    // 上一次SP存储的记录
    private var lastSpFileSize: Int = 0
    private var lastSpSize = 0
    private var lastSpType: String = "int"
    // 上一次MMKV存储的记录
    private var lastMMKVFileSize: Int = 0
    private var lastMMKVSize = 0
    private var lastMMKVType: String = "int"
    // 上一次DataStore存储的记录
    private var lastDataStoreFileSize: Int = 0
    private var lastDataStoreSize = 0
    private var lastDataStoreType: String = "int"
    // 上一次数据库的存储记录
    private var lastSQLFileSize: Int = 0
    private var lastSQLSize = 0
    private var lastSQLType: String = "int"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_local_data_activity)

        initView()
        initListener()
    }

    private fun initView(){
        spAddType = findViewById(R.id.sp_storage_data_type)
        spFileSize = findViewById(R.id.sp_file_size)
        spDataSize = findViewById(R.id.sp_data_size)
        spDataType = findViewById(R.id.sp_data_type)
        spClearType = findViewById(R.id.sp_clear_data_type)
        spMigrateType = findViewById(R.id.sp_migrate_data_type)
        btnWriteData = findViewById(R.id.btn_write_data)
        btnClearData = findViewById(R.id.btn_clear_data)
        btnMigrateData = findViewById(R.id.btn_migrate_data)
    }

    private fun initListener(){
        btnWriteData.setOnClickListener(this)
        btnClearData.setOnClickListener(this)
        btnMigrateData.setOnClickListener(this)
        spAddType.onItemSelectedListener = this
        spFileSize.onItemSelectedListener = this
        spDataSize.onItemSelectedListener = this
        spDataType.onItemSelectedListener = this
        spClearType.onItemSelectedListener = this
        spMigrateType.onItemSelectedListener = this
    }

    @ExperimentalTime
    @InternalCoroutinesApi
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_write_data -> {
                writeData()
            }
            R.id.btn_clear_data -> {
                clearData()
            }
            R.id.btn_migrate_data -> {
                checkData(writeType)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        when(parent?.id){
            R.id.sp_storage_data_type -> {
                val writeSource = resources.getStringArray(R.array.storeType)
                writeType = writeSource[position]
            }
            R.id.sp_file_size -> {
                val fs = resources.getStringArray(R.array.writeFileSize)
                fileSize = fs[position].toInt()
            }
            R.id.sp_data_size -> {
                val ds = resources.getStringArray(R.array.writeDataSize)
                dataSize = ds[position].toInt()
            }
            R.id.sp_data_type -> {
                val dt = resources.getStringArray(R.array.writeDataType)
                type = dt[position]
            }
        }
    }

    @ExperimentalTime
    private fun writeData(){
        when(writeType){
            "SharedPreferences" -> {
                storageModel.writeDataOnSp(fileSize, dataSize, type)
                lastSpFileSize = fileSize
                lastSpSize = dataSize
                lastSpType = type
            }
            "MMKV" -> {
                storageModel.writeDataOnMMKV(fileSize, dataSize, type)
                lastMMKVFileSize = fileSize
                lastMMKVSize = dataSize
                lastMMKVType = type
            }
            "DataStore" -> {
                storageModel.writeDataOnDataStore(fileSize, dataSize, type, this)
                lastDataStoreFileSize = fileSize
                lastDataStoreSize = dataSize
                lastDataStoreType = type
            }
            "SQL" -> {
                storageModel.writeDataOnSQL(fileSize, dataSize, type)
                lastSQLFileSize = fileSize
                lastSQLSize = dataSize
                lastSQLType = type
            }
        }
    }

    /**
     * 只针对上一次数据有效
     */
    private fun clearData(){
        when(writeType){
            "SharedPreferences" -> {
                storageModel.clearDataOnSp(fileSize)
            }
            "MMKV" -> {
                storageModel.clearDataOnMMKV(fileSize)
            }
            "DataStore" -> {
                storageModel.clearDataOnDataStore(fileSize, this)
            }
        }
    }

    /**
     * 检查上一次输入的数据
     */
    @ExperimentalTime
    @InternalCoroutinesApi
    private fun checkData(type: String){
        when(type){
            "SharedPreferences" -> {
                storageModel.checkDataOnSp(lastSpFileSize, lastSpSize, lastSpType)
            }
            "MMKV" -> {
                storageModel.checkDataOnMMKV(lastMMKVFileSize, lastMMKVSize, lastMMKVType)
            }
            "DataStore" -> {
                storageModel.checkDataOnDataStoreOutSide(lastDataStoreFileSize, lastDataStoreSize, lastDataStoreType, this)
            }
            "SQL" -> {
                storageModel.checkDataOnSQL(lastSQLFileSize, lastSQLSize, lastSQLType)
            }
        }
    }
}