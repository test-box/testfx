package ru.globux.testfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.globux.testfx.controls.PingTextField;
import ru.globux.testfx.controls.RowButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class PingApp extends Application {
    private Stage mainStage;
    private final AppController controller = new AppController(this);
    private final List<PingTextField> textFieldList = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        System.out.println("PingApp.start(): " + Thread.currentThread());
        this.mainStage = stage;
        Scene scene = buildScene();
        stage.setTitle("Pinger!");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
}

    private Scene buildScene() {
        GridPane gridPane = new GridPane();
        addPingerRow(gridPane);
        gridPane.setHgap(5);
        return new Scene(gridPane);
    }

    protected void addPingerRow(GridPane gridPane) {
        Button buttonPlus = new Button("+");
        buttonPlus.setOnAction(controller::onButtonPlusAction);
        Button buttonMinus = new RowButton("-");
        buttonMinus.setOnAction(controller::onButtonMinusAction);

        Label label = new Label("<- Enter address");
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);

        PingTextField textField = new PingTextField(label, buttonPlus, buttonMinus);
        textField.setStage(this.mainStage);
        textField.setPrefWidth(200);
        textField.setOnAction(controller::onTextFieldAction);

        int row = gridPane.getRowCount();
        gridPane.add(buttonPlus,0 , row);
        gridPane.add(textField, 1, row);
        gridPane.add(label, 2, row);
        gridPane.add(buttonMinus, 3, row);
        textFieldList.add(textField);
        this.mainStage.sizeToScene();
        System.out.println("gridPane, количество строк: " + gridPane.getRowCount());
    }

    public void stop() {
        for(PingTextField e: textFieldList) {
            Optional<ScheduledFuture> of = e.getOptFuture();
            of.ifPresent(f -> { if (!f.isCancelled()) f.cancel(true); });
        }
        Pinger.getInstance().shutdown();
    }

    public static void main(String[] args) {
        System.out.println("PingApp.main():" + Thread.currentThread());
        launch();

        for (Thread t: Thread.getAllStackTraces().keySet()) {
            System.out.println(t);
        }
    }

}