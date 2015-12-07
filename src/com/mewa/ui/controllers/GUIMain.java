package com.mewa.ui.controllers;

import com.mewa.data.location.World;
import com.mewa.data.ports.AbstractPort;
import com.mewa.utils.SerialClock;
import com.mewa.utils.StandardOutput;
import com.mewa.utils.i.Logger;
import com.mewa.utils.loggers.DebugLogger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Mewa on 2015-12-06.
 */
public class GUIMain {
    public static final int CELL_SIZE = 24;

    public static Logger logger = new DebugLogger(new StandardOutput(), new SerialClock());

    private Parent parent;
    private Scene scene;

    @FXML
    public Canvas canvas;
    @FXML
    public AnchorPane root;

    GraphicsContext graphicsContext;

    public GUIMain() throws IOException {
        logger.setLogLevel(Logger.VERBOSE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(this);
        parent = (Parent) loader.load();
        scene = new Scene(parent);
    }

    public void start(Stage stage) {
        logger.log(Logger.VERBOSE, "Creating World");
        World world = World.getInstance();
        logger.log(Logger.VERBOSE, "World created");

        double width = (world.getSouthwest().getX() - world.getNortheast().getX()) * CELL_SIZE;
        double height = (world.getSouthwest().getY() - world.getNortheast().getY()) * CELL_SIZE;
        canvas.setWidth(width);
        canvas.setHeight(height);
        stage.setWidth(width + 0.2 * CELL_SIZE);
        stage.setHeight(height + 0.2 * CELL_SIZE);

        graphicsContext = canvas.getGraphicsContext2D();

        world.start(graphicsContext, width, height, CELL_SIZE);

        stage.setScene(scene);
        stage.show();
    }

    public void click(MouseEvent event) {
        logger.log(Logger.VERBOSE, "click: %.2f, %.2f", event.getX(), event.getY());
    }
}
