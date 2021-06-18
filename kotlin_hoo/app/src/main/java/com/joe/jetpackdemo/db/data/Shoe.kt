package com.joe.jetpackdemo.db.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

/**
 * 鞋表
 */
@Entity(tableName = "shoe")
data class Shoe(
    @ColumnInfo(name = "shoe_name") val name: String
    , @ColumnInfo(name = "shoe_description") val description: String
    , @ColumnInfo(name = "shoe_price") val price: Float
    , @ColumnInfo(name = "shoe_brand") val brand: String
    , @ColumnInfo(name = "shoe_imgUrl") val imageUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    fun getPriceStr():String{
        return price.toString()
    }
}