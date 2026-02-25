package com.features;

import java.util.Stack;

public class CustomStack<T> extends Stack<T> {
  private final int maxSize;

  CustomStack(int size) {
    super();
    this.maxSize = size;
  }

  @Override
  public Object push(Object object) {
    while (this.size() > maxSize) {
      this.remove(0);
    }
    return super.push((T) object);
  }
}
