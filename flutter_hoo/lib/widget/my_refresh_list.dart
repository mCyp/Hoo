import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_hoo/style/res/gaps.dart';
import 'package:flutter_hoo/style/theme/strings.dart';
import 'package:flutter_hoo/widget/state_layout.dart';

class RefreshListView extends StatefulWidget {
  RefreshListView({@required this.onRefresh,
    @required this.itemBuilder,
    @required this.itemCount,
    this.onLoadMore,
    this.hasMore,
    this.stateType,
    this.pageSize,
    this.padding,
    this.itemExtent});

  final RefreshCallback onRefresh;
  final LoadMoreCallback onLoadMore;
  final int itemCount;
  final bool hasMore;
  final IndexedWidgetBuilder itemBuilder;
  final StateType stateType;
  final int pageSize;
  final EdgeInsetsGeometry padding;
  final double itemExtent;

  @override
  _RefreshListViewState createState() => _RefreshListViewState();
}

typedef RefreshCallback = Future<void> Function();
typedef LoadMoreCallback = Future<void> Function();

class _RefreshListViewState extends State<RefreshListView> {
  bool _loading = false;
  ScrollController _scrollController;

  @override
  void initState() {
    super.initState();

    _scrollController = new ScrollController();
    _scrollController.addListener(() {
      if(_scrollController.position.pixels == _scrollController.position.maxScrollExtent){
        _loadMore();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    bool isShow = widget.onLoadMore != null && _loading;
    Widget child = RefreshIndicator(
      onRefresh: widget.onRefresh,
      child: GridView.builder(
          itemCount: widget.itemCount,
          padding: widget.padding,
          gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: 2,
              childAspectRatio: 1,
              mainAxisSpacing: 8,
              crossAxisSpacing: 8),
          itemBuilder: (BuildContext context, int index) {
            return widget.itemBuilder(context, index);
          }),
    );
    return SafeArea(
      child: NotificationListener(
        onNotification: (ScrollNotification note) {
          if (note.metrics.pixels == note.metrics.maxScrollExtent &&
              note.metrics.axis == Axis.vertical) {
            _loadMore();
          }
          return true;
        },
        child: child,
      ),
    );
  }

  Future<void> _loadMore() async {
    if (widget.onLoadMore == null) return;
    if (_loading) return;
    if (!widget.hasMore) return;

    setState(() {
      _loading = true;
    });
    await widget.onLoadMore();
    setState(() {
      _loading = false;
    });
  }
}

class MoreWidget extends StatelessWidget {
  final bool hasMore;

  MoreWidget(this.hasMore);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 10.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          if (hasMore) const CupertinoActivityIndicator(),
          if (hasMore) Gaps.hGap4,
          Text(
            hasMore ? Strings.state_loading : Strings.state_empty,
            style: Theme
                .of(context)
                .textTheme
                .subtitle1,
          )
        ],
      ),
    );
  }
}
