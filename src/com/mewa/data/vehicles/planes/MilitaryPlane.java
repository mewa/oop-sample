package com.mewa.data.vehicles.planes;

import com.mewa.data.location.Route;
import com.mewa.data.type.Military;
import com.mewa.data.weapons.Weapon;
import com.mewa.ui.controllers.GUIMain;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by Mewa on 2015-10-10.
 */
public class MilitaryPlane extends Plane implements Military {
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.TEAL);
        gc.setFont(Font.font("sans-serif", FontWeight.BOLD, 14));
        gc.fillText(
                "Int" + getId(),
                getLocation().getX() * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE,
                GUIMain.CELL_SIZE
        );
    }

    @Override
    public void setRoute(Route route, int direction) {
        super.setRoute(route, direction);
        if (route != null) {
            route.incStop(this, direction);
        }
    }

    @Override
    public Weapon getWeapon() {
        return null;
    }
}
