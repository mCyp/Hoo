import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_hoo/common/utils/sp_util.dart';
import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/provider/theme_provider.dart';
import 'package:flutter_hoo/style/theme/theme.dart';
import 'package:flutter_hoo/ui/splash/splash.dart';
import 'package:oktoast/oktoast.dart';
import 'package:provider/provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await SpUtil.getInstance();
  await DBProvider.getInstance();
  await SystemChrome.setPreferredOrientations(<DeviceOrientation>[DeviceOrientation.portraitUp,DeviceOrientation.portraitDown]).then((_){
    runApp(
      MultiProvider(
        providers:[
          ChangeNotifierProvider(create: (_)=> ThemeProvider())
        ],
        child: MyApp(),
      )
    );
  });
}

SystemUiOverlayStyle setNavigationBarTextColor(bool light){
  return SystemUiOverlayStyle(
    systemNavigationBarColor: Colors.black,
    systemNavigationBarDividerColor: null,
    systemNavigationBarIconBrightness: Brightness.light,
    statusBarColor: null,
    statusBarIconBrightness: light ? Brightness.light : Brightness.dark,
    statusBarBrightness: light ? Brightness.dark : Brightness.light,
  );
}

class MyApp extends StatelessWidget {
  final ThemeData theme;
  MyApp({this.theme});

  @override
  Widget build(BuildContext context) {
    if(Platform.isAndroid){
      SystemUiOverlayStyle systemUiOverlayStyle = SystemUiOverlayStyle(statusBarColor: Colors.transparent);
      SystemChrome.setSystemUIOverlayStyle(systemUiOverlayStyle);
    }
    return OKToast(
      child: ChangeNotifierProvider(
        create: (_) => ThemeProvider(),
        child: Consumer<ThemeProvider>(
          builder: (_,provider,__){
            if(provider.getThemeMode() == ThemeMode.system){
              return MaterialApp(
                title: 'Hoo',
                theme: theme ?? ThemeCenter.getCenterTheme(),
                darkTheme: ThemeCenter.getCenterTheme(isDark: true),
                themeMode: provider.getThemeMode(),
                home: NewHome(),
              );
            }else {
              return MaterialApp(
                title: 'Hoo',
                theme: provider.getThemeMode() == ThemeMode.dark ? ThemeCenter.getCenterTheme(isDark: true) : ThemeCenter.getCenterTheme(isDark: false),
                themeMode: provider.getThemeMode(),
                home: NewHome(),
              );
            }
          },
        )
      ),
    );
  }
}

class NewHome extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return AnnotatedRegion<SystemUiOverlayStyle>(
      value: setNavigationBarTextColor(true),
      child: Scaffold(
        body: SplashPage(),
      ),
    );
  }
}