import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';

class HooEmptyWidget extends StatelessWidget {
  final String text;
  const HooEmptyWidget({Key key, this.text = "感觉产生点数据吧！"}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: double.infinity,
      width: double.infinity,
      color: ThemeUtils.getBgColor(context),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(
            height: 200,
            width: 200,
            child: Image.asset(
              'assets/images/empty_bg.jpg'
            ),
          ),
          Text(text, style: Theme.of(context).textTheme.bodyText1)
        ],
      ),
    );
  }
}
