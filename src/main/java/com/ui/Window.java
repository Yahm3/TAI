package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

@SuppressWarnings("unused")
public class Window {
  public static JFrame findFrame = new JFrame();
  public static JFrame findAndReplaceFrame = new JFrame();
  public static JFrame frame = new JFrame();
  private static JTextArea textArea = new JTextArea();
  private JLabel label;
  private static JPanel fileContentPanel = new JPanel();
  private static Rectangle maxWindow = GraphicsEnvironment.getLocalGraphicsEnvironment()
      .getMaximumWindowBounds();
  private static JMenuItem newFileItem;

  public Window() {
    super();
    frame.setFont(textArea.getFont());
    frame.setSize(new Dimension(maxWindow.width, maxWindow.height));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(null);
    frame.setResizable(true);
    frame.setJMenuBar(addMenuBar());
    frame.add(addTextArea(), BorderLayout.CENTER);
    frame.add(statusLabel(), BorderLayout.SOUTH);
    frame.add(addFileContentPanel(), BorderLayout.WEST);
    setVisible();
  }

  private void findAndReplace() {
    findAndReplaceFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    findAndReplaceFrame.setLayout(new BorderLayout());
    findAndReplaceFrame.setTitle("Find and Replace");
    findAndReplaceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    findAndReplaceFrame.setSize(350, 315);
    findAndReplaceFrame.setResizable(false);
    findAndReplaceFrame.setLocationRelativeTo(null);

    var findP = new JPanel(new FlowLayout());
    var findLabel = new JLabel("Find: ");
    var findInputF = new JTextField(20);
    findP.add(findLabel, FlowLayout.LEFT);
    findP.add(findInputF);
    findP.setVisible(true);

    var replaceP = new JPanel(new FlowLayout());
    var replaceLabel = new JLabel("Replace With: ");
    var replaceInputF = new JTextField(20);
    replaceP.add(replaceLabel, FlowLayout.LEFT);
    replaceP.add(replaceInputF);
    replaceP.setVisible(true);

    var replace = new JButton("Replace");
    replace.addActionListener((e) -> {
      System.out.println("Replace");
    });

    var replaceAll = new JButton("Replace All");
    replaceAll.addActionListener((e) -> {// :NOTE: There is a method in Java called replaceAll that replaces all
                                         // occurances of input string
      System.out.println("Replace All");
    });

    var replaceBtnP = new JPanel(new FlowLayout());
    replaceBtnP.add(replace);
    replaceBtnP.add(replaceAll);
    replaceBtnP.setVisible(true);

    findAndReplaceFrame.add(findP, BorderLayout.NORTH);
    findAndReplaceFrame.add(replaceP, BorderLayout.CENTER);
    findAndReplaceFrame.add(replaceBtnP, BorderLayout.SOUTH);

    findAndReplaceFrame.pack();
    findAndReplaceFrame.setVisible(true);
  }

