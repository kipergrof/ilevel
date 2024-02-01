package com.android.application;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class NetworkHandler extends Handler {
    public static final String NETWORK_KEY = "network_key";
    NetworkInterface networkInterface;

    public interface NetworkInterface {
        void TcpNetworkCallback(Bundle bundle);

        void TcpNetworkClosed(int i);

        void TcpNetworkConnected(int i);
    }

    public NetworkHandler(NetworkInterface networkInterface2) {
        this.networkInterface = networkInterface2;
    }

    public enum Messages {
        MESSAGES_NONE(0),
        MESSAGES_TCP_CONNECTED(1),
        MESSAGES_TCP_DATA(2),
        MESSAGES_TCP_CLOSED(3);
        
        private final int i;

        private Messages(int i2) {
            this.i = i2;
        }

        public int getInt() {
            return this.i;
        }
    }

    public void handleMessage(Message message) {
        Bundle bundle = message.getData();
        switch (Messages.values()[message.what]) {
            case MESSAGES_TCP_CONNECTED:
                this.networkInterface.TcpNetworkConnected(message.arg2);
                return;
            case MESSAGES_TCP_DATA:
                this.networkInterface.TcpNetworkCallback(bundle);
                return;
            case MESSAGES_TCP_CLOSED:
                this.networkInterface.TcpNetworkClosed(message.arg1);
                return;
            default:
                return;
        }
    }
}
