package eight;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;
import java.util.List;

public class Dealer {

    private int[] cardNumbers = new int[Constant.ALL_CARD_COUNT];
    private Card[] cards = new Card[Constant.ALL_CARD_COUNT];
    private final int initPlayCardCnt = Constant.PLAY_CARD_COUNT;
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
        System.out.println("dealMessage: serial = " + msg.getSerial());
        this.setSerial(msg.getSerial() + 1);
        switch (msg.getAction()) {
        case GAMESTARTING:
            started++;
            if (Constant.PLAY_COUNT == started) {
                gameStart();
            }
            break;
        case CARDINIT:
            dealInitCard(msg);
            break;
        case CARDCHU:
            dealChu(msg);
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
        for (int i = 0; i < Constant.PLAY_COUNT; i++) {
            int myCardCount = (i == 0) ? initPlayCardCnt + 1 : initPlayCardCnt;

            if (main.getTablePos() % Constant.PLAY_COUNT == i) {
                List<Card> cardList = Arrays.asList(cards);
                List<Card> myCardList = cardList.subList(cardNum, cardNum + myCardCount);
                main.getMyPlayer().initHandCard(myCardList.toArray(new Card[myCardList.size()]));
                cardNum += myCardCount;
                main.addMsg(main.getMyHand().displayAllHandCardStr());
                main.getMyPlayer().displayTable();
            } else {
                cardNum += myCardCount;
            }
        }
    }

    private void dealChu(Message msg) {
        int cardNo = msg.getCardNumbers()[0];
        int pos = msg.getPos();

        if (main.getTablePos() % Constant.PLAY_COUNT == pos) {
            main.getMyHand().chu(cardNo);
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
