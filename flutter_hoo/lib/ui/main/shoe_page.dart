import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/common/utils/theme_utils.dart';
import 'package:flutter_hoo/db/shoe.dart';
import 'package:flutter_hoo/repository/shoe_repository.dart';
import 'package:flutter_hoo/ui/main/shoe/shoe_item.dart';
import 'package:flutter_hoo/widget/loading_builder.dart';
import 'package:flutter_hoo/widget/state_layout.dart';
import 'package:loading_more_list/loading_more_list.dart';

class ShoePage extends StatefulWidget {
  @override
  _ShoePageState createState() => _ShoePageState();
}

class _ShoePageState extends State<ShoePage> with TickerProviderStateMixin {
  ShoeRepository shoeRepository;
  StateType type = StateType.loading;

  @override
  void initState() {
    super.initState();
    shoeRepository = ShoeRepository();
  }

  @override
  void dispose() {
    shoeRepository.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: ThemeUtils.getBgGrayColor(context),
      child: RefreshIndicator(
        child: LoadingMoreList<Shoe>(ListConfig<Shoe>(
            itemBuilder: (context, item, pos) => ShoeItem(item),
            sourceList: shoeRepository,
            lastChildLayoutType: LastChildLayoutType.foot,
            gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
              maxCrossAxisExtent: 300.0,
              crossAxisSpacing: 3.0,
              mainAxisSpacing: 3.0,
            ),
            indicatorBuilder: loadingBuilder)),
        onRefresh: _refresh,
      ),
    );
  }

  Future<void> _refresh() async {
    await shoeRepository.refresh();
  }
}
