import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.geometry.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by tranb7 on 5/29/18.
 */
public class DisplayTextFiles {

    public static void viewFiles(Stage primaryStage, StudentFiles studentFiles) {
        GridPane root = new GridPane();
        SplitPane window = new SplitPane();
        CodeArea fileOne = new CodeArea();
        fileOne.setParagraphGraphicFactory(LineNumberFactory.get(fileOne));
        CodeArea fileTwo = new CodeArea();
        fileTwo.setParagraphGraphicFactory(LineNumberFactory.get(fileTwo));

        ObservableList<File> files = FXCollections.observableArrayList(studentFiles.similarFiles);
        ComboBox<File> comboBox = new ComboBox<>(files);

        Button viewFiles = new Button("View files");

        GridPane.setHalignment(viewFiles, HPos.CENTER);
        GridPane.setHalignment(comboBox, HPos.RIGHT);

        viewFiles.setOnAction(e -> {

            try {
                if (comboBox.getValue() != null && !comboBox.getValue().toString().isEmpty()) {
                    String content = new String(Files.readAllBytes(Paths.get(comboBox.getValue().getAbsolutePath())));
                    fileTwo.replaceText(0, 0, content);
                }
            } catch (IOException error) {

            }
        });

        try {

            String content = new String(Files.readAllBytes(Paths.get(studentFiles.studentFile.getAbsolutePath())));
            fileOne.replaceText(0, 0, content);
        } catch (IOException error) {

        }

        root.setHgap(8);
        root.setVgap(8);
        Insets value = new Insets(0, 10, 0, 10);
        fileOne.setPadding(value);
        fileTwo.setPadding(value);
        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(cons1);

        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);
        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rcons1, rcons2);
        root.add(viewFiles, 0, 0);
        root.add(comboBox, 0, 0);
        root.add(window, 0, 1);

        Scene scene = new Scene(root, 1000, 1000);
        VirtualizedScrollPane<CodeArea> leftScreen = new VirtualizedScrollPane<>(fileOne);
        VirtualizedScrollPane<CodeArea> rightScreen = new VirtualizedScrollPane<>(fileTwo);

        window.getItems().addAll(leftScreen, rightScreen);
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
