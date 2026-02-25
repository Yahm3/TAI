package com.features;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UndoRedoTest {

  @Test
  public void shouldReturnAString() {
    assertNotNull(new com.features.UndoRedo().testUndoRedo());
  }

}
