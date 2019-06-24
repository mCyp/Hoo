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

   /* // 查询一个
    @Query("SELECT * FROM shoe WHERE id=:id")
    fun findShoeById(id: Long): Shoe?

    // 查询多个 通过品牌查询多款鞋
    @Query("SELECT * FROM shoe WHERE shoe_brand=:brand")
    fun findShoesByBrand(brand: String): List<Shoe>

    // 模糊查询 排序 同名鞋名查询鞋
    @Query("SELECT * FROM shoe WHERE shoe_name LIKE :name ORDER BY shoe_brand ASC")
    fun findShoesByName(name:String):List<Shoe>

    // 配合RxJava 通过Id查询单款鞋子
    @Query("SELECT * FROM shoe WHERE id=:id")
    fun findShoeByIdRx(id: Long): Flowable<Shoe>*/

    // 配合LiveData 返回所有的鞋子
    @Query("SELECT * FROM shoe")
    fun getAllShoesLD(): LiveData<List<Shoe>>

    // 配合LiveData 通过Id查询单款鞋子
    @Query("SELECT * FROM shoe WHERE id=:id")
    fun findShoeByIdLD(id: Long): LiveData<Shoe>


    /**
     * 通过品牌查询鞋子
     */
    @Query("SELECT * FROM shoe WHERE shoe_brand=:brand")
    fun findShoesByBrandLD(brand: String): LiveData<List<Shoe>>

    // 根据收藏结合 查询用户喜欢的鞋的集合
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