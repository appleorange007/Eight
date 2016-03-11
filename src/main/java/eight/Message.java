package eight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Message {
    private static int serial;
    private Action action;
    private int pos;
    private int[] cardNumbers;

    private boolean includeServer;

    public int getSerial() {
        return serial;
    }

    public int[] getCardNumbers() {
        return cardNumbers;
    }

    public void setCardNumbers(int[] cardNumbers) {
        this.cardNumbers = cardNumbers;
    }

    public void setSerial(int num) {
        serial = num;
    }

    public void incSerial() {
        serial++;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isIncludeServer() {
        return includeServer;
    }

    public void setIncludeServer(boolean includeServer) {
        this.includeServer = includeServer;
    }

    public boolean isValid() {
        return serial >= 0;
    }

    public void clear() {
        serial = -1;
    }

    public String toString() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

}
