package kore.botssdk.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kore.botssdk.utils.NetworkUtility;

public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtility.isNetworkConnectionAvailable(context)) {
            if (BotSocketConnectionManager.getInstance().connection_state == BaseSocketConnectionManager.CONNECTION_STATE.DISCONNECTED)
                BotSocketConnectionManager.getInstance().checkConnectionAndRetry(context, false);
        }

    }
}
