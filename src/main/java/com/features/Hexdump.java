package com.features;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Hexdump {

  public static String HexdumpData(String filePath) {
    StringBuilder output = new StringBuilder();
    try {
      // 1. Call the Nim binary (make sure the path to 'main' is correct)
      // We pass the filePath as an argument to the Nim program
      ProcessBuilder pb = new ProcessBuilder("./src/main", filePath);
      Process process = pb.start();

      // 2. Read the output from Nim's stdout
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }

      int exitCode = process.waitFor();
      if (exitCode != 0) {
        return "Error: Nim exited with code " + exitCode;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return "Error: Could not execute Nim binary. " + e.getMessage();
    }
    return output.toString();
  }
}
