package com.mewa.ui.controllers;

import com.mewa.Main;
import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.utils.SerialClock;
import com.mewa.utils.StandardOutput;
import com.mewa.utils.i.Logger;
import com.mewa.utils.loggers.DebugLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Mewa on 2015-12-06.
 */
public class GUIMain {
    public static final int CELL_SIZE = 30;

    public static Logger logger = new DebugLogger(new StandardOutput(), new SerialClock());

    private Parent parent;
    private Scene scene;

    @FXML
    public Canvas worldCanvas;
    @FXML
    public Canvas vehicleCanvas;
    @FXML
    public AnchorPane root;

    private GraphicsContext worldGraphicsContext;
    private GraphicsContext objectGraphicsContext;
    private World world;
    private Scene portScene;
    private Stage infoStage;
    private Stage mainStage;

    public GUIMain() throws IOException {
        logger.setLogLevel(Logger.VERBOSE);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(this);
        scene = new Scene((Parent) loader.load());
    }

    public void start(Stage stage) {
        mainStage = stage;
        logger.log(Logger.VERBOSE, "Creating World");
        world = World.getInstance();
        logger.log(Logger.VERBOSE, "World created");

        double width = (world.getNortheast().getX() - world.getSouthwest().getX() + 1) * CELL_SIZE;
        double height = (world.getNortheast().getY() - world.getSouthwest().getY() + 1) * CELL_SIZE;

        worldCanvas.setWidth(width);
        worldCanvas.setHeight(height);
        vehicleCanvas.setWidth(width);
        vehicleCanvas.setHeight(height);

        stage.setWidth(width + 0.2 * CELL_SIZE);
        stage.setHeight(height + CELL_SIZE);

        worldGraphicsContext = worldCanvas.getGraphicsContext2D();
        objectGraphicsContext = vehicleCanvas.getGraphicsContext2D();

        world.start(worldGraphicsContext, objectGraphicsContext, width, height, CELL_SIZE);

        stage.setScene(scene);
        stage.show();
    }

    public void click(MouseEvent event) {
        logger.log(Logger.VERBOSE, "click: %.2f, %.2f", event.getX(), event.getY());
    }

    public void vehicleClicked(MouseEvent event) {
        Location clickLocation = new Location((event.getX() - 0.5 * CELL_SIZE) / CELL_SIZE, (event.getY() - 0.5 * CELL_SIZE) / CELL_SIZE);
        logger.log(Logger.VERBOSE, "clickLocation: %s", clickLocation);
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            for (AbstractPort port : world.getPorts()) {
                if (world.checkCollision(port.getLocation(), clickLocation)) {
                    logger.log(Logger.VERBOSE, "Port clicked: %s", port);
                    port.onClick(this);
                    return;
                }
            }
            for (Vehicle vehicle : world.getVehicles()) {
                if (world.checkCollision(vehicle.getLocation(), clickLocation)) {
                    logger.log(Logger.VERBOSE, "Vehicle clicked: %s", vehicle);
                    vehicle.onClick(this);
                    return;
                }
            }
        } else if (event.getButton().equals(MouseButton.SECONDARY)) {
            for (Route route : world.getRoutes()) {
                for (Location stop : route.getStops()) {
                    if (world.checkCollision(stop, clickLocation)) {
                        logger.log(Logger.VERBOSE, "Route clicked: %s", route);
                        route.onClick(this);
                        return;
                    }
                }
            }
        }
    }

    public void showPortPanel(AbstractPort port) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("port_info.fxml"));
        try {
            portScene = new Scene((Parent) loader.load());
            InfoPane portPane = loader.getController();
            portPane.setPort(port);
            if (infoStage == null) {
                infoStage = new Stage();
                //infoStage.setAlwaysOnTop(true);
            }
            infoStage.hide();
            infoStage.setScene(portScene);
            infoStage.setX(mainStage.getX() + mainStage.getWidth() + 10);
            infoStage.setY(mainStage.getY());
            infoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRoutePanel(Route route) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("port_info.fxml"));
        try {
            portScene = new Scene((Parent) loader.load());
            InfoPane portPane = loader.getController();
            portPane.setRoute(route);
            if (infoStage == null) {
                infoStage = new Stage();
                //infoStage.setAlwaysOnTop(true);
            }
            infoStage.hide();
            infoStage.setScene(portScene);
            infoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showVehiclePanel(Vehicle route) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("port_info.fxml"));
        try {
            portScene = new Scene((Parent) loader.load());
            InfoPane portPane = loader.getController();
            portPane.setVehicle(route);
            if (infoStage == null) {
                infoStage = new Stage();
                //infoStage.setAlwaysOnTop(true);
            }
            infoStage.hide();
            infoStage.setScene(portScene);
            infoStage.setX(mainStage.getX() + mainStage.getWidth() + 10);
            infoStage.setY(mainStage.getY());
            infoStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
