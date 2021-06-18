import 'package:flutter_hoo/common/constant/baseConstant.dart';
import 'package:flutter_hoo/common/utils/sp_util.dart';
import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/db/fav_shoe.dart';
import 'package:loading_more_list_library/loading_more_list_library.dart';

class FavShoeRepository extends LoadingMoreBase<FavShoe> {
  int _pageIndex = 1;
  bool _hasMore = true;
  int _userId = 0;

  @override
  bool get hasMore => _hasMore;

  @override
  Future<bool> refresh([bool notifyStateChanged = false]) {
    _pageIndex = 1;
    return super.refresh(notifyStateChanged);
  }

  @override
  Future<bool> loadData([bool isLoadMoreAction = false]) async {
    _userId = SpUtil.getInt(BaseConstant.user_id);
    DBProvider provider = DBProvider.getInstance();
    int startPos = (_pageIndex - 1) * 20;
    int endPos = startPos + 20;
    List<FavShoe> shoes = await provider.queryFavShoesByUserID(_userId, startPos, endPos);
    if (_pageIndex == 1) {
      clear();
    }
    _hasMore = shoes.length != 0;
    addAll(shoes);
    _pageIndex++;
    return true;
  }
}
