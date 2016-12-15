package com.hsenid.calculator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

/**
 * Created by hsenid on 12/15/16.
 */
public class History {
    private Logger logger;

    public History() {
        logger = LogManager.getLogger(History.class);
    }

    public void load(JList<Object> historyList, DefaultListModel<String> historyListArray) {
        FileDialog fd = new FileDialog(new JFrame(), "Choose file...", FileDialog.LOAD);
        fd.setDirectory("/home/hsenid/Documents");
        fd.setVisible(true);
        String filename = fd.getDirectory().concat("/").concat(fd.getFile());
        if (filename != null && filename.endsWith(".txt")) {
            try {
                RandomAccessFile loadFile = new RandomAccessFile(filename, "r");
                FileChannel loadFileChannel = loadFile.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                StringBuilder line = new StringBuilder();

                while (loadFileChannel.read(buffer) > -1) {
                    buffer.flip();
                    for (int i = 0; i < buffer.limit(); i++) {
                        char ch = (char) buffer.get();
                        if (ch == '\n') {
                            historyListArray.addElement(line.toString());
                            line.setLength(0);
                        } else
                            line.append(ch);
                    }
                    buffer.clear();
                }
                historyList.setListData(historyListArray.toArray());
                loadFile.close();
                JOptionPane.showMessageDialog(null, "Successfully loaded!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException fnfe) {
                logger.info(fnfe);
                JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioe) {
                logger.info(ioe);
                JOptionPane.showMessageDialog(null, "Could not write to the file specified!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                logger.info(ex);
                JOptionPane.showMessageDialog(null, "Unknown error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void save(DefaultListModel<String> historyList) {
        FileDialog fd = new FileDialog(new JFrame(), "Save as..", FileDialog.SAVE);
        fd.setDirectory("/home/hsenid/Documents");
        fd.setFile("CalHistory ".concat(new Date().toString()).concat(".txt"));
        fd.setVisible(true);
        String filename = fd.getDirectory().concat("/").concat(fd.getFile());
        if (filename != null && filename.endsWith(".txt")) {
            //File open process
            try {
                RandomAccessFile saveFile = new RandomAccessFile(filename, "rw");
                FileChannel saveFileChannel = saveFile.getChannel();
                ByteBuffer buffer = ByteBuffer.allocate(48);

                for (Object line : historyList.toArray()) {
                    buffer.clear();
                    String lineToWrite = line.toString();

                    if (!lineToWrite.endsWith("\n"))
                        lineToWrite = lineToWrite.concat("\n");

                    buffer.put(lineToWrite.getBytes());
                    buffer.flip();

                    while (buffer.hasRemaining()) {
                        saveFileChannel.write(buffer);
                    }
                }
                saveFile.close();
                JOptionPane.showMessageDialog(null, "Successfully saved!", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException fnfe) {
                logger.info(fnfe);
                JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ioe) {
                logger.info(ioe);
                JOptionPane.showMessageDialog(null, "Could not write to the file specified!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                logger.info(ex);
                JOptionPane.showMessageDialog(null, "Unknown error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
