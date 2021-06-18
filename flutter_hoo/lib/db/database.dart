import 'dart:ffi';
import 'dart:io';

import 'package:flutter_hoo/db/fav_shoe.dart';
import 'package:flutter_hoo/db/shoe.dart';
import 'package:flutter_hoo/db/use.dart';
import 'package:path/path.dart';
import 'package:path_provider/path_provider.dart';
import 'package:sqflite/sqflite.dart';

class DBProvider {
  static final DBProvider _singleton = DBProvider._internal();

  factory DBProvider() => _singleton;

  static DBProvider getInstance() => _singleton;

  static Future<DBProvider> getInstanceAndInit() async {
    _db = await _singleton._initDB();
    return _singleton;
  }

  DBProvider._internal();

  static Database _db;

  Future<Database> get db async {
    if (_db != null) return _db;
    _db = await _initDB();
    return _db;
  }

  Future<Database> _initDB() async {
    // 路径获取是通过 path_provider 库，需要自行添加依赖
    Directory documentsDirectory = await getApplicationDocumentsDirectory();
    String path = join(documentsDirectory.path, 'dbHoo');
    return await openDatabase(path, onCreate: _onCreate, version: 1);
  }

  // 创建表
  Future _onCreate(Database db, int version) async {
    db.execute(
        "CREATE TABLE IF NOT EXISTS user("
            "id INTEGER PRIMARY KEY autoincrement, "
            "account TEXT, "
            "pwd TEXT, "
            "name TEXT, "
            "headImage Text);");
    db.execute(
        "CREATE TABLE IF NOT EXISTS shoe("
            "id INTEGER PRIMARY KEY autoincrement, "
            "name TEXT, "
            "description TEXT, "
            "price REAL, "
            "brand Text, "
            "imageUrl Text);");
    db.execute(
        "CREATE TABLE IF NOT EXISTS fav_shoe("
            "id INTEGER PRIMARY KEY autoincrement, "
            "shoe_id INTEGER, "
            "user_id INTEGER, "
            "date INTEGER , "
            "FOREIGN KEY(user_id) REFERENCES user(id), "
            "FOREIGN KEY(shoe_id) REFERENCES shoe(id));");
    return;
  }

  // ##### User表

  // 新增用户
  Future<void> insertUser(User user) async {
    var _db = await db;
    await _db.insert("user", user.toMap(),
        conflictAlgorithm: ConflictAlgorithm.fail);
  }

  // 新增用户
  Future<void> insertUserRaw(User user) async {
    var _db = await db;
    await _db.rawInsert("INSERT INTO USER (account, pwd, name, headImage) VALUES (?, ?, ?, ?);",[user.account,user.pwd,user.name,user.headImage]);
  }

  // 更新用户
  Future<void> updateUser(User user) async {
    var _db = await db;
    await _db.update("user", user.toMap(), where: "id = ?",whereArgs: [user.id]);
  }

  // 删除
  Future<void> deleteUser(int userId) async {
    var _db = await db;
    await _db.delete("user", where: "id = ?",whereArgs: [userId]);
  }

