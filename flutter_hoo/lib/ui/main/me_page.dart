import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/constant/baseConstant.dart';
import 'package:flutter_hoo/common/utils/image_utils.dart';
import 'package:flutter_hoo/common/utils/sp_util.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/db/use.dart';
import 'package:flutter_hoo/provider/theme_provider.dart';
import 'package:flutter_hoo/widget/load_image.dart';
import 'package:provider/provider.dart';

class MePage extends StatefulWidget {
  @override
  _MePageState createState() => _MePageState();
}

class _MePageState extends State<MePage> {
  int _userId;
  User _user;
  bool _isFollowSystem;

  @override
  void initState() {
    super.initState();
    queryUser();
    _isFollowSystem =
        SpUtil.getString(BaseConstant.theme, defValue: "System") == "System";
  }

  @override
  Widget build(BuildContext context) {
    double screenHeight = context.screenHeight;
    double screenWidth = context.screenWidth;

    return Container(
      color: ThemeUtils.getBgGrayColor(context),
      child: Stack(
        children: [
          Positioned(
            left: 0,
            top: 0,
            child: SizedBox(
              width: screenWidth,
              height: screenHeight * 0.3,
              child: DecoratedBox(
                decoration:
                    BoxDecoration(color: ThemeUtils.getMainColor(context)),
              ),
            ),
          ),
          Positioned(
            left: 16,
            top: screenHeight * 0.3 - 50,
            child: SizedBox(
              width: 100,
              height: 100,
              child: ClipRRect(
                borderRadius: BorderRadius.circular(4),
                child: LoadImage(
                  (_user == null || _user.headImage == null)
                      ? "default_header"
                      : _user.headImage,
                  format: ImageFormat.jpeg,
                ),
              ),
            ),
          ),
          Positioned(
            left: 124,
            top: screenHeight * 0.3 - 50,
            child: SizedBox(
              width: screenWidth - 124,
              height: 50,
              child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  _user == null ? "Enjoy" : _user.name,
                  style: Theme.of(context).textTheme.headline4.copyWith(
                      color: ThemeUtils.getTextMainWhiteColor(context)),
                ),
              ),
            ),
          ),
          Positioned(
            left: 124,
            top: screenHeight * 0.3,
            child: SizedBox(
              width: screenWidth - 124,
              height: 50,
              child: Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  _user == null ? "do something enjoy" : _user.account,
                  style: Theme.of(context)
                      .textTheme
                      .bodyText1
                      .copyWith(color: ThemeUtils.getTextGrayColor(context)),
                ),
              ),
            ),
          ),
          Positioned(
            left: 0,
            top: screenHeight * 0.3 + 60,
            child: _buildListCard(screenWidth),
          )
        ],
      ),
    );
  }

  Widget _buildListCard(double screenWidth) {
    return Container(
      width: screenWidth - 16 * 2,
      margin: EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: ThemeUtils.getTextMainWhiteColor(context),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Column(
        children: [
          _buildListItem(
              screenWidth,
              Icons.accessibility,
              "数据存储",
              Icon(
                Icons.chevron_right,
                size: 24,
                color: ThemeUtils.getTextGrayColor(context),
              )),
          _buildListItem(screenWidth, Icons.wb_sunny_sharp, "黑夜模式跟随系统",
              _buildThemeSwitch()),
        ],
      ),
    );
  }

  Widget _buildListItem(
      double screenWidth, IconData iconData, String text, Widget lastWidget) {
    return Container(
      width: screenWidth,
      height: 56,
      padding: EdgeInsets.symmetric(horizontal: 16),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Icon(
            iconData,
            size: 24,
            color: ThemeUtils.getMainTextColor(context),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 20.0),
            child: Text(
              text,
              style: Theme.of(context)
                  .textTheme
                  .bodyText1
                  .copyWith(color: ThemeUtils.getMainTextColor(context)),
            ),
          ),
          Spacer(),
          lastWidget,
        ],
      ),
    );
  }

  void queryUser() async {
    _userId = SpUtil.getInt(BaseConstant.user_id);
    if (_userId > 0) {
      DBProvider provider = DBProvider.getInstance();
      User user = await provider.queryUserByUserId(_userId);
      print(user.toString());
      setState(() {
        _user = user;
      });
    }
  }

  Widget _buildThemeSwitch() {
    return Consumer<ThemeProvider>(builder: (ctx, provider, child) {
      return Switch(
        value: _isFollowSystem,
        onChanged: (active) => _onThemeChange(active, provider),
      );
    });
  }

  void _onThemeChange(bool active, ThemeProvider provider) {
    provider.setTheme(
        active ? ThemeMode.system : provider.getLastUnFollowSystemModel());
    setState(() {
      _isFollowSystem = active;
    });
  }
}
