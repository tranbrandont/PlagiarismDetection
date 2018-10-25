import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by tranb7 on 5/29/18.
 */
public class DisplayTextFiles {

  public static void viewFiles(Stage primaryStage, File assignmentOne, File assignmentTwo) {
    SplitPane window = new SplitPane();
    ScrollPane fileOne = new ScrollPane();
    ScrollPane fileTwo = new ScrollPane();
    Insets value = new Insets(0, 10, 0, 10);
    fileOne.setPadding(value);
    fileTwo.setPadding(value);
//    HBox.setHgrow(fileOne, Priority.ALWAYS);
//    HBox.setHgrow(fileTwo, Priority.ALWAYS);
//    fileOne.setMaxWidth(Double.MAX_VALUE);
//    fileTwo.setMaxWidth(Double.MAX_VALUE);


    try {
      String content = new String(Files.readAllBytes(Paths.get(assignmentOne.getAbsolutePath())));
      Text text = new Text(content);
      fileOne.setContent(text);
    }
    catch (IOException error) {

    }

    try {
      String content = new String(Files.readAllBytes(Paths.get(assignmentTwo.getAbsolutePath())));
      Text text = new Text(content);
      fileTwo.setContent(text);
    }
    catch (IOException e) {

    }

    Scene scene = new Scene(window, 1000, 1000);

    window.getItems().addAll(fileOne, fileTwo);
    window.setDividerPositions(0.5f);
    primaryStage.setScene(scene);

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    //set Stage boundaries to visible bounds of the main screen
    primaryStage.setX(primaryScreenBounds.getMinX());
    primaryStage.setY(primaryScreenBounds.getMinY());
    primaryStage.setWidth(primaryScreenBounds.getWidth());
    primaryStage.setHeight(primaryScreenBounds.getHeight());
    primaryStage.setTitle("Copy Catch");
    primaryStage.show();
  }
}
