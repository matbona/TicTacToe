package pl.mateuszbona.tictactoe.game;

import pl.mateuszbona.tictactoe.game.ai.AI;
import pl.mateuszbona.tictactoe.game.ai.utils.AILevel;
import pl.mateuszbona.tictactoe.game.field.Field;
import pl.mateuszbona.tictactoe.game.field.utils.FieldCoordinates;
import pl.mateuszbona.tictactoe.game.field.utils.FieldType;

import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.CIRCLE_SIGN;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.CROSS_SIGN;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.DEFAULT_WIN_CASE;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.FIELD_SIDE_SIZE;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.SPLIT_SIGN;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_0;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_1;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_2;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_3;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_4;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_5;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_6;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.WIN_CASE_7;
import static pl.mateuszbona.tictactoe.game.field.utils.FieldType.CIRCLE;
import static pl.mateuszbona.tictactoe.game.field.utils.FieldType.CROSS;
import static pl.mateuszbona.tictactoe.game.field.utils.FieldType.EMPTY;

public class GameEngine implements Game {
    private FieldType beginningPlayer;
    private FieldType actualPlayer;
    private Field[][] gameBoard;
    private int winCaseNumber;
    private boolean isGameRunning;
    private boolean isWinner;
    private boolean isDraw;
    private boolean isComputerPlayer;
    private boolean isComputerPlayerStarting;
    private AI computerPlayer;

    public GameEngine(FieldType beginningPlayer) {
        newGame(beginningPlayer);
        this.isComputerPlayer = false;
        this.isDraw = false;
    }

    public GameEngine(FieldType beginningPlayer, AILevel level) {
        this(beginningPlayer);
        this.isComputerPlayer = true;
        this.computerPlayer = new AI(level);
    }

    public GameEngine(FieldType beginningPlayer, AILevel level, boolean isComputerPlayerStarting) {
        this(beginningPlayer, level);
        this.isComputerPlayerStarting = isComputerPlayerStarting;
    }

    @Override
    public void newGame() {
        newGame(beginningPlayer);
    }

    @Override
    public void newGame(FieldType beginningPlayer) {
        this.beginningPlayer = beginningPlayer;
        this.actualPlayer = beginningPlayer;
        this.isGameRunning = true;
        this.isWinner = false;
        this.winCaseNumber = DEFAULT_WIN_CASE;

        initializeGameBoard();
    }

    @Override
    public int getWinCaseNumber() {
        return winCaseNumber;
    }

    @Override
    public int makeMoveForComputerPlayer() {
        return computerPlayer.makeMoveAI(this);
    }

    @Override
    public String makeMove(String tag) {
        setFieldForActualPlayer(tag);
        findWinner();
        findDraw();
        changeActualPlayer();

        return getPreviousPlayer();
    }

    @Override
    public String getActualPlayer() {
        return actualPlayer == CROSS ? CROSS_SIGN : CIRCLE_SIGN;
    }

    @Override
    public String getPreviousPlayer() {
        return getActualPlayer().equals(CROSS_SIGN) ? CIRCLE_SIGN : CROSS_SIGN;
    }

    @Override
    public String getBeginningPlayer() {
        return beginningPlayer == CROSS ? CROSS_SIGN : CIRCLE_SIGN;
    }

    @Override
    public String getWinner() {
        return getPreviousPlayer();
    }

    @Override
    public void pause() {
        isGameRunning = false;
    }

    @Override
    public void resume() {
        isGameRunning = true;
    }

    @Override
    public boolean isGameRunning() {
        return isGameRunning;
    }

    @Override
    public boolean isWinner() {
        return isWinner;
    }

    @Override
    public boolean isDraw() {
        return isDraw;
    }

    @Override
    public boolean isComputerPlayer() {
        return isComputerPlayer;
    }

    @Override
    public boolean isComputerPlayerStarting() {
        return isComputerPlayerStarting;
    }

    @Override
    public boolean isFieldStillInGame(String tag) {
        FieldCoordinates fieldCoordinates = convertTagToFieldCoordinates(tag);
        int row = fieldCoordinates.getRow();
        int column = fieldCoordinates.getColumn();
        FieldType fieldToCheck = gameBoard[row][column].getState();

        return fieldToCheck == EMPTY;
    }

    @Override
    public boolean hasFreeFields() {
        boolean result = false;

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[i][j].getState() == EMPTY) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public Field[][] getGameBoard() {
        return gameBoard;
    }

    private void initializeGameBoard() {
        gameBoard = new Field[FIELD_SIDE_SIZE][FIELD_SIDE_SIZE];

        for (int i = 0; i < FIELD_SIDE_SIZE; i++) {
            for (int j = 0; j < FIELD_SIDE_SIZE; j++) {
                gameBoard[i][j] = new Field(EMPTY);
            }
        }
    }

    private void setFieldForActualPlayer(String tag) {
        FieldCoordinates fieldCoordinates = convertTagToFieldCoordinates(tag);
        int row = fieldCoordinates.getRow();
        int column = fieldCoordinates.getColumn();

        gameBoard[row][column].setState(actualPlayer);
    }

    private void changeActualPlayer() {
        actualPlayer = actualPlayer == CROSS ? CIRCLE : CROSS;
    }

    private FieldCoordinates convertTagToFieldCoordinates(String tag) {
        String[] tmp = tag.split(SPLIT_SIGN);
        int row = Integer.valueOf(tmp[0]);
        int column = Integer.valueOf(tmp[1]);

        return new FieldCoordinates(row, column);
    }

    private void findDraw() {
        isDraw = !isWinner() && !hasFreeFields();

        if (isDraw) {
            pause();
        }
    }

    private void findWinner() {
        if (isWinnerInRows() || isWinnerInColumns() || isWinnerInDiagonals()) {
            pause();
            isWinner = true;
        }
    }

    private boolean isWinnerInRows() {
        if (gameBoard[0][0].getState() == actualPlayer
                && gameBoard[0][1].getState() == actualPlayer
                && gameBoard[0][2].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_0);
            return true;
        }

        if (gameBoard[1][0].getState() == actualPlayer
                && gameBoard[1][1].getState() == actualPlayer
                && gameBoard[1][2].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_1);
            return true;
        }

        if (gameBoard[2][0].getState() == actualPlayer
                && gameBoard[2][1].getState() == actualPlayer
                && gameBoard[2][2].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_2);
            return true;
        }

        return false;
    }

    private boolean isWinnerInColumns() {
        if (gameBoard[0][0].getState() == actualPlayer
                && gameBoard[1][0].getState() == actualPlayer
                && gameBoard[2][0].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_3);
            return true;
        }

        if (gameBoard[0][1].getState() == actualPlayer
                && gameBoard[1][1].getState() == actualPlayer
                && gameBoard[2][1].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_4);
            return true;
        }

        if (gameBoard[0][2].getState() == actualPlayer
                && gameBoard[1][2].getState() == actualPlayer
                && gameBoard[2][2].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_5);
            return true;
        }

        return false;
    }

    private boolean isWinnerInDiagonals() {
        if (gameBoard[0][0].getState() == actualPlayer
                && gameBoard[1][1].getState() == actualPlayer
                && gameBoard[2][2].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_6);
            return true;
        }

        if (gameBoard[0][2].getState() == actualPlayer
                && gameBoard[1][1].getState() == actualPlayer
                && gameBoard[2][0].getState() == actualPlayer) {

            setWinCaseNumber(WIN_CASE_7);
            return true;
        }

        return false;
    }

    private void setWinCaseNumber(int winCaseNumber) {
        this.winCaseNumber = winCaseNumber;
    }
}
