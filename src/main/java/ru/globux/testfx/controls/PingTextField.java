package ru.globux.testfx.controls;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

public class PingTextField extends TextField {
    private final Label label;
    private final Button buttonPlus;
    private final Button buttonMinus;
    private Optional<ScheduledFuture> optFuture = Optional.empty();
    private Optional<Stage> optStage = Optional.empty();

    public PingTextField(Label label, Button buttonPlus, Button buttonMinus) {
        super();
        this.label = label;
        this.buttonPlus = buttonPlus;
        this.buttonMinus = buttonMinus;
        ((RowButton) buttonMinus).setTextField(this);
    }

    public Label getLabel() {
        return this.label;
    }

    public Button getButtonPlus() {
        return this.buttonPlus;
    }

    public Button getButtonMinus() {
        return this.buttonMinus;
    }

    public Optional<ScheduledFuture> getOptFuture() {
        return this.optFuture;
    }

    public Optional<Stage> getOptStage() {
        return this.optStage;
    }

    public void setStage(Stage stage) {
        this.optStage = Optional.ofNullable(stage);
    }

    public void saveFuture(ScheduledFuture future) {
        cancelPingWorker();
        this.optFuture = Optional.ofNullable(future);
    }

    public void cancelPingWorker() {
        this.optFuture.ifPresent(f -> f.cancel(true));
    }

}
