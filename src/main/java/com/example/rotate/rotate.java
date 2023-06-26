package com.example.rotate;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;
//main class
public class rotate extends Application {
    @Override
    public void start(Stage st) {
//create a hexagon
        Polygon hx = new Polygon();
//Add coordinates to the created hexagon
        hx.getPoints().addAll(220.0, 60.0,
                420.0, 60.0,
                430.0, 160.0,
                430.0, 260.0,
                230.0, 260.0,
                180.0, 160.0);
//fill color for the created hexagon
        hx.setFill(Color.YELLOW);
//rotate transition creation
        RotateTransition rt = new RotateTransition();
//Set the duration for the rotate transition created
        rt.setDuration(Duration.millis(2000));
//Set the node for the rotate transition created
        rt.setNode(hx);
//Set the angle of the rotate transition created
        rt.setByAngle(360);
//Set the cycle count for rotate transition created
        rt.setCycleCount(60);
//Set false as the auto reverse value
        rt.setAutoReverse(false);
//start playing the animation
        rt.play();
//Create a Group object
        Group g = new Group(hx);
//Create a scene object
        Scene sc = new Scene(g, 600, 325);
//Set title of the Stage st
        st.setTitle("Animation example ");
//Add scene to the stage st
        st.setScene(sc);
//Display the contents of the stage st
        st.show();
    }
    //main method
    public static void main(String[] args){
//launches the application
        launch(args);
    }
}

