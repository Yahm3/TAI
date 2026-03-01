package com.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

//@SuppressWarnings("unused")
public class Window {
  private static JFrame frame = new JFrame();

  public Window() {
    super();
    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    var maxWindow = env.getMaximumWindowBounds();

    frame.setSize(new Dimension(maxWindow.width, maxWindow.height));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(null);
    frame.setResizable(true);
    frame.setJMenuBar(addMenuBar());
    setVisible();
  }

  public JMenuBar addMenuBar() {
    try {
    } catch (Exception e) {
    }

    // :NOTE: Adding of the things here
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu());
    menuBar.add(searchMenu());
    menuBar.add(editMenu());
    menuBar.add(settingMenu());
    return menuBar;
  }

  public JMenu fileMenu() {
    JMenu fileMenu = new JMenu("File");

    // :NOTE: Menu stuff
    JMenuItem openFolderItem = new JMenuItem("Open Folder");
    JMenuItem openItem = new JMenuItem("Open File");
    JMenuItem saveItem = new JMenuItem("Save file");
    JMenuItem saveAsItem = new JMenuItem("Save As");
    JMenuItem exitItem = new JMenuItem("Exit");

    exitItem.addActionListener((e) -> {
      System.exit(0);
    });

    // :NOTE: Add to Menu
    fileMenu.add(openFolderItem);
    fileMenu.add(openItem);
    fileMenu.addSeparator();
    fileMenu.add(saveItem);
    fileMenu.add(saveAsItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);
    System.out.println("[INFO]: fileMenu working...");
    return fileMenu;
  }

  public JMenu searchMenu() {
    JMenu searchMenu = new JMenu("Search");

    // :NOTE: Menu stuff
    JMenuItem findItem = new JMenuItem("Find...");
    JMenuItem findNextItem = new JMenuItem("Find Next");
    JMenuItem findPreviousItem = new JMenuItem("Find Precious");
    JMenuItem replaceItem = new JMenuItem("Replace...");
    JMenuItem gotoLineItem = new JMenuItem("Goto Line...");

    // :NOTE: Add to Menu
    searchMenu.add(findItem);
    searchMenu.add(findNextItem);
    searchMenu.add(findPreviousItem);
    searchMenu.addSeparator();
    searchMenu.add(replaceItem);
    searchMenu.addSeparator();
    searchMenu.add(gotoLineItem);
    System.out.println("[INFO]: searchMenu working...");
    return searchMenu;
  }

  public JMenu editMenu() {
    JMenu editMenu = new JMenu("Edit");

    // :NOTE: Menu stuff
    JMenuItem undoItem = new JMenuItem("Undo");
    JMenuItem redoItem = new JMenuItem("Redo");
    JMenuItem cutItem = new JMenuItem("Cut");
    JMenuItem copyItem = new JMenuItem("Copy");
    JMenuItem pasteItem = new JMenuItem("Paste");
    JMenuItem deleteItem = new JMenuItem("Delete");
    JMenuItem indentItem = new JMenuItem("Indent");

    // :NOTE: Add to Menu
    editMenu.add(undoItem);
    editMenu.add(redoItem);
    editMenu.addSeparator();
    editMenu.add(cutItem);
    editMenu.add(copyItem);
    editMenu.add(pasteItem);
    editMenu.add(deleteItem);
    editMenu.addSeparator();
    editMenu.add(indentItem);
    editMenu.add(deleteItem);
    System.out.println("[INFO]: editMenu working...");
    return editMenu;
  }

  public JMenu settingMenu() {
    JMenu settingMenu = new JMenu("Setting");

    JMenu theme = new JMenu("theme");
    String[] themes = { "Light", "Dark", "Dracula", "Gradient Fuchsia", "Gradient Blue", "Gradient Green", "Cyan light",
        "darkPurple" };

    for (String theme_name : themes) {
      JMenuItem item = new JMenuItem(theme_name);

      item.addActionListener((e) -> {
        if (theme_name.equalsIgnoreCase("light")) {
          FlatLightLaf.setup();
        } else if (theme_name.equalsIgnoreCase("dark")) {
          FlatDarkLaf.setup();
        } else if (theme_name.equalsIgnoreCase("Dracula")) {
          FlatDarculaLaf.setup();
        } else if (theme_name.equalsIgnoreCase("Cyan light")) {
          FlatCyanLightIJTheme.setup();
        } else if (theme_name.equalsIgnoreCase("darkPurple")) {
          com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme.setup();
        } else if (theme_name.equalsIgnoreCase("Gradient Green")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme.setup();
        } else if (theme_name.equalsIgnoreCase("Gradient Fuchsia")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme.setup();
        } else if (theme_name.equalsIgnoreCase("Gradient Blue")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme.setup();
        } else {
          JOptionPane.showMessageDialog(null, "Could not set " + theme_name, "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        SwingUtilities.updateComponentTreeUI(frame);
        System.out.println("[INFO] Current theme: " + theme_name);
      });
      theme.add(item);
    }

    settingMenu.add(theme);
    System.out.println("[INFO]: settingMenu working...");
    return settingMenu;
  }

  public void setVisible() {
    frame.setVisible(true);
  }
}
