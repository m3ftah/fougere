package fr.inria.rsommerard.fougere.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import fr.inria.rsommerard.fougere.Fougere;

/**
 * Created by Romain on 10/08/16.
 */
public class Passive implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    @Override
    public void run() {
        Log.d(Fougere.TAG, "[Passive] Started");

        try {
            this.serverSocket = new ServerSocket(54412);
        } catch (IOException e) {
            Log.e(Fougere.TAG, "[Passive] Error on ServerSocket initialization");
        }

        if (this.serverSocket == null) {
            return;
        }

        try {
            this.socket = this.serverSocket.accept();
            // Warning: Order is important! First create output for the header!
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
            Log.d(Fougere.TAG, "[Passive] Socket OK");
            this.process();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(Fougere.TAG, "[Passive] KO");
        } finally {
            this.release();
        }
    }

    private void process() throws IOException, ClassNotFoundException {
        if ( ! Protocol.HELLO.equals(this.receive())) {
            return;
        }

        this.send(Protocol.HELLO);

        if ( ! Protocol.ACK.equals(this.receive())) {
            return;
        }

        Log.d(Fougere.TAG, "[Passive] Process done");
    }

    private void send(final String msg) throws IOException {
        this.output.writeObject(msg);
        this.output.flush();

        Log.d(Fougere.TAG, "[Passive] Sent: " + msg);
    }

    private String receive() throws IOException, ClassNotFoundException {
        String received = (String) this.input.readObject();

        Log.d(Fougere.TAG, "[Passive] Received: " + received);

        return received;
    }

    private void release() {
        Log.d(Fougere.TAG, "[Passive] Release resources");

        this.closeInputStream();
        this.closeOutputStream();
        this.closeSocket();
        this.closeServerSocket();
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

    private void closeServerSocket() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            // Nothing
        }
    }
}
