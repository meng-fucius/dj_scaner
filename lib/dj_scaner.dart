import 'dart:async';

import 'package:flutter/services.dart';

class DjScaner {
  static const String scanActionName =
      "com.android.server.scannerservice.asscaner";
  static const MethodChannel _channel = MethodChannel('dj_scaner');
  static const EventChannel _eventChannel =
      EventChannel('com.dj.pda.scaner/scandata');
  static StreamSubscription? _streamSubscription;

  static Future<StreamSubscription> addListener(
      void Function(dynamic data)? onData) async {
    await scanEnabled(true);
    if (_streamSubscription == null) {
      return _eventChannel.receiveBroadcastStream().listen(onData);
    } else {
      return _streamSubscription!;
    }
  }

  static void cancel() {
    if (_streamSubscription != null) {
      _streamSubscription!.cancel();
      _streamSubscription = null;
    }
    scanEnabled(false);
  }

  static Future init() async {
    print('初始化');

    ///默认关闭声音
    await soundPlay(false);

    ///默认关闭震动
    await viberate(false);

    ///默认开机自启动
    await bootStart(true);

    ///设置广播名称
    await broadcastName(scanActionName);

    ///默认关闭扫描
    await scanEnabled(false);
    print('初始化完成');
  }

  ///声音开关
  static Future soundPlay(bool onOpen) async {
    await _channel.invokeMethod('soundPlay', {
      'onOpen': onOpen,
    });
  }

  ///震动开关
  static Future viberate(bool onOpen) async {
    await _channel.invokeMethod('viberate', {
      'onOpen': onOpen,
    });
  }

  ///连续扫描开关
  static Future scanContinue(bool onOpen) async {
    await _channel.invokeMethod('scanContinue', {
      'onOpen': onOpen,
    });
  }

  ///开机自启动
  static Future bootStart(bool onOpen) async {
    await _channel.invokeMethod('bootStart', {
      'onOpen': onOpen,
    });
  }

  ///启动/禁用扫描头
  static Future scanEnabled(bool onOpen) async {
    await _channel.invokeMethod('scanEnabled', {
      'onOpen': onOpen,
    });
  }

  ///设置广播名称
  static Future broadcastName(String name) async {
    await _channel.invokeMethod('broadcastName', {
      'name': name,
    });
  }
}
