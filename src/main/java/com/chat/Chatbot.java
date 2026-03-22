package com.chat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/*
 * :NOTE: For now I will be testing with the grok API
 * Later I will add other LLMs
 */
@SuppressWarnings("unused")
public class Chatbot {
  private static final String API_KEY = "";

  public String sendMessageToChatAPI(String userMessaage) {
    try {
      String requestBody = String.format("""
          {
            "model": "openai/gpt-oss-120b",
            "messages": [
              {"role": "user", "content": "%s"}
            ],
            "temperature": 1
          }
          """, userMessaage.replace("\"", "\\\""));
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
          .header("Authorization", "Bearer " + API_KEY)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(requestBody))
          .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        return parseChatbotResponse(response.body());
      } else {
        return "ERROR: " + response.statusCode() + "\n" + response.body();
      }

    } catch (Exception e) {
      e.printStackTrace();
      return "ERROR: " + e.getMessage();
    }
  }

  private String parseChatbotResponse(String body) {
    try {
      JsonObject jsonResponse = com.google.gson.JsonParser.parseString(body).getAsJsonObject();
      JsonArray choices = jsonResponse.getAsJsonArray("choices");
      if (choices.size() > 0) {
        JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
        return message.get("content").getAsString();
      } else {
        return "No response from the chatbot";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Error while parsing the chatbot response";
    }
  }

  public static void addChatToFrame() {
    javax.swing.JPanel chatPnl = new javax.swing.JPanel(new java.awt.BorderLayout());
    chatPnl.setPreferredSize(new java.awt.Dimension(300, com.ui.Window.maxWindow.height));
    javax.swing.JTextArea chatArea = new javax.swing.JTextArea(15, 20);
    chatArea.setEditable(false);
    chatArea.setLineWrap(true);
    chatArea.setWrapStyleWord(true);

    javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(chatArea);
    chatPnl.add(scrollPane, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel inputPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
    javax.swing.JTextField userInputField = new javax.swing.JTextField();
    javax.swing.JButton sendButton = new javax.swing.JButton("Send");

    inputPanel.add(userInputField, java.awt.BorderLayout.CENTER);
    inputPanel.add(sendButton, java.awt.BorderLayout.EAST);
    chatPnl.add(inputPanel, java.awt.BorderLayout.SOUTH);

    sendButton.addActionListener(e -> {
      String userMessage = userInputField.getText().trim();
      if (!userMessage.isEmpty()) {
        chatArea.append("You: " + userMessage + "\n");
        userInputField.setText("");
        new Thread(() -> {
          Chatbot cb = new Chatbot();
          String chatbotResponse = cb.sendMessageToChatAPI(userMessage);
          javax.swing.SwingUtilities.invokeLater(() -> {
            chatArea.append("chatbot: " + chatbotResponse + "\n");
          });
        }).start();
      }
    });

    com.ui.Window.frame.add(chatPnl, java.awt.BorderLayout.EAST);
    userInputField.requestFocusInWindow();
  }
}
