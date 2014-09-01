/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package binaryclock;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import javafx.util.Duration;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

/**
 *
 * @author emwhfm
 */
public class BinaryClock extends Application {
    
    private final ClockPane clockPane = new ClockPane();
    private final Label labelTime = new Label("TID:TID:TID");
    
    @Override
    public void start(Stage primaryStage) {
                
        HBox bottomPane = new HBox(20);
        bottomPane.setPadding(new Insets(30));
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.getChildren().add(labelTime);
        labelTime.setStyle("-fx-text-fill: lightgreen");
        
        BorderPane root = new BorderPane();
        root.setCenter(clockPane);
        root.setBottom(bottomPane);
        root.setStyle("-fx-background-color: black");      
        
        // Create an animation for alternating text
        Timeline animation = new Timeline(
            new KeyFrame(Duration.millis(250), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play(); // Start animation
        
        Scene scene = new Scene(root, 350, 300);
        
        primaryStage.setTitle("Binary clock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create a handler for updating timec
    EventHandler<ActionEvent> eventHandler = e -> {
        clockPane.setCurrentTime();
        String timeString = String.format("%02d:%02d:%02d", clockPane.getHour(), 
                clockPane.getMinute(), clockPane.getSecond());
        labelTime.setText(timeString);
    };
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

class ClockPane extends Pane {
       
    private final Color COLOR_ON = Color.BLUE;
    private final Color COLOR_OFF = Color.GRAY;
    
    private int hour;
    private int minute;
    private int second;

    private final ArrayList<Circle> circles = new ArrayList<>();

    public ClockPane() {        
        //getChildren().clear();
        for (int i=0; i<20; i++) {
            circles.add(new Circle());           
            getChildren().add(circles.get(i));
        }        
        setCurrentTime();
    }
     
    public int getHour() {
        return hour;
    }
    
    public int getMinute() {
        return minute;
    }
    
    public int getSecond() {
        return second;
    }

    public void setCurrentTime() {
        // Construct a calendar for the current date and time
        Calendar calendar = new GregorianCalendar();

        // Set current hour, minute and second
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.second = calendar.get(Calendar.SECOND);
        paintClock(); // Repaint the clock
    }   
    
    protected void paintClock() {
    
        // Disable all 
        for (int i=0; i < circles.size(); i++) {
            circles.get(i).setFill(COLOR_OFF);
        }
        
        double X0 = getWidth() / 12;
        double Y0 = getHeight() / 8;       
        //double RADIUS = getWidth() > getHeight() ? getHeight() / 8 : getWidth() / 12;
        double RADIUS = X0 > Y0 ? Y0 : X0;
        
        // Hour - MSB        
	double cx = X0;
	double cy = Y0 + 4 * RADIUS;
	int hour_msb = hour / 10;
	int bit_weight = 2;
	for (int i=0; i < 2; i++) {
            if (hour_msb / bit_weight > 0) {		
                circles.get(i).setFill(COLOR_ON);
		hour_msb -= bit_weight;
	    }	    
            circles.get(i).setCenterX(cx);
            circles.get(i).setCenterY(cy);   
            circles.get(i).setRadius(RADIUS);
	    bit_weight >>= 1;
	    cy += 2 * RADIUS;
	}
        
        // Hour - LSB
	cx = X0 + 2 * RADIUS;
	cy = Y0;
	int hour_lsb = hour - (hour / 10) * 10;
	bit_weight = 8;
	for (int i=0; i < 4; i++) {
	      if (hour_lsb / bit_weight > 0) {
		circles.get(i + 2).setFill(COLOR_ON);
		hour_lsb -= bit_weight;
	    }	   
            circles.get(i + 2).setCenterX(cx);
            circles.get(i + 2).setCenterY(cy); 
            circles.get(i + 2).setRadius(RADIUS);
	    bit_weight >>= 1;
	    cy += 2 * RADIUS;
	}
        
        // Minute - MSB
	cx = X0 + 4 * RADIUS;
	cy = Y0 + 2 * RADIUS;
	int min_msb = minute / 10;
	bit_weight = 4;
	for (int i=0; i < 3; i++) {
	    if (min_msb / bit_weight > 0) {
                circles.get(i + 6).setFill(COLOR_ON);
                min_msb -= bit_weight;
	    }
            circles.get(i + 6).setCenterX(cx);
            circles.get(i + 6).setCenterY(cy);  
            circles.get(i + 6).setRadius(RADIUS);
	    bit_weight >>= 1;
	    cy += 2 * RADIUS;
	}
        
        // Minute - LSB
	cx = X0 + 6 * RADIUS;
	cy = Y0;
	int min_lsb = minute - (minute / 10) * 10;
	bit_weight = 8;
	for (int i=0; i < 4; i++) {
	      if (min_lsb / bit_weight > 0) {
		circles.get(i + 9).setFill(COLOR_ON);
		min_lsb -= bit_weight;
	    }	
            circles.get(i + 9).setCenterX(cx);
            circles.get(i + 9).setCenterY(cy);  
            circles.get(i + 9).setRadius(RADIUS);
	    bit_weight >>= 1;
	    cy += 2 * RADIUS;
	}
        
        // Second - MSB
	cx = X0 + 8 * RADIUS;
	cy = Y0 + 2 * RADIUS;
	int sec_msb = second / 10;
	bit_weight = 4;
	for (int i=0; i < 3; i++) {
	      if (sec_msb / bit_weight > 0) {
		circles.get(i + 13).setFill(COLOR_ON);
		sec_msb -= bit_weight;
	    }
            circles.get(i + 13).setCenterX(cx);
            circles.get(i + 13).setCenterY(cy);  
            circles.get(i + 13).setRadius(RADIUS);
	    bit_weight >>= 1;
	    cy += 2 * RADIUS;
	}
        
        // Second - LSB
	cx = X0 + 10 * RADIUS;
	cy = Y0;
	int sec_lsb = second - (second / 10) * 10;
	bit_weight = 8;
	for (int i=0; i < 4; i++) {
	      if (sec_lsb / bit_weight > 0) {
		circles.get(i + 16).setFill(COLOR_ON);
		sec_lsb -= bit_weight;
	    }	    
            circles.get(i + 16).setCenterX(cx);
            circles.get(i + 16).setCenterY(cy); 
            circles.get(i + 16).setRadius(RADIUS);
	    bit_weight >>= 1;
	    cy += 2 * RADIUS;
	}
        
    }        
}