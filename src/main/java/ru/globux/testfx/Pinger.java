package ru.globux.testfx;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;
import java.util.concurrent.*;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import ru.globux.testfx.controls.PingTextField;


public class Pinger {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static class Holder {
        private static final Pinger INSTANCE = new Pinger();
    }

    public static Pinger getInstance() {
        return Holder.INSTANCE;
    }

    public void ping(PingTextField textField) {
        String address = textField.getText();
        if (Optional.ofNullable(textField.getText()).isPresent()
            && textField.getText().isEmpty()) {
            changeLabel(textField, ": Остановлен", Color.DARKGREY);
            textField.cancelPingWorker();
            return;
        }
        Runnable runnable = getPingWorker(textField);
        System.out.println("Pinger.ping(): " + Thread.currentThread());
        System.out.println("New Runnable: " + runnable + ", with text: " + textField.getText());
        ScheduledFuture future = executor.scheduleWithFixedDelay(runnable, 0,3, TimeUnit.SECONDS);
        textField.saveFuture(future);
    }


    public void shutdown() {
        executor.shutdown();
    }

    // To run in FX Application Thread
    private void changeLabel(PingTextField textField, String newText, Color color) {
        if (textField.getLabel().getText().equals(newText)) {
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textField.getLabel().setTextFill(color);
                textField.getLabel().setText(newText);
                textField.getOptStage().ifPresent(Window::sizeToScene);
                System.out.println("runLater: " + Thread.currentThread());
            }
        });
    }

    // To run with ScheduledExecutorService
    private Runnable getPingWorker(PingTextField textField) {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("PingWorker: " + Thread.currentThread() + "  /  " + this);
                if (textField.getOptFuture().isEmpty()) {
                    System.out.println("PingWorker: " + " future is Empty!");
                    return;
                }
                if (textField.getOptFuture().get().isCancelled()) return;
                if (isReachable(textField)) {
                    changeLabel(textField, "Online", Color.GREEN);
                } else {
                    changeLabel(textField, "Offline", Color.RED);
                }
            }
        };
    }

    private boolean isReachable(PingTextField textField) {
        boolean result = false;
        try {
            InetAddress addr = InetAddress.getByName(textField.getText());
            result = addr.isReachable(3000);
        } catch (IOException e) {
            System.err.println(e);
        }
        return result;
    }

//    private void pause(long duration) {
//        try {
//            Thread.sleep(duration);
//        } catch (InterruptedException e) {
//            System.err.println("Пауза прервана");
//        }
//    }

//    private boolean isReachable(PingTextField textField) {
//        if (ThreadLocalRandom.current().nextInt(2) == 1) {
//            pause(1000);
//            return true;
//        }
//        else {
//            pause(1000);
//            return false;
//        }
//    }

}