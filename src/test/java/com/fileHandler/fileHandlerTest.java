package com.fileHandler;

import javax.swing.JTextArea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//@SuppressWarnings("unused")
public class fileHandlerTest {

  @Test
  @DisplayName("Must return a boolean value")
  void dummyTestCode() {
    FileHandler fH = new FileHandler();
    Assertions.assertEquals(fH.saveFile(new String(), new JTextArea()), true);
  }
}
