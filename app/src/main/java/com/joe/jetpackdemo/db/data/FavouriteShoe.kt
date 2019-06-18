package com.joe.jetpackdemo.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

/**
 * 喜欢的球鞋
 */
@Entity(
    tableName = "fav_shoe"
    , foreignKeys = [ForeignKey(entity = Shoe::class, parentColumns = ["id"], childColumns = ["shoe_id"])
        , ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"])]
)
data class FavouriteShoe(
    @ColumnInfo(name = "shoe_id") val shoeId: Long
    , @ColumnInfo(name = "user_id") val userId: Long
    , @ColumnInfo(name = "fav_date") val date: Date
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}