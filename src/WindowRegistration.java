package src;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.Random;


/**
 * La classe Window Registration permette di inizializzare un'interfaccia grafica di registratione al gioco.
 * Infatti per poi lanciare il client al click di registrazione.
 */

public class WindowRegistration {
    private static boolean RIGHT_TO_LEFT = false; //variabile che mi setta l'orientamento del posizione degli elementi nella finestra
    private static final int SIZE_OF_TEXTFIELD = 10; // variabile che mi gestisce la lunghezza textfield
    public static  String IMG_PATH = "img/Memory.png"; // stringa per path del logo

    /*
    * setCloseWindow è un metodo che gestisce la chiusura della finestra
    */

    private static void setCloseWindow(final JFrame frame) {
        int input = JOptionPane.showOptionDialog(frame, // la root è il frame
                "Sicuro di voler uscire del gioco?",
                "Esci",
                JOptionPane.YES_NO_OPTION, // tipo di button dell' alert
                JOptionPane.INFORMATION_MESSAGE, // tipo di alert
                null,null,null);
        if( input == JOptionPane.YES_OPTION)
            System.exit(0); // se clicko Si mi esce dal gioco (ovviamente da sistemare)
    }

    /*
     * settingEventRegistration è un metodo che gestisce l'evento dell'immissione del nome nel textfied
     */
    private static void settingEventRegistration(final JFrame fr, final JTextField tf){
        if(tf.getText() == null || tf.getText().isEmpty()){
            //gestisco il caso in cui non aggiungo nessun nome
            JOptionPane.showOptionDialog(null,
                    "Non hai inserito il nome utente!",
                    "Esci",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE, // tipo di messaggio
                    null,null,null);



        }
        // gestisco il caso in cui nella textfield inserisco uno spazio vuoto (non li vogio avere!)

        else if(tf.getText().contains(" ") ){
            JOptionPane.showOptionDialog(null,
                    "Non inserire \"Spazi\" nello UserName",
                    "Esci",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE, // tipo di messaggio errore
                    null,null,null);

        }
        else{
            // nel caso in cui è tutto ok, allora lancio il client e gli passo la stringa
            Thread t = new Thread() {
        		public void run() {
                	Client cl = new Client(tf.getText());
                	fr.setVisible(false);
				}
			};
			
			t.start();

        }

    }

    /*
     * addComponentsToPane è un metodo che crea la borad layout per inserire tutti gli oggetti
     */

    public static void addComponentsToPane(final JFrame pane) {


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

        final JButton btnRegistration = new JButton("Registrati"); // creo la il button per avviare la registrazione


        // gestisco ora l'evento legato al button di registrazione al click
        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingEventRegistration(pane,userEntry);
                btnRegistration.setEnabled(false);
            }
        });
        
        //gestisco l'evento legato al button ma premendo invio
        btnRegistration.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                }
                	settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                }
            }
        });

        //gestisco l'evento di registrazione eseguendo l'invio da textfield
        userEntry.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry);
                    btnRegistration.setEnabled(false);
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
        );
        //setto gli oggetti con l'orientamento verticaole
        groupRegistration.setVerticalGroup(groupRegistration.createSequentialGroup()
                .addComponent(userLabel) // aggiungo la label
                .addComponent(userEntry) // aggiungo la textfield
                .addComponent(btnRegistration) //aggiungo il button
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
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("Registrazione - Memory");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame);

        //Use the content pane's default BorderLayout. No need for
        //setLayout(new BorderLayout());
        //Display the window.
        frame.setSize(new Dimension(275,275));
        frame.setResizable(false);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
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
        /* Turn off metal's use bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
