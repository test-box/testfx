package ru.globux.testfx;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import ru.globux.testfx.controls.PingTextField;
import ru.globux.testfx.controls.RowButton;

public class AppController {
    private final PingApp app;

    public AppController(PingApp app) {
        this.app = app;
    }

    public void onTextFieldAction(ActionEvent actionEvent) {
        System.out.println("AppController.onTextFieldAction():" + Thread.currentThread());
        PingTextField textField = (PingTextField) actionEvent.getSource();
        Pinger.getInstance().ping(textField);
    }

    public void onButtonPlusAction(ActionEvent actionEvent) {
        GridPane gridPane = (GridPane) ((Button) actionEvent.getSource()).getParent();
        app.addPingerRow(gridPane);
    }

    public void onButtonMinusAction(ActionEvent actionEvent) {
        RowButton button = ((RowButton) actionEvent.getSource());
        PingTextField textField = button.getTextField();
        GridPane gridPane = (GridPane) textField.getParent();
            ObservableList<Node> list = gridPane.getChildren();
            list.remove(textField.getButtonPlus());
            list.remove(textField.getLabel());
            list.remove(textField.getButtonMinus());
            list.remove(textField);
            textField.getOptStage().ifPresent(Window::sizeToScene);
    }

}
