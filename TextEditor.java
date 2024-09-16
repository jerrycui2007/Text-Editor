/**
 * [TextEditor.java]
 * Class for the text editing interface
 *
 * @author Jerry Cui
 * @version 1.0
 */

// Required imports
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Class for the window
 */
public class TextEditor extends JFrame implements ActionListener {
    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel fontLabel;
    JSpinner fontSizeSpinner;
    JButton fontColourButton;
    JComboBox<String> fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    /**
     * Constructor for the <code>TextEditor</code> object, creates the window
     */
    TextEditor() {
        // Set up the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);  // always opens in center of the screen

        // Area for typing text
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 12));  // default font

        // Vertical scroll bar
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Font size changer
        fontLabel = new JLabel("Font size: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(12);
        fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN,
                (int) fontSizeSpinner.getValue())));

        // Colour changer
        fontColourButton = new JButton("Colour");
        fontColourButton.addActionListener(this);

        // Font changer
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox<>(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Times New Roman");  // default font

        // Menu bar options
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        // Action listeners for each option
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        // Set up the mnu options
        fileMenu.add(fileMenu);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);

        // Add everything to the JFrame
        this.add(fontLabel);
        this.add(fontSizeSpinner);

        this.add(fontColourButton);
        this.add(fontBox);

        this.add(scrollPane);

        this.setVisible(true);

    }

    /**
     * actionPerformed
     * Processes any events received from
     * - <code>fontColourButton</code>
     * - <code>fontBox</code>
     * - <code>openItem</code>
     * - <code>saveItem</code>
     * - <code>exitItem</code>
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Colour button was clicked, allow user to choose a colour and change the text colour to match
        if (e.getSource() == fontColourButton) {
            Color color = JColorChooser.showDialog(null, "Choose a colour", Color.black);

            textArea.setForeground(color);
        }

        // Font was changed, change text to new font
        if (e.getSource() == fontBox) {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        // User wants to open a text file
        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));  // default search directory is project folder

            // Filter for .txt files only
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    // Take in the data in the .txt file
                    Scanner fileIn = new Scanner(file);
                    if (file.isFile()) {
                        while (fileIn.hasNextLine()) {
                            String line = fileIn.nextLine() + "\n";
                            textArea.append(line);  // add it to the current text area
                        }
                    }

                    fileIn.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }

        // User wants to save a text file
        if (e.getSource() == saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));   // default search directory is project folder

            // Filter for .txt files only
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showSaveDialog(null);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                try {
                    // Write all the text to the new file
                    PrintWriter fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                    fileOut.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // User wants to exit the program
        if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }
}
