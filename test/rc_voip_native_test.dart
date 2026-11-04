import 'package:flutter_test/flutter_test.dart';
import 'package:rc_voip_native/rc_voip_native.dart';
import 'package:rc_voip_native/rc_voip_native_platform_interface.dart';
import 'package:rc_voip_native/rc_voip_native_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockRcVoipNativePlatform
    with MockPlatformInterfaceMixin
    implements RcVoipNativePlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final RcVoipNativePlatform initialPlatform = RcVoipNativePlatform.instance;

  test('$MethodChannelRcVoipNative is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelRcVoipNative>());
  });

  test('getPlatformVersion', () async {
    RcVoipNative rcVoipNativePlugin = RcVoipNative();
    MockRcVoipNativePlatform fakePlatform = MockRcVoipNativePlatform();
    RcVoipNativePlatform.instance = fakePlatform;

    expect(await rcVoipNativePlugin.getPlatformVersion(), '42');
  });
}
