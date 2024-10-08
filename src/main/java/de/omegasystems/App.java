package de.omegasystems;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.omegasystems.components.GameComponent;
import de.omegasystems.components.MenubarComponent;
import de.omegasystems.components.dialog.ChangeValueDialog;
import de.omegasystems.dataobjects.MenubarAttributeHolder;

public class App {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App().CreateAndShowGUI();
            }
        });
    }

    private static App instance;

    private JFrame frame;
    private MenubarAttributeHolder toolbarAttributes = new MenubarAttributeHolder();

    public App() {
        instance = this;
    }

    private void CreateAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("TTRPG Map Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getToolbarAttributes().VIEW_GRID_OPEN_SCALE_DIALOG
                .addObserver(abs -> new ChangeValueDialog.DoubleDialog(frame, getToolbarAttributes().VIEW_GRID_SCALE));
        getToolbarAttributes().VIEW_GRID_OPEN_THICKNESS_DIALOG
                .addObserver(
                        a -> new ChangeValueDialog.DoubleDialog(frame, getToolbarAttributes().VIEW_GRID_THICKNESS));

        var img = rqeuestImageFromUser();
        if (img == null)
            System.exit(0);

        frame.add(new GameComponent(img));
        frame.setJMenuBar(new MenubarComponent(toolbarAttributes));

        frame.setMaximumSize(new Dimension(800, 600));
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationByPlatform(true);

        frame.setVisible(true); // Show the window

    }

    /**
     * 
     * @return An image if successfull or null otherwise
     */
    private Image rqeuestImageFromUser() {

        try {
            return ImageIO.read(new File(System.getProperty("user.dir") + "/ressources/img/Tavern_Battlemap.jpg"));
        } catch (Exception e) {
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle("Choose a map");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes()));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                return ImageIO.read(selectedFile);
            } catch (Exception e) {
            }
        }

        return null;
    }

    public MenubarAttributeHolder getToolbarAttributes() {
        return toolbarAttributes;
    }

    public static App getInstance() {
        return instance;
    }
}
