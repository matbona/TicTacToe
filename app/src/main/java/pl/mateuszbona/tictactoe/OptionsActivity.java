package pl.mateuszbona.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.mateuszbona.tictactoe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mateuszbona.tictactoe.sound.Sound;
import pl.mateuszbona.tictactoe.sound.SoundType;

public class OptionsActivity extends AppCompatActivity {
    public static final String SHARED_PREFERENCES_FILE = "pl.mateuszbona.tictactoe.config";
    public static final String PREFERENCE_SOUND = "sound";
    public static final String PREFERENCE_BEGINNER = "beginner";
    public static final String PREFERENCE_COMPUTER_PLAYER = "computer";
    public static final String PREFERENCE_COMPUTER_PLAYER_STARTS = "computerStarts";
    public static final String PREFERENCE_LEVEL = "level";

    @BindView(R.id.chbPlaySound)
    CheckBox chkPlaySound;

    @BindView(R.id.btnCross)
    RadioButton btnCross;

    @BindView(R.id.btnCircle)
    RadioButton btnCircle;

    @BindView(R.id.chbComputerPlayer)
    CheckBox chbComputerPlayer;

    @BindView(R.id.chbComputerPlayerStarts)
    CheckBox chbComputerPlayerStarts;

    @BindView(R.id.btnEasy)
    RadioButton btnEasy;

    @BindView(R.id.btnMedium)
    RadioButton btnMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        readConfig();
    }

    private void readConfig() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        chkPlaySound.setChecked(sharedPreferences.getBoolean(PREFERENCE_SOUND, true));

        btnCross.setChecked(sharedPreferences.getBoolean(PREFERENCE_BEGINNER, true));
        btnCircle.setChecked(!btnCross.isChecked());

        chbComputerPlayer.setChecked(sharedPreferences.getBoolean(PREFERENCE_COMPUTER_PLAYER, false));
        chbComputerPlayerStarts.setChecked(sharedPreferences.getBoolean(PREFERENCE_COMPUTER_PLAYER_STARTS, false));

        btnEasy.setChecked(sharedPreferences.getBoolean(PREFERENCE_LEVEL, true));
        btnMedium.setChecked(!btnEasy.isChecked());
    }

    @OnClick(R.id.btnSave)
    public void save() {
        Sound.playSound(this, SoundType.POP);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putBoolean(PREFERENCE_SOUND, chkPlaySound.isChecked());
        sharedPreferencesEditor.putBoolean(PREFERENCE_BEGINNER, btnCross.isChecked());
        sharedPreferencesEditor.putBoolean(PREFERENCE_COMPUTER_PLAYER, chbComputerPlayer.isChecked());
        sharedPreferencesEditor.putBoolean(PREFERENCE_COMPUTER_PLAYER_STARTS, chbComputerPlayerStarts.isChecked());
        sharedPreferencesEditor.putBoolean(PREFERENCE_LEVEL, btnEasy.isChecked());

        sharedPreferencesEditor.commit();

        finish();
    }

    @OnClick(R.id.btnBack)
    public void back() {
        Sound.playSound(this, SoundType.POP);
        finish();
    }

}
