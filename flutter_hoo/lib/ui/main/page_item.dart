import 'package:flutter/cupertino.dart';

class PageItem extends StatelessWidget {
  final String title;
  final Color color;

  PageItem(this.title, this.color);

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(10),
      color: color,
      child: Center(
        child: Text(title),
      ),
    );
  }
}
