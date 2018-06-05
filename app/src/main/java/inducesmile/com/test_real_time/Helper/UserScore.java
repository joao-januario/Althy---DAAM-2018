package inducesmile.com.test_real_time.Helper;

public class UserScore {
    private static final UserScore INSTANCE = new UserScore();



    private int total_score=0;

    private UserScore(){

    }

    public static UserScore getInstance(){
        return INSTANCE;
    }


    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int score) {
        total_score =  total_score + score;
    }

    public void clear_score(){
        total_score=0;
    }



}
