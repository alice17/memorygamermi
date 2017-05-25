package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * La classe Window Registration permette di inizializzare un'interfaccia grafica di registratione al gioco.
 * Lancia il client al click di registrazione.
 */

public class WindowRegistration {
    private static boolean RIGHT_TO_LEFT = false; //variabile che mi setta l'orientamento del posizione degli elementi nella finestra
    private static final int SIZE_OF_TEXTFIELD = 10; // variabile che mi gestisce la lunghezza textfield
    public static String IMG_PATH = "img/Memory.png"; // stringa per path del logo
    public static Board board;
    public static boolean awaitTurn;
    public static JLabel waiting;
    public static JLabel feedback;
    public static JFrame frame;
    public static JButton btnRegistration;
    public static String serverAddr;


    /*
    * setCloseWindow è un metodo che gestisce la chiusura della finestra
    */
    public WindowRegistration(String serverAddr) {

        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        this.serverAddr = serverAddr;

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private void setCloseWindow(final JFrame frame) {
        int input = JOptionPane.showOptionDialog(frame, // la root è il frame
                "Are you sure you want to exit the game?",
                "Exit",
                JOptionPane.YES_NO_OPTION, // tipo di button dell' alert
                JOptionPane.INFORMATION_MESSAGE, // tipo di alert
                null,null,null);
        if( input == JOptionPane.YES_OPTION)
            System.exit(0); // se clicko Si mi esce dal gioco (ovviamente da sistemare)
    }

    /*
     * settingEventRegistration è un metodo che gestisce l'evento dell'immissione del nome nel textfied
     */
    private void settingEventRegistration(final JFrame fr, JTextField tf){
        if(tf.getText() == null || tf.getText().isEmpty()){
            //gestisco il caso in cui non aggiungo nessun nome
            JOptionPane.showOptionDialog(null,
                    "You don't add a username!",
                    "Exit",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE, // tipo di messaggio
                    null,null,null);
        }


        // gestisco il caso in cui nella textfield inserisco uno spazio vuoto (non li vogio avere!)
        else if(tf.getText().contains(" ") ){
            JOptionPane.showOptionDialog(null,
                    "Not insert \"Blank Spaces\" in the Username",
                    "Exit",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE, // tipo di messaggio errore
                    null,null,null);

        }else{
            // nel caso un cui è tutto ok, allora lancio il client e gli passo la stringa
            //appena confermato l'username la finestra windowregistration scompare con fr.setVisible()
            // si può anche cambiare.
            //Come ultima cosa crea una board e avvia il thread doClientThread che durerà fino alla fine.
            //Questo thread direi che lo possa chiamare direttamente la board anche senza bloccare la grafica.
            //Non dovrebbe cambiare tanto, si può provare
            String userName = tf.getText();
            board = new Board(this);
            board.init(userName,serverAddr);
        }
    }

    /*
     * addComponentsToPane è un metodo che crea la borad layout per inserire tutti gli oggetti
     */

    public void addComponentsToPane(final JFrame pane) {


        if (!(pane.getLayout() instanceof BorderLayout)) { // controllo se il frame è in modalità bordarLayout
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        if (RIGHT_TO_LEFT) { // controllo se la posizione è deve essere orientale oppure occidentale
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.getContentPane().setForeground(Color.WHITE); // imposto il colore dello sfondo

        JPanel panelImg = new JPanel(); // creo il panel per inserire il logo

        // cerco di aprire l'immagine
        try{
            BufferedImage imgLogo;
            imgLogo = ImageIO.read(new File(IMG_PATH));
            ImageIcon icon = new ImageIcon(imgLogo);
            JLabel labelLogo = new JLabel(icon); // immetto l'icona all'interno di una label
            panelImg.add(labelLogo,BorderLayout.CENTER); // e posiziono la label al centro

        }catch (IOException e){
            e.printStackTrace();
        }

        panelImg.setSize(new Dimension(400,375)); // imposto la dimensione del panel
        panelImg.setBorder(new EmptyBorder(10,0,0,0)); // imposto il bordo in alto in modo che non sia attaccato al bordo della finestra
        pane.add(panelImg, BorderLayout.PAGE_START); // aggiungo il panel nella finestra (PAGE_START equivale alla parte TOP della finestra)


        JPanel panelRegistration = new JPanel(); // creo il panel di registrazione
        JLabel userLabel = new JLabel("User"); // creo la label con scritto User


        final JTextField userEntry = new JTextField(); // creo la textfield per l'immissione del nome della persona che si registra
        userEntry.setColumns(SIZE_OF_TEXTFIELD); // imposto la grandezza della textfield

        btnRegistration = new JButton("Registration"); // creo la il button per avviare la registrazione

        feedback = new JLabel();
        waiting = new JLabel();

        // gestisco ora l'evento legato al button di registrazione al click
        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        settingEventRegistration(pane,userEntry);
                        btnRegistration.setEnabled(false);  
                    }     
                });
                t.start();
            }
        });

        //gestisco l'evento legato al button ma premendo invio
        btnRegistration.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    //settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);    
                }
            }
        });

        //gestisco l'evento di registrazione eseguendo l'invio da textfield
        userEntry.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnRegistration.setEnabled(false);
                    userEntry.setEditable(false);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            settingEventRegistration(pane,userEntry);
                            btnRegistration.setEnabled(false);
                            userEntry.setEditable(false);  
                        }     
                    });
                    t.start();
                }

            }
        });

        // creo ora il raggruppamento degli oggetti userlabel, textfield ed il button di registrazione
        GroupLayout groupRegistration = new GroupLayout(panelRegistration); // vado a passargli il root dove creare il GroupLayout
        groupRegistration.setAutoCreateGaps(true); // setto il spazio fra gli oggetti


        //setto gli oggetti con  l'orientamento orizontale
        groupRegistration.setHorizontalGroup(groupRegistration.createParallelGroup()
                .addComponent(userLabel) // aggiungo la label
                .addComponent(userEntry) // aggiungo la textfield
                .addComponent(btnRegistration) // aggiungo il button
                .addComponent(feedback)
                .addComponent(waiting)
        );
        //setto gli oggetti con l'orientamento verticaole
        groupRegistration.setVerticalGroup(groupRegistration.createSequentialGroup()
                .addComponent(userLabel) // aggiungo la label
                .addComponent(userEntry) // aggiungo la textfield
                .addComponent(btnRegistration) //aggiungo il button
                .addComponent(feedback)
                .addComponent(waiting)
        );

        // gestisco l'evento di chiusura della finestra
        pane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setCloseWindow(pane);

            }
        });

        panelRegistration.setBorder(new EmptyBorder(50,50,0,50)); // setto i bordi del panel della registrazione
        pane.add(panelRegistration, BorderLayout.CENTER); // posizione il panel al centro
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("Registration - Memory");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame);

        //Use the content pane's default BorderLayout. 
        //Display the window.
        frame.setSize(new Dimension(275,275));
        frame.setResizable(false);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    }

    public void notifySubscribe() {
        feedback.setText("Registration Successful");
        waiting.setText("I'm waiting for other players to start the game.");
    }

    public void notifyErrorSubscribe() {
        int input = JOptionPane.showOptionDialog(null, // la root è il frame
                                "Registration not occured",
                                "Sorry",
                                JOptionPane.YES_OPTION, // tipo di button dell' alert
                                JOptionPane.INFORMATION_MESSAGE, // tipo di alert
                                null,null,null);
                        if(input == JOptionPane.YES_OPTION)
                            System.exit(0);
    }

    public void notifyGameStart() {
        feedback.setText("");
        waiting.setText("Starting game...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        frame.setVisible(false);
    }

    public void notifyErrorGameStart() {
        int exit = JOptionPane.showConfirmDialog(null,
                                    "Can't find other players. Do you want to exit?" ,
                                    "Sorry",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE);
                            if (exit == JOptionPane.YES_OPTION)
                                System.exit(0);
                            else{
                                feedback.setText("Can't find other players.");
                                waiting.setText("Exit game.");
                            }
    }
}
