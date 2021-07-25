package com.example.composehoo.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 鞋表
 */
@Entity(tableName = "shoe")
data class Shoe(
    @ColumnInfo(name = "shoe_name") var name: String
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

    override fun toString(): String {
        return "id: ${id}, shoe_name: ${name}, description: ${description}, shoe_price: ${price}, brand: ${brand}"
    }
}