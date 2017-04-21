package pl.mateuszbona.tictactoe.game.field;

import pl.mateuszbona.tictactoe.game.field.utils.FieldType;

public class Field {
    private FieldType state;

    public Field(FieldType state) {
        this.state = state;
    }

    public FieldType getState() {
        return state;
    }

    public void setState(FieldType state) {
        this.state = state;
    }
}
