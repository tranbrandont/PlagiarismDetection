import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ChooseDirectoryGui extends JPanel
        implements ActionListener {
    static private final String newline = "\n";
    JButton openButton;
    JTextArea log;
    JFileChooser fc;
    private static String filepath;
    private static int running = 0;

    public ChooseDirectoryGui() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        openButton = new JButton("Choose the directory where students' assignments are located");
        openButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        int returnVal = fc.showOpenDialog(ChooseDirectoryGui.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            filepath = file.getAbsolutePath();
            log.append("Opening: " + file.getName() + "." + newline);
        } else {
            log.append("Open command cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());
        running = 1;


    }


    // Create the GUI and show it.

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooser");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add content to the window.
        frame.add(new ChooseDirectoryGui());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static int main(String[] args) throws InterruptedException{
        createAndShowGUI();
        while (running == 0) {
            Thread.sleep(1000);
        }
        return running; //waits until file is chosen before continuing program

    }

    public static String getPath() {
        return filepath;

    }
}
