package com.chat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JTextPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import io.github.cdimascio.dotenv.Dotenv;

/*
 * :NOTE: For now I will be testing with the grok API
 * Later I will add other LLMs
 */
@SuppressWarnings("unused")
public class Chatbot {
  private static final Dotenv dotenv = Dotenv.load();
  private static final String API_KEY = dotenv.get("GROQ_API_KEY");

  public String sendMessageToChatAPI(String userMessage) {
    if (API_KEY == null || API_KEY.isEmpty() || API_KEY.isBlank()) {
      return "ERROR: API key not found in .env file";
    }
    try {
      List<com.google.gson.JsonObject> history = ChatStorage.loadHistory();

      JsonObject newUserMsg = new JsonObject();
      newUserMsg.addProperty("role", "user");
      newUserMsg.addProperty("content", userMessage);
      history.add(newUserMsg);

      JsonObject root = new JsonObject();
      root.addProperty("model", "openai/gpt-oss-120b");
      root.addProperty("temperature", 1);

      JsonArray messagesArray = new JsonArray();
      for (JsonObject msg : history) {
        messagesArray.add(msg);
      }
      root.add("messages", messagesArray);

      String finalJsonBody = new Gson().toJson(root);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
          .header("Authorization", "Bearer " + API_KEY)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(finalJsonBody))
          .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        String botResponse = parseChatbotResponse(response.body());

        com.google.gson.JsonObject botMsg = new com.google.gson.JsonObject();
        botMsg.addProperty("role", "assistant");
        botMsg.addProperty("content", botResponse);

        history.add(botMsg);
        ChatStorage.saveHistory(history);

        return botResponse;
      } else {
        return "ERROR: " + response.statusCode() + " - " + response.body();
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

  private static void appendToPane(JTextPane pane, String msg) {
    try {
      javax.swing.text.Document doc = pane.getDocument();
      doc.insertString(doc.getLength(), msg, null);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static String extractCodeBlock(String response) {
    int first = response.indexOf("```");
    if (first != -1) {
      int startOfCode = response.indexOf("\n", first);
      if (startOfCode == -1) {
        startOfCode = first + 3;
      } else {
        startOfCode += 1;
      }
      int last = response.indexOf("```", startOfCode);
      if (last != -1) {
        return response.substring(startOfCode, last).trim();
      }
    }
    return response.trim();
  }

  public static void addChatToFrame() {
    javax.swing.JPanel chatPnl = new javax.swing.JPanel(new java.awt.BorderLayout());
    chatPnl.setPreferredSize(new java.awt.Dimension(300, com.ui.Window.maxWindow.height));

    JTextPane chatPane = new JTextPane();
    chatPane.setEditable(false);

    javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(chatPane);
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
        appendToPane(chatPane, "You: " + userMessage + "\n\n");
        userInputField.setText("");

        new Thread(() -> {
          String apiPayloadMessage = userMessage;
          if (apiPayloadMessage.contains("@current")) {
            org.fife.ui.rsyntaxtextarea.RSyntaxTextArea activeArea = com.ui.Window.getActiveTextArea();
            if (activeArea != null && !activeArea.getText().isEmpty()) {
              String fileContent = "\n\n--- START OF CURRENT FILE ---\n"
                  + activeArea.getText()
                  + "\n--- END OF CURRENT FILE ---\n";
              apiPayloadMessage = apiPayloadMessage.replace("@current", fileContent);
            } else {
              apiPayloadMessage = apiPayloadMessage.replace("@current", "[No file open or file is empty]");
            }
          }

          Chatbot cb = new Chatbot();
          String chatbotResponse = cb.sendMessageToChatAPI(apiPayloadMessage);

          javax.swing.SwingUtilities.invokeLater(() -> {
            appendToPane(chatPane, "Chatbot: " + chatbotResponse + "\n");

            if (chatbotResponse.contains("```")) {
              JButton applyBtn = new JButton("Apply to Editor");
              applyBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
              applyBtn.addActionListener(evt -> {
                String extractedText = extractCodeBlock(chatbotResponse);
                org.fife.ui.rsyntaxtextarea.RSyntaxTextArea activeArea = com.ui.Window.getActiveTextArea();
                if (activeArea != null) {
                  activeArea.setText(extractedText);
                }
              });
              chatPane.insertComponent(applyBtn);
            }
            appendToPane(chatPane, "\n\n");
            chatPane.setCaretPosition(chatPane.getDocument().getLength());
          });
        }).start();
      }
    });

    com.ui.Window.frame.add(chatPnl, java.awt.BorderLayout.EAST);
    userInputField.requestFocusInWindow();
  }
}
