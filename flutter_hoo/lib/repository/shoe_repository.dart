import 'package:flutter_hoo/db/database.dart';
import 'package:flutter_hoo/db/shoe.dart';
import 'package:loading_more_list_library/loading_more_list_library.dart';

class ShoeRepository extends LoadingMoreBase<Shoe>{

  int _pageIndex = 1;
  bool _hasMore = true;

  @override
  bool get hasMore => _hasMore;

  @override
  Future<bool> refresh([bool notifyStateChanged = false]) {
    _pageIndex = 1;
    return super.refresh(notifyStateChanged);
  }

  @override
  Future<bool> loadData([bool isLoadMoreAction = false]) async {
    DBProvider provider = DBProvider.getInstance();
    int startPos = (_pageIndex - 1) * 20;
    int endPos = startPos + 20;
    List<Shoe> shoes = await provider.queryShoeByPos(startPos,endPos);
    if(_pageIndex == 1){
      clear();
    }
    _hasMore = shoes.length != 0;
    addAll(shoes);
    _pageIndex++;
    return true;
  }
}
