package com.mewa.ui.controllers;

import com.mewa.data.GameObject;
import com.mewa.data.GameObjectUpdateListener;
import com.mewa.data.Localizable;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.data.passengers.Passenger;
import com.mewa.data.ports.*;
import com.mewa.data.type.Civil;
import com.mewa.data.type.CivilVehicle;
import com.mewa.data.type.Military;
import com.mewa.data.vehicles.Vehicle;
import com.mewa.data.vehicles.planes.MilitaryPlane;
import com.mewa.data.vehicles.planes.PassengerPlane;
import com.mewa.data.vehicles.planes.Plane;
import com.mewa.data.vehicles.ships.AircraftCarrier;
import com.mewa.data.vehicles.ships.CruiseShip;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.*;

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
                                PassengerPlane plane = new PassengerPlane((int) (30 + Math.random() * 200));
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
                                CruiseShip cs = new CruiseShip((int) (300 + Math.random() * 2000));
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
        final ListView<Passenger> listView;
        if (port instanceof Civil) {
            final Civil civil = (Civil) port;
            addRow("numPassengers", civil.getNumberOfPassengers() + "");
            listView = getPassengerListView(civil);

            port.setGameObjectListener(new GameObjectUpdateListener() {
                                           @Override
                                           public void onGameObjectUpdated(final GameObject gameObject) {
                                               final ObservableList<Passenger> passengers;
                                               synchronized (civil.getPassengers()) {
                                                   passengers = FXCollections.observableArrayList(civil.getPassengers());
                                               }
                                               Platform.runLater(new Runnable() {
                                                   @Override
                                                   public void run() {
                                                       if (listView != null) {
                                                           synchronized (listView) {
                                                               listView.setItems(passengers);
                                                           }
                                                       }
                                                       if (gameObject instanceof Civil) {
                                                           propertyMap.get("numPassengers").setText(((Civil) gameObject).getNumberOfPassengers() + "");
                                                       }
                                                   }
                                               });
                                           }
                                       }
            );
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
            final Plane plane = (Plane) vehicle;
            addRow("Fuel", plane.getFuel() + "");
            addButton("Awaryjne lądowanie", new OnClickListener() {
                @Override
                public void onClick() {
                    if (plane instanceof CivilVehicle) {
                        PassengerPlane civilVehicle = (PassengerPlane) plane;
                        AbstractPort emergencyAirport = World.getInstance().getClosestCivilAirport(civilVehicle);
                        Route route = new Route(null, emergencyAirport);
                        route.add(plane);
                        route.add(emergencyAirport);
                        civilVehicle.setRoute(route, 1);
                    } else if (plane instanceof Military) {
                        MilitaryPlane militaryPlane = (MilitaryPlane) plane;
                    }
                }
            });
        }
        final ListView<Passenger> listView;
        if (vehicle instanceof CivilVehicle) {
            CivilVehicle civil = (CivilVehicle) vehicle;
            addRow("Capacity", civil.getCapacity() + "");
            addRow("numPassengers", civil.getNumberOfPassengers() + "");
            listView = getPassengerListView(civil);
        } else {
            listView = null;
        }
        vehicle.setGameObjectListener(new GameObjectUpdateListener() {
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
                            if (gameObject instanceof CivilVehicle) {
                                CivilVehicle civil = (CivilVehicle) gameObject;
                                propertyMap.get("Capacity").setText("" + civil.getCapacity());
                                propertyMap.get("numPassengers").setText("" + civil.getNumberOfPassengers());
                                if (listView != null) {
                                    synchronized (listView) {
                                        listView.setItems(FXCollections.observableArrayList(civil.getPassengers()));
                                    }
                                }
                            }
                        } else {
                            vehicle.setGameObjectListener(null);
                        }
                    }
                });
            }
        });
    }

    public ListView<Passenger> getPassengerListView(Civil civil) {
        final ListView<Passenger> passengerListView = new ListView<Passenger>();
        passengerListView.setMinWidth(450);
        passengerListView.setItems(FXCollections.<Passenger>observableArrayList(civil.getPassengers()));
        passengerListView.setCellFactory(new Callback<ListView<Passenger>, ListCell<Passenger>>() {
            @Override
            public ListCell<Passenger> call(ListView<Passenger> param) {
                final ListCell<Passenger> cell = new ListCell<Passenger>() {
                    @Override
                    protected void updateItem(Passenger item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getFirstName() + " " + item.getLastName() + " [" + item.getId() + "]" + " | "
                                            + (item.getTrip() != null && !item.getTrip().isFinished() ? item.getTrip().getNextRoute().getKey().getOrigin(item.getTrip().getNextRoute().getValue())
                                            + " -> "
                                            + item.getTrip().getNextRoute().getKey().getDestination(item.getTrip().getNextRoute().getValue()) : null)
                            );
                        }
                    }
                };
                return cell;
            }
        });
        passengerListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(passengerListView.getSelectionModel().getSelectedItem());
            }
        });
        main.getChildren().add(passengerListView);
        return passengerListView;
    }

    public interface OnClickListener {
        public void onClick();
    }
}
