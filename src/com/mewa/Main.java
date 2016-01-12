package com.mewa;

import com.mewa.data.location.World;
import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Main extends Application implements Serializable{

    public static Logger logger = GUIMain.logger;
    private transient GUIMain gui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        gui = new GUIMain();
        gui.start(primaryStage);
    }

}
