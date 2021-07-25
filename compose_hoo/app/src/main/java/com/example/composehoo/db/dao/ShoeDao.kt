package com.example.composehoo.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.composehoo.db.data.Shoe

@Dao
interface ShoeDao {

    // 通过鞋子的范围寻找Index
    @Query("SELECT * FROM shoe WHERE id between :startIndex AND :endIndex ORDER BY id ASC")
    suspend fun findShoesByIndexRange(startIndex: Long, endIndex: Long): List<Shoe>

    // 配合LiveData 返回所有的鞋子
    /*@Query("SELECT * FROM shoe")
    fun getAllShoesLD(): PagingSource<Int, Shoe>*/
    //fun getAllShoesLD(): LiveData<List<Shoe>>

    // 配合LiveData 通过Id查询单款鞋子
    @Query("SELECT * FROM shoe WHERE id=:id")
    fun findShoeByIdLD(id: Long): LiveData<Shoe?>


    /**
     * 通过品牌查询鞋子
     */
    /*@Query("SELECT * FROM shoe WHERE shoe_brand IN (:brand)")
    fun findShoesByBrandLD(brand: Array<String>): PagingSource<Int, Shoe>
    //fun findShoesByBrandLD(brand: String): LiveData<List<Shoe>>*/

    // 根据收藏结合 查询用户喜欢的鞋的集合
    @Query(
        "SELECT shoe.id, shoe.shoe_name, shoe.shoe_description, shoe.shoe_price, shoe.shoe_brand, shoe.shoe_imgUrl " +
                "FROM shoe " +
                "INNER JOIN fav_shoe ON fav_shoe.shoe_id = shoe.id " +
                "WHERE fav_shoe.user_id = :userId AND fav_shoe.id between :startId AND :endId "
    )
    suspend fun findShoesByUserId(userId: Long, startId: Long, endId: Long): List<Shoe>


    // 增加一双鞋子
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShoe(shoe: Shoe)

    // 增加多双鞋子
    // 除了List之外，也可以使用数组
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoes(shoes: List<Shoe>)

    // 删除一双鞋子
    @Delete
    fun deleteShoe(shoe: Shoe)

    // 删除多个鞋子
    // 参数也可以使用数组
    @Delete
    fun deleteShoes(shoes: List<Shoe>)

    // 更新一双鞋
    @Update
    fun updateShoe(shoe: Shoe)

    // 更新多双鞋
    // 参数也可以是集合
    @Update
    fun updateShoes(shoes: Array<Shoe>)
}