# dj_scaner

东集PDA手持扫描设备flutter端插件

## 初始化
```dart
DjScaner.init();
```
初始化内容：声音关闭，震动关闭，开机自启动，扫描头关闭

##监听扫描内容
```dart
await DjScaner.addListener((data) {
                    _code = data;
                    setState(() {});
                  });
```
##结束监听
```dart
    DjScaner.cancel();
```
