package fr.inria.rsommerard.fougere.wifidirect;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.inria.rsommerard.fougere.Fougere;
import fr.inria.rsommerard.fougere.data.Data;
import fr.inria.rsommerard.fougere.data.DataPool;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectData;
import fr.inria.rsommerard.fougere.data.wifidirect.WiFiDirectDataPool;

/**
 * Created by Romain on 10/08/16.
 */
public class Passive implements Runnable {

    private final WiFiDirectDataPool wiFiDirectDataPool;
    private final DataPool dataPool;
    private ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Passive(final DataPool dataPool, final WiFiDirectDataPool wiFiDirectDataPool) {
        this.wiFiDirectDataPool = wiFiDirectDataPool;
        this.dataPool = dataPool;
    }

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
            e.printStackTrace();
            Log.e(Fougere.TAG, "[Passive] KO");
        } finally {
            this.release();
        }
    }

    private void process() throws IOException, ClassNotFoundException {
        if ( ! Protocol.HELLO.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.HELLO + " not received");
            return;
        }

        this.send(Protocol.HELLO);

        this.send(Protocol.SEND);

        String json = this.receive();

        List<WiFiDirectData> dataReceived = WiFiDirectData.deGsonify(json);
        for (WiFiDirectData dt : dataReceived) {
            this.dataPool.insert(Data.reset(WiFiDirectData.toData(dt)));
        }

        this.send(Protocol.ACK);

        if ( ! Protocol.SEND.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.SEND + " not received");
            return;
        }

        List<WiFiDirectData> dataTemp = this.wiFiDirectDataPool.getAll();
        List<WiFiDirectData> dataToSend = new ArrayList<>();

        for (WiFiDirectData dt : dataTemp) {
            dataToSend.add(WiFiDirectData.reset(dt));
        }

        this.send(WiFiDirectData.gsonify(dataToSend));

        if ( ! Protocol.ACK.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.ACK + " not received");
            return;
        }

        for (WiFiDirectData dt : dataTemp) {
            int newSent = dt.getSent() + 1;

            if (newSent >= dt.getDisseminate()) {
                this.wiFiDirectDataPool.delete(dt);
                continue;
            }

            dt.setSent(newSent);

            this.wiFiDirectDataPool.update(dt);
        }

        Log.d(Fougere.TAG, "[Passive] Process done");

        this.send(Protocol.OUT);

        if ( ! Protocol.OUT.equals(this.receive())) {
            Log.e(Fougere.TAG, "[Passive] " + Protocol.OUT + " not received");
        }
    }

    private void send(final String content) throws IOException {
        Message msg = new Message(content);

        this.output.writeObject(msg);
        this.output.flush();

        Log.d(Fougere.TAG, "[Passive] Sent: " + content);
    }

    private String receive() throws IOException, ClassNotFoundException {
        Message received = (Message) this.input.readObject();

        String content = received.getContent();
        Log.d(Fougere.TAG, "[Passive] Received: " + content);

        return content;
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
