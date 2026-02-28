package com.features;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
public class UndoRedoTest {

  @Test
  public void shouldReturnAString() {
    assertEquals(new com.features.UndoRedo().testUndoRedo(), new String());
  }

}
