package com.tai;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ui.Window;
import com.features.EditorSettings;
import com.ui.Splash;

public class TAI {
  public static void init() {
    SwingUtilities.invokeLater(() -> {
      try {
        String savedTheme = EditorSettings.getSavedTheme();
        UIManager.setLookAndFeel(savedTheme);
      } catch (Exception e) {
        System.err.println("WARNING: Could not setup flatlaf");
        com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme.setup();
      }
      new Window();
    });
  }

  public static void main(String[] args) {
    boolean skipSplash = false;
    for (String arg : args) {
      if (arg.equalsIgnoreCase("--noSplash") || arg.equalsIgnoreCase("-ns")) {
        skipSplash = true;
        break;
      }
    }

    if (skipSplash) {
      System.out.println("[INFO]: Running with no splash");
      init();
    } else {
      var splash = new Splash();
      splash.start(() -> {
        try {
          Thread.sleep(5000);
        } catch (InterruptedException ex) {
          System.err.println("[WARNING]: Splash thread Interrupted");
        }
        splash.setDone();
        init();
      });
    }

  }
}
