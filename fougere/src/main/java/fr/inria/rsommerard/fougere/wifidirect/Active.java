package fr.inria.rsommerard.fougere.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.Data;

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
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Active] KO");
        } finally {
            this.release();
        }
    }

    private void process() throws IOException, ClassNotFoundException {
        this.send(Protocol.HELLO);

        if ( ! Protocol.HELLO.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.HELLO + " not received");
            return;
        }

        if ( ! Protocol.SEND.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.SEND + " not received");
            return;
        }

        ArrayList<Data> data = new ArrayList<>();
        data.add(new Data(UUID.randomUUID().toString(), "This is a data! #Lol"));

        this.send(Data.gsonify(data));

        if ( ! Protocol.ACK.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Active] " + Protocol.ACK + " not received");
            return;
        }

        this.send(Protocol.SEND);

        String json = receive();

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

    private void send(final String content) throws IOException {
        Message msg = new Message(content);

        this.output.writeObject(msg);
        this.output.flush();

        Log.d(Fougere.TAG, "[Active] Sent: " + content);
    }

    private String receive() throws IOException, ClassNotFoundException {
        Message received = (Message) this.input.readObject();

        String content = received.getContent();
        Log.d(Fougere.TAG, "[Active] Received: " + content);

        return content;
    }
}
