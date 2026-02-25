package com.features;

@SuppressWarnings("unused")
public class UndoRedo {
  private static StringBuilder doc = new StringBuilder();
  private final CustomStack<Character> redoStack = new CustomStack<>(5000);
  private final CustomStack<Character> undoStack = new CustomStack<>(5000);

  public UndoRedo() {
  }

  private void append(char X) {
    doc.append(X);

    // :NOTE: Clear the statck on a new write
    redoStack.clear();
  }

  private void undo() {
    if (doc.length() > 0) {
      char last = doc.charAt(doc.length() - 1);
      doc.deleteCharAt(doc.length() - 1);
      redoStack.push(last);
    }
  }

  private void redo() {
    if (!doc.isEmpty()) {
      char ch = redoStack.pop();
      doc.append(ch);
    }
  }

  public String testUndoRedo() {
    return doc.toString();
  }
}
