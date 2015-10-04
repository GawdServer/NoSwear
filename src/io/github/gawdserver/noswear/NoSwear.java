package io.github.gawdserver.noswear;

import io.github.gawdserver.api.plugin.Plugin;
import io.github.gawdserver.api.plugin.PluginDir;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vinnie on 2/18/2015.
 */
public class NoSwear implements Plugin {
    public static final Logger logger = Logger.getLogger("NoSwear");
    public static final HashMap<String, Integer> offences = new HashMap<>();
    public static final ArrayList<String> badWords = new ArrayList<>();
    public static boolean banPlayer;
    public static int tolerance;
    public static String banMessage;
    public static String warnMessage;

    public void setDefaults() {
        // Config
        banPlayer = true;
        tolerance = 3;
        banMessage = "You have been banned for swearing.";
        warnMessage = "Do not swear or be banned!";
        // Default list
        badWords.add("fuck");
        badWords.add("shit");
        badWords.add("bitch");
        badWords.add("ass");
    }

    private void loadConfig(File configFile) {
            try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    // Ignore comment
                    if (currentLine.startsWith("#")) {
                        continue;
                    }
                    // Config
                    if (currentLine.startsWith("banPlayer=")) {
                        banPlayer = Boolean.parseBoolean(currentLine.substring(10));
                        continue;
                    }
                    if (currentLine.startsWith("tolerance=")) {
                        tolerance = Integer.parseInt(currentLine.substring(10));
                        continue;
                    }
                    if (currentLine.startsWith("banMessage=")) {
                        banMessage = currentLine.substring(11);
                        continue;
                    }
                    if (currentLine.startsWith("warnMessage=")) {
                        warnMessage = currentLine.substring(12);
                        continue;
                    }
                    // Bad Word
                    badWords.add(currentLine);
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error loading configuration.", ex);
            }
    }

    private void saveConfig(File configFile) {
        try {
            FileWriter fw = new FileWriter(configFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("# NoSwear Configuration");
            bw.newLine();
            bw.write("#");
            bw.newLine();
            bw.write("# Ban player after (tolerance) bad words?");
            bw.newLine();
            bw.write("banPlayer=" + banPlayer);
            bw.newLine();
            bw.write("# How many bad words before ban?");
            bw.newLine();
            bw.write("tolerance=" + tolerance);
            bw.newLine();
            bw.write("# Message to display on ban");
            bw.newLine();
            bw.write("banMessage=" + banMessage);
            bw.newLine();
            bw.write("# Message to display after swearing");
            bw.newLine();
            bw.write("warnMessage=" + warnMessage);
            bw.newLine();
            bw.write("#");
            bw.newLine();
            bw.write("# Bad Words");
            bw.newLine();
            for (String word : badWords) {
                bw.write(word);
                bw.newLine();
            }
            bw.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error saving configuration.", ex);
        }
    }

    @Override
    public void startup() {
        File configFile = new File(PluginDir.getPluginDir(), "NoSwear/config.ini");
        if (configFile.exists()) {
            loadConfig(configFile);
        } else {
            setDefaults();
            configFile.getParentFile().mkdirs();
            saveConfig(configFile);
        }
    }

    @Override
    public void shutdown() {}
}
