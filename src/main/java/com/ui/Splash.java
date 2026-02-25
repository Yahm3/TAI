package com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Splash extends JPanel {
  private JFrame frame;
  private BufferedImage image = null;
  private JProgressBar progressBar;

  public Splash() {
    try {
      // Ensure this path is correct relative to where you RUN the program
      java.net.URL imgURL = getClass().getResource("/misc/sit.png");
      if (imgURL != null) {
        image = ImageIO.read(imgURL);
      }
    } catch (IOException e) {
      System.err.println("ERROR: Could not find Splash Image at the specified path.");
    }
    this.setLayout(new BorderLayout());

    progressBar = new JProgressBar(0, 100);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
    progressBar.setString("Loading...");
    progressBar.setForeground(new Color(46, 204, 113));
    progressBar.setBackground(Color.DARK_GRAY);
    progressBar.setBorderPainted(false);
    progressBar.setPreferredSize(new Dimension(593, 20));
    progressBar.setBorderPainted(false);
    progressBar.setIndeterminate(true);
    progressBar.setPreferredSize(new Dimension(593, 25));
    this.add(progressBar, BorderLayout.SOUTH);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      Graphics2D g2d = (Graphics2D) g;
      g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight() - progressBar.getHeight(), null);
    }
  }

  public void updateProgressBar(int value, String message) {
    SwingUtilities.invokeLater(() -> {
      progressBar.setValue(value);
      progressBar.setString(message);
    });
  }

  public void setDone() {
    if (frame != null) {
      frame.dispose();
    }
  }

  public void start(Runnable func) {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setSize(593, 420);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setUndecorated(true);
    frame.add(this);
    frame.setVisible(true);

    new Thread(() -> {
      func.run();
    }).start();
  }
}
