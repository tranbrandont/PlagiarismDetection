
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class HomePageView extends Application {

    private static String assignmentDirectory;


    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);
    }

    private void initUI(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        //create all the parts of the GUI
        Button chooseDirectory = new Button("Directory");
        TextField directoryPath = new TextField();
        ListView view = new ListView();
        Button runProgram = new Button("Run");

        DirectoryChooser directoryChooser = new DirectoryChooser();


        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);
        root.getColumnConstraints().add(cons1);

        ColumnConstraints cons2 = new ColumnConstraints();
        cons2.setHgrow(Priority.ALWAYS);

        root.getColumnConstraints().addAll(cons1, cons2);

        RowConstraints rcons1 = new RowConstraints();
        rcons1.setVgrow(Priority.NEVER);

        RowConstraints rcons2 = new RowConstraints();
        rcons2.setVgrow(Priority.ALWAYS);

        root.getRowConstraints().addAll(rcons1, rcons2);


        chooseDirectory.setOnAction(e -> {
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                assignmentDirectory = selectedDirectory.getAbsolutePath();
                directoryPath.setText(assignmentDirectory);
            }
        });

        runProgram.setOnAction(e -> {
            if(directoryPath.getText() == "") {
                //throw error
            } else {
                File dir = new File(assignmentDirectory);
                File[] directoryListing = dir.listFiles();
                ConvertFile.InitializeCPPLists("cpp_keywords.txt");
                ConvertFile.InitializeCommonKeywordsMap();

                System.out.println(assignmentDirectory);
                // converts each file in directory to generalized file.
                String str = Long.toHexString(Double.doubleToLongBits(Math.random()));
                int validFileCount = 1;
                ArrayList<String> fileNames = new ArrayList<String>();
                for (File assignment : directoryListing)
                {
                    String name = assignment.getName();
                    name = name.substring(name.length() - 3, name.length());
                    if (name.equals("cpp"))
                    {
                        fileNames.add(assignment.getName());
                        System.out.print(validFileCount + ": ");
                        System.out.println(assignment);
                        ConvertFile.textConverter(assignment, str);
                        // ConvertFile.textConverter(assignment, str);
                        validFileCount++;
                    }
                }
                String[][] scores = FileComparer.CompareFiles(str, validFileCount - 1);
                for (int i = 0; i < scores.length; i++)
                {
                    for (int j = i; j < scores.length; j++)
                    {
                        //Only prints those above given threshold
                        if (StrToDouble(scores[i][j]) >= 90)
                        {
                            System.out.println(fileNames.get(i).substring(0, 10) + "~ is " + scores[i][j]
                                    + "% similar to assignment " + fileNames.get(j+1).substring(0, 10) + "~");
                        }
                    }
                }
                System.out.println("Done!");
            }
        });

        GridPane.setHalignment(runProgram, HPos.RIGHT);

        root.add(chooseDirectory, 0, 0);
        root.add(directoryPath, 1, 0, 2, 1);
        root.add(view, 0, 1, 4, 2);
        root.add(runProgram, 3, 0);

        Scene scene = new Scene(root, 280, 300);

        primaryStage.setTitle("Copy Catch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static double StrToDouble(String str)
    {
        return Double.parseDouble(str);
    }
}

