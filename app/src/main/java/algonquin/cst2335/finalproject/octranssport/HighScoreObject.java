package algonquin.cst2335.finalproject;

public class HighScoreObject {

    private long id;
    private String userName;
    private String difficultyLevel;
    private int correctAnswers;
    private int total;

    public HighScoreObject(String userName, String difficultyLevel, int correctAnswers, int total) {
        this.userName = userName;
        this.difficultyLevel = difficultyLevel;
        this.correctAnswers = correctAnswers;
        this.total = total;
    }

    public HighScoreObject(long id, String userName, String difficultyLevel, int correctAnswers, int total) {
        this.id = id;
        this.userName = userName;
        this.difficultyLevel = difficultyLevel;
        this.correctAnswers = correctAnswers;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}

