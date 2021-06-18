import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/db/use.dart';
import 'package:flutter_hoo/style/theme/strings.dart';
import 'package:flutter_hoo/ui/main/main_page.dart';
import 'package:flutter_hoo/widget/my_button.dart';
import 'package:flutter_hoo/widget/my_textfield.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:oktoast/oktoast.dart';

class RegisterPage extends StatefulWidget {
  @override
  RegisterPageState createState() {
    return RegisterPageState();
  }
}

class RegisterPageState extends State<RegisterPage> {
  bool isButtonEnable = false;
  final TextEditingController accountController = new TextEditingController();
  final TextEditingController pwdController = new TextEditingController();
  final TextEditingController emailController = new TextEditingController();

  bool isEmailError = false;

  @override
  void initState() {
    super.initState();
  }

  void _onTextChange() {
    Pattern pattern = r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';
    RegExp exp = RegExp(pattern);
    String email = emailController.text.toString();
    String account = accountController.text.toString();
    String pwd = pwdController.text.toString();
    bool emailError = !exp.hasMatch(email);
    bool currentState =
        account.isNotEmpty && pwd.isNotEmpty && email.isNotEmpty;
    if (currentState != isButtonEnable || emailError != isEmailError) {
      setState(() {
        if (currentState != isButtonEnable) isButtonEnable = currentState;
        if (emailError != isEmailError) isEmailError = emailError;
      });
    }
  }

  void _onRegister() async {
    String email = emailController.text.toString();
    String account = accountController.text.toString();
    String pwd = pwdController.text.toString();
    Pattern pattern =
        r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';
    RegExp exp = RegExp(pattern);
    if (!exp.hasMatch(email)) {
      setState(() {
        isEmailError = true;
      });
      return;
    }
    User user = User(email, pwd, account, null);
    DBProvider provider =
        await DBProvider.getInstanceAndInit();
    await provider.insertUser(user);
    showToast("注册成功！", textPadding: EdgeInsets.all(10));
    Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (ctx) => MainPage()),
            (Route<dynamic> route) => false);
  }

  @override
  Widget build(BuildContext context) {
    double screenWidth = context.screenWidth;
    double height = context.screenHeight - 56.0 - MediaQuery.of(context).padding.top - 50;
    return OKToast(
      child: Scaffold(
        appBar: AppBar(
          title: Text("Cancel", style: Theme.of(context).textTheme.subtitle2),
          leading: IconButton(
            icon: Icon(Icons.arrow_back, color: Theme.of(context).primaryColor),
            onPressed: () {
              Navigator.pop(context);
            },
          ),
          centerTitle: false,
          backgroundColor: Theme.of(context).scaffoldBackgroundColor,
        ),
        body: SingleChildScrollView(
          child: Container(
            height: height,
            child: Column(
              children: [
                SizedBox(
                  width: screenWidth,
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(20, 20, 0, 20),
                    child: Text(
                      Strings.register_title,
                      style: Theme.of(context)
                          .textTheme
                          .headline4
                          .copyWith(fontWeight: FontWeight.bold),
                      textAlign: TextAlign.start,
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(30, 10, 30, 0),
                  child: HooTextField(
                    Icons.email,
                    Strings.email_address,
                    emailController,
                    (txt) => _onTextChange(),
                    isError: isEmailError,
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(30, 10, 30, 0),
                  child: HooTextField(
                    Icons.face,
                    Strings.login_account_hint,
                    accountController,
                    (txt) => _onTextChange(),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(30, 10, 30, 0),
                  child: HooTextField(
                    Icons.lock_open,
                    Strings.login_pwd_hint,
                    pwdController,
                    (txt) => _onTextChange(),
                    isPassWord: true,
                  ),
                ),
                Spacer(),
                Padding(
                    padding: const EdgeInsets.fromLTRB(30, 0, 30, 100),
                    child: HooButton(
                      isButtonEnable,
                      Strings.button_sign_up,
                      _onRegister,
                    )),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
