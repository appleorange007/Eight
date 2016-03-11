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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainInterface extends JFrame implements ActionListener, MouseMotionListener,
        MouseListener, WindowListener {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(MainInterface.class);
    BufferedImage offscreen;
    Image title;
    Image cardsPic;
    Graphics g;
    Dimension screenSize;
    ImageIcon imageI;
    JLabel image;
    JPanel panel;

    JPanel imageP;

    JTextArea msg;
    JMenuItem menuItem;
    JMenu menu;
    JMenuBar menuBar;
    JScrollPane scrollPane;
    JTextField input;

    int tablePos;
    SendMessage actionHolder;
    ReceiveMessage listenThread;

    private boolean myTurn = false;
    private Hand hand;
    private Player player;

    int mouseX;
    int mouseY;

    public int getTablePos() {
        return tablePos;
    }

    public void setTablePos(int tablePos) {
        this.tablePos = tablePos;
    }

    public Player getMyPlayer() {
        return player;
    }

    public Hand getMyHand() {
        return hand;
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

        offscreen = new BufferedImage(1150, 550, BufferedImage.TYPE_3BYTE_BGR);
        g = offscreen.getGraphics();

        g.drawImage(title, 30, 120, this);
        g.setColor(Color.red);
        g.drawLine(0, 450, 450, 450);
        screenSize = toolkit.getScreenSize();

        // ToDo: card not display
        g.drawImage(cardsPic, 0, 0, this);

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

        JButton redBut = new JButton("Go");
        redBut.addActionListener(this);

        JButton blueBut = new JButton("Peng");
        blueBut.addActionListener(this);

        JButton whiteBut = new JButton("Chi");
        whiteBut.addActionListener(this);
        whiteBut.setEnabled(false);

        // redBut.setBounds(40, 40, 20, 20);
        // redBut.setVisible(true);
        // panel.add(redBut, c);

        panel.add(menuBar, c);
        c.gridy = 1;

        imageP = new JPanel();
        imageP.add(image);
        imageP.add(redBut);
        imageP.add(blueBut);
        imageP.add(whiteBut);

        panel.add(imageP, c);
        // panel.add(image, c);
        // panel.add(redBut);
        c.gridy = 2;
        panel.add(scrollPane, c);
        c.gridy = 3;
        panel.add(input, c);
        // c.gridy = 2;
        this.addMouseListener(new MyMouseListener());

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

            current.setRect(current.getX(), (msg.getLineCount() + 1) * scrollunitinc,
                    current.getWidth(), current.getHeight());
            msg.scrollRectToVisible(current);
        } catch (Exception ex) {
            System.out.println("\n Error scrolling to end " + ex);
        }

    }

    public static void main(String[] args) {

        int pos = Position.getInstance().getAvaiablePos();
        logger.info("start pos=" + pos);
        try {
            MainInterface frame = new MainInterface();
            frame.setTitle("Eight");
            frame.setResizable(false);

            frame.pack();
            frame.setVisible(true);
            frame.setTablePos(pos % Constant.PLAY_COUNT);

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
        if (arg0.getClickCount() >= 2) {
            // double clicked
            System.out.println("ui:" + arg0.getClickCount());
            System.out.println("drop card!");

            if (myTurn) {
                int selection = hand.mouseClick(mouseX, mouseY);
                // if (dealer != null && selection != -1)
                // dealer.cardSelection(selection);

                if (hand != null && selection != -1)
                    hand.mouseDoubleClick(selection);
            }
        } else {
            // clicked
        }
    }

    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent me) {
        if (myTurn) {
            int selection = hand.mouseClick(mouseX, mouseY);
            // if (dealer != null && selection != -1)
            // dealer.cardSelection(selection);

            if (player != null && selection != -1)
                player.cardSelection(selection);
        }
    }

    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseMoved(MouseEvent me) {
        // TODO Auto-generated method stub
        // ajusting so mouse points are over image
        mouseX = me.getX() - 5;
        mouseY = me.getY() - 45;
        if (screenSize.width < 1024) {// scaling mouse movement if screen to big
            mouseX = mouseX * 100 / 75;
            mouseY = mouseY * 100 / 75;
        }
        // addMsg("X: " + mouseX + " Y: " + mouseY);

    }

    public void actionPerformed(ActionEvent event) {

        String label = event.getActionCommand();
        if (label.equals("Quit")) {
            System.out.println("actionPerform: Quit!!");
            this.addMsg("actionPerform:" + tablePos);

            Message message = new Message();
            message.setSerial(0);
            message.setAction(Action.GAMEOVER);
            message.setIncludeServer(false);
            message.setPos(tablePos);
            actionHolder.pushAction(message);
        } else if (label.equals("Start Game")) {
            if (null == actionHolder) {
                actionHolder = SendMessage.getInstance();
                actionHolder.setTablePos(tablePos);
            }

            if (null == listenThread) {
                listenThread = new ReceiveMessage(this, cardsPic, g);
                Thread t = new Thread(listenThread);
                t.start();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            hand = new Hand(this, cardsPic, g);
            player = new Player(this, cardsPic, g);

            System.out.println("actionPerform: start!!");
            this.addMsg("actionPerform:" + tablePos);

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

    public static class MyMouseListener extends MouseAdapter {
        private static boolean flag = false;

        private static int clickNum = 0;

        @Override
        public void mouseClicked(MouseEvent e) {
            MyMouseListener.flag = false;
            System.out.println(clickNum);
            if (MyMouseListener.clickNum == 1) {
                System.out.println("execute doutlbe click event");
                MyMouseListener.clickNum = 0;
                MyMouseListener.flag = true;
                return;
            }

            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                private int n = 0;

                //
                @Override
                public void run() {
                    if (MyMouseListener.flag) {
                        MyMouseListener.clickNum = 0;
                        this.cancel();
                        return;
                    }
                    if (n == 1) {
                        System.out.println("double clicked");
                        MyMouseListener.flag = true;
                        MyMouseListener.clickNum = 0;
                        n = 0;
                        this.cancel();
                        return;
                    }
                    clickNum++;
                    n++;
                    System.out.println("" + n);
                    System.out.println(clickNum);
                }
            }, new Date(), 200);
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
