package pl.mateuszbona.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mateuszbona.tictactoe.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mateuszbona.tictactoe.sound.Sound;
import pl.mateuszbona.tictactoe.sound.SoundType;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnStartNewGame)
    public void startNewGame() {
        Sound.playSound(this, SoundType.POP);
        startActivity(new Intent(this, MainActivity.class));
    }

    @OnClick(R.id.btnOptions)
    public void options() {
        Sound.playSound(this, SoundType.POP);
        startActivity(new Intent(this, OptionsActivity.class));
    }

    @OnClick(R.id.btnAbout)
    public void about() {
        Sound.playSound(this, SoundType.POP);

        AlertDialog.Builder dlgAbout  = new AlertDialog.Builder(this);
        dlgAbout.setMessage("Tic Tac Toe\nver. 1.0\nAutor: Mateusz Bona");
        dlgAbout.setTitle("O programie...");
        dlgAbout.setPositiveButton("OK", null);
        dlgAbout.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dlgAbout.create().show();
    }

    @OnClick(R.id.btnExit)
    public void exit() {
        Sound.playSound(this, SoundType.POP);
        finish();
    }
}
