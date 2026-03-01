package com.fileHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("unused")
public class FileHandler {
  private static final Logger log = Logger.getLogger(FileHandler.class.getName());
  private static final String defaultFile = "untitled.txt";
  private static boolean changedDocument = false;

  public boolean openFile(String filename, JTextArea area) {

    area.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void changedUpdate(DocumentEvent arg0) {
        changedDocument = true;
      }

      @Override
      public void insertUpdate(DocumentEvent arg0) {
      }

      @Override
      public void removeUpdate(DocumentEvent arg0) {
      }
    });

    if (!area.getText().isEmpty() && changedDocument) {// :NOTE: If there is a file that was changed and not saved
      var openSave = JOptionPane.showConfirmDialog(null, "Save before closing?", "Open File",
          JOptionPane.YES_NO_OPTION);
      if (openSave == JOptionPane.YES_OPTION) {// :NOTE: If the user chooses to save before opening
        saveFile(filename, area);
      }
    }
    return false;
  }

  public boolean saveFile(String openedFile, JTextArea area) {
    boolean isSaved = false;
    try (Writer fw = new OutputStreamWriter(new FileOutputStream(openedFile), "UTF-8")) {// :TODO: Support other
                                                                                         // encoding as well
      fw.write(area.getText());
      log.info("Saved file: " + openedFile);
      isSaved = true;
    } catch (IOException e) {
      log.warning("could not save file: " + openedFile);
      JOptionPane.showConfirmDialog(null, "Could not save file", "ERROR", JOptionPane.ERROR_MESSAGE);
      // :TODO: Write the output of the stacktrace to the .log file
    }
    return isSaved;
  }

}
