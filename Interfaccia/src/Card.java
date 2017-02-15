
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by salvatorealescio on 05/02/17.
 */
public class Card extends JButton {
    private int id;
    private boolean matched = false;

    public void setImageLogo() {
        try {
            BufferedImage imgLogoMemory = ImageIO.read(new File("src/img/memory-icon.png"));
            this.setIcon(new ImageIcon(imgLogoMemory));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void removeImage(){
        if(this.getIcon() != null){
            this.setIcon(null);
        }
    }

    public void setImage(){
        try{
            BufferedImage imgVal = ImageIO.read(new File("src/img/card_icon/"+String.valueOf(this.getId())+".png"));
            this.setIcon(new ImageIcon(imgVal));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setMatched(boolean matched){
        this.matched = matched;
    }
    public boolean getMatched(){
        return this.matched;
    }
}
