package CMS.Logic;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class ErrorMessageHandler{
    private final Label errorMessageLabel;
    private final Timeline clearErrorTimeLine;

    ErrorMessageHandler(Label errorMessageLabel, int duration, String color) {
        this.errorMessageLabel = errorMessageLabel;
        this.clearErrorTimeLine = new Timeline(new KeyFrame(Duration.seconds(duration), event -> clearError()));
        this.errorMessageLabel.setStyle("-fx-text-fill: " + color + ";");
    }

    public void displayError(String errorMessage){
        Platform.runLater(() -> {
            errorMessageLabel.setText(errorMessage);
            clearErrorTimeLine.playFromStart();
        });
    }

    private void clearError(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), errorMessageLabel);
        fadeTransition.setToValue(0);
        fadeTransition.setOnFinished(event -> {
            errorMessageLabel.setText("");
            errorMessageLabel.setOpacity(1);
        });
        fadeTransition.play();
    }
}
