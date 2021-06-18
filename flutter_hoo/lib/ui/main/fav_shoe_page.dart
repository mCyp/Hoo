import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/db/fav_shoe.dart';
import 'package:flutter_hoo/repository/fav_shoe_repository.dart';
import 'package:flutter_hoo/ui/main/shoe/fav_shoe_item.dart';
import 'package:flutter_hoo/widget/loading_builder.dart';
import 'package:flutter_hoo/widget/state_layout.dart';
import 'package:loading_more_list/loading_more_list.dart';

class FavShoePage extends StatefulWidget {
  @override
  _FavShoePageState createState() => _FavShoePageState();
}

class _FavShoePageState extends State<FavShoePage>
    with TickerProviderStateMixin {
  FavShoeRepository favShoeRepository;
  StateType type = StateType.loading;

  @override
  void initState() {
    super.initState();
    favShoeRepository = FavShoeRepository();
  }

  @override
  void dispose() {
    favShoeRepository.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: ThemeUtils.getBgGrayColor(context),
      child: RefreshIndicator(
        child: LoadingMoreList<FavShoe>(ListConfig<FavShoe>(
            itemBuilder: (context, item, pos) => FavShoeItem(item),
            sourceList: favShoeRepository,
            lastChildLayoutType: LastChildLayoutType.foot,
            indicatorBuilder: loadingBuilder)),
        onRefresh: _refresh,
      ),
    );
  }

  Future<void> _refresh() async {
    await favShoeRepository.refresh();
  }
}
