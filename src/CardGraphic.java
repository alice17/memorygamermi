package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/*Classe CardGraphic, gestisce le carte a livello grafico, 
carica le varie immagini rappresentare su di esse*/
public class CardGraphic extends JButton implements Serializable{
    private int value;
    private int id;
    private boolean matched = false;

    public void setImageLogo() {
        try {
            BufferedImage imgLogoMemory = ImageIO.read(new File("img/memory-icon.png"));
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
            BufferedImage imgVal = ImageIO.read(new File("img/card_icon/"+String.valueOf(this.getValue())+".png"));
            this.setIcon(new ImageIcon(imgVal));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void setValue(int value){ this.value = value; }
    public void setId(int id){ this.id = id;}
    public int getId(){ return this.id; }
    public int getValue(){ return this.value; }
    public void setMatched(boolean matched){ this.matched = matched; }
    public boolean isMatched(){ return this.matched; }
}
