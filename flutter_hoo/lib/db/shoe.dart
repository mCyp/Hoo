class Shoe {
  final int id;
  final String name;
  final String description;
  final double price;
  final String brand;
  final String imageUrl;

  Shoe(this.name, this.description, this.price, this.brand,
      this.imageUrl,{this.id});

  factory Shoe.fromJson(Map<String,dynamic> map){
    return Shoe(map['name'], map['description'], map['price'], map['brand'], map['imageUrl'],id: map['id']);
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'price': price,
      'brand': brand,
      'imageUrl': imageUrl
    };
  }
}