/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hungerbow;
/**
 *
 * @author Cloud
 */
public class MainFrame extends javax.swing.JFrame implements java.awt.event.KeyListener, java.awt.event.WindowListener {
    private hungerbow.Panel panel;
    
    public MainFrame() {
        super("Hunger Bow 2.3 | Created By Mohammad Kahfi Nim 190155201047");
        inisialisasi();
    }
    
    final void inisialisasi() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {}
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            setResizable(false);
            getContentPane().setBackground(java.awt.Color.black);
            setSize(820, 600);
            setLocationRelativeTo(null);
            
            panel = new Panel();
            setContentPane(panel);
            addKeyListener(this);
            addWindowListener(this);
            if(true) {
            java.awt.image.BufferedImage blankCursorImg = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            java.awt.Cursor blankCursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new java.awt.Point(0,0),null);
            this.setCursor(blankCursor);
            }
            try {
                Thread.sleep(3000);
            } catch(InterruptedException ex) {}
            show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new MainFrame();
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {}

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        panel.keyPressed(e);
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {}

    @Override
    public void windowOpened(java.awt.event.WindowEvent e) {}

    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowClosed(java.awt.event.WindowEvent e) {}

    @Override
    public void windowIconified(java.awt.event.WindowEvent e) {}

    @Override
    public void windowDeiconified(java.awt.event.WindowEvent e) {}

    @Override
    public void windowActivated(java.awt.event.WindowEvent e) {}

    @Override
    public void windowDeactivated(java.awt.event.WindowEvent e) {}
    
}

