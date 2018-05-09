package inducesmile.com.test_real_time.Multiplayer;

import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;

/**
 * Created by joao on 08/05/2018.
 */

public class RoomConfigLocal {
    private static final RoomConfigLocal INSTANCE = new RoomConfigLocal();
    private String roomID;
    private ArrayList<String> mParticipants;
    private String myId;
    private int message=-1;



    private RoomConfigLocal(){
    }


    public static RoomConfigLocal getInstance(){
        return INSTANCE;
    }

    public void setRoomID(String roomID){
        this.roomID=roomID;
    }

    public void setParticipants (ArrayList<String> participants){
        mParticipants=participants;
    }

    public void setMyId(String myId){
        this.myId=myId;
    }

    public String getRoomID(){
        return roomID;
    }

    public ArrayList<String> getParticipants(){
        return mParticipants;
    }

    public String getMyId(){
        return myId;
    }

    public synchronized void  setMessage(int message){
        this.message=message;
        notifyAll();
    }

    public synchronized int getMessage(){
        Log.d("start","Starting thread");
        while (message==-1){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("returning",Integer.toString(message));
        return message;
    }


}
