package com.myzuji.gui;

import com.myzuji.gui.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KeyGeneratorApplication extends Application {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Parent loadView(String fxmlPath) {
        FXMLLoader fxmlLoader = new FXMLLoader(KeyGeneratorApplication.class.getResource(fxmlPath));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("视图加载失败：" + fxmlPath);
            return null;
        }
    }

    @Override
    public void start(Stage stage) {
        KeyGeneratorApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(KeyGeneratorApplication.class.getResource("/view/index.fxml"));
        fxmlLoader.setControllerFactory(t -> MainController.class);
        fxmlLoader.setController(new MainController());
        stage.setScene(new Scene(loadView("/view/index.fxml")));
        stage.setTitle("密钥生成器!");
        stage.show();
    }
}
