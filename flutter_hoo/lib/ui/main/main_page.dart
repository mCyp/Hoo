import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/provider/theme_provider.dart';
import 'package:flutter_hoo/style/theme/strings.dart';
import 'package:flutter_hoo/ui/main/fav_shoe_page.dart';
import 'package:flutter_hoo/ui/main/me_page.dart';
import 'package:flutter_hoo/ui/main/shoe_page.dart';
import 'package:lottie/lottie.dart';
import 'package:provider/provider.dart';

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  int _currentIndex = 0;
  PageController _controller;
  bool _isActive = false;
  bool _isShowDarkBtn = false;
  String _modelHint = "";
  GlobalKey<DarkLottieWidgetState> _darkLottieKey = GlobalKey();

  @override
  void initState() {
    super.initState();
    _controller =
        PageController(initialPage: 0, keepPage: true, viewportFraction: 1);
  }

  @override
  void dispose() {
    super.dispose();

    _controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    _isActive = context.isDark;
    _modelHint = _isShowDarkBtn ? (_isActive ? "黑夜" : "白天") : "";
    return Scaffold(
      appBar: _buildAppbar(),
      bottomNavigationBar: _buildBottomNavigationBar(),
      body: _buildPageView(context.screenWidth, context.screenHeight),
    );
  }

  Widget _buildAppbar() {
    return AppBar(
      title: Text(
        "Hoo",
        style: Theme.of(context)
            .textTheme
            .headline5
            .copyWith(color: Theme.of(context).scaffoldBackgroundColor),
      ),
      centerTitle: false,
      automaticallyImplyLeading: false,
      backgroundColor: Theme.of(context).primaryColor,
      actions: <Widget>[
        SizedBox(
          height: 56,
          child: Center(
            child: Text(
              _modelHint,
              style: Theme.of(context).textTheme.bodyText1.copyWith(
                  color: ThemeUtils.getTextMainWhiteColor(context),
                  fontWeight: FontWeight.bold),
            ),
          ),
        ),
        Visibility(
          child: Consumer<ThemeProvider>(builder: (ctx, provider, child) {
            // 黑暗模式的Provider
            return Switch(
                value: _isActive,
                activeColor: ThemeUtils.getTextMainWhiteColor(context),
                inactiveThumbColor: ThemeUtils.getTextMainWhiteColor(context),
                onChanged: (active) {
                  _onThemeModelSelect(active, provider);
                });
          }),
          visible: _isShowDarkBtn,
        )
      ],
    );
  }

  Widget _buildBottomNavigationBar() {
    return BottomNavigationBar(
      items: [
        BottomNavigationBarItem(
          icon: Icon(Icons.home),
          label: Strings.main_bottom_home,
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.favorite),
          label: Strings.main_bottom_fav,
        ),
        BottomNavigationBarItem(
          icon: Icon(Icons.person),
          label: Strings.main_bottom_me,
        ),
      ],
      selectedItemColor: Theme.of(context).primaryColor,
      unselectedItemColor: Theme.of(context).unselectedWidgetColor,
      onTap: (index) => onSelect(index),
      currentIndex: _currentIndex,
      type: BottomNavigationBarType.fixed,
    );
  }

  Widget _buildPageView(double screenWidth, double screenHeight) {
    return Stack(
      alignment: Alignment.center,
      children: [
        PageView(
          children: [
            ShoePage(),
            FavShoePage(),
            MePage(),
          ],
          controller: _controller,
          onPageChanged: (index) => onSelect(index),
        ),
        DarkLottieWidget(_darkLottieKey),
      ],
    );
  }

  void onSelect(int pos) {
    setState(() {
      _isShowDarkBtn = pos == 2;
      _currentIndex = pos;
      _controller.animateToPage(_currentIndex,
          duration: Duration(milliseconds: 200), curve: Curves.easeInOut);
    });
  }

  void _onThemeModelSelect(bool isActive, ThemeProvider provider) {
    if (provider.getThemeMode() == ThemeMode.system) return;
    provider.setTheme(isActive ? ThemeMode.dark : ThemeMode.light);
    _darkLottieKey.currentState.showAnimation(isActive);
    setState(() {
      _isActive = isActive;
      _modelHint = _isActive ? "黑夜" : "白天";
    });
  }
}

class DarkLottieWidget extends StatefulWidget {
  DarkLottieWidget(Key key) : super(key: key);

  @override
  DarkLottieWidgetState createState() => DarkLottieWidgetState();
}

class DarkLottieWidgetState extends State<DarkLottieWidget>
    with TickerProviderStateMixin {
  AnimationController _animationController;
  bool _isVisible = false;

  @override
  void initState() {
    super.initState();

    _animationController =
        AnimationController(vsync: this, duration: Duration(milliseconds: 1000));

    _animationController.addStatusListener((status) {
      bool v = true;
      switch (status) {
        case AnimationStatus.forward:
          {
            v = true;
          }
          break;
        case AnimationStatus.reverse:
          {
            v = true;
          }
          break;
        case AnimationStatus.dismissed:
          {
            v = false;
          }
          break;
        case AnimationStatus.completed:
          {
            v = false;
          }
          break;
      }
      if (v != _isVisible) {
        setState(() {
          _isVisible = v;
        });
      }
    });
  }

  @override
  void dispose() {
    super.dispose();

    _animationController.dispose();
  }

  void showAnimation(bool isActive) {
    try {
      if (isActive) {
        _animationController.forward().orCancel;
      } else {
        _animationController.reverse().orCancel;
      }
    } on TickerCanceled {}
  }

  @override
  Widget build(BuildContext context) {
    return Visibility(
      visible: _isVisible,
      child: Container(
        decoration: BoxDecoration(
          color: ThemeUtils.isDark(context) ? Colors.white38 : Colors.black26,
          borderRadius: const BorderRadius.all(Radius.circular(10)),
        ),
        width: 200,
        height: 200,
        alignment: Alignment.center,
        child: Lottie.asset(
          'assets/lottie/dark.json',
          controller: _animationController,
        ),
      ),
    );
  }
}
