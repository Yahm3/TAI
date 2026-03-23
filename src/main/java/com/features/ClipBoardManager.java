package com.features;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ClipBoardManager {
  private static Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

  public ClipBoardManager() {
  }

  public static void copy(RSyntaxTextArea area) {
    String textToCopy = area.getSelectedText();
    StringSelection selection = new StringSelection(textToCopy);
    clipboard.setContents(selection, null);
  }

  public static void paste(RSyntaxTextArea area) {
    area = com.ui.Window.getActiveTextArea();
    if (area != null) {
      area.paste();
    }
  }

  public static void cut(RSyntaxTextArea area) {
    area = com.ui.Window.getActiveTextArea();
    if (area != null) {
      area.cut();
    }
  }
}
