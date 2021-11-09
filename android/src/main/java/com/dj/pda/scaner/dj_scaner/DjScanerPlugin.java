package com.dj.pda.scaner.dj_scaner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * DjScanerPlugin
 */
public class DjScanerPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private EventChannel eventChannel;
    private Context context;
    private static final String SCANACTION = "com.android.server.scannerservice.asscaner";
    private static final String SCANCHANNEL = "com.dj.pda.scaner/scandata";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "dj_scaner");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), SCANCHANNEL);
        eventChannel.setStreamHandler(new EventChannel.StreamHandler() {
            private BroadcastReceiver createScanReceiver(final EventChannel.EventSink events) {
                return new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent.getAction().equals(SCANACTION)) {
                            String code = intent.getStringExtra("scannerdata");
                            System.out.println(code);
                            events.success(code);
                        }
                    }
                };
            }

            ;
            private BroadcastReceiver scanReceiver;

            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                System.out.println("注册广播");
                scanReceiver = createScanReceiver(events);
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(SCANACTION);
                intentFilter.setPriority(Integer.MAX_VALUE);
                context.registerReceiver(scanReceiver, intentFilter);
            }

            @Override
            public void onCancel(Object arguments) {
                System.out.println("注销广播");
                context.unregisterReceiver(scanReceiver);
            }
        });
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("soundPlay")) {
            boolean arg=(boolean) call.argument("onOpen");
            System.out.println("声音 "+(arg?"开启":"关闭"));
            Intent intent = new Intent("com.android.scanner.service_settings");
            intent.putExtra("sound_play",arg );
            context.sendBroadcast(intent);
            result.success(true);
        } else if (
                call.method.equals("viberate")
        ) {
            boolean arg=(boolean) call.argument("onOpen");
            System.out.println("震动"+(arg?"开启":"关闭"));
            Intent intent = new Intent("com.android.scanner.service_settings");
            intent.putExtra("viberate", arg);
            context.sendBroadcast(intent);
            result.success(true);
        } else if (
                call.method.equals("scanContinue")
        ) {
            boolean arg=(boolean) call.argument("onOpen");
            System.out.println("连续扫描"+(arg?"开启":"关闭"));
            Intent intent = new Intent("com.android.scanner.service_settings");
            intent.putExtra("scan_continue", arg);
            context.sendBroadcast(intent);
            result.success(true);
        } else if (
                call.method.equals("bootStart")
        ) {
            boolean arg=(boolean) call.argument("onOpen");
            System.out.println("开机自启动"+(arg?"开启":"关闭"));
            Intent intent = new Intent("com.android.scanner.service_settings");
            intent.putExtra("boot_start", arg);
            context.sendBroadcast(intent);
            result.success(true);
        } else if (
                call.method.equals("scanEnabled")
        ) {
            boolean arg=(boolean) call.argument("onOpen");
            System.out.println("扫描开关：");
            System.out.println(arg);
            Intent intent = new Intent("com.android.scanner.ENABLED");
            intent.putExtra("enabled",arg);
            context.sendBroadcast(intent);
            result.success(true);
        } else if (
                call.method.equals("broadcastName")
        ) {
            System.out.println("设置广播名称：");
            System.out.println((String) call.argument("name"));
            Intent intent = new Intent("com.android.scanner.service_settings");
            intent.putExtra("action_barcode_broadcast", (String) call.argument("name"));
            context.sendBroadcast(intent);
            result.success(true);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
