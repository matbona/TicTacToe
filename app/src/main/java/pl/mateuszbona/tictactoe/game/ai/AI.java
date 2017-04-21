package pl.mateuszbona.tictactoe.game.ai;

import java.util.Random;

import pl.mateuszbona.tictactoe.game.GameEngine;
import pl.mateuszbona.tictactoe.game.ai.utils.AILevel;
import pl.mateuszbona.tictactoe.game.field.Field;
import pl.mateuszbona.tictactoe.game.field.utils.FieldType;

import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.CANNOT_WIN_THE_GAME;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.CROSS_SIGN;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.FIELD_SIDE_SIZE;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.NO_EMPTY_FIELDS;
import static pl.mateuszbona.tictactoe.game.constants.GameEngineConstants.NO_POTENTIAL_WINNER_FOUND;
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

public class AI {
    private AILevel level;

    public AI(AILevel level) {
        this.level = level;
    }

    public int makeMoveAI(GameEngine game) {
        switch (level) {
            case EASY:
                return makeMoveAILevelEasy(game);

            case MEDIUM:
                return makeMoveAILevelMedium(game);

            case HARD:
                return makeMoveAILevelHard(game);

            default:
                return NO_EMPTY_FIELDS;
        }
    }

    private int makeMoveAILevelEasy(GameEngine game) {
        int row;
        int column;
        Field[][] gameBoard = game.getGameBoard();

        while (true) {
            if (game.hasFreeFields()) {
                row = new Random().nextInt(gameBoard.length);
                column = new Random().nextInt(gameBoard.length);

                if (gameBoard[row][column].getState() == EMPTY) {
                    return getIndexFromGameBoard(row, column);
                }
            } else {
                return NO_EMPTY_FIELDS;
            }
        }
    }

    private int makeMoveAILevelMedium(GameEngine game) {
        int result = checkIfComputerPlayerCanWin(game);
        if (result != CANNOT_WIN_THE_GAME) {
            return result;
        }

        result = checkIfPlayerCanWin(game);
        if (result != CANNOT_WIN_THE_GAME) {
            return result;
        }

        if (checkIfComputerPlayerCanPutSignInMiddleField(game)) {
            return getIndexFromGameBoard(1, 1);
        }

        return makeMoveAILevelEasy(game);
    }

    private int makeMoveAILevelHard(GameEngine game) {
        // TODO: Implement method at hard level of AI
        return 0;
    }

    private int getIndexFromGameBoard(int row, int column) {
        return FIELD_SIDE_SIZE * row + column;
    }

    private int checkIfPlayerCanWin(GameEngine game) {
        FieldType playerSign = game.getPreviousPlayer() == CROSS_SIGN ? CROSS : CIRCLE;

        int result = findPotentialWinnerInRows(game, playerSign);
        if (result != NO_POTENTIAL_WINNER_FOUND) {
            return result;
        }

        result = findPotentialWinnerInColumns(game, playerSign);
        if (result != NO_POTENTIAL_WINNER_FOUND) {
            return result;
        }

        result = findPotentialWinnerInDiagonals(game, playerSign);
        if (result != NO_POTENTIAL_WINNER_FOUND) {
            return result;
        }

        return CANNOT_WIN_THE_GAME;
    }

    private int checkIfComputerPlayerCanWin(GameEngine game) {
        FieldType computerPlayerSign = game.getActualPlayer() == CROSS_SIGN ? CROSS : CIRCLE;

        int result = findPotentialWinnerInRows(game, computerPlayerSign);
        if (result != NO_POTENTIAL_WINNER_FOUND) {
            return result;
        }

        result = findPotentialWinnerInColumns(game, computerPlayerSign);
        if (result != NO_POTENTIAL_WINNER_FOUND) {
            return result;
        }

        result = findPotentialWinnerInDiagonals(game, computerPlayerSign);
        if (result != NO_POTENTIAL_WINNER_FOUND) {
            return result;
        }

        return CANNOT_WIN_THE_GAME;
    }

    private boolean checkIfComputerPlayerCanPutSignInMiddleField(GameEngine game) {
        return game.isFieldStillInGame("1;1");
    }

