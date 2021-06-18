import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/ui/login/login_page.dart';
import 'package:flutter_hoo/ui/login/register_page.dart';
import 'package:flutter_hoo/ui/login/welcome_page.dart';

class SplashPage extends StatefulWidget {
  @override
  _SplashPageState createState() => _SplashPageState();
}

class _SplashPageState extends State<SplashPage>
    with SingleTickerProviderStateMixin {
  Animation<double> progressAnimation;
  Animation<double> scalaAnimation;
  AnimationController controller;

  @override
  void initState() {
    super.initState();

    controller =
        new AnimationController(value: 0, duration: Duration(seconds: 2), vsync: this);
    scalaAnimation = new Tween<double>(begin: 100, end: 40)
        .animate(CurvedAnimation(parent: controller, curve: Interval(0, 0.4)));
    progressAnimation = new Tween<double>(begin: 0, end: 1).animate(
        CurvedAnimation(
            parent: controller, curve: Interval(0.4, 1, curve: Curves.easeIn)));
    controller.addStatusListener((status) {
      if (status == AnimationStatus.completed) {
        print(controller.value);
        Navigator.pushReplacement(
            context, MaterialPageRoute(builder: (ctx) => WelcomePage()));
      }
    });
    controller.forward();
  }

  @override
  void dispose() {
    super.dispose();

    controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return new Container(
      color: ThemeUtils.getMainColor(context),
      child: new Center(
        child: new Stack(
          alignment: Alignment.center,
          children: [
            AnimatedBuilder(
              animation: controller,
              builder: (context, widget) {
                return Text("Hoo",
                    style: TextStyle(
                        color: ThemeUtils.getTextMainWhiteColor(context),
                        fontSize: scalaAnimation.value,
                        fontWeight: FontWeight.bold));
              },
            ),
            AnimatedBuilder(
              animation: controller,
              builder: (context, widget) {
                return SizedBox(
                  width: 120,
                  height: 120,
                  child: CircularProgressIndicator(
                    backgroundColor: Colors.transparent,
                    value: progressAnimation.value,
                    valueColor: AlwaysStoppedAnimation(ThemeUtils.getTextMainWhiteColor(context)),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
