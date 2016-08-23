package fr.inria.rsommerard.fougereapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.inria.rsommerard.fougere.Fougere;

public class MainActivity extends AppCompatActivity {

    private Fougere fougere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fougere = new Fougere(this);
        this.fougere.start();
    }

    @Override
    protected void onDestroy() {
        this.fougere.stop();
        super.onDestroy();
    }
}
