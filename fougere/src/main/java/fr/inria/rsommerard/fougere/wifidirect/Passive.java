package fr.inria.rsommerard.fougere.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 10/08/16.
 */
public class Passive extends Thread implements Runnable {

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(54412);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Fougere.TAG, "[ConnectionServer] Error on ServerSocket initialization");
        }

        while (this.checkThreadStatus()) {
            try {
                Socket socket = this.serverSocket.accept();
                Log.d(Fougere.TAG, "[ConnectionServer] Socket OK");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(Fougere.TAG, "[ConnectionServer] Socket KO");
            }
        }
    }

    private boolean checkThreadStatus() {
        if (Thread.currentThread().isInterrupted()) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return false;
        }

        return true;
    }
}
