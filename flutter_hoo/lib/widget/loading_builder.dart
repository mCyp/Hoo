import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/widget/hoo_empty_widget.dart';
import 'package:loading_more_list/loading_more_list.dart';
import 'package:lottie/lottie.dart';
import 'package:path/path.dart';

Widget loadingBuilder(BuildContext context, IndicatorStatus status) {
  return HooLoadingIndicator(status);
}

class HooLoadingIndicator extends StatelessWidget {
  HooLoadingIndicator(this.status,
      {this.tryAgain,
      this.text = "",
      this.backgroundColor = Colors.transparent,
      this.isSliver = false});

  String text = "";
  Function tryAgain;
  Color backgroundColor = Colors.transparent;
  bool isSliver = true;
  Widget emptyWidget = HooEmptyWidget(text: "什么都没有哦～");
  final IndicatorStatus status;

  @override
  Widget build(BuildContext context) {
    Widget widget = null;

    switch (status) {
      case IndicatorStatus.none:
        widget = Container(
          width: 0.0,
          height: 0.0,
        );
        break;
      case IndicatorStatus.loadingMoreBusying:
        widget = Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Container(
              margin: const EdgeInsets.only(right: 10),
              height: 36,
              width: 36,
              child: getIndicator(context, strokeWidth: 2.0, r: 10.0),
            ),
            Text(text != null && text.isNotEmpty ? text : "数据不能停...",
                style: Theme.of(context).textTheme.bodyText1)
          ],
        );
        widget = _setBackground(context, widget, 76.0);
        break;
      case IndicatorStatus.fullScreenBusying:
        widget = Lottie.asset(
          'assets/lottie/full_screen_loading.json',
          width: 300,
          height: 300,
        );
        widget = _setBackground(context, widget, double.infinity);
        if (isSliver) {
          widget = SliverFillRemaining(
            child: widget,
          );
        } else {
          widget = CustomScrollView(
            slivers: [
              SliverFillRemaining(
                child: widget,
              )
            ],
          );
        }
        break;
      case IndicatorStatus.error:
        widget = Text(text != null ? text : "当前发生错误了～");
        widget = _setBackground(context, widget, 35.0);
        if (tryAgain != null) {
          widget = GestureDetector(
            onTap: () => tryAgain(),
            child: widget,
          );
        }
        break;
      case IndicatorStatus.fullScreenError:
        widget = Text(
          text != null ? text : 'load failed,try again.',
        );
        widget = _setBackground(context, widget, double.infinity);
        if (tryAgain != null) {
          widget = GestureDetector(
            onTap: () {
              tryAgain();
            },
            child: widget,
          );
        }
        if (isSliver) {
          widget = SliverFillRemaining(
            child: widget,
          );
        } else {
          widget = CustomScrollView(
            slivers: <Widget>[
              SliverFillRemaining(
                child: widget,
              )
            ],
          );
        }
        break;
      case IndicatorStatus.noMoreLoad:
        widget = Text(text != null ? text : "哥，这回真的没有了～");
        widget = _setBackground(context, widget, 35.0);
        break;
      case IndicatorStatus.empty:
        widget = emptyWidget;
        break;
      default:
        widget = Text("haha");
        break;
    }
    return widget;
  }

  Widget _setBackground(BuildContext context, Widget widget, double height, {Color bgColor}) {
    widget = Container(
      width: double.infinity,
      height: height,
      child: widget,
      color: bgColor != null
          ? bgColor
          : backgroundColor != null
              ? backgroundColor
              : ThemeUtils.getBgColor(context),
      alignment: Alignment.center,
    );
    return widget;
  }

  Widget getIndicator(BuildContext context,
      {double strokeWidth = 4.0, double r = 20.0}) {
    final ThemeData theme = Theme.of(context);
    return theme.platform == TargetPlatform.iOS
        ? CupertinoActivityIndicator(
            animating: true,
            radius: r,
          )
        : CircularProgressIndicator(
            strokeWidth: strokeWidth,
            valueColor: AlwaysStoppedAnimation<Color>(theme.primaryColor),
          );
  }
}
