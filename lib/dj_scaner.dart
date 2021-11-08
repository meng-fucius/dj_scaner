
import 'dart:async';

import 'package:flutter/services.dart';

class DjScaner {
  static const MethodChannel _channel = MethodChannel('dj_scaner');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
