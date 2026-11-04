import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'rc_voip_native_platform_interface.dart';

/// An implementation of [RcVoipNativePlatform] that uses method channels.
class MethodChannelRcVoipNative extends RcVoipNativePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('rc_voip_native');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>(
      'getPlatformVersion',
    );
    return version;
  }
}
