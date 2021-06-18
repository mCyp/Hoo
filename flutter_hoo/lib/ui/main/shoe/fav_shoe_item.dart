import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/db/fav_shoe.dart';
import 'package:flutter_hoo/db/shoe.dart';
import 'package:flutter_hoo/ui/detail/shoe_detail_page.dart';
import 'package:flutter_hoo/widget/load_image.dart';

class FavShoeItem extends StatelessWidget {
  FavShoeItem(this._favShoe) {
    _shoe = _favShoe.shoe;
  }

  final FavShoe _favShoe;
  Shoe _shoe;

  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width;
    return SizedBox(
      width: screenWidth,
      height: screenWidth * 3 / 4,
      child: Padding(
        padding: EdgeInsets.only(left: 10, top: 10, right: 10),
        child: ClipRRect(
          borderRadius: BorderRadius.circular(4),
          child: Material(
            child: Ink(
              child: InkWell(
                onTap: () {
                  Navigator.of(context)
                      .push(PageRouteBuilder(pageBuilder: (ctx, start, end) {
                    return new FadeTransition(
                      opacity: start,
                      child: ShoeDetailPage(_shoe),
                    );
                  }));
                },
                child: Stack(
                  children: [
                    Positioned(
                      width: screenWidth,
                      height: screenWidth * 3 / 4,
                      child: Hero(
                        tag: _shoe.imageUrl,
                        child: LoadImage(
                          _shoe.imageUrl,
                          fit: BoxFit.cover,
                        ),
                      ),
                    ),
                    Positioned(
                      top: 10,
                      left: 16,
                      child: Text(
                        _shoe.name,
                        style: Theme.of(context)
                            .textTheme
                            .headline5
                            .copyWith(fontWeight: FontWeight.bold),
                      ),
                    ),
                    Positioned(
                      left: 10,
                      top: 40,
                      child: DecoratedBox(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.all(Radius.circular(4)),
                          border: new Border.all(width: 1, color: ThemeUtils.getOutProviderColor(context)),
                          shape: BoxShape.rectangle,
                        ),
                        child: Padding(
                          padding: EdgeInsets.symmetric(vertical: 4, horizontal: 6),
                          child: Text(
                            _shoe.price.toString(),
                            style: Theme.of(context).textTheme.subtitle1,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
