class User {
  int id;
  String account;
  String pwd;
  String name;
  String headImage;

  User(this.account, this.pwd, this.name, this.headImage, {this.id});

  User.fromJson(Map<String,dynamic> json){
    id = json['id'];
    account = json['account'];
    pwd = json['pwd'];
    name = json['name'];
    headImage = json['headImage'];
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'account': account,
      'pwd': pwd,
      'name': name,
      'headImage': headImage,
    };
  }

  @override
  String toString() {
    return "{$id,$account,$pwd,$name,$headImage}";
  }
}
