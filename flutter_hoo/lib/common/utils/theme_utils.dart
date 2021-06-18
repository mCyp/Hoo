import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hoo/style/theme/colors.dart';

class ThemeUtils {

  static bool isDark(BuildContext context) {
    return Theme.of(context).brightness == Brightness.dark;
  }

  static Color getBgColor(BuildContext context) {
    return isDark(context) ? CustomColors.dark_bg_color : CustomColors.bg_color;
  }

  static Color getTextMainWhiteColor(BuildContext context) {
    return isDark(context) ? CustomColors.dark_text_main_white : CustomColors.text_main_white;
  }

  static Color getTextSecondWhiteColor(BuildContext context){
    return isDark(context) ? CustomColors.text_gray_300 : CustomColors.dark_text_gray_300;
  }

  static Color getOutProviderColor(BuildContext context){
    return isDark(context) ? CustomColors.dark_outline_color : CustomColors.outline_color;
  }

  static Color getMainColor(BuildContext context){
    return isDark(context) ? CustomColors.dark_primary_color : CustomColors.primary_color;
  }

  static Color getMainTextColor(BuildContext context){
    return isDark(context) ? CustomColors.dark_text_main : CustomColors.text_main;
  }

  static Color getTextGrayColor(BuildContext context){
    return isDark(context) ? CustomColors.dark_text_gray_300 : CustomColors.text_gray_300;
  }

  static Color getBgGrayColor(BuildContext context){
    return isDark(context) ? CustomColors.dark_bg_gray_color : CustomColors.bg_gray_color;
  }

}

extension ThemeExtension on BuildContext {
  bool get isDark => ThemeUtils.isDark(this);
  Color get backgroundColor => Theme.of(this).scaffoldBackgroundColor;
  Color get dialogBackgroundColor => Theme.of(this).canvasColor;
  double get screenWidth => MediaQuery.of(this).size.width;
  double get screenHeight => MediaQuery.of(this).size.height;
}