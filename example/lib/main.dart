import 'dart:async';

import 'package:dj_scaner/dj_scaner.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _code = '';
  bool _init = false;

  @override
  void initState() {
    super.initState();
  }

  Future<void> initPlatformState() async {
    try {
      await DjScaner.init();
      _init = true;
    } on PlatformException {
      print('init  false');
    }
    if (!mounted) return;
    setState(() {});
  }

  @override
  void dispose() {
    DjScaner.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              TextButton(
                onPressed: () async {
                  await initPlatformState();
                },
                child: const Text('初始化'),
              ),
              const SizedBox(
                height: 10,
              ),
              TextButton(
                onPressed: () async {
                  await DjScaner.addListener((data) {
                    _code = data;
                    setState(() {});
                  });
                },
                child: const Text('开始扫描'),
              ),
              const SizedBox(
                height: 10,
              ),
              TextButton(
                onPressed: () async {
                  DjScaner.cancel();
                },
                child: const Text('结束扫描'),
              ),
              Text(_code),
              const SizedBox(
                height: 10,
              ),
              Text(_init.toString()),
            ],
          ),
        ),
      ),
    );
  }
}
