package com.mewa.data.vehicles.ships;

import com.mewa.data.ports.AbstractPort;
import com.mewa.data.ports.HasPort;
import com.mewa.data.ports.MilitaryAirport;
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
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.TEAL);
        gc.setFont(Font.font("sans-serif", FontWeight.BOLD, 14));
        gc.fillText(
                "AC",
                getLocation().getX() * GUIMain.CELL_SIZE,
                getLocation().getY() * GUIMain.CELL_SIZE,
                GUIMain.CELL_SIZE
        );
    }
}
