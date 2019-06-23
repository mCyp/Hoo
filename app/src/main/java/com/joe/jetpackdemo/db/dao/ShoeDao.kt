package com.joe.jetpackdemo.db.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.joe.jetpackdemo.db.data.Shoe
import com.joe.jetpackdemo.db.data.User

/**
 * 鞋子的方法
 */
@Dao
interface ShoeDao {

    // 选择所有的鞋
    @Query("SELECT * FROM shoe")
    fun getAllShoes(): LiveData<List<Shoe>>

    @Query("SELECT * FROM shoe WHERE id=:id")
    fun findShoeById(id: Long): LiveData<Shoe>

    /**
     * 通过品牌查询鞋子
     */
    @Query("SELECT * FROM shoe WHERE shoe_brand=:brand")
    fun findShoeByBrand(brand: String): LiveData<List<Shoe>>

    /**
     * 查询用户喜欢的鞋
     */
    @Query(
        "SELECT * FROM shoe " +
                "INNER JOIN fav_shoe ON fav_shoe.shoe_id = shoe.id " +
                "INNER JOIN user ON fav_shoe.user_id = user.id " +
                "WHERE user.id = :userId"
    )
    fun findShoeByUserId(userId: String): LiveData<List<Shoe>>


    // 增加一双鞋子
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoe(shoe: Shoe)

    // 增加多双鞋子
    // 除了List之外，也可以使用数组
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoes(shoes: List<Shoe>)

    // 删除一双鞋子
    @Delete
    fun deleteShoe(shoe: Shoe)

    // 删除多个鞋子
    // 参数也可以使用数组
    @Delete
    fun deleteShoes(shoes:List<Shoe>)

    // 更新一双鞋
    @Update
    fun updateShoe(shoe:Shoe)

    // 更新多双鞋
    // 参数也可以是集合
    @Update
    fun updateShoes(shoes:Array<Shoe>)
}