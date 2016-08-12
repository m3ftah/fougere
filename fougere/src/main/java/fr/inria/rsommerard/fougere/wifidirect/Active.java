package fr.inria.rsommerard.fougere.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 10/08/16.
 */
public class Active implements Runnable {

    private static final int SOCKET_TIMEOUT = 7000;
    private static final int NB_ATTEMPTS = 5;

    private final InetAddress groupOwnerAddress;
    private Socket socket;
    private int failCounter;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Active(final InetAddress groupOwnerAddress) {
        this.groupOwnerAddress = groupOwnerAddress;
    }

    @Override
    public void run() {
        Log.d(Fougere.TAG, "[Active] Started");

        while ( this.socket == null || ! this.socket.isConnected()) {
            // Warning: This is important to recreate a fresh object after a connect attempt!
            this.socket = new Socket();

            try {
                this.socket.connect(new InetSocketAddress(this.groupOwnerAddress, 54412), SOCKET_TIMEOUT);
                // Warning: Order is important! First create output for the header!
                this.output = new ObjectOutputStream(this.socket.getOutputStream());
                this.input = new ObjectInputStream(this.socket.getInputStream());
            } catch (IOException e) {
                this.failCounter++;
                Log.e(Fougere.TAG, "[Active] failCounter: " + this.failCounter);
            }

            if (this.failCounter >= NB_ATTEMPTS) {
                Log.e(Fougere.TAG, "[Active] Cannot open socket with the groupOwner after " +
                        this.failCounter + " attempts");
                return;
            }
        }

        Log.d(Fougere.TAG, "[Active] OK");

        try {
            this.process();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(Fougere.TAG, "[Active] KO");
        } finally {
            this.release();
        }
    }

    private void process() throws IOException, ClassNotFoundException {
        this.send(Protocol.HELLO);

        if ( ! Protocol.ACK.equals(this.receive())) {
            return;
        }

        if ( ! Protocol.HELLO.equals(this.receive())) {
            return;
        }

        this.send(Protocol.ACK);

        Log.d(Fougere.TAG, "[Active] Process done");
    }

    private void release() {
        Log.d(Fougere.TAG, "[Active] Release resources");

        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
    }

    private void closeInputStream() {
        try {
            this.input.close();
        } catch (IOException e) {
            // Nothing
        }
    }

    private void closeOutputStream() {
        try {
            this.output.close();
        } catch (IOException e) {
            // Nothing
        }
    }

    private void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException e) {
            // Nothing
        }
    }

    private void send(final String msg) throws IOException, ClassNotFoundException {
        this.output.writeObject(msg);
        this.output.flush();

        Log.d(Fougere.TAG, "[Active] Sent: " + msg);
    }

    private String receive() throws IOException, ClassNotFoundException {
        String received = (String) this.input.readObject();

        Log.d(Fougere.TAG, "[Active] Received: " + received);

        return received;
    }
}
