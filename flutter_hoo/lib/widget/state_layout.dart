import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/style/res/gaps.dart';
import 'package:flutter_hoo/style/theme/dimen.dart';
import 'package:flutter_hoo/widget/load_image.dart';

class StateLayout extends StatelessWidget {
  StateLayout({Key key, @required this.type, @required this.url, @required this.hintText})
      : super(key: key);

  final StateType type;
  final String url;
  final String hintText;

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        if (type == StateType.loading)
          const CupertinoActivityIndicator(
            radius: 16.0,
          )
        else if (type == StateType.empty)
          Opacity(
            opacity: context.isDark ? 0.5 : 1,
            child: LoadAssetImage(
              url,
              width: 120,
            ),
          ),
        const SizedBox(width: double.infinity, height: Dimens.gap_dp16,),
        Text(hintText, style: Theme.of(context).textTheme.subtitle1,),
        Gaps.vGap16,
      ],
    );
  }
}

enum StateType {
  // 加载中
  loading,
  // 空布局
  empty,
  // 数据
  finished,
  // 错误
  error
}
