package com.mewa.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Mewa on 2015-12-06.
 */
public interface Drawable {
    /**
     * rysuje obiekt w kontekscie graficznym gc
     * @param gc
     */
    void draw(GraphicsContext gc);
}
