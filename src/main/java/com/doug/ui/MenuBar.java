package com.doug.ui;

import com.doug.mandelbrot.Mandelbrot;
import com.doug.xml.Project;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuBar extends JMenuBar {
    private static final String INVALID_INPUT = "Invalid Input";
    private JMenuItem setParameters;
    private JMenuItem setMaxIter;
    private JMenuItem restoreDefaults;
    private JMenuItem saveImage;
    private JMenuItem saveProject;
    private JMenuItem openProject;
    private final JFrame parent;

    public MenuBar(JFrame parent)
    {
        super();

        this.parent = parent;

        createMenus();
        createListeners();
    }

    private void createMenus()
    {
        saveImage = new JMenuItem("Save Image");
        openProject = new JMenuItem("Open");
        saveProject = new JMenuItem("Save");

        JMenu fileMenu = new JMenu("File");
        fileMenu.add(openProject);
        fileMenu.add(saveProject);
        fileMenu.add(saveImage);

        setParameters = new JMenuItem("Set Parameters...");
        setMaxIter = new JMenuItem("Set max iterations...");
        restoreDefaults = new JMenuItem("Restore Defaults");

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(setParameters);
        editMenu.add(setMaxIter);
        editMenu.add(restoreDefaults);

        this.add(fileMenu);
        this.add(editMenu);
    }

    private void createListeners()
    {
        setParameters.addActionListener(e -> displaySetParameters());
        setMaxIter.addActionListener(e -> displaySetMaxIter());
        restoreDefaults.addActionListener(e -> restoreDefaults());
        saveImage.addActionListener(e -> saveImage());
        saveProject.addActionListener(e -> saveProject());
        openProject.addActionListener(e -> openProject());
    }
    
    private void displaySetParameters()
    {
        JLabel xStartLabel = new JLabel("X Start:");
        JLabel xEndLabel = new JLabel("X End:");
        JLabel yStartLabel = new JLabel("Y Start:");
        JLabel yEndLabel = new JLabel("Y End:");

        JTextField xStartField = new JTextField(Mandelbrot.getInstance().getXStart().toString(), 10);
        JTextField xEndField = new JTextField(Mandelbrot.getInstance().getXEnd().toString(), 10);
        JTextField yStartField = new JTextField(Mandelbrot.getInstance().getYStart().toString(), 10);
        JTextField yEndField = new JTextField(Mandelbrot.getInstance().getYEnd().toString(), 10);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setPreferredSize(new Dimension(200, 200));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipady = 5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        dialogPanel.add(xStartLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        dialogPanel.add(xStartField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        dialogPanel.add(xEndLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        dialogPanel.add(xEndField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        dialogPanel.add(yStartLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        dialogPanel.add(yStartField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        dialogPanel.add(yEndLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        dialogPanel.add(yEndField, constraints);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        JDialog dialog = new JDialog(parent, "Set Parameters", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(200, 200));
        dialog.setResizable(false);

        okButton.addActionListener(e1 -> {
            double xStart;
            double xEnd;
            double yStart;
            double yEnd;

            try {
                xStart = Double.parseDouble(xStartField.getText());
                xEnd = Double.parseDouble(xEndField.getText());
                yStart = Double.parseDouble(yStartField.getText());
                yEnd = Double.parseDouble(yEndField.getText());
            } catch (NumberFormatException nfe)
            {
                JOptionPane.showMessageDialog(dialog, "Please use valid numbers.",
                        INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (xStart > xEnd)
            {
                JOptionPane.showMessageDialog(dialog, "XStart must be less than XEnd",
                        INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (yStart > yEnd)
            {
                JOptionPane.showMessageDialog(dialog, "YStart must be less than YEnd",
                        INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
                return;
            }

            ImagePanel.getInstance().setParameters(xStart, xEnd, yStart, yEnd);
            dialog.dispose();
        });

        cancelButton.addActionListener(e12 -> dialog.dispose());

        dialog.add(dialogPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void displaySetMaxIter()
    {
        JLabel maxIterLabel = new JLabel("Max Iterations:");
        JTextField maxIterField = new JTextField(Mandelbrot.getInstance().getMaxIter().toString(), 10);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setPreferredSize(new Dimension(220, 200));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.ipady = 5;
        constraints.gridx = 0;
        constraints.gridy = 0;
        dialogPanel.add(maxIterLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        dialogPanel.add(maxIterField, constraints);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        JDialog dialog = new JDialog(parent, "Set Max Iterations", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(220, 200));
        dialog.setResizable(false);

        okButton.addActionListener(e1 -> {
            float maxIter;

            try {
                maxIter = Float.parseFloat(maxIterField.getText());
            } catch (NumberFormatException nfe)
            {
                JOptionPane.showMessageDialog(dialog, "Please use valid numbers.",
                        INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (maxIter < 50)
            {
                JOptionPane.showMessageDialog(dialog, "Max Iteration must be at least 50.",
                        INVALID_INPUT, JOptionPane.ERROR_MESSAGE);
                return;
            }

            ImagePanel.getInstance().setMaxIter(maxIter);
            dialog.dispose();
        });

        cancelButton.addActionListener(e12 -> dialog.dispose());

        dialog.add(dialogPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private void restoreDefaults()
    {
        int result = JOptionPane.showConfirmDialog(parent, "Restore Defaults?",
                "Restore Defaults?", JOptionPane.YES_NO_OPTION);

        if (result == 0)
        {
            ImagePanel.getInstance().restoreDefaults();
        }
    }

    private void saveImage()
    {
        BufferedImage image = ImagePanel.getInstance().getCurrentImage();

        if (image == null)
        {
            JOptionPane.showMessageDialog(parent, "Unable to save the image because it doesn't exist.",
                    "Missing Image", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG & PNG Images", "jpg", "png");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showSaveDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (file.exists())
            {
                int result = JOptionPane.showConfirmDialog(parent, "Replace the existing file?",
                        "File Exists", JOptionPane.YES_NO_OPTION);

                if (result == 1)
                {
                    return;
                }
            }

            String fileType;

            if (file.getName().toLowerCase().endsWith(".png"))
            {
                fileType = "png";
            }
            else if (file.getName().toLowerCase().endsWith("jpg"))
            {
                fileType = "jpg";
            }
            else
            {
                JOptionPane.showMessageDialog(parent, file.getName() + " is and unknown file type.",
                        "Unknown File Type", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ImageIO.write(image, fileType, file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Unable to save the file " + file.getName() + ".",
                        "Unable to Save", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveProject()
    {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "XML Files", "xml");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showSaveDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            if (file.exists())
            {
                int result = JOptionPane.showConfirmDialog(parent, "Replace the existing file?",
                        "File Exists", JOptionPane.YES_NO_OPTION);

                if (result == 1)
                {
                    return;
                }
            }

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(ImagePanel.getInstance().saveProject(), file);
            } catch (JAXBException e) {
                JOptionPane.showMessageDialog(chooser, "Unable to save the file " + file.getName() + ".",
                        "File Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openProject()
    {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "XML Files", "xml");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            Project project;

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                project = (Project)jaxbUnmarshaller.unmarshal(file);
            } catch (JAXBException e) {
                JOptionPane.showMessageDialog(chooser, "Unable to open the file " + file.getName() + ".",
                        "File Open Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ImagePanel.getInstance().loadProject(project);
        }
    }
}
