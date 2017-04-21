package pl.mateuszbona.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.mateuszbona.tictactoe.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.mateuszbona.tictactoe.computerplayer.ComputerPlayerAsyncTask;
import pl.mateuszbona.tictactoe.game.GameEngine;
import pl.mateuszbona.tictactoe.game.ai.utils.AILevel;
import pl.mateuszbona.tictactoe.game.field.utils.FieldType;
import pl.mateuszbona.tictactoe.sound.Sound;
import pl.mateuszbona.tictactoe.sound.SoundType;

import static android.os.AsyncTask.Status.RUNNING;
import static pl.mateuszbona.tictactoe.OptionsActivity.PREFERENCE_BEGINNER;
import static pl.mateuszbona.tictactoe.OptionsActivity.PREFERENCE_COMPUTER_PLAYER;
import static pl.mateuszbona.tictactoe.OptionsActivity.PREFERENCE_COMPUTER_PLAYER_STARTS;
import static pl.mateuszbona.tictactoe.OptionsActivity.PREFERENCE_LEVEL;
import static pl.mateuszbona.tictactoe.OptionsActivity.SHARED_PREFERENCES_FILE;
import static pl.mateuszbona.tictactoe.constants.CurrentGameConstants.COMPUTER_PLAYER_STARTS;
import static pl.mateuszbona.tictactoe.constants.CurrentGameConstants.PLAY_WITH_COMPUTER;
import static pl.mateuszbona.tictactoe.constants.CurrentGameConstants.VIBRATION_TIME;
import static pl.mateuszbona.tictactoe.game.ai.utils.AILevel.EASY;
import static pl.mateuszbona.tictactoe.game.ai.utils.AILevel.MEDIUM;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.EMPTY_FIELD_SIGN;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.NO_EMPTY_FIELDS;
import static pl.mateuszbona.tictactoe.game.field.utils.FieldType.CIRCLE;
import static pl.mateuszbona.tictactoe.game.field.utils.FieldType.CROSS;
import static pl.mateuszbona.tictactoe.sound.Sound.playSound;
import static pl.mateuszbona.tictactoe.sound.SoundType.DRAW;
import static pl.mateuszbona.tictactoe.sound.SoundType.ERROR;
import static pl.mateuszbona.tictactoe.sound.SoundType.POP;
import static pl.mateuszbona.tictactoe.sound.SoundType.START;
import static pl.mateuszbona.tictactoe.sound.SoundType.WINNER;

public class MainActivity extends AppCompatActivity implements ComputerPlayerAsyncTask.ComputerPlayerMakeMove {

    @BindViews({R.id.btn00, R.id.btn01, R.id.btn02, R.id.btn10, R.id.btn11, R.id.btn12, R.id.btn20, R.id.btn21, R.id.btn22})
    public List<Button> gameBoard;

    @BindView(R.id.tvGameMessage)
    public TextView tvGameMessage;

