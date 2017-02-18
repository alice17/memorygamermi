package src;
import javax.swing.*;
import java.util.List;

/**
 * Created by salvo92 on 09/02/17.
 */
public class Score {
    private int SCORE = 0;
    private String USER;

    public int getScore(){
        return this.SCORE;
    }

    public void updateScore(){
        this.SCORE += 1;
    }

    public void setUserForScoring(String username){
        this.USER = username;
    }




}
