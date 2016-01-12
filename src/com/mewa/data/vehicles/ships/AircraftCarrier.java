package com.mewa.data.vehicles.ships;

import com.mewa.data.location.Location;
import com.mewa.data.location.Route;
import com.mewa.data.location.World;
import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.HasPort;
import com.mewa.data.ports.MilitaryAirport;
import com.mewa.data.type.Military;
import com.mewa.data.vehicles.planes.MilitaryPlane;
import com.mewa.data.weapons.Weapon;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import java.util.Map;

/**
 * Created by Mewa on 2015-10-10.
 */
public class AircraftCarrier extends Ship implements Military, HasPort {
    private AbstractPort airport = new MilitaryAirport();

    @Override
    public AbstractPort getPort() {
        return airport;
    }

    @Override
    public Weapon getWeapon() {
        return null;
    }

    @Override
    public void setRoute(final Route route, int direction) {
        if (route != null) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep((long) (3000 + Math.random() * 3000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int pos = (int) (Math.random() * (World.getInstance().getMilitaryAirports().size() - 1));
                    MilitaryPlane militaryPlane = new MilitaryPlane();
                    militaryPlane.setLocation(new Location(getLocation().getX(), getLocation().getY()));
                    Pair<Route, Integer> r = World.getInstance().getMilitaryAirports().get(pos).getRandomRoute();
                    militaryPlane.setRoute(r.getKey(), r.getValue());

                    double dx, dy;
                    synchronized (route) {
                        dx = route.getOrigin(r.getValue()).getLocation().getX() - getLocation().getX();
                        dy = route.getOrigin(r.getValue()).getLocation().getY() - getLocation().getY();
                    }

                    militaryPlane.setFuel(militaryPlane.getFuel() + Math.sqrt(dx * dx + dy * dy));
                    World.getInstance().registerGameObject(militaryPlane);
                }
            }.start();
        }
        super.setRoute(route, direction);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.TEAL);
        gc.setFont(Font.font("sans-serif", FontWeight.BOLD, 14));
        gc.fillText(
                "AC" + getId(),
                getLocation().getX() * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE,
                GUIMain.CELL_SIZE
        );
    }
}
