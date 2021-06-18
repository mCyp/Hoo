import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/constant/baseConstant.dart';
import 'package:flutter_hoo/common/utils/sp_util.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/db/fav_shoe.dart';
import 'package:flutter_hoo/db/shoe.dart';
import 'package:flutter_hoo/widget/load_image.dart';
import 'dart:math' as math;

import 'package:lottie/lottie.dart';

class ShoeDetailPage extends StatefulWidget {
  final Shoe _shoe;

  const ShoeDetailPage(this._shoe, {Key key}) : super(key: key);

  @override
  _ShoeDetailPageState createState() => _ShoeDetailPageState(_shoe);
}

class _ShoeDetailPageState extends State<ShoeDetailPage>
    with TickerProviderStateMixin {
  final Shoe _shoe;
  int _userId;
  FavShoe _favShoe;
  bool _showBackAndFavour = true;
  AnimationController _animationController;

  _ShoeDetailPageState(this._shoe);

  @override
  void initState() {
    super.initState();

    _animationController = AnimationController(
        vsync: this, duration: const Duration(milliseconds: 700));
    queryShoe();
  }

  void queryShoe() async {
    _userId = SpUtil.getInt(BaseConstant.user_id);
    DBProvider provider = DBProvider.getInstance();
    FavShoe favShoe = await provider.queryFavShoeByUserID(_userId, _shoe.id);
    setState(() {
      _favShoe = favShoe;
      if (_favShoe == null) {
        _animationController.value = 0;
      } else {
        _animationController.value = 1;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    double screenHeight = MediaQuery.of(context).size.height;
    double screenWidth = MediaQuery.of(context).size.width;

    return Container(
      color: Colors.black,
      width: screenWidth,
      height: screenHeight,
      child: Stack(
        children: [
          Positioned(
            top: 0,
            left: 0,
            child: SizedBox(
              width: screenWidth,
              height: screenHeight * 0.5 + 20,
              child: Hero(
                  tag: _shoe.imageUrl,
                  child: LoadImage(_shoe == null || _shoe.imageUrl == null ? "" : _shoe.imageUrl)),
            ),
          ),
          Positioned(
            top: 32,
            left: 16,
            child: _buildBackIcon(),
          ),
          Positioned(
            top: screenHeight * 0.5,
            child: _buildNameBrandPriceText(screenWidth, screenHeight)
          ),
          Positioned(
            top: screenHeight * 0.7,
            child: _buildDescText(screenWidth, screenHeight)
          ),
          Positioned(
            top: screenHeight * 0.5 - 26,
            right: 32,
            child: _buildVisible(_buildFavButton(), _showBackAndFavour,),
          ),
          Positioned(
            top: screenHeight * 0.5 - 26 - 171,
            right: 24,
            child: _buildLottieAnimation(),
          )
        ],
      ),
    );
  }

  Widget _buildBackIcon(){
    return _buildVisible(
        Material(
          child: IconButton(
            icon: Icon(
              Icons.arrow_back,
              size: 32,
              color: Theme.of(context).primaryColor,
            ),
            padding: EdgeInsets.all(10),
            splashColor: Colors.transparent,
            highlightColor: Colors.transparent,
            onPressed: (){
              setState(() {
                _showBackAndFavour = false;
              });
              Navigator.of(context).pop();
            },
          ),
        ),
        _showBackAndFavour);
  }

  // 名称 / 品牌 / 价格介绍
  Widget _buildNameBrandPriceText(double screenWidth, double screenHeight){
    return Container(
      decoration: BoxDecoration(
        color: Colors.black,
        borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20)),
      ),
      width: screenWidth,
      height: screenHeight * 0.2,
      child: Flex(
        direction: Axis.horizontal,
        children: [
          Expanded(
            child: Text(
              _shoe != null ? _shoe.name : "--",
              style: Theme.of(context).textTheme.subtitle1.copyWith(
                  fontSize: 18,
                  color: ThemeUtils.getTextMainWhiteColor(context)
              ),
              textAlign: TextAlign.center,
            ),
          ),
          Expanded(
            child: Text(
              _shoe != null ? _shoe.brand : "--",
              style: Theme.of(context)
                  .textTheme
                  .subtitle2
                  .copyWith(fontSize: 16, color: ThemeUtils.getTextSecondWhiteColor(context)),
              textAlign: TextAlign.center,
            ),
          ),
          Expanded(
            child: Text(
              _shoe != null ? _shoe.price.toString() : "--",
              style: Theme.of(context)
                  .textTheme
                  .subtitle2
                  .copyWith(fontSize: 16, color: ThemeUtils.getTextSecondWhiteColor(context)),
              textAlign: TextAlign.center,
            ),
          ),
        ],
      ),
    );
  }

  // 点赞按钮
  Widget _buildFavButton() {
    return SizedBox(
      width: 52,
      height: 52,
      child: AnimatedBuilder(
        animation: _animationController,
        builder: (ctx, child) {
          return _FloatingAnimationButton(
            _animationController,
            () {
              _onFavouriteClick();
            },
          );
        },
      ),
    );
  }

  Widget _buildDescText(double screenWidth, double screenHeight){
    return SizedBox(
      width: screenWidth,
      height: screenHeight * 0.2,
      child: Padding(
        padding: EdgeInsets.all(20),
        child: Text(
          _shoe != null ? _shoe.description.toString() : " ",
          style: Theme.of(context)
              .textTheme
              .bodyText1
              .copyWith(fontSize: 14, color: ThemeUtils.getTextSecondWhiteColor(context)),
        ),
      ),
    );
  }

  Widget _buildLottieAnimation(){
    return Lottie.asset(
      'assets/lottie/favour.json',
      controller: _animationController,
      width: 72,
      height: 171,
    );
  }

  Widget _buildVisible(Widget child, bool isVisible) {
    return Visibility(
      child: child,
      visible: isVisible,
    );
  }

  void _onFavouriteClick() async {
    try {
      DBProvider dbProvider = DBProvider.getInstance();
      if (_favShoe == null) {
        FavShoe favShoe =
            new FavShoe(_userId, _shoe.id, DateTime.now().millisecond);
        print("create FavShoe" + favShoe.shoeId.toString());
        await dbProvider.insertFavShoe(favShoe);
        _favShoe = favShoe;
        await _animationController.forward().orCancel;
      } else {
        await dbProvider.deleteFavShoe(_favShoe);
        _favShoe = null;
        await _animationController.reverse().orCancel;
      }
    } on TickerCanceled {}
  }

  @override
  void dispose() {
    super.dispose();
    _animationController.dispose();
  }
}

// ignore: must_be_immutable
class _FloatingAnimationButton extends StatelessWidget {
  final Animation<double> controller;
  final VoidCallback callback;

  Animation<Color> colorAnimation;
  Animation<double> rotationAnimation;

  _FloatingAnimationButton(this.controller, this.callback, {Key key})
      : super(key: key) {
    colorAnimation = ColorTween(begin: Colors.pink[500], end: Colors.grey[500])
        .animate(CurvedAnimation(parent: controller, curve: Curves.easeIn));
    rotationAnimation = Tween<double>(begin: 0.0, end: 2 * math.pi)
        .animate(CurvedAnimation(parent: controller, curve: Curves.ease));
  }

  Widget _buildAnimation(BuildContext context, Widget child) {
    return Transform.rotate(
      angle: rotationAnimation.value,
      child: FloatingActionButton(
        backgroundColor: colorAnimation.value,
        onPressed: callback,
        child: Icon(
          Icons.favorite,
          size: 24,
          color: Colors.white,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(
      animation: controller,
      builder: _buildAnimation,
    );
  }
}
