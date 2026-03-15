package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.AbstractAction;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javax.swing.Action;

import com.features.EditorSettings;
import com.features.Search;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;

//@SuppressWarnings("unused")
public class Window {
  public static JFrame findFrame = new JFrame();
  private static JFrame findAndReplaceFrame = new JFrame();
  private static JFrame gotoLineFrame = new JFrame();
  public static JFrame frame = new JFrame();
  // private static RSyntaxTextArea getActiveTextArea() = new RSyntaxTextArea();
  private static int tabCount = 1;
  private static JTabbedPane tabbedPane = new JTabbedPane();
  private JLabel label;
  private static JPanel fileContentPanel = new JPanel();
  private static Rectangle maxWindow = GraphicsEnvironment.getLocalGraphicsEnvironment()
      .getMaximumWindowBounds();
  private static JMenuItem newFileItem;
  private final UndoManager undo;
  private Document doc;

  public Window() {
    super();
    addNewTab("TAI V0.0.1" + tabCount++, startupInfo());
    frame.setFont(
        new Font(EditorSettings.getSavedFamily(), EditorSettings.getSavedStyle(), EditorSettings.getSavedSize()));
    frame.setSize(new Dimension(maxWindow.width, maxWindow.height));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(null);
    frame.setResizable(true);
    frame.setJMenuBar(addMenuBar());
    frame.add(tabbedPane, BorderLayout.CENTER);
    frame.add(statusLabel(), BorderLayout.SOUTH);
    frame.add(addFileContentPanel(), BorderLayout.WEST);

    // :NOTE: RSyntaxTextArea stuff
    RSyntaxTextArea active = getActiveTextArea();
    active.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
    active.setCodeFoldingEnabled(true);
    active.setBackground(frame.getBackground());
    active.setHighlightCurrentLine(false);
    active.setFont(new Font(frame.getFont().getFontName(), frame.getFont().getStyle(), frame.getFont().getSize()));

    undo = new UndoManager();
    doc = getActiveTextArea().getDocument();

    // Listen for undo and redo events
    doc.addUndoableEditListener(new UndoableEditListener() {
      @Override
      public void undoableEditHappened(UndoableEditEvent e) {
        undo.addEdit(e.getEdit());
      }
    });

    // Create an undo action and add it to the text component
    getActiveTextArea().getActionMap().put("Undo", new AbstractAction("Undo") {
      @Override
      public void actionPerformed(ActionEvent evt) {
        try {
          if (undo.canUndo()) {
            undo.undo();
          }
        } catch (CannotUndoException e) {
          e.printStackTrace();
        }
      }
    });
    // Bind the undo action to ctl-Z
    getActiveTextArea().getInputMap().put(KeyStroke.getKeyStroke("Control Z"), "Undo");

    // Create a redo action and add it to getActiveTextArea()
    getActiveTextArea().getActionMap().put("Redo", new AbstractAction("Redo") {
      @Override
      public void actionPerformed(ActionEvent evt) {
        try {
          if (undo.canRedo()) {
            undo.redo();
          }
        } catch (CannotRedoException e) {
          e.printStackTrace();
        }
      }
    });
    // Bind the undo action to ctl-Y
    getActiveTextArea().getInputMap().put(KeyStroke.getKeyStroke("Control Y"), "Redo");

    String savedTheme = EditorSettings.getSavedTheme();
    applyTheme(savedTheme);

    String family = EditorSettings.getSavedFamily();
    int style = EditorSettings.getSavedStyle();
    int size = EditorSettings.getSavedSize();
    updateFont(family, style, size); // This method you already have!
    setVisible();
  }

  private JPanel createTabTitleComponent(String title) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
    panel.setOpaque(false);

    JLabel titleLabel = new JLabel(title);
    JButton closeButton = new JButton("x");

    closeButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(2, 4, 2, 4)));
    closeButton.setContentAreaFilled(false);
    closeButton.setRolloverEnabled(true);
    closeButton.putClientProperty("JButton.hoveredBackground", new Color(255, 100, 100, 100));
    closeButton.putClientProperty("JButton.hoveredBackground", Color.RED);
    closeButton.setFocusable(false);
    closeButton.setPreferredSize(new Dimension(20, 20));

    closeButton.addActionListener(e -> {
      int index = tabbedPane.indexOfTabComponent(panel);
      if (index != -1) {
        // TODO: Add a "Save before closing?" check here
        tabbedPane.remove(index);

        if (tabbedPane.getTabCount() == 0) {
          addNewTab("untitled " + tabCount++, "");
        }
      }
    });

    panel.add(titleLabel);
    panel.add(closeButton);
    return panel;
  }

  private static RSyntaxTextArea getActiveTextArea() {
    if (tabbedPane.getTabCount() == 0)
      return null;
    JScrollPane scrollPane = (JScrollPane) tabbedPane.getSelectedComponent();
    if (scrollPane != null) {
      return (RSyntaxTextArea) scrollPane.getViewport().getView();
    }
    return null;
  }

  private void applyTheme(String themeClassName) {
    try {
      UIManager.setLookAndFeel(themeClassName);
      SwingUtilities.updateComponentTreeUI(frame);
      EditorSettings.saveTheme(themeClassName);
      if (getActiveTextArea() != null) {
        getActiveTextArea().setBackground(frame.getBackground());
      }
      System.out.println("[INFO] Theme applied and saved: " + themeClassName);
    } catch (Exception e) {
      System.err.println("[ERROR] Failed to apply theme: " + themeClassName);
      e.printStackTrace();
    }
  }

  private void gotoLine() {
    gotoLineFrame.setIconImage(new ImageIcon(getClass().getResource("/icons/sit.png")).getImage());
    gotoLineFrame.setLayout(new BorderLayout());
    gotoLineFrame.setTitle("Find and Replace");
    gotoLineFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    gotoLineFrame.setSize(350, 315);
    gotoLineFrame.setResizable(false);
    gotoLineFrame.setLocationRelativeTo(null);

    var gotoLineP = new JPanel(new FlowLayout());
    var gotoLineLabel = new JLabel("Line: ");
    var gotoLineInputF = new JTextField(20);
    var userInput = new JButton("Go");
    userInput.addActionListener((e) -> {
      try {
        int lineNum = Integer.parseInt(gotoLineInputF.getText());
        int totalLines = getActiveTextArea().getLineCount();

        if (lineNum > 0 && lineNum <= totalLines) {
          int offset = getActiveTextArea().getLineStartOffset(lineNum - 1);
          getActiveTextArea().setCaretPosition(offset);

          getActiveTextArea().requestFocusInWindow();
          gotoLineFrame.dispose();
        } else {
          Search.vibrate(gotoLineFrame);
        }
      } catch (NumberFormatException nfe) {
        Search.vibrate(gotoLineFrame);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    gotoLineP.add(gotoLineLabel, FlowLayout.LEFT);
    gotoLineP.add(gotoLineInputF);
    gotoLineP.add(userInput);
    gotoLineP.setVisible(true);

    gotoLineFrame.add(gotoLineP);
    gotoLineFrame.pack();
    gotoLineFrame.setVisible(true);
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
      String findText = findInputF.getText();
      String replaceText = replaceInputF.getText();
      String selection = getActiveTextArea().getSelectedText();

      if (selection != null && selection.equals(findText)) {
        getActiveTextArea().replaceSelection(replaceText);
      }

      String fullText = getActiveTextArea().getText();
      int nextIndex = fullText.indexOf(findText, getActiveTextArea().getCaretPosition());

      if (nextIndex != -1) {
        getActiveTextArea().setSelectionStart(nextIndex);
        getActiveTextArea().setSelectionEnd(nextIndex + findText.length());
      } else {
        int wrapIndex = fullText.indexOf(findText, 0);
        if (wrapIndex != -1) {
          getActiveTextArea().setSelectionStart(wrapIndex);
          getActiveTextArea().setSelectionEnd(wrapIndex + findText.length());
        } else {
          Search.vibrate(findAndReplaceFrame);
        }
      }
    });

    var replaceAll = new JButton("Replace All");
    replaceAll.addActionListener((e) -> {
      var selectedText = getActiveTextArea().getSelectedText();

      if (selectedText != null) {
        findInputF.setText(selectedText);
        getActiveTextArea().replaceSelection(replaceInputF.getText());
        return;
      }
      getActiveTextArea()
          .setText(getActiveTextArea().getText().replaceAll(findInputF.getText(), replaceInputF.getText()));
    });

    var replaceBtnP = new JPanel(
        new FlowLayout());
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
        getActiveTextArea().getHighlighter().removeAllHighlights();
      }
    });

    JPanel inputP = new JPanel(new FlowLayout());

    JLabel findL = new JLabel("Find: ");
    JTextField inputF = new JTextField(20);

    JButton searchBtn = new JButton("Search");
    searchBtn.addActionListener((e) -> {
      if (inputF.getText() == null || inputF.getText().isEmpty() || inputF.getText().isBlank()) {
        Search.vibrate(findFrame);
      }
      com.features.Search.searchText(getActiveTextArea(), inputF);
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

  public String startupInfo() {
    return "author: https://github.com/Yahm3/" +
        "\nVersion: 0.0.1" +
        "\nAbout: This version just has some features as it this application is still being developed" +
        "\nResources: " +
        "\n\t\tFlatLaf(https://www.formdev.com/flatlaf/)" +
        "\n\t\t RSyntaxTextArea(https://github.com/bobbylight/RSTALanguageSupport)" +
        "\n\t\t autocomplete(https://mvnrepository.com/artifact/com.fifesoft/autocomplete)" +
        "\n\t\tGson(https://github.com/google/gson)";
  }

  public JScrollPane addTextArea() {
    var area = getActiveTextArea();
    area.setEditable(true);
    area.setText(startupInfo());
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    area.addCaretListener(new CaretListener() {

      @Override
      public void caretUpdate(CaretEvent e) {
        JTextArea editArea = (JTextArea) e.getSource();
        try {
          int caretPos = editArea.getCaretPosition();
          int line = editArea.getLineOfOffset(caretPos);
          int col = caretPos - editArea.getLineStartOffset(line);
          int lines = getActiveTextArea().getLineCount();
          line++;
          col++;
          var perc = (line / (double) lines) * 100;
          var perc_str = perc == 100 ? "Bottom" : perc == 0 ? "Top" : String.format("%.0f%%", perc);
          updateStatus(line, col, perc_str);
        } catch (Exception ex) {
        }
      }
    });
    JScrollPane scrollPane = new JScrollPane(getActiveTextArea());
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

  private void saveFile(JTextArea area) {
    if (area.getText().isEmpty() || area.getText().isBlank()) {
      /*
       * Save what because there is nothing already saved?
       * But at the same time if the file was already opened from a saved state that
       * counts as a change and therefore the user should be
       * able to save the file even if it's empty
       * :TODO: Do something here
       */
      Search.vibrate(frame);
      return;
    }
    try {
      String textCaptured = area.getText();
      // :TODO: Procedurally add FileNameExtensionFilters
      JFileChooser fileChooser = new JFileChooser("./");
      fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
      var checkInput = fileChooser.showSaveDialog(frame);
      if (checkInput == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        if ((raf.readLine()) != null) {
          var choice = JOptionPane.showConfirmDialog(frame, "Do you want to override current file", "Save file",
              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
          if (choice == JOptionPane.YES_OPTION) {
            var _checkInput = fileChooser.showSaveDialog(frame);
            if (_checkInput == JFileChooser.APPROVE_OPTION) {
              try {
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(textCaptured.trim() + "\n");
                out.flush();
                out.close();
                System.out.println("[INFO]: Saved file: " + file.getName() + " at " + file.getAbsolutePath());
              } catch (IOException e) {
                e.printStackTrace();
              }
              raf.close();
            } else {
              raf.close();
              return;
            }
          } else {
            raf.close();
            return;
          }
        }

      } else if (checkInput == JOptionPane.NO_OPTION || checkInput == JOptionPane.CLOSED_OPTION) {
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void openFile(JTextArea area) {
    if (!area.getText().isEmpty() || !area.getText().isBlank()) {
      var choice = JOptionPane.showConfirmDialog(frame, "Save file before closing?", "Open File",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
      if (choice == JOptionPane.YES_OPTION) {
        saveFile(getActiveTextArea());
      } else if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
        return;
      }
    }
    JFileChooser fileChooser = new JFileChooser("./");
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
        getActiveTextArea().setText(sb.toString());
        bufferedReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void addNewTab(String title, String content) {
    RSyntaxTextArea newTextArea = new RSyntaxTextArea();
    newTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
    newTextArea.setCodeFoldingEnabled(true);
    newTextArea.setHighlightCurrentLine(false);
    newTextArea.setText(content);

    String family = EditorSettings.getSavedFamily();
    String style = String.valueOf(EditorSettings.getSavedStyle());
    String size = String.valueOf(EditorSettings.getSavedSize());
    newTextArea.setFont(new Font(family, Integer.valueOf(style), Integer.valueOf(size)));

    newTextArea.setBackground(frame.getBackground());

    JScrollPane scrollPane = new JScrollPane(newTextArea);
    tabbedPane.addTab(title, scrollPane);

    int index = tabbedPane.getTabCount() - 1;
    tabbedPane.setTabComponentAt(index, createTabTitleComponent(title));

    // tabbedPane.addTab(title, scrollPane);
    tabbedPane.setSelectedComponent(scrollPane);
  }

  public JMenu fileMenu() {
    JMenu fileMenu = new JMenu("File");

    // :NOTE: Menu stuff
    newFileItem = new JMenuItem("New file");
    newFileItem.addActionListener((e) -> {
      String title = "untitled " + tabCount++;
      addNewTab(title, "");
    });
    JMenuItem openItem = new JMenuItem("Open File");
    openItem.addActionListener((e) -> {
      openFile(getActiveTextArea());
    });
    JMenuItem saveItem = new JMenuItem("Save file");
    saveItem.addActionListener((e) -> {
      saveFile(getActiveTextArea());
    });
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
    gotoLineItem.addActionListener((e) -> {
      gotoLine();// :NOTE: Default is ZERO
    });

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
    Action undoAction = new AbstractAction("Undo") {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (undo.canUndo())
          undo.undo();
      }
    };

    Action redoAction = new AbstractAction("Redo") {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (undo.canRedo())
          undo.redo();
      }
    };

    undoItem.addActionListener(undoAction);
    redoItem.addActionListener(redoAction);

    getActiveTextArea().getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
    getActiveTextArea().getActionMap().put("Undo", undoAction);
    getActiveTextArea().getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    getActiveTextArea().getActionMap().put("Redo", redoAction);

    // :TODO: Implement these features
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
        String themeClass = "";
        if (theme_name.equalsIgnoreCase("light")) {
          FlatLightLaf.setup();
          themeClass = FlatLightLaf.class.getName();
        } else if (theme_name.equalsIgnoreCase("dark")) {
          FlatDarkLaf.setup();
          themeClass = FlatDarkLaf.class.getName();
        } else if (theme_name.equalsIgnoreCase("Dracula")) {
          FlatDarculaLaf.setup();
          themeClass = FlatDarculaLaf.class.getName();
        } else if (theme_name.equalsIgnoreCase("Cyan light")) {
          FlatCyanLightIJTheme.setup();
          themeClass = FlatCyanLightIJTheme.class.getName();
        } else if (theme_name.equalsIgnoreCase("darkPurple")) {
          com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme.setup();
          themeClass = com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme.class.getName();
        } else if (theme_name.equalsIgnoreCase("Gradient Green")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme.setup();
          themeClass = com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme.class.getName();
        } else if (theme_name.equalsIgnoreCase("Gradient Fuchsia")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme.setup();
          themeClass = com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme.class.getName();
        } else if (theme_name.equalsIgnoreCase("Gradient Blue")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme.setup();
          themeClass = FlatCyanLightIJTheme.class.getName();
        } else {
          JOptionPane.showMessageDialog(null, "Could not set " + theme_name, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        if (!themeClass.isEmpty()) {
          EditorSettings.saveTheme(themeClass);
        }

        SwingUtilities.updateComponentTreeUI(frame);
        System.out.println("[INFO] Current theme: " + theme_name);
      });
      theme.add(item);
    }
    return theme;
  }

  public static void updateFont(String name, int style, int size) {
    if (tabbedPane.getTabCount() == 0)
      return;

    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
      JScrollPane scP = (JScrollPane) tabbedPane.getComponentAt(i);
      RSyntaxTextArea textArea = (RSyntaxTextArea) scP.getViewport().getView();
      if (textArea != null) {
        Font currentFont = textArea.getFont();
        String newName = (name != null) ? name : currentFont.getFamily();
        int newStyle = (style != -1) ? style : currentFont.getStyle();
        int newSize = (size != -1) ? size : currentFont.getSize();
        textArea.setFont(new Font(newName, newStyle, newSize));
      }
    }

    EditorSettings.saveFont(name, style, size);
  }

  private static JMenu fontStyleMenu() {
    JMenu fontSizeMenu = new JMenu("Font Style");
    JMenuItem plain = new JMenuItem("PLAIN");
    JMenuItem bold = new JMenuItem("BOLD");
    JMenuItem italic = new JMenuItem("ITALIC");
    plain.addActionListener((e) -> {
      updateFont(null, Font.PLAIN, -1);
    });
    bold.addActionListener((e) -> {
      updateFont(null, Font.BOLD, -1);
    });
    italic.addActionListener((e) -> {
      updateFont(null, Font.ITALIC, -1);

    });
    fontSizeMenu.add(plain);
    fontSizeMenu.add(bold);
    fontSizeMenu.add(italic);
    return fontSizeMenu;
  }

  private static JMenu fontSizeMenu() {
    JMenu fontSizeMenu = new JMenu("Font Size");
    int[] sizes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
        28, 29, 30, 31, 32, 33, 34, 35 };
    for (int _sizes : sizes) {
      JMenuItem item = new JMenuItem("" + _sizes);
      item.addActionListener((e) -> {
        updateFont(null, -1, _sizes);
      });
      fontSizeMenu.add(item);
    }
    return fontSizeMenu;
  }

  private static JMenu fontMenu() {
    JMenu fontMenu = new JMenu("Font Family");
    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (String font_name : fonts) {
      JMenuItem item = new JMenuItem(font_name);
      item.addActionListener((e) -> {
        updateFont(font_name, -1, -1);
      });
      fontMenu.add(item);
    }
    // :NOTE: Total number of fonts: 2252
    return fontMenu;
  }

  public JMenu settingMenu() {
    JMenu settingMenu = new JMenu("Setting");
    settingMenu.add(themeMenu());
    settingMenu.add(fontMenu());
    settingMenu.add(fontStyleMenu());
    settingMenu.add(fontSizeMenu());
    System.out.println("[INFO]: settingMenu working...");
    return settingMenu;
  }

  public void setVisible() {
    frame.setVisible(true);
  }
}
