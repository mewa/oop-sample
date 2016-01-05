package com.mewa.ui.controllers;

import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.Airport;
import com.mewa.data.ports.CivilAirport;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.planes.Plane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * Created by Mewa on 2015-12-19.
 */
public class InfoPane {
    @FXML
    private VBox main;
    @FXML
    private TitledPane root;
    @FXML
    private VBox properties;
    @FXML
    private VBox values;

    public InfoPane() {
    }

    public void setPort(AbstractPort port) {
        root.setText(port.toString());
        addRow("Location", String.format("%.2f x %.2f", port.getLocation().getX(), port.getLocation().getY()));
        if (port instanceof Airport) {
            Airport airport = (Airport) port;
            addRow("Capacity", airport.getCapacity() + "");
            if (airport instanceof CivilAirport) {
                final CivilAirport civilAirport = (CivilAirport) airport;
                addButton("Nowy samolot", new OnClickListener() {
                    @Override
                    public void onClick() {
                        PassengerPlane plane = new PassengerPlane(10);
                        civilAirport.acceptDeparture(plane);
                        World.getInstance().registerGameObject(plane);
                    }
                });
            }
        }
    }

    private void addRow(String key, String value) {
        properties.getChildren().add(new Label(key));
        values.getChildren().add(new Label(value));
    }

    private void addButton(String text, final OnClickListener action) {
        Button btn = new Button(text);
        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                action.onClick();
            }
        });
        main.getChildren().add(btn);
    }

    public void setRoute(Route route) {
        root.setText(route.toString());
        for (Location location : route.getStops()) {
            addRow("Stop", String.format("%.2f x %.2f", location.getX(), location.getY()));
        }
    }

    public void setVehicle(Vehicle vehicle) {
        root.setText(vehicle.toString());
        addRow("Location", String.format("%.2f x %.2f", vehicle.getLocation().getX(), vehicle.getLocation().getY()));
        addRow("Route", vehicle.getRoute() + "");
        addRow("Direction", vehicle.getDirection() + "");
        if (vehicle instanceof Plane) {
            Plane plane = (Plane) vehicle;
            addRow("Fuel", String.format("%.2f", plane.getFuel()));
            addRow("Speed", String.format("%.3f", plane.getSpeed()));
        }
    }

    public interface OnClickListener {
        public void onClick();
    }
}
