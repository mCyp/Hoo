import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/style/theme/strings.dart';

class HooTextField extends StatelessWidget {
  HooTextField(this.icon, this.hintText, this._controller, this.changedCallback,
      {this.errorText, this.isError = false, this.isPassWord = false});

  TextEditingController _controller;
  final IconData icon;
  final String hintText;
  final ValueChanged<String> changedCallback;
  String errorText;
  bool isError = false;
  bool isPassWord;
  double _borderWidth = 1;

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 75,
      width: double.infinity,
      child: TextField(
        controller: _controller,
        decoration: InputDecoration(
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            borderSide: BorderSide(
                width: _borderWidth, color: Theme.of(context).dividerColor),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            borderSide: BorderSide(
                width: _borderWidth, color: Theme.of(context).primaryColor),
          ),
          errorBorder: OutlineInputBorder(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            borderSide: BorderSide(
                width: _borderWidth, color: Theme.of(context).errorColor),
          ),
          focusedErrorBorder: OutlineInputBorder(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            borderSide: BorderSide(
                width: _borderWidth, color: Theme.of(context).errorColor),
          ),
          errorText: isError ? errorText : null,
          prefixIcon: Icon(icon),
          hintText: hintText,
          hintStyle: Theme.of(context).textTheme.bodyText2,
          contentPadding: EdgeInsets.symmetric(vertical: 25, horizontal: 20),
        ),
        onChanged: changedCallback,
        style: Theme.of(context).textTheme.bodyText1,
        obscureText: isPassWord,
      ),
    );
  }
}
