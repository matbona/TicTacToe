package pl.mateuszbona.tictactoe.computerplayer;

import android.os.AsyncTask;

import static pl.mateuszbona.tictactoe.constants.CurrentGameConstants.WAIT_FOR_COMPUTER_PLAYER_MOVE;

public class ComputerPlayerAsyncTask extends AsyncTask<Void, Void, Void> {
    private final ComputerPlayerMakeMove computerPlayerMakeMove;

    public ComputerPlayerAsyncTask(ComputerPlayerMakeMove computerPlayerMakeMove) {
        this.computerPlayerMakeMove = computerPlayerMakeMove;
    }

    @Override
    protected void onPreExecute() {
        computerPlayerMakeMove.beforeComputerPlayerMakeMove();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(WAIT_FOR_COMPUTER_PLAYER_MOVE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        computerPlayerMakeMove.onComputerPlayerMakeMove();
    }

    public interface ComputerPlayerMakeMove {
        void beforeComputerPlayerMakeMove();

        void onComputerPlayerMakeMove();
    }
}
