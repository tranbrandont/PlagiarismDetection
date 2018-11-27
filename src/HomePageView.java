
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomePageView extends Application {

    private static String assignmentDirectory;
    private static ArrayList<File> validFiles;
    private static List<FileStats> fileStats;
    private static ListView<OutputCell> outputListView;
    private static Stage dialogStage;
    private static Label progressText;
    private static boolean comparisonRan = false;
    private final int initialValue = 70;

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

        Label spinnerLabal = new Label("Similarity Threshold");
        Spinner similarityTreshhold = new Spinner();
        similarityTreshhold.setEditable(true);

        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, initialValue);

        similarityTreshhold.setValueFactory(valueFactory);

        similarityTreshhold.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                similarityTreshhold.increment(0); // won't change value, but will commit editor
            }
        });
        outputListView = new ListView<OutputCell>();
        Button runProgram = new Button("Run");

        DirectoryChooser directoryChooser = new DirectoryChooser();



        ColumnConstraints cons1 = new ColumnConstraints();
        cons1.setHgrow(Priority.NEVER);

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
            if(directoryPath.getText().equals("")) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("No Selected Directory");
                alert.setHeaderText(null);
                alert.setContentText("You must select a directory to run the scan on first.");
                alert.showAndWait();
                return;

            } else {
            	if(comparisonRan)
            	{
            		// Ask user if they want to run ANOTHER comparison with dialog
            		// Inform them that prior results will be lost.
            		// Get buttonclick result and proceed or return
            		Alert alert = new Alert(AlertType.CONFIRMATION);
            		alert.setTitle("New Comparison Confirmation");
            		alert.setHeaderText(null);
            		alert.setContentText("If you run another comparison, your prior results will be lost. Proceed?");

            		Optional<ButtonType> result = alert.showAndWait();
            		if (result.get() == ButtonType.CANCEL)
            		{
            			return;
            		}
            	}
            	outputListView.setItems(null);
            	fileStats = new ArrayList<FileStats>();
                File dir = new File(assignmentDirectory);
                File[] directoryListing = dir.listFiles();
                ConvertFile.InitializeCPPLists("cpp_keywords.txt");
                ConvertFile.InitializeCommonKeywordsMap();

                System.out.println(assignmentDirectory);
                // converts each file in directory to generalized file.
                //String str = Long.toHexString(Double.doubleToLongBits(Math.random()));
                int validFileCount = 1;
                validFiles = new ArrayList<File>();
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

                        ConvertFile.textConverter(assignment, fileStats.get(fileStats.size()-1));
                        validFileCount++;
                    }
                }

                Task<Void> task = new Task<Void>()
                {
                    @Override public Void call()
                    {
                        final int totalComparisons = (fileStats.size()*(fileStats.size()-1))/2;
                        int completedComparisons = 0;
                        FileStats.SetScoresSize(fileStats.size()-1);
                        diff_match_patch dmp = new diff_match_patch();
                        FileComparer.FindOutliers(fileStats);
                        for (int i=0; i < fileStats.size()-1; i++)
                        {
                            for (int j = i+1; j <= fileStats.size()-1; j++ )
                            {
                            	String str1 = fileStats.get(i).GetAllLinesAsString();
                				String str2 = fileStats.get(j).GetAllLinesAsString();
                				// DIFF IMPLEMENTATION
                			    LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(str1, str2);
                			    dmp.diff_cleanupSemantic(diff);
                			    //System.out.println(diff);
                			    int distance = dmp.diff_levenshtein(diff);
                			    // /DIFF IMPLEMENTATION
                				//int distance = FileComparer.CalculateEditDistance(str1, str2);

                				int bigger = Math.max(str1.length(), str2.length());
                				double percent = (bigger - distance) / (double) bigger * 100;
                				FileStats.scores[i][j - 1] = Double.parseDouble(String.format("%.2f", percent));
                				completedComparisons++;
                            	updateProgress(completedComparisons, totalComparisons);
                            }
                        }
                        return null;
                    }
                };
                task.setOnSucceeded(ep -> {
                	DisplayResults(Integer.parseInt(similarityTreshhold.getValue().toString()));
                });
                dialogStage = new Stage();
                dialogStage.initStyle(StageStyle.UTILITY);
                dialogStage.setOnCloseRequest(pe ->{
                	pe.consume();
                });
                dialogStage.setResizable(false);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                ProgressBar pbar = new ProgressBar();
                progressText = new Label();

                pbar.progressProperty().bind(task.progressProperty());
                pbar.setPrefWidth(300);
                pbar.progressProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> ov, Number t, Number newValue) {
                    	String str = Integer.toString((int)((double)newValue*100)+1)+ "%";
                        progressText.setText(str);
                    }
                });
                BorderPane bp = new BorderPane ();
                bp.setPadding(new Insets (10,10,10,10));
                bp.setLeft(pbar);
                bp.setRight(progressText);
                BorderPane.setAlignment(progressText, Pos.CENTER_RIGHT);
                BorderPane.setAlignment(pbar, Pos.CENTER_LEFT);
                Scene scene = new Scene(bp);
                dialogStage.setScene(scene);
                dialogStage.setHeight(100);
                dialogStage.setWidth(400);
                dialogStage.setTitle("Comparing Files...");
                dialogStage.show();
                new Thread(task).start();;
            }
        });



        GridPane.setHalignment(runProgram, HPos.RIGHT);
        //add items to window
        root.add(chooseDirectory, 0, 0);
        root.add(directoryPath, 1, 0);
        root.add(outputListView, 0, 1, 5, 2);
        root.add(runProgram, 4, 0);
        root.add(spinnerLabal, 2, 0);
        root.add(similarityTreshhold, 3, 0);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Copy Catch");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* No longer needed?
    private static double StrToDouble(String str)
    {
        return Double.parseDouble(str);
    }
    */
    public void DisplayResults(int similarityThreshhold) {
        comparisonRan = true;
        dialogStage.hide();
        double scores[][] = FileStats.scores;

        List<OutputCell> listResults = new ArrayList<>();
        for (int i = 0; i < scores.length; i++) {
            StudentFiles studentFiles = new StudentFiles(validFiles.get(i));
            for (int j = i; j < scores.length; j++) {
                //Only prints those above given threshold
                //TODO: set this to a variable
                if (scores[i][j] >= similarityThreshhold) {
                    FileSimilarity fileSimilarity = new FileSimilarity(validFiles.get(j + 1),scores[i][j]);
                    studentFiles.addFile(fileSimilarity);
                }
            }
            if (!studentFiles.similarFiles.isEmpty()) {
                listResults.add(new OutputCell(studentFiles));
            }
        }
        ObservableList<OutputCell> observableListResults = FXCollections.observableList(listResults);
        outputListView.setItems(observableListResults);
        System.out.println("Done!");
    }
}

