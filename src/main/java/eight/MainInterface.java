package eight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainInterface extends JFrame implements ActionListener, MouseMotionListener,
        MouseListener, WindowListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    BufferedImage offscreen;
    Image title;
    Image cardsPic;
    Graphics g;
    Dimension screenSize;
    ImageIcon imageI;
    JLabel image;
    JPanel panel;
    JTextArea msg;
    JMenuItem menuItem;
    JMenu menu;
    JMenuBar menuBar;
    JScrollPane scrollPane;
    JTextField input;

    int tablePos;
    SendMessage actionHolder;
    ListenThread listenThread;

    public int getTablePos() {
        return tablePos;
    }

    public void setTablePos(int tablePos) {
        this.tablePos = tablePos;
    }

    MainInterface() {
        MediaTracker tracker = new MediaTracker(this);// Used to track loading of image
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        title = toolkit.getImage(this.getClass().getResource("/SHtitle.jpg"));
        cardsPic = toolkit.getImage(this.getClass().getResource("/changpai614.png"));

        tracker.addImage(title, 1);

        try {
            // Waiting until image loaded.
            tracker.waitForAll();
        } catch (InterruptedException e) {

        }

        offscreen = new BufferedImage(450, 550, BufferedImage.TYPE_3BYTE_BGR);
        g = offscreen.getGraphics();

        g.drawImage(title, 30, 120, this);

        g.setColor(Color.red);

        g.drawLine(0, 450, 450, 450);

        screenSize = toolkit.getScreenSize();

        imageI = new ImageIcon(offscreen);

        image = new JLabel(imageI);

        addMouseMotionListener(this);
        addMouseListener(this);
        requestFocus();

        input = new JTextField();
        input.addActionListener(this);
        msg = new JTextArea("Welcome to Shithead the card game\n", 4, 20);
        msg.setLineWrap(true);
        msg.setEditable(false);
        msg.setDisabledTextColor(Color.black);
        scrollPane = new JScrollPane(msg, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(gridbag);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        panel.setBackground(Color.white);
        getContentPane().add(panel);

        menuBar = new JMenuBar();

        // Build the first menu.
        menu = new JMenu("File");
        menuItem = new JMenuItem("Start Game",
                KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Start game now");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Quit",
                KeyEvent.VK_S);
        menuItem.getAccessibleContext().setAccessibleDescription(
                "Quit game now");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuBar.add(menu);

        panel.add(menuBar, c);
        c.gridy = 1;
        panel.add(image, c);

        c.gridy = 2;
        panel.add(scrollPane, c);
        c.gridy = 3;
        panel.add(input, c);

        addMsg("Detected Screen Size: " + screenSize.width + "x" + screenSize.height);

        if (screenSize.width < 1024) {
            addMsg("For optimal graphics use 1024x768 resolution");
        }

    }

    public void addMsg(String message)
    {

        if (message != null) {
            msg.append(message + "\n");
        }
        try {
            Rectangle current = msg.getVisibleRect();
            int scrollunitinc = msg.getScrollableUnitIncrement(current, SwingConstants.VERTICAL, 1);
            // System.out.println("scrollunitinc " + scrollunitinc + " Y " + current.getY());
            // current.setRect(current.getX(), current.getY() + scrollunitinc, current.getWidth(),
            // current.getHeight());
            current.setRect(current.getX(), (msg.getLineCount() + 1) * scrollunitinc,
                    current.getWidth(), current.getHeight());
            // System.out.println("Y " + current.getY());
            msg.scrollRectToVisible(current);
        } catch (Exception ex) {
            System.out.println("\n Error scrolling to end " + ex);
        }

    }

    private static void usage() {
        System.out.println("$0 0/1/2/3");

    }

    public static void main(String[] args) {

        if (args.length == 0) {
            usage();
            return;
        }

        int pos = Integer.parseInt(args[0]);

        try {
            MainInterface frame = new MainInterface();
            // frame.setTitle("Eight");
            frame.setResizable(false);

            frame.pack();
            frame.setVisible(true);
            frame.setTablePos(pos);

        } catch (Exception e) {
            System.out.println("System Error: " + e);
        }
    }

    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowClosing(WindowEvent arg0) {
        // TODO Auto-generated method stub
        System.exit(0);
    }

    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void actionPerformed(ActionEvent event) {
        String label = event.getActionCommand();
        if (label.equals("Quit")) {
            actionHolder.setRunning(false);
        } else if (label.equals("Start Game")) {

            if (null == listenThread) {
                ListenThread listenThread = new ListenThread(this, cardsPic, g);
                Thread t = new Thread(listenThread);
                t.start();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("actionPerform:");
            this.addMsg("actionPerform:" + tablePos);
            if (null == actionHolder) {

                actionHolder = SendMessage.getInstance();
                actionHolder.setTablePos(tablePos);
                // actionHolder.runAction();
            }

            Message message = new Message();
            message.setSerial(0);
            message.setAction(Action.GAMESTARTING);
            message.setIncludeServer(true);
            message.setPos(tablePos);
            actionHolder.pushAction(message);

        } else {
            addMsg(": " + input.getText());

            input.setText("");
        }
    }

    public void repaint()
    {
        // if(screenSize.width < 1024){
        // offscreen2 = offscreen.getScaledInstance(338, 413, Image.SCALE_FAST);
        // imageI.setImage(offscreen2);
        // }
        panel.repaint();
        // panel.update();
    }
}
