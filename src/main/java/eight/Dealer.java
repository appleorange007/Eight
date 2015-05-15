package eight;

import java.awt.Graphics;
import java.awt.Image;

public class Dealer {

    private int[] cardNumbers = new int[124];
    private Card[] cards = new Card[124];
    private final int initPlayCardCnt = 25;
    private int started = 0;
    private int serial = 0;
    private MainInterface main;
    private Image cardspic;
    private Graphics g;

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

        this.setSerial(msg.getSerial() + 1);
        switch (msg.getAction()) {
        case GAMESTARTING:
            started++;
            if (4 == started) {
                gameStart();
            }
            break;
        case CARDINIT:
            dealInitCard(msg);
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

    private void SendInitcard() {
        Message sendCard = new Message();
        sendCard.setAction(Action.CARDINIT);
        sendCard.setIncludeServer(true);
        sendCard.setSerial(serial);

        sendCard.setCardNumbers(cardNumbers);
        SendMessage.getInstance().pushAction(sendCard);
    }

    private void dealInitCard(Message msg) {

        for (int i = 0; i < cardNumbers.length; i++) {
            cards[i] = new Card(cardNumbers[i], cardspic, main, g);
        }
        int pos = main.getTablePos();
        String cardStr = "";
        if (0 == pos) {
            for (int i = 0; i < initPlayCardCnt + 1; i++) {
                cardStr = cardStr + cards[i].cardNumber + " ";
            }
        } else if (1 == pos) {
            for (int i = initPlayCardCnt + 1; i < initPlayCardCnt * 2 + 1; i++) {
                cardStr = cardStr + cards[i].cardNumber + " ";
            }
        } else if (2 == pos) {
            for (int i = initPlayCardCnt * 2 + 1; i < initPlayCardCnt * 3 + 1; i++) {
                cardStr = cardStr + cards[i].cardNumber + " ";
            }
        } else {
            for (int i = initPlayCardCnt * 3 + 1; i < initPlayCardCnt * 4 + 1; i++) {
                cardStr = cardStr + cards[i].cardNumber + " ";
            }
        }
        main.addMsg(cardStr);
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
