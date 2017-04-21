package pl.mateuszbona.tictactoe.game;

import pl.mateuszbona.tictactoe.game.field.Field;
import pl.mateuszbona.tictactoe.game.field.utils.FieldType;

public interface Game {
    void newGame();

    void newGame(FieldType beginningPlayer);

    int getWinCaseNumber();

    int makeMoveForComputerPlayer();

    String makeMove(String tag);

    String getActualPlayer();

    String getPreviousPlayer();

    String getBeginningPlayer();

    String getWinner();

    void pause();

    void resume();

    boolean isGameRunning();

    boolean isWinner();

    boolean isDraw();

    boolean isComputerPlayer();

    boolean isComputerPlayerStarting();

    boolean isFieldStillInGame(String tag);

    boolean hasFreeFields();

    Field[][] getGameBoard();
}
