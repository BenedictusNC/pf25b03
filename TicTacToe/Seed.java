package TicTacToe;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public enum Seed {   // to save as "Seed.java"
    CROSS("X", "image/Diamond.png"),   // displayName, imageFilename
    NOUGHT("O", "image/Emerald.png"),
    NO_SEED(" ", null);

    private String displayName;
    private Image img = null;

    private Seed(String name, String imageFilename) {
        this.displayName = name;

        if (imageFilename != null) {
            URL imgURL = getClass().getClassLoader().getResource(imageFilename);
            ImageIcon icon = null;
            if (imgURL != null) {
                icon = new ImageIcon(imgURL);
                //System.out.println(icon);  // debugging
            } else {
                System.err.println("Couldn't find file " + imageFilename);
            }
            img = icon.getImage();
        }
    }

    public String getDisplayName() {
        return displayName;
    }
    public Image getImage() {
        return img;
    }
}