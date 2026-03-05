package com.chat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
public class ChatbotTest {

  @Disabled
  @DisplayName("Should return some string")
  public void shouldReturnAString() {
    assertNotNull(new com.chat.Chatbot().sendMessageToChatAPI("Some message"));
  }

  // :TODO: Write a test for an invalid API_KEY
}
