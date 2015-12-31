package eight;

import java.awt.Graphics;
import java.awt.Image;

public class Dealer {
    private final int ALL_CARD_COUNT = 124;
    private final int PLAY_CARD_COUNT = 25;
    private final int PLAY_COUNT = 2; // 4, for debug to 1
    private int[] cardNumbers = new int[ALL_CARD_COUNT];
    private Card[] cards = new Card[ALL_CARD_COUNT];
    private final int initPlayCardCnt = PLAY_CARD_COUNT;
    private int started = 0;
    private int serial = 0;
    private MainInterface main;
    private Image cardspic;
    private Graphics g;

    private Player[] plays = new Player[PLAY_COUNT];

    public void setSerial(int serial) {
        this.serial = serial;
    }

    Dealer(MainInterface mainI, Image cardspic, Graphics g) {
        this.main = mainI;
        this.cardspic = cardspic;
        this.g = g;

    }

    public void dealMessage(Message msg) {

        if (msg.getSerial() < 0) {
            return;
        }
        System.out.println("dealMessage: serial = " + msg.getSerial());
        this.setSerial(msg.getSerial() + 1);
        switch (msg.getAction()) {
        case GAMESTARTING:
            started++;
            if (PLAY_COUNT == started) {
                gameStart();
            }
            break;
        case CARDINIT:
            dealInitCard(msg);
            break;
        case GAMEOVER:
            gameOver();
            break;
        default:
            break;

        }
    }

    private void gameStart() {
        initCard();
        shuffle();
        SendInitcard();
    }

    private void gameOver() {
        this.main.listenThread.setEnd();
    }

    private void SendInitcard() {
        Message sendCard = new Message();
        sendCard.setAction(Action.CARDINIT);
        sendCard.setIncludeServer(true);
        sendCard.setSerial(serial);

        sendCard.setCardNumbers(cardNumbers);
        SendMessage.getInstance().pushAction(sendCard);
    }

    private void dealInitCard(Message msg) {

        for (int i = 0; i < msg.getCardNumbers().length; i++) {
            cards[i] = new Card(msg.getCardNumbers()[i], cardspic, main, g);
        }

        int cardNum = 0;
        for (int i = 0; i < PLAY_COUNT; i++) {
            plays[i] = new Player(cardspic, main, g);

            for (int cardCnt = 0; cardCnt < initPlayCardCnt; cardCnt++) {
                plays[i].addHandCard(cards[cardNum++]);
            }

            if (0 == i) {
                plays[i].addHandCard(cards[cardNum++]);
            }

            main.addMsg(plays[i].displayAllHandCardStr());
            plays[i].displayTable();
        }

    }

    private void shuffle() {
        for (int r = 0; r < 2; r++) {
            // number of times pile shuffled
            for (int n = 0; n < cards.length; n++) {
                int newlocation = (int) Math.round(Math.random() * (cards.length - 1));
                int temp = cardNumbers[newlocation];
                cardNumbers[newlocation] = cardNumbers[n];
                cardNumbers[n] = temp;
            }
        }
        return;
    }

    private void initCard() {
        int cardNum = 0;
        for (int i = 0; i < 30; i++) {
            for (int repeat = 0; repeat < 4; repeat++) {
                cardNumbers[cardNum++] = i;
            }
        }
        int except = (int) (Math.random() * 5);

        for (int i = 0; i < 5; i++) {
            if (except == i) {
                continue;
            }
            cardNumbers[cardNum++] = 30 + i;
        }
        return;
    }
}
