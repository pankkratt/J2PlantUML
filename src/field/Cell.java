package field;

import java.applet.*;
import java.awt.*;

class Cell {
    Sign sign;
    enum Sign {
        DECK,
        DESTROYED,
        EMPTY,
        MARKED,
    }

    Cell() {
        sign = Sign.EMPTY;
    }

    void setSign(Sign sign) {
        this.sign = sign;
    }

    Sign getSign() {
        return sign;
    }
}
