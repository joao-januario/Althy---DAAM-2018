package inducesmile.com.test_real_time.Multiplayer;

import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;

import java.lang.reflect.Array;
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
    private ArrayList<Integer> host_deciders = new ArrayList<>();
    private int myNumberHost;
    private boolean host=true;


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

    public void setMyNumberHost(int number){
        myNumberHost=number;
    }

    public synchronized boolean im_host(){
        return host;
    }

    public synchronized void  setMessage(byte[] message){
        if (message[0]=='H'){
            Log.d("Message","Host message received");
            host_deciders.add((int) message[1]);
            if (host_deciders.size()==mParticipants.size()-1){
                notifyAll();
            }
        }


    }

    public synchronized int getMessage(){
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



    public synchronized void decideHost(){
        while (host_deciders.size()!=mParticipants.size()-1){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("Decider","Host is being decided");
            Log.d("My Number", Integer.toString(myNumberHost));
        for (Integer i: host_deciders){
            Log.d("other_number",Integer.toString(i));
            if (i>myNumberHost){
                host=false;
            }
        }



    }


}
