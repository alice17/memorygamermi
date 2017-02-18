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


public class WindowRegistration {
    private static boolean RIGHT_TO_LEFT = false;
    private static final int SIZE_OF_TEXTFIELD = 10;
    public static  String IMG_PATH = "img/Memory.png";

    private static void setCloseWindow(final JFrame frame) {
        int input = JOptionPane.showOptionDialog(frame,
                "Sicuro di voler uscire del gioco?",
                "Esci",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);
        if( input == JOptionPane.YES_OPTION)
            System.exit(0);
    }

    private static void settingEventRegistration(JFrame fr, JTextField tf, JLabel lb){
        if(tf.getText() == null || tf.getText().isEmpty()){

            JOptionPane.showOptionDialog(null,
                    "Non hai inserito il nome utente!",
                    "Esci",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,null,null);



        }
        else if(tf.getText().contains(" ") ){
            JOptionPane.showOptionDialog(null,
                    "Non inserire \"Spazi\" nello UserName",
                    "Esci",
                    JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,null,null);

        }
        else{
                Client cl = new Client(tf.getText());
                fr.setVisible(false);


        }

    }





    public static void addComponentsToPane(final JFrame pane) {


        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }

        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(
                    java.awt.ComponentOrientation.RIGHT_TO_LEFT);
        }

        pane.getContentPane().setForeground(Color.WHITE);

        JPanel panelImg = new JPanel();

        try{
            BufferedImage imgLogo;
            imgLogo = ImageIO.read(new File(IMG_PATH));
            ImageIcon icon = new ImageIcon(imgLogo);
            JLabel labelLogo = new JLabel(icon);
            panelImg.add(labelLogo,BorderLayout.CENTER);

        }catch (IOException e){
            e.printStackTrace();
        }

        panelImg.setSize(new Dimension(400,375));
        panelImg.setBorder(new EmptyBorder(10,0,0,0));
        pane.add(panelImg, BorderLayout.PAGE_START);

        //Make the center component big, since that's the
        //typical usage of BorderLayout.

        JPanel panelRegistration = new JPanel();
        JLabel userLabel = new JLabel("User");
        final JLabel responseLabel = new JLabel();

        final JTextField userEntry = new JTextField();
        userEntry.setColumns(SIZE_OF_TEXTFIELD);

        JButton btnRegistration = new JButton("Registrati");
        final PaginaPrincipale pgMemory = new PaginaPrincipale();


        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingEventRegistration(pane,userEntry,responseLabel);
            }
        });

        btnRegistration.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry,responseLabel);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                }
                settingEventRegistration(pane,userEntry,responseLabel);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry,responseLabel);
                }
            }
        });


        userEntry.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry,responseLabel);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry,responseLabel);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    settingEventRegistration(pane,userEntry,responseLabel);
                }

            }



        });
        GroupLayout groupRegistration = new GroupLayout(panelRegistration);
        groupRegistration.setAutoCreateGaps(true);



        groupRegistration.setHorizontalGroup(groupRegistration.createParallelGroup()
                .addComponent(userLabel)
                .addComponent(userEntry)
                .addComponent(btnRegistration)
                .addComponent(responseLabel)
        );

        groupRegistration.setVerticalGroup(groupRegistration.createSequentialGroup()
                .addComponent(userLabel)
                .addComponent(userEntry)
                .addComponent(btnRegistration)
                .addComponent(responseLabel)
        );


        pane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setCloseWindow(pane);

            }
        });

        panelRegistration.setBorder(new EmptyBorder(50,50,0,50));
        pane.add(panelRegistration, BorderLayout.CENTER);


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