  // 查询所有的用户
  Future<List<User>> queryAllUsers() async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query("user");
    if(result.length == null) return List<User>();
    return List.generate(result.length, (i) => User.fromJson(result[i]));
  }

  // 获取用户
  Future<User> queryUserByNameAndPwd(String account, String pwd) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query("user",
        where: 'name = ? and pwd = ?', whereArgs: [account, pwd]);
    Map<String, dynamic> r = result.isNotEmpty ? result.first : null;
    if (r == null) return null;
    return User.fromJson(r);
  }

  // 通过用户Id获取用户
  Future<User> queryUserByUserId(int userId) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query("user",
        where: 'id = ?', whereArgs: [userId]);
    Map<String, dynamic> r = result.isNotEmpty ? result.first : null;
    if (r == null) return null;
    return User.fromJson(r);
  }

  // ##### Shoe表
  Future<void> insertShoe(List<Shoe> shoes) async {
    var _db = await db;
    shoes.forEach((shoe) async {
      await _db.insert('shoe', shoe.toMap(),
          conflictAlgorithm: ConflictAlgorithm.fail);
    });
  }

  // 获取鞋子
  Future<List<Shoe>> queryShoe() async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query('shoe', orderBy: 'id');
    if (result.isEmpty) return List<Shoe>();
    return List.generate(result.length, (i) => Shoe.fromJson(result[i]));
  }

  // 通过用户Id获取用户
  Future<Shoe> queryShoeById(int shoeId) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query("shoe",
        where: 'id = ?', whereArgs: [shoeId]);
    Map<String, dynamic> r = result.isNotEmpty ? result.first : null;
    if (r == null) return null;
    return Shoe.fromJson(r);
  }

  // 获取指定位置的Shoe列表
  Future<List<Shoe>> queryShoeByPos(int startPos, int endPos) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query('shoe',
        where: ' id > ? and id <= ?',
        whereArgs: [startPos, endPos],
        orderBy: 'id');
    if (result.isEmpty) return List<Shoe>();
    return List.generate(result.length, (i) => Shoe.fromJson(result[i]));
  }

  // 获取指定位置的Shoe列表
  Future<List<Shoe>> queryShoeByPosAndBrand(int startPos, int endPos, String brand) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query('shoe',
        where: ' id > ? and id <= ? and brand = ?',
        whereArgs: [startPos, endPos, brand],
        orderBy: 'id ASC');
    if (result.isEmpty) return List<Shoe>();
    return Future.delayed(Duration(seconds: 3), ()=>(List.generate(result.length, (i) => Shoe.fromJson(result[i]))));
  }

  // #### FavShoe表

  // 插入鞋子
  Future<void> insertFavShoe(FavShoe favShoe) async {
    var _db = await db;
    await _db.insert("fav_shoe", favShoe.toMap(),
        conflictAlgorithm: ConflictAlgorithm.replace);
  }

  // 删除收藏记录
  Future<void> deleteFavShoe(FavShoe favShoe) async {
    var _db = await db;
    await _db.delete("fav_shoe", where: "id = ?",whereArgs: [favShoe.id]);
  }

  // 查询某人的收藏记录
  Future<FavShoe> queryFavShoeByUserID(int userId, int shoeId) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.query('fav_shoe',
        where: ' user_id = ? and shoe_id = ?',
        whereArgs: [userId,shoeId]);
    if (result.isEmpty) return null;
    var _favShoe = FavShoe.fromJson(result.first);
    if(_favShoe == null)
      return null;
    // 单独查询鞋子表
    var _shoe = await queryShoeById(_favShoe.shoeId);
    _favShoe.shoe = _shoe;
    return _favShoe;
  }

  // 查询某人的收藏记录
  Future<List<FavShoe>> queryFavShoesByUserID(int userId, int start, int end) async {
    var _db = await db;
    List<Map<String, dynamic>> result = await _db.rawQuery("SELECT fav_shoe.id, shoe_id, user_id, shoe.name, shoe.price, shoe.imageUrl, shoe.description, shoe.brand "
        "FROM fav_shoe LEFT OUTER JOIN shoe ON fav_shoe.shoe_id = shoe.id "
        "WHERE user_id = ? and fav_shoe.id > ? and fav_shoe.id <= ?;",[userId, start, end]);
    if (result.isEmpty) return List.empty();
    return List.generate(result.length, (i) => FavShoe.fromJsonAndShoe(result[i]));
  }

  Future<void> doTransaction() async {
    var _db = await db;
    Batch batch = _db.batch();
    User one = new User("200722649@qq.com", "123456", "ChenHa", null);
    User two = new User("200622649@qq.com", "123456", "WangHa", null);
    batch.insert("user", one.toMap());
    batch.insert("user", two.toMap());
    batch.delete("user",where: "id = ?",whereArgs: [6]);
    batch.query("user");
    // results 的数量有四个
    // 第一个 - 插入用户one的结果
    // 第二个 - 插入用户two的结果
    // 第三个 - 删除用户id为6的结果
    // 第四个 - 查询所有用户的结果
    List<dynamic> results = await batch.commit();
  }


}
