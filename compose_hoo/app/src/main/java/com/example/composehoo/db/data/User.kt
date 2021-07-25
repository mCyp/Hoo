package com.example.composehoo.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @ColumnInfo(name = "user_account") val account: String // 账号
    , @ColumnInfo(name = "user_pwd") val pwd: String // 密码
    , @ColumnInfo(name = "user_name") val name: String // 账号
    , @ColumnInfo(name = "user_url") var headImage:String // 头像地址
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}