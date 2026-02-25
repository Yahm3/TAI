package com.chat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ChatbotTest {

  @Test
  public void shouldReturnAString() {
    assertNotNull(new com.chat.Chatbot().sendMessageToChatAPI("Some message"));
  }
}
