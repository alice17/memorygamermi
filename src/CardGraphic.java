package src;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * la classe CardGraphic permette di creare un oggetto Carta che si presenterà nella board
 */
public class CardGraphic extends JButton implements Serializable{
    private int value; // valore della carta
    private int id; // id univoco per identificare la carta

    private boolean matched = false; // variabile se mi identifica se una carta fa parte di un match oppure no

    /*metodi che mi permette di settare le immagine della card quando la carta è coperta*/
    public void setImageLogo() {
        try {
            BufferedImage imgLogoMemory = ImageIO.read(new File("img/memory-icon.png"));
            this.setIcon(new ImageIcon(imgLogoMemory));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* metodo che mi rimuove l'immagine dalla carta (viene utilizzata in fase di rimozione della immagine del logo per l'immagine della carta)*/
    public void removeImage(){
        if(this.getIcon() != null){
            this.setIcon(null);
        }
    }
    /* metodo che mi gestisce il set dell'immagine riferita al valore */
    public void setImage(){
        try{
            BufferedImage imgVal = ImageIO.read(new File("img/card_icon/"+String.valueOf(this.getValue())+".png"));
            this.setIcon(new ImageIcon(imgVal));
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


    public void setValue(int value){
        this.value = value;
    } // metodo che mi imposta il valore della carte

    public void setId(int id){
        this.id = id;
    } // metodo che mi setta l'ID univoco della carta

    public int getId(){
        return this.id;
    } // metodo che mi ritorna l'ID univoco della carta

    public int getValue(){ return this.value; } // metodo che mi ritorna il valore

    public void setMatched(boolean matched){
        this.matched = matched;
    } // metodo che mi etichetta se la carta è stata selezionata per un matching

    public boolean getMatched(){
        return this.matched;
    } // metodo che ritorna il valore booleano di matching
}
