
import 'rc_voip_native_platform_interface.dart';

class RcVoipNative {
  Future<String?> getPlatformVersion() {
    return RcVoipNativePlatform.instance.getPlatformVersion();
  }
}
