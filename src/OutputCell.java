import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OutputCell extends HBox {
    Label label = new Label();
    Button viewFiles = new Button();

    public OutputCell(File firstFile, File secondFile, String score) {
        super();

        label.setText(firstFile.getName().substring(0, 10) + "~ is " + score
                + "% similar to assignment " + secondFile.getName().substring(0, 10) + "~");
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);

        viewFiles.setText("View files");

        viewFiles.setOnAction(e -> {
            try {
                java.awt.Desktop.getDesktop().edit(firstFile);
            }
            catch (IOException err) {

            }
        });

        this.getChildren().addAll(label, viewFiles);
    }
}
