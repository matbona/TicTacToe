package pl.mateuszbona.tictactoe.game.field.utils;

public class FieldCoordinates {
    private int row;
    private int column;

    public FieldCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
