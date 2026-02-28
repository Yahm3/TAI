package com.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SplashTest {

  @Test
  @DisplayName("Just check if the object is not null")
  public void dummyTest() {
    Splash splash = new Splash();
    Assertions.assertNotNull(splash);
  }
}
