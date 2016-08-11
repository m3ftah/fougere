package fr.inria.rsommerard.fougere.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 10/08/16.
 */
public class Active extends Thread implements Runnable {

    private static final int SOCKET_TIMEOUT = 10000;

    private final InetAddress groupOwnerAddress;

    public Active(final InetAddress groupOwnerAddress) {
        this.groupOwnerAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.groupOwnerAddress, 54412),
                    SOCKET_TIMEOUT);
            Log.d(Fougere.TAG, "[Active] Socket opened with the groupOwner");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Active] Cannot open socket with the groupOwner");
        }
    }
}
