package com;

import com.gui.Frame;
import com.util.Util;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        Util.checkDirectory();

        SwingUtilities.invokeLater(() -> {
            Frame frame = new Frame();
            frame.showFrame();
        });
    }
}
