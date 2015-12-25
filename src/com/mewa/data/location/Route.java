package com.mewa.data.location;

import com.mewa.Main;
import com.mewa.data.ports.HasPort;
import com.mewa.ui.Drawable;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mewa on 2015-10-10.
 */
public class Route implements Drawable {
    private final List<Location> locations = Collections.synchronizedList(new ArrayList<Location>());

    public List<Location> getStops() {
        return locations;
    }

    public Location getOrigin() {
        return locations.get(0);
    }

    public Location getDestination() {
        return locations.get(locations.size() - 1);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(1.5);
        Main.logger.log(Logger.VERBOSE, "draw " + this + " " + locations);
        gc.moveTo((locations.get(0).getX() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE, (locations.get(0).getY() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE);
        for (int i = 0; i < locations.size() - 1; ++i) {
            gc.lineTo((locations.get(i + 1).getX() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE, (locations.get(i + 1).getY() + 0.25 + Math.random() / 2) * GUIMain.CELL_SIZE);
        }
        gc.stroke();
    }
}
