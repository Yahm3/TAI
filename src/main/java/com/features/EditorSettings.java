package com.features;

import java.awt.Font;
import java.util.prefs.Preferences;

public class EditorSettings {
  private static final Preferences prefs = Preferences.userNodeForPackage(EditorSettings.class);
  private static final String THEME_KEY = "current_theme";

  public static void saveFont(String family, Integer style, Integer size) {
    if (family != null)
      prefs.put("font_family", family);
    if (style != null && style != -1)
      prefs.putInt("font_style", style);
    if (size != null && size != -1)
      prefs.putInt("font_size", size);
  }

  public static String getSavedFamily() {
    return prefs.get("font_family", "Monospaced");
  }

  public static int getSavedStyle() {
    return prefs.getInt("font_style", Font.PLAIN);
  }

  public static int getSavedSize() {
    return prefs.getInt("font_size", 12);
  }

  public static void saveTheme(String themeClassName) {
    prefs.put(THEME_KEY, themeClassName);
  }

  public static String getSavedTheme() {
    return prefs.get(THEME_KEY, "com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme");
  }
}
