package src;
import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import java.awt.*;
import java.awt.event.*;


public class PaginaPrincipale {

    private static void setExitControl(){
        int input = JOptionPane.showOptionDialog(null,
                "Sicuro di voler uscire del gioco?",
                "Esci",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);
        System.out.println(input);
        if(input == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }

    private static void setAboutControl(){
        JOptionPane.showOptionDialog(null,
                "Il gioco di memory è stato realizzato da: Salvatore Alescio,\n" +
                        "Alice Valentini, Andrea Zuccarini. Per il progetto di \n" +
                        "Sistemi Distribuiti",
                "Esci",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);
    }

    private static void setRegoleControl(){
        JOptionPane.showOptionDialog(null,
                      "Memory, noto anche come coppie, è un popolare gioco di carte che richiede concentrazione e memoria.\n" +
                         "Nel gioco, le carte sono inizialmente mescolate e disposte coperte sul tavolo. I giocatori, a turno,\n"+
                         "scoprono due carte;  se queste formano una \"coppia\", vengono incassate dal giocatore di turno, che può scoprirne altre due;\n" +
                         "altrimenti, vengono nuovamente coperte e rimesse nella loro posizione originale sul tavolo, e il turno passa al prossimo giocatore.\n"+
                         "Vince il giocatore che riesce a scoprire più coppie.",
                "Esci",
                JOptionPane.CLOSED_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,null,null);

    }

    public static void createPaginaPrincipale(final JFrame pane) {

        pane.setTitle("Memory");
        pane.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setExitControl();
            }
        });

        //creo la menubar
        JMenuBar menuBar = new JMenuBar();

        //creo il primo menu delle impostazioni
        JMenu menuImpostazioni = new JMenu("Impostazioni");
        JMenuItem esci = new JMenuItem("Esci", KeyEvent.VK_Q);
        KeyStroke crtlQKeyStroke = KeyStroke.getKeyStroke("control Q");
        esci.setAccelerator(crtlQKeyStroke);
        menuBar.add(menuImpostazioni);

        esci.setToolTipText("Exit Application");
        esci.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setExitControl();
            }
        });

        esci.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();

            }

            @Override
            public void menuKeyPressed(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                   setExitControl();
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
                if(menuKeyEvent.getKeyCode() == KeyEvent.VK_Q)
                    setExitControl();

            }
        });


        menuImpostazioni.add(esci);
        pane.setJMenuBar(menuBar);

        //creo il secondo menu delle regole
        JMenu menuRegole = new JMenu("?");
        JMenuItem regole = new JMenuItem("Regole");
        JMenuItem about = new JMenuItem("about");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAboutControl();
            }
        });

        regole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRegoleControl();
            }
        });
        menuRegole.add(regole);
        menuRegole.add(about);
        menuBar.add(menuRegole);

    }


    public static void createMainPage(){
        Board frame = new Board();
        createPaginaPrincipale(frame);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(new Dimension(700,675));
        frame.setVisible(true);
    }

}
