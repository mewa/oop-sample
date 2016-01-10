package com.mewa.ui.controllers;

import com.mewa.data.GameObject;
import com.mewa.data.GameObjectUpdateListener;
import com.mewa.data.Localizable;
import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.ports.*;
import com.mewa.data.type.Civil;
import com.mewa.data.type.Military;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.data.vehicles.ships.CruiseShip;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

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

    private Map<String, Label> propertyMap = Collections.synchronizedMap(new WeakHashMap<String, Label>());

    private static Object currentObject;

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
                        new Thread() {
                            @Override
                            public void run() {
                                PassengerPlane plane = new PassengerPlane(10);
                                civilAirport.depart(plane);
                            }
                        }.start();
                    }
                });
            }
        } else if (port instanceof NavalPort) {
            final NavalPort navalPort = (NavalPort) port;
            if (navalPort instanceof CivilNavalPort) {
                addButton("Nowy wycieczkowiec", new OnClickListener() {
                    @Override
                    public void onClick() {
                        new Thread() {
                            @Override
                            public void run() {
                                CruiseShip cs = new CruiseShip(1234);
                                navalPort.depart(cs);
                            }
                        }.start();
                    }
                });
            } else if (navalPort instanceof MilitaryNavalPort) {
                addButton("Nowy lotniskowiec", new OnClickListener() {
                    @Override
                    public void onClick() {
                        new Thread() {
                            @Override
                            public void run() {
                                AircraftCarrier ac = new AircraftCarrier();
                                navalPort.depart(ac);
                            }
                        }.start();
                    }
                });
            }
        }
    }

    private void addRow(String key, String value) {
        properties.getChildren().add(new Label(key));
        Label valueLabel = new Label(value);
        values.getChildren().add(valueLabel);
        propertyMap.put(key, valueLabel);
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
        for (Localizable location : route.getStops()) {
            addRow(location.toString(), String.format("%.2f x %.2f", location.getLocation().getX(), location.getLocation().getY()));
        }
    }

    public void setVehicle(final Vehicle vehicle) {
        currentObject = vehicle;
        root.setText(vehicle.toString());
        addRow("Location", String.format("%.2f x %.2f", vehicle.getLocation().getX(), vehicle.getLocation().getY()));
        addRow("Route", vehicle.getRoute() + "");
        addRow("Next stop", vehicle.getRoute().getNextStop(vehicle) + "");
        addRow("Direction", vehicle.getDirection() + "");
        if (vehicle instanceof Plane) {
            Plane plane = (Plane) vehicle;
            addRow("Fuel", plane.getFuel() + "");
        }
        if (vehicle instanceof Civil) {
            Civil civil = (Civil) vehicle;
            addRow("Capacity", civil.getCapacity() + "");
            addRow("numPassengers", civil.getNumberOfPassengers() + "");
        }
        vehicle.setLocationUpdateListener(new GameObjectUpdateListener() {
            @Override
            public void onGameObjectUpdated(final GameObject gameObject) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (currentObject == vehicle) {
                            propertyMap.get("Location").setText(String.format("%.2f x %.2f", gameObject.getLocation().getX(), gameObject.getLocation().getY()));
                            if (gameObject instanceof Vehicle) {
                                Vehicle v = (Vehicle) gameObject;
                                propertyMap.get("Route").setText(v.getRoute() + "");
                                propertyMap.get("Next stop").setText(v.getRoute() != null ? v.getRoute().getNextStop(v) + "" : "");
                                propertyMap.get("Direction").setText(v.getDirection() + "");
                            }
                            if (gameObject instanceof Plane) {
                                Plane plane = (Plane) gameObject;
                                propertyMap.get("Fuel").setText(String.format("%.3f", plane.getFuel()));
                            }
                            if (gameObject instanceof Civil) {
                                Civil civil = (Civil) gameObject;
                                propertyMap.get("Capacity").setText("" + civil.getCapacity());
                                propertyMap.get("numPassengers").setText("" + civil.getNumberOfPassengers());
                            }
                        } else {
                            vehicle.setLocationUpdateListener(null);
                        }
                    }
                });
            }
        });
    }

    public interface OnClickListener {
        public void onClick();
    }
}
