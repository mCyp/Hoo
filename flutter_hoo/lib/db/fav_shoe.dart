import 'package:flutter_hoo/db/shoe.dart';
import 'package:flutter_hoo/db/use.dart';

class FavShoe {
  final int id;
  final int userId;
  User user;
  final int shoeId;
  Shoe shoe;
  final int date;

  FavShoe(this.userId, this.shoeId, this.date, {this.id, this.user, this.shoe});

  factory FavShoe.fromJson(Map<String, dynamic> map) {
    return FavShoe(map['user_id'], map['shoe_id'], map['date'], id: map['id']);
  }

  factory FavShoe.fromJsonAndShoe(Map<String, dynamic> map) {
    Shoe shoe =
        Shoe(map['name'], map['description'], map['price'], map['brand'], map['imageUrl'], id: map['shoe_id']);
    return FavShoe(map['user_id'], map['shoe_id'], map['date'], id: map['id'], shoe: shoe);
  }

  Map<String, dynamic> toMap() {
    return {'id': id, 'user_id': userId, 'shoe_id': shoeId, 'date': date};
  }

  @override
  String toString() {
    return "id: " + id.toString() + ", userId: " + userId.toString() + ", shoeId: "+ shoeId.toString();
  }
}
