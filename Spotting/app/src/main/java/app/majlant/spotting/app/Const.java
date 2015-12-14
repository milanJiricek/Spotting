package app.majlant.spotting.app;

/**
 * Created by majlant on 19.11.15.
 *
 * @author Milan Jiricek <milan.jiricekpv@gmail.com></milan.jiricekpv@gmail.com>
 */
public class Const {
    //TODO dodelat navratovy chyby a opravit echo na serveru
    public static final int OK = 0;
    public static final int INCORRECT_NICK_OR_PASS = -1;
    public static final int MISSING_REQUIRED_PARAM = -2;
    public static final int CONN_ERROR = -3;
    public static final int UNKNOWN_TAG_VALUE = -4;
    public static final int USER_ALREADY_EXISTED = -5;
    public static final int ERROR_OCCURRED_IN_REGISTRATION = -6;
    public static final int NICK_OR_MAIL_DOES_NOT_EXIST = -7;
    public static final int MAIL_NEW_PASS_NOT_SENT = -8;
    private static final String TAG = "Const";
}
