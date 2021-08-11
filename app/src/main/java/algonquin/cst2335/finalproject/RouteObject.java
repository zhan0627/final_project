package algonquin.cst2335.finalproject;

public class RouteObject {
    public static final int STATE_UNANSWERED = 0;
    public static final int STATE_WRONG = 1;
    public static final int STATE_RIGHT = 2;

    private int quId;
    private String type;
    private String qu;
    private static String correct;
    private String inCorrect[];
    private int state = STATE_UNANSWERED;

    public RouteObject(int id, String type, String qu, String correct, String[] inCorrect) {
        super();
        this.quId = id;
        this.type = type;
        this.qu = qu;
        this.correct = correct;
        this.inCorrect = inCorrect;
    }

    public int getId() {
        return quId;
    }

    public String getQuestion() {
        return qu;
    }

    public String gettype() {
        return type;
    }

    public static String getcorrect() {
        return correct;
    }

    public String[] getinCorrect() {
        return inCorrect;
    }

    public int getState() {
        return state;
    }

    public static void setState(int state) {
       // this.state = state;
    }
}


