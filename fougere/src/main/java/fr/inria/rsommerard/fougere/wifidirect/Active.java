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

    private static final int SOCKET_TIMEOUT = 3000;

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

        this.socket = new Socket();

        while ( ! this.socket.isConnected()) {
            this.waitAMoment();
            this.connect();

            if (failCounter >= 3) {
                Log.e(Fougere.TAG, "[Active] Cannot open socket with the groupOwner after " +
                        this.failCounter + " attempts");
                return;
            }
        }

        try {
            this.initializeStreams();
            this.process();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(Fougere.TAG, "[Active] KO");
        } finally {
            this.release();
        }
    }

    private void waitAMoment() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void initializeStreams() throws IOException {
        this.input = new ObjectInputStream(this.socket.getInputStream());
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
    }

    private void process() throws IOException, ClassNotFoundException {
        Log.d(Fougere.TAG, "[Active] Socket opened with the groupOwner");

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

    private void connect() {
        try {
            this.socket.connect(new InetSocketAddress(this.groupOwnerAddress, 54412), SOCKET_TIMEOUT);
        } catch (IOException e) {
            this.failCounter++;
            Log.e(Fougere.TAG, "[Active] failCounter: " + this.failCounter);
        }
    }
}
