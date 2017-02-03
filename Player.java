/**
 * Created by alice on 02/02/17.
 */
public class Player {
    public String username;
    public Integer id;
    public Integer points;

    public Player(String user, Integer id){
        this.points = 0;
        this.username = user;
        this.id = id;
    }

    public String getUsername(){ return this.username; }
}
