
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class HomePageView extends Application {

    private static String assignmentDirectory;
    
    private static List<FileStats> fileStats;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initUI(primaryStage);
    }

    private void initUI(Stage primaryStage) {

        //create gridpane for window
        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        //create all the parts of the GUI
        Button chooseDirectory = new Button("Directory");
        TextField directoryPath = new TextField();
        ListView<OutputCell> outputListView = new ListView<OutputCell>();
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

        //on directory button click
        chooseDirectory.setOnAction(e -> {
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory != null) {
                assignmentDirectory = selectedDirectory.getAbsolutePath();
                directoryPath.setText(assignmentDirectory);
            }
        });

        //on run program button click
        runProgram.setOnAction(e -> {
            if(directoryPath.getText() == "") {
                //throw error
            } else {
            	fileStats = new ArrayList<FileStats>();
                File dir = new File(assignmentDirectory);
                File[] directoryListing = dir.listFiles();
                ConvertFile.InitializeCPPLists("cpp_keywords.txt");
                ConvertFile.InitializeCommonKeywordsMap();

                System.out.println(assignmentDirectory);
                // converts each file in directory to generalized file.
                //String str = Long.toHexString(Double.doubleToLongBits(Math.random()));
                int validFileCount = 1;
                ArrayList<File> validFiles = new ArrayList<File>();
                for (File assignment : directoryListing)
                {
                    String name = assignment.getName();
                    name = name.substring(name.length() - 3, name.length());
                    if (name.equals("cpp"))
                    {
                        validFiles.add(assignment);
                        System.out.print(validFileCount + ": ");
                        System.out.println(assignment);
                        
                        // Implementing FileStats
                        fileStats.add(new FileStats(assignment.getName()));
                        
                        // ConvertFile.textConverter(assignment, str);
                        
                        ConvertFile.textConverter(assignment, fileStats.get(fileStats.size()-1));
                        validFileCount++;
                    }
                }
                //String[][] scores = FileComparer.CompareFiles(str, validFileCount - 1);
                String[][] scores = FileComparer.CompareFiles(fileStats, runProgram);
                List<OutputCell> listResults = new ArrayList<>();
                for (int i = 0; i < scores.length; i++)
                {
                    for (int j = i; j < scores.length; j++)
                    {
                        //Only prints those above given threshold
                        if (StrToDouble(scores[i][j]) >= 90)
                        {
                            listResults.add(new OutputCell(validFiles.get(i), validFiles.get(j+1), scores[i][j]));
                        }
                    }
                }
                ObservableList<OutputCell> observableListResults = FXCollections.observableList(listResults);
                outputListView.setItems(observableListResults);
                System.out.println("Done!");
            }
        });

        GridPane.setHalignment(runProgram, HPos.RIGHT);
        //add items to window
        root.add(chooseDirectory, 0, 0);
        root.add(directoryPath, 1, 0, 2, 1);
        root.add(outputListView, 0, 1, 4, 2);
        root.add(runProgram, 3, 0);

        Scene scene = new Scene(root, 550, 600);

        primaryStage.setTitle("Copy Catch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static double StrToDouble(String str)
    {
        return Double.parseDouble(str);
    }
}

