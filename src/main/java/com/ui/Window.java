package com.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.formdev.flatlaf.FlatDarculaLaf;

@SuppressWarnings("unused")
public class Window {
  private static JFrame frame = new JFrame();

  public Window() {
    super();
    frame.setSize(900, 500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(null);
    frame.setMinimumSize(new Dimension(900, 500));
    frame.setJMenuBar(addMenuBar());
    setVisible();
  }

  private JMenuBar addMenuBar() {
    try {
      FlatDarculaLaf.setup();
    } catch (Exception e) {
      System.out.println("WARNING: could not setup flatlaf");
    }
    System.setProperty("flatlaf.menuBarEmbedded", "true");

    // :NOTE: Adding of the things here
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu());
    menuBar.add(settingMenu());
    return menuBar;
  }

  public JMenu fileMenu() {
    JMenu fileMenu = new JMenu("File");
    Icon saveIcon = new ImageIcon(getClass().getResource("./"));// :TODO: Add icons
    Icon openIcon = new ImageIcon(getClass().getResource("./"));
    System.out.println("[INFO]: fileMenu working...");
    return fileMenu;
  }

  public JMenu settingMenu() {
    JMenu settingMenu = new JMenu("Setting");
    JMenuItem theme = new JMenuItem("theme");
    Icon Darktheme = new ImageIcon(getClass().getResource("./"));// :TODO: Add icons
    Icon Light = new ImageIcon(getClass().getResource("./"));
    Icon DraculaLight = new ImageIcon(getClass().getResource("./"));
    Icon DraculaDark = new ImageIcon(getClass().getResource("./"));

    // :NOTE: Add things here!
    settingMenu.add(theme);
    System.out.println("[INFO]: settingMenu working...");
    return settingMenu;
  }

  public void setVisible() {
    frame.setVisible(true);
  }
}