    private int[][] winCases = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    private GameEngine game;
    private ComputerPlayerAsyncTask computerPlayerAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initializeGame();
        makeMoveIfComputerPlayerStarts();
        setGameMessage(getString(R.string.player_move, game.getBeginningPlayer()));
    }

    @OnClick({R.id.btn00, R.id.btn01, R.id.btn02, R.id.btn10, R.id.btn11, R.id.btn12, R.id.btn20, R.id.btn21, R.id.btn22})
    public void onFieldClick(Button field) {
        String tag = field.getTag().toString();

        if (!game.isGameRunning() || !game.isFieldStillInGame(tag)) {
            vibrate();
            playSound(this, ERROR);
        } else {
            field.setText(game.makeMove(tag));
            shouldComputerPlayerMakeMove();
            shouldContinueGame(false);
        }
    }

    @OnClick(R.id.btnNewGame)
    public void newGame() {
        cancelComputerPlayerMove();
        game.newGame();
        setGameMessage(getString(R.string.player_move, game.getBeginningPlayer()));
        clearGameBoard();
        setDefaultColorForGameBoard();
        makeMoveIfComputerPlayerStarts();
        playSound(this, START);
    }

    @OnClick(R.id.btnLeaveGame)
    public void leaveGame() {
        Sound.playSound(this, POP);
        finish();
    }

    @Override
    public void beforeComputerPlayerMakeMove() {
        game.pause();
    }

    @Override
    public void onComputerPlayerMakeMove() {
        int index = game.makeMoveForComputerPlayer();

        if (index != NO_EMPTY_FIELDS) {
            String tag = gameBoard.get(index).getTag().toString();

            gameBoard.get(index).setText(game.makeMove(tag));
            shouldContinueGame(true);
        }
    }

    private void initializeGame() {
//        if (PLAY_WITH_COMPUTER) {
//            game = new GameEngine(CROSS, MEDIUM, COMPUTER_PLAYER_STARTS);
//        } else {
//            game = new GameEngine(CROSS);
//        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        boolean playWithComputer = sharedPreferences.getBoolean(PREFERENCE_COMPUTER_PLAYER, false);


        if (playWithComputer) {
            boolean isComputerFirst = sharedPreferences.getBoolean(PREFERENCE_COMPUTER_PLAYER_STARTS, false);

            game = new GameEngine(getStartingSign(), getAILevel(), isComputerFirst);
        } else {
            game = new GameEngine(getStartingSign());
        }
    }

    private FieldType getStartingSign() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean startingSign = sharedPreferences.getBoolean(PREFERENCE_BEGINNER, true);

        return startingSign ? CROSS : CIRCLE;
    }

    private AILevel getAILevel() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        boolean aiLevel = sharedPreferences.getBoolean(PREFERENCE_LEVEL, true);

        return aiLevel ? EASY : MEDIUM;
    }

    private void makeMoveIfComputerPlayerStarts() {
        if (game.isComputerPlayerStarting()) {
            shouldComputerPlayerMakeMove();
        }
    }

    private void setGameMessage(String message) {
        tvGameMessage.setText(message);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VIBRATION_TIME);
    }

//    private void playSound(this, SoundType soundType) {
//        final MediaPlayer mediaPlayer;
//
//        switch (soundType) {
//            case START:
//                mediaPlayer = MediaPlayer.create(this, R.raw.start);
//                break;
//
//            case POP:
//                mediaPlayer = MediaPlayer.create(this, R.raw.pop);
//                break;
//
//            case WINNER:
//                mediaPlayer = MediaPlayer.create(this, R.raw.winner);
//                break;
//
//            case ERROR:
//                mediaPlayer = MediaPlayer.create(this, R.raw.error);
//                break;
//
//            case DRAW:
//                mediaPlayer = MediaPlayer.create(this, R.raw.draw);
//                break;
//
//            default:
//                mediaPlayer = MediaPlayer.create(this, R.raw.pop);
//                break;
//        }
//
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mediaPlayer.release();
//            }
//        });
//
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.start();
//            }
//        });
//    }

    private void cancelComputerPlayerMove() {
        if (computerPlayerAsyncTask != null && computerPlayerAsyncTask.getStatus() == RUNNING) {
            computerPlayerAsyncTask.cancel(true);
        }
    }

    private void clearGameBoard() {
        for (Button field : gameBoard) {
            field.setText(EMPTY_FIELD_SIGN);
        }
    }

    private void shouldComputerPlayerMakeMove() {
        if (game.isGameRunning() && game.isComputerPlayer()) {
            computerPlayerAsyncTask = new ComputerPlayerAsyncTask(this);
            computerPlayerAsyncTask.execute();
        }
    }

    private void shouldContinueGame(boolean resume) {
        if (game.isWinner()) {
            setGameMessage(getString(R.string.winner_message, game.getWinner()));
            changeColorForWinnersFields();
            playSound(this, WINNER);
        } else if (game.isDraw()) {
            setGameMessage(getString(R.string.draw_message));
            playSound(this, DRAW);
        } else {
            if (resume) {
                game.resume();
            }

            setGameMessage(getString(R.string.player_move, game.getActualPlayer()));
            playSound(this, POP);
        }
    }

    private void changeColorForWinnersFields() {
        int index = game.getWinCaseNumber();
        int[] fields = winCases[index];

        for (int field : fields) {
            gameBoard.get(field).setTextColor(ContextCompat.getColor(this, R.color.winner_color));
        }
    }

    private void setDefaultColorForGameBoard() {
        for (Button field : gameBoard) {
            field.setTextColor((ContextCompat.getColor(this, R.color.button_default)));
        }
    }
}
