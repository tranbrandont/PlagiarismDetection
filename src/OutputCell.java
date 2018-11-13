import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.File;

public class OutputCell extends HBox {
    Label label = new Label();
    Button viewFiles = new Button();

    public OutputCell(StudentFiles studentFiles) {
        super();

        label.setText(studentFiles.studentFile.getName().substring(0, 10) + "~ is similar to " +
                studentFiles.similarFiles.size() + " other files.");
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);

        viewFiles.setText("View files");

        viewFiles.setOnAction(e -> {
            Stage dialog = new Stage();
            DisplayTextFiles.viewFiles(dialog, studentFiles);
        });

        this.getChildren().addAll(label, viewFiles);
    }
}
