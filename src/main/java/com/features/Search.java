package com.features;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Search {
  private final static int VIBRATION_LENGTH = 10;
  private final static int VIBRATION_VELOCITY = 5;

  public Search() {
  }

  public static void vibrate(JFrame frame) {
    try {
      final int originalX = frame.getLocationOnScreen().x;
      final int originalY = frame.getLocationOnScreen().y;
      for (int i = 0; i < VIBRATION_LENGTH; i++) {
        Thread.sleep(10);
        frame.setLocation(originalX, originalY + VIBRATION_VELOCITY);
        Thread.sleep(10);
        frame.setLocation(originalX, originalY - VIBRATION_VELOCITY);
        Thread.sleep(10);
        frame.setLocation(originalX + VIBRATION_VELOCITY, originalY);
        Thread.sleep(10);
        frame.setLocation(originalX, originalY);
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }

  public static void searchText(JTextArea area, JTextField inputField) {
    String userPattern = inputField.getText();// :NOTE: I did not use trim() because regex also matches spaces, smart
                                              // huh :)
    if (userPattern.isEmpty()) {
      return;
    }
    try {
      area.getHighlighter().removeAllHighlights();

      Pattern pattern = Pattern.compile(userPattern, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(area.getText());

      javax.swing.text.Highlighter.HighlightPainter painter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(
          Color.YELLOW);
      boolean found = false;
      while (matcher.find()) {
        found = true;
        area.getHighlighter().addHighlight(matcher.start(), matcher.end(), painter);
      }
      if (!found) {
        com.features.Search.vibrate(com.ui.Window.findFrame);
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
