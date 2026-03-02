package com.tai;

import javax.swing.SwingUtilities;

import com.ui.Window;
import com.ui.Splash;

// @SuppressWarnings("unused")
public class TAI {
  public static void init() {
    SwingUtilities.invokeLater(() -> {
      try {
        com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme.setup();
      } catch (Exception e) {
        System.err.println("WARNING: Could not setup flatlaf");
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