  public void findSearch() {
    findFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    findFrame.setLayout(new BorderLayout());
    findFrame.setTitle("Search");
    findFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    findFrame.setSize(450, 115);
    findFrame.setResizable(false);
    findFrame.setLocationRelativeTo(null);
    findFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        textArea.getHighlighter().removeAllHighlights();
      }
    });

    JPanel inputP = new JPanel(new FlowLayout());

    JLabel findL = new JLabel("Find: ");
    JTextField inputF = new JTextField(20);

    JButton searchBtn = new JButton("Search");
    searchBtn.addActionListener((e) -> {
      com.features.Search.searchText(textArea, inputF);
    });

    inputP.add(findL, FlowLayout.LEFT);
    inputP.add(inputF);
    inputP.add(searchBtn);

    inputP.setVisible(true);
    findFrame.add(inputP, BorderLayout.CENTER);

    findFrame.setVisible(true);
  }

  public JMenu helpMenu() {
    JMenu helpMenu = new JMenu("Help");

    JMenuItem license = new JMenuItem("Licese");
    JMenuItem about = new JMenuItem("about");

    // :NOTE: Add to Menu
    helpMenu.add(license);
    helpMenu.addSeparator();
    helpMenu.add(about);
    System.out.println("[INFO]: helpMenu working...");
    return helpMenu;
  }

  public JPanel addFileContentPanel() {
    fileContentPanel.setBackground(Color.MAGENTA);
    fileContentPanel.setSize((int) (maxWindow.width * .05), maxWindow.height);
    fileContentPanel.setVisible(false);
    return fileContentPanel;
  }

  public static void setFileContentPanelVisible() {
    fileContentPanel.setVisible(true);
  }

  public JLabel statusLabel() {
    label = new JLabel();
    label.setText("Line: 1, Column: 1");
    label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    return label;
  }

  public void updateStatus(int line, int col, String perc_str) {
    label.setText("Ln: " + line + " Col: " + col + " " + perc_str);
  }

  public JScrollPane addTextArea() {
    textArea.setEditable(true);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.addCaretListener(new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        JTextArea editArea = (JTextArea) e.getSource();
        try {
          int caretPos = editArea.getCaretPosition();
          int line = editArea.getLineOfOffset(caretPos);
          int col = caretPos - editArea.getLineStartOffset(line);
          int lines = textArea.getLineCount();
          line++;
          col++;
          var perc = (line / (double) lines) * 100;
          var perc_str = perc == 100 ? "Bottom" : perc == 0 ? "Top" : String.format("%.0f%%", perc);
          updateStatus(line, col, perc_str);
        } catch (Exception ex) {
        }
      }
    });
    JScrollPane scrollPane = new JScrollPane(textArea);
    return scrollPane;
  }

  public JMenuBar addMenuBar() {
    // :NOTE: Adding of the things here
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu());
    menuBar.add(searchMenu());
    menuBar.add(editMenu());
    menuBar.add(settingMenu());
    menuBar.add(helpMenu());
    return menuBar;
  }

  private void openFile(JTextArea area) {
    if (!area.getText().isEmpty() || !area.getText().isBlank()) {
      var choice = JOptionPane.showConfirmDialog(frame, "Save file before closing?", "Open File",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.INFORMATION_MESSAGE);
      if (choice == JOptionPane.YES_OPTION) {
        // :TODO: save given file from the textArea
      }
    }
    JFileChooser fileChooser = new JFileChooser();
    // :TODO: Procedurally add FileNameExtensionFilters
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
    var checkInput = fileChooser.showOpenDialog(frame);
    if (checkInput == JFileChooser.APPROVE_OPTION) {
      File openedFile = fileChooser.getSelectedFile();
      try {
        FileReader fileReader = new FileReader(openedFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String string1 = "";
        StringBuilder sb = new StringBuilder();
        while ((string1 = bufferedReader.readLine()) != null) {
          sb.append(string1).append("\n");
        }
        textArea.setText(sb.toString());
        bufferedReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public JMenu fileMenu() {
    JMenu fileMenu = new JMenu("File");

    // :NOTE: Menu stuff
    newFileItem = new JMenuItem("New file");
    JMenuItem openItem = new JMenuItem("Open File");
    openItem.addActionListener((e) -> {
      openFile(textArea);
    });
    JMenuItem saveItem = new JMenuItem("Save file");
    JMenuItem saveAsItem = new JMenuItem("Save As");
    JMenuItem exitItem = new JMenuItem("Exit");

    exitItem.addActionListener((e) -> {
      System.out.println("[INFO]: Exiting application...");
      System.exit(0);
    });

    // :NOTE: Add to Menu
    fileMenu.add(newFileItem);
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
    findItem.addActionListener((e) -> {
      findSearch();
    });
    JMenuItem replaceItem = new JMenuItem("Replace...");
    replaceItem.addActionListener((e) -> {
      findAndReplace();
    });
    JMenuItem gotoLineItem = new JMenuItem("Goto Line...");

    // :NOTE: Add to Menu
    searchMenu.add(findItem);
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

  private static JMenu themeMenu() {
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
    return theme;
  }

  private static JMenu fontMenu() {// :BUG: This method somehow does not change the font
    JMenu fontMenu = new JMenu("Font");
    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (String font_name : fonts) {
      JMenuItem item = new JMenuItem(font_name);
      item.addActionListener((e) -> {
        frame.setFont(new Font(font_name, Font.PLAIN, 14));
        // :NOTE: Update after setting the font
        frame.revalidate();
        frame.repaint();
        System.out.println("[INFO] Current font: " + font_name);
      });
      fontMenu.add(item);
    }
    // :NOTE: Total number of fonts: 2252
    System.out.println("Area font: " + textArea.getFont());
    System.out.println("JFrame font: " + frame.getFont());
    return fontMenu;
  }

  public JMenu settingMenu() {
    JMenu settingMenu = new JMenu("Setting");
    settingMenu.add(themeMenu());
    settingMenu.add(fontMenu());
    System.out.println("[INFO]: settingMenu working...");
    return settingMenu;
  }

  public void setVisible() {
    frame.setVisible(true);
  }
}
