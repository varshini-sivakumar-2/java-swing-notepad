package com.varshini.notepad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadApp {

    static JTextArea textArea = new JTextArea();
    static JLabel statusLabel = new JLabel("Words: 0");

    public static void main(String[] args) {

        JFrame frame = new JFrame("Professional Notepad");

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(statusLabel, BorderLayout.WEST);
        frame.add(statusPanel, BorderLayout.SOUTH);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

     // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem findReplaceItem = new JMenuItem("Find & Replace");

        findReplaceItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));

        editMenu.add(findReplaceItem);
        menuBar.add(editMenu);
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Keyboard shortcuts
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        // View menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem darkModeItem = new JMenuItem("Toggle Dark Mode");
        viewMenu.add(darkModeItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);

        // New
        newItem.addActionListener(e -> textArea.setText(""));

        // Save
        saveItem.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    FileWriter writer = new FileWriter(chooser.getSelectedFile());
                    writer.write(textArea.getText());
                    writer.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Open
        openItem.addActionListener(e -> {
            try {
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    BufferedReader reader = new BufferedReader(
                            new FileReader(chooser.getSelectedFile()));
                    textArea.read(reader, null);
                    reader.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Exit
        exitItem.addActionListener(e -> System.exit(0));

        // Dark mode
        darkModeItem.addActionListener(e -> {
            if (textArea.getBackground().equals(Color.WHITE)) {
                textArea.setBackground(Color.DARK_GRAY);
                textArea.setForeground(Color.WHITE);
            } else {
                textArea.setBackground(Color.WHITE);
                textArea.setForeground(Color.BLACK);
            }
        });

        // About
        aboutItem.addActionListener(e ->
                JOptionPane.showMessageDialog(frame,
                        "Professional Notepad\nDeveloped using Java Swing",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE)
        );
        
        findReplaceItem.addActionListener(e -> {

            JDialog dialog = new JDialog(frame, "Find & Replace", true);
            dialog.setLayout(new GridLayout(3, 2, 10, 10));

            JTextField findField = new JTextField();
            JTextField replaceField = new JTextField();

            JButton findButton = new JButton("Find");
            JButton replaceButton = new JButton("Replace All");

            dialog.add(new JLabel("Find:"));
            dialog.add(findField);
            dialog.add(new JLabel("Replace With:"));
            dialog.add(replaceField);
            dialog.add(findButton);
            dialog.add(replaceButton);

            // Find functionality
            findButton.addActionListener(ev -> {
                String text = textArea.getText();
                String findText = findField.getText();

                int index = text.indexOf(findText);

                if (index >= 0) {
                    textArea.requestFocus();
                    textArea.select(index, index + findText.length());
                } else {
                    JOptionPane.showMessageDialog(dialog, "Text not found!");
                }
            });

            // Replace All functionality
            replaceButton.addActionListener(ev -> {
                String text = textArea.getText();
                String findText = findField.getText();
                String replaceText = replaceField.getText();

                text = text.replace(findText, replaceText);
                textArea.setText(text);
            });

            dialog.setSize(400, 150);
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        });

        // Word count update
        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = textArea.getText().trim();
                int words = text.isEmpty() ? 0 : text.split("\\s+").length;
                statusLabel.setText("Words: " + words);
            }
        });

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}