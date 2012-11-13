package de.schaf.apco.util;

import com.sixlegs.png.PngImage;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class TileSetSeperator {
    public static InputStream getTile(String FileLocation, int X_Start, int Y_Start, int width, int height) throws IOException {
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new BufferedInputStream((new Object(){}).getClass().getClassLoader().getResourceAsStream(FileLocation));
        BufferedImage img = new PngImage().read(in, true);

        img = img.getSubimage(X_Start, Y_Start, width, height);
        ImageIO.write(img,"PNG",out);
        
        return new ByteArrayInputStream(out.toByteArray());
    }
}
