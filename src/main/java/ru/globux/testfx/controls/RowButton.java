package ru.globux.testfx.controls;

import javafx.scene.control.Button;

public class RowButton extends Button {
    private PingTextField textField;

    public RowButton(String s) {
        super(s);
    }

    public PingTextField getTextField() {
        return this.textField;
    }

    public void setTextField(PingTextField textField) {
        this.textField = textField;
    }
}
