package pl.mateuszbona.tictactoe.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.mateuszbona.tictactoe.R;

import static pl.mateuszbona.tictactoe.OptionsActivity.PREFERENCE_SOUND;
import static pl.mateuszbona.tictactoe.OptionsActivity.SHARED_PREFERENCES_FILE;

public class Sound {
    public static void playSound(Context context, SoundType soundType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean isSound = sharedPreferences.getBoolean(PREFERENCE_SOUND, true);

        if (isSound) {
            final MediaPlayer mediaPlayer;

            switch (soundType) {
                case START:
                    mediaPlayer = MediaPlayer.create(context, R.raw.start);
                    break;

                case POP:
                    mediaPlayer = MediaPlayer.create(context, R.raw.pop);
                    break;

                case WINNER:
                    mediaPlayer = MediaPlayer.create(context, R.raw.winner);
                    break;

                case ERROR:
                    mediaPlayer = MediaPlayer.create(context, R.raw.error);
                    break;

                case DRAW:
                    mediaPlayer = MediaPlayer.create(context, R.raw.draw);
                    break;

                default:
                    mediaPlayer = MediaPlayer.create(context, R.raw.pop);
                    break;
            }

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }

}
