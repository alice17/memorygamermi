package src;

/*
Classe MainP utilizzata per avviare il client, puo' essere inserito l'indirizzo del server 
in formato ipv4 da riga di comando
*/

public class MainP {

	public static WindowRegistration windowMain;

	public static void main(String[] args) {

        
        String serverAddr;
        /* Se l'indirizzo del server in formato ipv4 e' stato inserito da riga di comando */
        /* L'indirizzo inserito viene letto come una stringa*/
        if (args.length == 1) {
        	serverAddr = args[0];
            
        } else {

            /*Se non viene inserito niente da riga di comando viene utilizzato localhost*/
        	serverAddr = "localhost";
            System.out.println("Nessun indirizzo ipv4 inserito per cercare il server di gioco.");
            System.out.println("Il server per l'iscrizione al gioco verra' ricercato in locale");
            System.out.println("Per utilizzare un server remoto inserire il suo indirizzo da riga di comando");
        }
        windowMain = new WindowRegistration(serverAddr);
        
    }
}