package com.android.application;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.android.application.NetworkHandler;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class NetworkThread {
    /* access modifiers changed from: private */
    public String TAG = NetworkThread.class.getSimpleName();
    /* access modifiers changed from: private */
    public BufferedInputStream bufferedInputStream;
    /* access modifiers changed from: private */
    public Bundle bundle;
    /* access modifiers changed from: private */
    public ByteArrayOutputStream byteArrayOutputStream;
    /* access modifiers changed from: private */
    public boolean closed = true;
    public ConnectThread connectThread;
    /* access modifiers changed from: private */
    public boolean connected = false;
    /* access modifiers changed from: private */
    public boolean connecting = false;
    /* access modifiers changed from: private */
    public byte[] dataIn;
    /* access modifiers changed from: private */
    public byte[] dataOut;
    /* access modifiers changed from: private */
    public InetSocketAddress inetSocketAddress;
    /* access modifiers changed from: private */
    public String ipAddress;
    /* access modifiers changed from: private */
    public Message message;
    /* access modifiers changed from: private */
    public NetworkHandler networkManager;
    /* access modifiers changed from: private */
    public OutputStream outputStream;
    /* access modifiers changed from: private */
    public int portNumber;
    public ReceiverThread receiverThread;
    public SendThread sendThread;
    /* access modifiers changed from: private */
    public Socket socket;

    public synchronized Socket getSocket() {
        return this.socket;
    }

    public synchronized void setSocket(Socket socket2) {
        if (socket2 != null) {
            this.socket = socket2;
        }
    }

    public synchronized boolean isConnected() {
        return this.connected;
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    public synchronized boolean isConnecting() {
        return this.connecting;
    }

    public void setConnected(boolean connected2) {
        this.connected = connected2;
        this.closed = !connected2;
    }

    public synchronized void startConnection(String ipAddress2, int portNumber2) {
        if (this.socket != null || this.connected || this.connecting) {
            Log.d(this.TAG, "already attempting");
        } else {
            this.ipAddress = ipAddress2;
            this.portNumber = portNumber2;
            this.connected = false;
            this.closed = true;
            this.connecting = true;
            this.connectThread = new ConnectThread();
            this.connectThread.start();
        }
    }

    public synchronized void startConnection(Socket socket2, boolean connected2) {
        boolean z = true;
        synchronized (this) {
            if (socket2 != null && connected2) {
                this.socket = socket2;
                this.connected = connected2;
                if (connected2) {
                    z = false;
                }
                this.closed = z;
                this.connecting = false;
                this.receiverThread = new ReceiverThread();
                this.receiverThread.start();
            } else if (this.socket == null || connected2 || this.connecting) {
                Log.d(this.TAG, "something bad");
            } else {
                this.connected = false;
                this.closed = true;
                this.connecting = true;
                this.connectThread = new ConnectThread();
                this.connectThread.start();
            }
        }
    }

    public synchronized void sendData(byte[] bytes) {
        if (!(this.socket == null || bytes == null)) {
            this.dataOut = bytes;
            this.sendThread = new SendThread();
            this.sendThread.start();
        }
    }

    public synchronized void stopConnection() {
        if (this.connected && !this.closed && this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException ioException) {
                Log.d(this.TAG, "IOException", ioException);
            }
            this.connected = false;
            this.closed = true;
            this.connecting = false;
            this.socket = null;
            this.receiverThread = null;
        }
        return;
    }

    public NetworkThread(NetworkHandler networkHandler, Socket socket2) {
        this.networkManager = networkHandler;
        if (socket2 != null) {
            this.socket = socket2;
            this.receiverThread = new ReceiverThread();
            this.receiverThread.start();
            return;
        }
        Log.d(this.TAG, "Something went wrong");
    }

    private class ConnectThread extends Thread {
        private Message message;

        private ConnectThread() {
        }

        public void run() {
            InetSocketAddress unused = NetworkThread.this.inetSocketAddress = new InetSocketAddress(NetworkThread.this.ipAddress, NetworkThread.this.portNumber);
            while (!NetworkThread.this.isConnected() && NetworkThread.this.isClosed()) {
                try {
                    Log.d(NetworkThread.this.TAG, "ConnectThread: Trying to Connect.");
                    Socket unused2 = NetworkThread.this.socket = new Socket();
                    NetworkThread.this.socket.connect(NetworkThread.this.inetSocketAddress, 500);
                    boolean unused3 = NetworkThread.this.connecting = false;
                    boolean unused4 = NetworkThread.this.connected = true;
                    boolean unused5 = NetworkThread.this.closed = false;
                    NetworkThread.this.receiverThread = new ReceiverThread();
                    NetworkThread.this.receiverThread.start();
                    this.message = NetworkThread.this.networkManager.obtainMessage();
                    this.message.what = NetworkHandler.Messages.MESSAGES_TCP_CONNECTED.getInt();
                    this.message.arg1 = (int) getId();
                    NetworkThread.this.networkManager.sendMessage(this.message);
                } catch (SocketTimeoutException e) {
                    Log.d(NetworkThread.this.TAG, ": ConnectThread: Not Connected Yet.");
                } catch (IOException ioException) {
                    Log.d(NetworkThread.this.TAG, ": IOException.. probably unreachable", ioException);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException interruptException) {
                        Log.d(NetworkThread.this.TAG, ": InterruptException", interruptException);
                    }
                    Socket unused6 = NetworkThread.this.socket = null;
                }
            }
            Log.d(NetworkThread.this.TAG, "ConnectThread: Dead..");
        }
    }

    public class ReceiverThread extends Thread {
        public ReceiverThread() {
        }

        public void run() {
            if (NetworkThread.this.getSocket() != null) {
                Bundle unused = NetworkThread.this.bundle = new Bundle();
                byte[] unused2 = NetworkThread.this.dataIn = new byte[1024];
                try {
                    BufferedInputStream unused3 = NetworkThread.this.bufferedInputStream = new BufferedInputStream(NetworkThread.this.socket.getInputStream());
                    ByteArrayOutputStream unused4 = NetworkThread.this.byteArrayOutputStream = new ByteArrayOutputStream();
                } catch (IOException ioException) {
                    Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": IOException", ioException);
                }
                while (NetworkThread.this.isConnected() && !NetworkThread.this.isClosed()) {
                    try {
                        NetworkThread.this.socket.setSoTimeout(500);
                        int count = NetworkThread.this.bufferedInputStream.read(NetworkThread.this.dataIn);
                        if (count == -1) {
                            Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": Receiver: Connection Lost.");
                            boolean unused5 = NetworkThread.this.connected = false;
                            boolean unused6 = NetworkThread.this.closed = true;
                            Message unused7 = NetworkThread.this.message = NetworkThread.this.networkManager.obtainMessage();
                            NetworkThread.this.message.what = NetworkHandler.Messages.MESSAGES_TCP_CLOSED.getInt();
                            NetworkThread.this.message.arg1 = (int) getId();
                            NetworkThread.this.message.setData(NetworkThread.this.bundle);
                            NetworkThread.this.networkManager.sendMessage(NetworkThread.this.message);
                        } else {
                            NetworkThread.this.byteArrayOutputStream.write(NetworkThread.this.dataIn, 0, count);
                            NetworkThread.this.bundle.putByteArray(NetworkHandler.NETWORK_KEY, NetworkThread.this.byteArrayOutputStream.toByteArray());
                            NetworkThread.this.byteArrayOutputStream.reset();
                            Message unused8 = NetworkThread.this.message = NetworkThread.this.networkManager.obtainMessage();
                            NetworkThread.this.message.what = NetworkHandler.Messages.MESSAGES_TCP_DATA.getInt();
                            NetworkThread.this.message.setData(NetworkThread.this.bundle);
                            NetworkThread.this.networkManager.sendMessage(NetworkThread.this.message);
                        }
                    } catch (SocketTimeoutException e) {
                        Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": Receiver: No Data Yet.");
                    } catch (Exception exception) {
                        Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": Exception", exception);
                    }
                }
                Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": Receive: Dead..");
                return;
            }
            Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": Receive: No Socket..");
        }
    }

    public class SendThread extends Thread {
        public SendThread() {
        }

        public void run() {
            if (NetworkThread.this.getSocket() == null || !NetworkThread.this.isConnected()) {
                Log.d(NetworkThread.this.TAG, "SendThread: connection unavailable");
                return;
            }
            Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": Send: Run.");
            byte[] data = NetworkThread.this.dataOut;
            try {
                OutputStream unused = NetworkThread.this.outputStream = NetworkThread.this.socket.getOutputStream();
                for (int i = 0; i < 1; i++) {
                    NetworkThread.this.outputStream.write(data);
                    NetworkThread.this.outputStream.flush();
                }
                NetworkThread.this.outputStream.flush();
            } catch (Exception ioException) {
                Log.d(NetworkThread.this.TAG, String.valueOf(getId()) + ": IOException", ioException);
            }
        }
    }
}
