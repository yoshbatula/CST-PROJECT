package org.example.cstproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFACONT extends Application {

    private DFA dfa;
    private Text resultText;

    @Override
    public void start(Stage primaryStage) {
        dfa = createSampleDFA();

        BorderPane root = new BorderPane();
        Pane pane = new Pane();
        drawDFA(pane);

        TextField inputField = new TextField();
        inputField.setStyle("-fx-border-width: 35px; -fx-border-radius: 10px");
        inputField.setPromptText("INPUT STRING");

        Button testButton = new Button("Test");
        testButton.setStyle("-fx-font-size: 14px; -fx-border-radius: 30px; -fx-background-color: white");
        testButton.setOnAction(e -> processInput(inputField.getText(), pane));

        // Initialize the resultText
        resultText = new Text();
        resultText.setStyle("-fx-font-size: 24px; -fx-padding: 15px");

        VBox resultBox = new VBox(resultText);
        resultBox.setAlignment(Pos.CENTER); // Center the text
        resultBox.setSpacing(10); // Add some spacing if needed

        root.setCenter(pane);
        root.setBottom(inputField);
        root.setAlignment(inputField, Pos.CENTER);
        root.setRight(testButton);
        root.setTop(resultBox); // Add the VBox to the top of the layout

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("DFA VISUALIZATION");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private DFA createSampleDFA() {
        DFA dfa = new DFA();
        State q0 = new State("q0", false);
        State q1 = new State("q1", false);
        State q2 = new State("q2", false);
        State q3 = new State("q3", false);
        State q4 = new State("q4", true); // Final accepting state

        dfa.addState(q0);
        dfa.addState(q1);
        dfa.addState(q2);
        dfa.addState(q3);
        dfa.addState(q4);
        dfa.setStartState(q0);

        // Define transitions leading to q4
        dfa.addTransition(new Transition(q0, 'a', q1));
        dfa.addTransition(new Transition(q1, 'a', q1)); // Loop on 'a'
        dfa.addTransition(new Transition(q1, 'b', q2));
        dfa.addTransition(new Transition(q2, 'c', q3));
        dfa.addTransition(new Transition(q1, 'c', q3)); // Transition for 'c'
        dfa.addTransition(new Transition(q2, 'b', q2));
        dfa.addTransition(new Transition(q2, 'a', q1));
        dfa.addTransition(new Transition(q1, 'c', q3)); // Also for 'c'
        dfa.addTransition(new Transition(q3, 'd', q4));

        return dfa;
    }

    private void drawDFA(Pane pane) {
        double radius = 50;
        double xOffset = 150;  // Starting X position
        double yOffset = 200;  // Y position (constant)
        int stateCount = 0;
        Map<State, Circle> stateCircles = new HashMap<>();

        // Define a specific order for the states
        List<State> orderedStates = Arrays.asList(
                dfa.getStateByName("q0"),
                dfa.getStateByName("q1"),
                dfa.getStateByName("q2"),
                dfa.getStateByName("q3"),
                dfa.getStateByName("q4")
        );

        // Draw states in the specified order
        for (State state : orderedStates) {
            Circle circle = new Circle(xOffset + (stateCount * 150), yOffset, radius);
            circle.setFill(state.isAccepting ? Color.LIGHTGREEN : Color.LIGHTGRAY);
            circle.setStroke(Color.BLACK);
            circle.setUserData(state); // Set user data for state
            Text stateLabel = new Text(state.name);
            stateLabel.setX(circle.getCenterX() - 10);
            stateLabel.setY(circle.getCenterY() + 5);

            pane.getChildren().addAll(circle, stateLabel);
            stateCircles.put(state, circle);
            stateCount++;
        }

        // Draw transitions
        for (Transition transition : dfa.getTransitions()) {
            Circle fromCircle = stateCircles.get(transition.fromState);
            Circle toCircle = stateCircles.get(transition.toState);

            double startX = fromCircle.getCenterX();
            double startY = fromCircle.getCenterY();
            double endX = toCircle.getCenterX();
            double endY = toCircle.getCenterY();

            double fromAngle = Math.atan2(endY - startY, endX - startX);
            double toAngle = Math.atan2(startY - endY, startX - endX);
            double offsetXFrom = Math.cos(fromAngle) * radius;
            double offsetYFrom = Math.sin(fromAngle) * radius;
            double offsetXTo = Math.cos(toAngle) * radius;
            double offsetYTo = Math.sin(toAngle) * radius;

            Line line = new Line(startX - offsetXFrom, startY - offsetYFrom, endX + offsetXTo, endY + offsetYTo);
            line.setStroke(Color.BLUE);
            line.setStrokeWidth(2);
            line.setStrokeLineCap(StrokeLineCap.ROUND);
            line.setUserData(transition); // Set user data for highlighting
            pane.getChildren().add(line);

            double midX = (startX + endX) / 2;
            double midY = (startY + endY) / 2;
            Text label = new Text(String.valueOf(transition.inputSymbol));
            label.setX(midX);
            label.setY(midY - 10);
            pane.getChildren().add(label);
        }
    }

    private void processInput(String input, Pane pane) {
        clearHighlights(pane); // Clear previous highlights
        dfa.setCurrentState(dfa.getStartState());

        // Start backtracking
        backtrack(input, pane, 0); // Use 0 as the starting index
    }

    private boolean backtrack(String input, Pane pane, int index) {
        clearHighlights(pane); // Clear previous highlights
        highlightState(pane, dfa.getCurrentState()); // Highlight the current state

        // Check if we've processed all characters
        if (index == input.length()) {
            // End of input, check acceptance
            boolean result = dfa.isAccepting(dfa.getCurrentState());
            resultText.setText("Input: " + input + " is " + (result ? "accepted" : "rejected")); // Use result here
            return result;
        }
        char currentChar = input.charAt(index);
        State currentState = dfa.getCurrentState();
        Transition transition = dfa.getTransition(currentState, currentChar);

        // Try to find a valid transition
        if (transition != null) {
            highlightTransition(pane, transition); // Highlight the transition
            dfa.setCurrentState(transition.toState); // Move to the next state

            // Recursive call to process the next character
            if (backtrack(input, pane, index + 1)) {
                return true; // Accepted
            }

            // Reset state after backtrack
            dfa.setCurrentState(currentState);
        }

        // Explore all transitions from the current state if the direct transition failed
        for (Transition t : dfa.getTransitionsFromState(currentState)) {
            highlightTransition(pane, t); // Highlight the alternative transition
            dfa.setCurrentState(t.toState); // Move to the next state

            if (backtrack(input, pane, index + 1)) { // Process the next character
                return true; // Accepted
            }

            // Reset state after backtrack
            dfa.setCurrentState(currentState);
        }

        return false; // Not accepted
    }

    private void highlightState(Pane pane, State state) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                if (circle.getUserData() instanceof State && circle.getUserData().equals(state)) {
                    circle.setStroke(Color.RED); // Highlight current state
                    circle.setStrokeWidth(4);
                } else {
                    circle.setStroke(Color.BLACK); // Reset other states
                    circle.setStrokeWidth(2);
                }
            }
        }
    }

    private void highlightTransition(Pane pane, Transition transition) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Line) {
                Line line = (Line) node;
                if (line.getUserData() instanceof Transition && line.getUserData().equals(transition)) {
                    line.setStroke(Color.ORANGE); // Highlight current transition
                    line.setStrokeWidth(4);
                } else {
                    line.setStroke(Color.BLUE); // Reset other transitions
                    line.setStrokeWidth(2);
                }
            }
        }
    }

    private void clearHighlights(Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                circle.setStroke(Color.BLACK); // Reset stroke color
                circle.setStrokeWidth(2); // Reset width
            } else if (node instanceof Line) {
                Line line = (Line) node;
                line.setStroke(Color.BLUE); // Reset line color
                line.setStrokeWidth(2); // Reset line width
            }
        }
    }
}
