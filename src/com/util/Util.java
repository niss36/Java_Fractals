package com.util;

import com.gui.panels.PanelFractal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static final Path SCREENSHOTS_DIR = Paths.get(System.getProperty("user.home"), "Fractals Screenshots");

    public static void takeScreenshot(PanelFractal fractal) throws IOException {

        BufferedImage image = fractal.getImage();

        String fileName = String.format("%s_%s_%s", fractal.getName(), getTimeStamp(), fractal.getAdditionalInfo());

        Path pathTemp = SCREENSHOTS_DIR.resolve(fileName + ".png");

        int i = 1;
        while (Files.exists(pathTemp)) {
            pathTemp = SCREENSHOTS_DIR.resolve(fileName + "_" + i + ".png");
            i++;
        }

        try (OutputStream stream = Files.newOutputStream(pathTemp)) {
            ImageIO.write(image, "png", stream);
            System.out.println("Screenshot saved to " + pathTemp);
        }
    }

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("YYMMdd-HHmmss");

    private static String getTimeStamp() {

        return FORMAT.format(new Date());
    }

    public static void checkDirectory() throws IOException {
        if (Files.notExists(SCREENSHOTS_DIR))
            Files.createDirectory(SCREENSHOTS_DIR);
    }

    public static int[] generatePalette(int paletteLength, Vec3Colour... keyColours) {

        int numColours = keyColours.length;
        if (paletteLength % numColours != 0)
            throw new IllegalArgumentException("Palette length is not a multiple of the number of colours");

        int[] palette = new int[paletteLength];

        int fraction = paletteLength / numColours;

        for (int c = 0; c < numColours; c++) {

            int start = c * fraction;

            for (int i = 0; i < fraction; i++) {
                float ratio = (float) i / fraction;
                Vec3Colour colour = keyColours[c].multiply(1 - ratio).add(keyColours[(c + 1) % numColours].multiply(ratio));
                palette[start + i] = colour.getRGB();
            }
        }

        return palette;
    }
}
