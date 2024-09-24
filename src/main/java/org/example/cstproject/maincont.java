package org.example.cstproject;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class maincont extends Application {

    @FXML
    private Button dfaBTN;

    @FXML
    private Button ndfaBTN;

    @FXML
    private Button pdaBTN;

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();

        dfaBTN = new Button("DFA");
        ndfaBTN = new Button("NDFA");
        pdaBTN = new Button("PDA");

        setupButtonAnimation(dfaBTN, root);
        setupButtonAnimation(ndfaBTN, root);
        setupButtonAnimation(pdaBTN, root);

        root.getChildren().addAll(dfaBTN, ndfaBTN, pdaBTN);

        Scene scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Animated Button Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupButtonAnimation(Button button, StackPane root) {
        Circle circle = new Circle(60);
        circle.setStroke(Color.WHITE);
        circle.setStrokeType(StrokeType.INSIDE);
        circle.setOpacity(0);
        circle.setFill(null);

        button.setOnMouseEntered(e -> {
            circle.setOpacity(1);
            animateBorder(circle);
        });

        button.setOnMouseExited(e -> {
            circle.setOpacity(0);
        });

        root.getChildren().add(circle);
        StackPane.setAlignment(circle, Pos.CENTER);
    }

    private void animateBorder(Circle circle) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), circle);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.5);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1.5);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    public void Nav(ActionEvent event) throws IOException {
        if (event.getSource() == dfaBTN) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DFA.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } else if (event.getSource() == ndfaBTN) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NDFA.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } else if (event.getSource() == pdaBTN) {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PUSHDOWN.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        }
    }
}