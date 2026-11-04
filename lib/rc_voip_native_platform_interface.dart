import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'rc_voip_native_method_channel.dart';

abstract class RcVoipNativePlatform extends PlatformInterface {
  /// Constructs a RcVoipNativePlatform.
  RcVoipNativePlatform() : super(token: _token);

  static final Object _token = Object();

  static RcVoipNativePlatform _instance = MethodChannelRcVoipNative();

  /// The default instance of [RcVoipNativePlatform] to use.
  ///
  /// Defaults to [MethodChannelRcVoipNative].
  static RcVoipNativePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [RcVoipNativePlatform] when
  /// they register themselves.
  static set instance(RcVoipNativePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
