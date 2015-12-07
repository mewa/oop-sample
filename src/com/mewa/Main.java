package com.mewa;

import com.mewa.ui.controllers.GUIMain;
import com.mewa.utils.i.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Logger logger = GUIMain.logger;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false);
        new GUIMain().start(primaryStage);
    }

}
