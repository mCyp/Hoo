import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hoo/common/constant/baseConstant.dart';
import 'package:flutter_hoo/common/utils/sp_util.dart';
import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/db/shoe.dart';
import 'package:flutter_hoo/style/theme/strings.dart';
import 'package:flutter_hoo/ui/login/login_page.dart';
import 'package:flutter_hoo/ui/login/register_page.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/widget/my_button.dart';

class WelcomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    double screenWidth = context.screenWidth;
    double paddingTop = MediaQuery.of(context).padding.top;
    bool isInitShoes =
        SpUtil.getBool(BaseConstant.is_shoe_init, defValue: false);
    if (!isInitShoes) {
      _loadShoes();
      SpUtil.putBool(BaseConstant.is_shoe_init, true);
    }
    return SafeArea(
      child: SingleChildScrollView(
        child: Container(
          width: screenWidth,
          height: context.screenHeight - paddingTop,
          color: ThemeUtils.getBgColor(context),
          child: Column(
            children: [
              AspectRatio(
                aspectRatio: 1.25,
                child: Image.asset(
                  "assets/images/welcome_bg.png",
                  width: screenWidth,
                  fit: BoxFit.cover,
                ),
              ),
              Spacer(flex: 1),
              Text(
                Strings.welcome_info_1,
                style: Theme.of(context)
                    .textTheme
                    .headline6
                    .copyWith(color: ThemeUtils.getMainTextColor(context)),
              ),
              Text(
                Strings.welcome_info_2,
                style: Theme.of(context)
                    .textTheme
                    .headline6
                    .copyWith(color: ThemeUtils.getMainTextColor(context)),
              ),
              Text(
                Strings.welcome_info_3,
                style: Theme.of(context)
                    .textTheme
                    .headline6
                    .copyWith(color: ThemeUtils.getMainTextColor(context)),
              ),
              Padding(
                padding: EdgeInsets.fromLTRB(20, 52, 20, 0),
                child: HooButton(
                    true,
                    Strings.welcome_button_login,
                    () => Navigator.push(context,
                        MaterialPageRoute(builder: (ctx) => LoginPage()))),
              ),
              Padding(
                padding: EdgeInsets.fromLTRB(20, 8, 20, 0),
                child: HooButton(
                    true,
                    Strings.welcome_button_register,
                    () => Navigator.push(context,
                        MaterialPageRoute(builder: (ctx) => RegisterPage()))),
              ),
              Spacer(flex: 1),
            ],
          ),
        ),
      ),
    );
  }

  Future<String> _loadShoeJson() async {
    return await rootBundle.loadString("assets/json/shoes.json");
  }

  Future<void> _loadShoes() async {
    String shoeJson = await _loadShoeJson();
    List<dynamic> list = json.decode(shoeJson);
    List<Shoe> shoes = list.map((e) => Shoe.fromJson(e)).toList();
    shoes.forEach((element) {
      print(element.name);
    });
    DBProvider provider = await DBProvider.getInstanceAndInit();
    await provider.insertShoe(shoes);
  }
}
