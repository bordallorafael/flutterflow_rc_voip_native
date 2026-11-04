package br.com.rccondominio.rc_voip_native

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.hiennv.flutter_callkit_incoming.CallkitConstants
import com.hiennv.flutter_callkit_incoming.CallkitIncomingBroadcastReceiver
import com.hiennv.flutter_callkit_incoming.CallkitNotificationService
import com.hiennv.flutter_callkit_incoming.Data
import com.hiennv.flutter_callkit_incoming.getDataActiveCalls
import java.util.Locale

class RcVoipFirebaseReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "RC_VOIP_ANDROID"
        private val UUID_PATTERN = Regex(
            "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$"
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras ?: return

        if (extras.getString("type") != "voip_call_ended") {
            return
        }

        val callId = (extras.getString("call_id") ?: extras.getString("id"))
            ?.trim()
            ?.lowercase(Locale.ROOT)
            .orEmpty()

        if (!UUID_PATTERN.matches(callId)) {
            Log.w(TAG, "Cancelamento nativo ignorado: call_id invalido.")
            return
        }

        Log.i(
            TAG,
            "Cancelamento nativo recebido; id=$callId; " +
                "reason=${extras.getString("reason") ?: "-"}"
        )

        cleanupCall(context.applicationContext, callId)
    }

    private fun cleanupCall(context: Context, callId: String) {
        try {
            val calls = getDataActiveCalls(context)

            if (calls.any { !it.id.equals(callId, ignoreCase = true) }) {
                Log.w(
                    TAG,
                    "Cancelamento nativo adiado: outra chamada presente; id=$callId"
                )
                return
            }

            val current = calls.firstOrNull {
                it.id.equals(callId, ignoreCase = true)
            }
            val data = current ?: Data(mapOf("id" to callId))
            val bundle = data.toBundle()

            val endIntent = CallkitIncomingBroadcastReceiver.getIntentEnded(
                context,
                bundle
            )
            CallkitIncomingBroadcastReceiver().onReceive(context, endIntent)
            CallkitNotificationService.stopService(context)

            val notifications = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as android.app.NotificationManager

            notifications.cancel(callId.hashCode())
            notifications.cancel(
                (
                    bundle.getString(CallkitConstants.EXTRA_CALLKIT_CALLING_ID)
                        ?: callId
                    ).hashCode()
            )

            Log.i(
                TAG,
                "Cancelamento nativo processado; id=$callId; " +
                    "estavaNaLista=${current != null}"
            )
        } catch (error: Exception) {
            Log.e(
                TAG,
                "Falha no cancelamento nativo; id=$callId; " +
                    "tipo=${error.javaClass.simpleName}"
            )
        }
    }
}