    private int findPotentialWinnerInRows(GameEngine game, FieldType player) {
        Field[][] gameBoard = game.getGameBoard();

        if (gameBoard[0][0].getState() == EMPTY
                && gameBoard[0][1].getState() == player
                && gameBoard[0][2].getState() == player) {
            
            return 0;
        }

        if (gameBoard[0][0].getState() == player
                && gameBoard[0][1].getState() == EMPTY
                && gameBoard[0][2].getState() == player) {

            return 1;
        }

        if (gameBoard[0][0].getState() == player
                && gameBoard[0][1].getState() == player
                && gameBoard[0][2].getState() == EMPTY) {

            return 2;
        }
        
        
        
        
        
        
        if (gameBoard[1][0].getState() == EMPTY
                && gameBoard[1][1].getState() == player
                && gameBoard[1][2].getState() == player) {
            
            return 3;
        }

        if (gameBoard[1][0].getState() == player
                && gameBoard[1][1].getState() == EMPTY
                && gameBoard[1][2].getState() == player) {

            return 4;
        }

        if (gameBoard[1][0].getState() == player
                && gameBoard[1][1].getState() == player
                && gameBoard[1][2].getState() == EMPTY) {

            return 5;
        }

        
        
        
        

        if (gameBoard[2][0].getState() == EMPTY
                && gameBoard[2][1].getState() == player
                && gameBoard[2][2].getState() == player) {
            
            return 6;
        }

        if (gameBoard[2][0].getState() == player
                && gameBoard[2][1].getState() == EMPTY
                && gameBoard[2][2].getState() == player) {

            return 7;
        }

        if (gameBoard[2][0].getState() == player
                && gameBoard[2][1].getState() == player
                && gameBoard[2][2].getState() == EMPTY) {

            return 8;
        }
        
                
        
        
        
        
        return NO_POTENTIAL_WINNER_FOUND;
    }

    private int findPotentialWinnerInColumns(GameEngine game, FieldType player) {
        Field[][] gameBoard = game.getGameBoard();

        if (gameBoard[0][0].getState() == EMPTY
                && gameBoard[1][0].getState() == player
                && gameBoard[2][0].getState() == player) {

            return 0;
        }

        if (gameBoard[0][0].getState() == player
                && gameBoard[1][0].getState() == EMPTY
                && gameBoard[2][0].getState() == player) {

            return 3;
        }

        if (gameBoard[0][0].getState() == player
                && gameBoard[1][0].getState() == player
                && gameBoard[2][0].getState() == EMPTY) {

            return 6;
        }
        
        
        
        
        

        if (gameBoard[0][1].getState() == EMPTY
                && gameBoard[1][1].getState() == player
                && gameBoard[2][1].getState() == player) {
            
            return 1;
        }

        if (gameBoard[0][1].getState() == player
                && gameBoard[1][1].getState() == EMPTY
                && gameBoard[2][1].getState() == player) {

            return 4;
        }

        if (gameBoard[0][1].getState() == player
                && gameBoard[1][1].getState() == player
                && gameBoard[2][1].getState() == EMPTY) {

            return 7;
        }





        if (gameBoard[0][2].getState() == EMPTY
                && gameBoard[1][2].getState() == player
                && gameBoard[2][2].getState() == player) {

            return 2;
        }

        if (gameBoard[0][2].getState() == player
                && gameBoard[1][2].getState() == EMPTY
                && gameBoard[2][2].getState() == player) {

            return 5;
        }

        if (gameBoard[0][2].getState() == player
                && gameBoard[1][2].getState() == player
                && gameBoard[2][2].getState() == EMPTY) {

            return 8;
        }






        return NO_POTENTIAL_WINNER_FOUND;
    }

    private int findPotentialWinnerInDiagonals(GameEngine game, FieldType player) {
        Field[][] gameBoard = game.getGameBoard();

        if (gameBoard[0][0].getState() == EMPTY
                && gameBoard[1][1].getState() == player
                && gameBoard[2][2].getState() == player) {

            return 0;
        }

        if (gameBoard[0][0].getState() == player
                && gameBoard[1][1].getState() == EMPTY
                && gameBoard[2][2].getState() == player) {

            return 4;
        }

        if (gameBoard[0][0].getState() == player
                && gameBoard[1][1].getState() == player
                && gameBoard[2][2].getState() == EMPTY) {

            return 8;
        }





        if (gameBoard[0][2].getState() == EMPTY
                && gameBoard[1][1].getState() == player
                && gameBoard[2][0].getState() == player) {

            return 2;
        }

        if (gameBoard[0][2].getState() == player
                && gameBoard[1][1].getState() == EMPTY
                && gameBoard[2][0].getState() == player) {

            return 4;
        }

        if (gameBoard[0][2].getState() == player
                && gameBoard[1][1].getState() == player
                && gameBoard[2][0].getState() == EMPTY) {

            return 6;
        }






        return NO_POTENTIAL_WINNER_FOUND;
    }
}
