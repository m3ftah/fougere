package fr.inria.rsommerard.fougereapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
