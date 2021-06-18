import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_hoo/common/utils/image_utils.dart';

class LoadImage extends StatelessWidget {
  final String img;
  final double width;
  final double height;
  final BoxFit fit;
  final ImageFormat format;
  final String holderImage;
  final int cachedWidth;
  final int cachedHeight;

  LoadImage(this.img,
      {Key key,
      this.width,
      this.height,
      this.fit = BoxFit.cover,
      this.format = ImageFormat.png,
      this.holderImage = 'default',
      this.cachedWidth,
      this.cachedHeight})
      : assert(img != null, 'The [image] argument must not be null.'),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    if (img.isEmpty || img.startsWith('http')) {
      Widget _image = LoadAssetImage(
        holderImage,
        width: width,
        height: height,
        fit: fit,
        format: format,
      );
      return CachedNetworkImage(
        imageUrl: img,
        placeholder: (_, __) => _image,
        errorWidget: (_, __, dynamic error) => _image,
        width: width,
        height: height,
        fit: fit,
        memCacheWidth: cachedWidth,
        memCacheHeight: cachedHeight,
      );
    } else {
      return LoadAssetImage(
        img,
        width: width,
        height: height,
        fit: fit,
        cachedWidth: cachedWidth,
        cachedHeight: cachedHeight,
        format: format,
      );
    }
  }
}

class LoadAssetImage extends StatelessWidget {
  final String img;
  final double width;
  final double height;
  final BoxFit fit;
  final ImageFormat format;
  final int cachedWidth;
  final int cachedHeight;
  final Color color;

  LoadAssetImage(this.img,
      {Key key,
      this.width,
      this.height,
      this.fit,
      this.format = ImageFormat.png,
      this.cachedWidth,
      this.cachedHeight,
      this.color})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Image.asset(
      ImageUtils.getImgPath(img, format: format),
      height: height,
      width: width,
      cacheWidth: cachedWidth,
      cacheHeight: cachedWidth,
      fit: fit,
      color: color,
      excludeFromSemantics: true,
    );
  }
}
