package inducesmile.com.test_real_time.Multiplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Game.Question_Activity;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;

/**
 * Created by joao on 08/05/2018.
 */

public class RoomConfigLocal extends Question_Activity{
    private static final RoomConfigLocal INSTANCE = new RoomConfigLocal();
    private String roomID;
    private ArrayList<String> mParticipants;
    private ArrayList<String> playersReady = new ArrayList<>();
    private String myId;
    private int message=-1;
    private ArrayList<Integer> host_deciders = new ArrayList<>();
    private ArrayList<Integer> questionsPicked = new ArrayList<>();
    private int myNumberHost;
    private boolean host=true;
    private boolean settingUpQuestions=false;
    private boolean readyToPlay=false;
    public Context current_context;

    private RoomConfigLocal(){
    }


    MultiplayerQuestionHandler multiplayerHandler = MultiplayerQuestionHandler.getInstance();

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

    public synchronized void setReady(boolean readyToPlay){
        this.readyToPlay=readyToPlay;
        notifyAll();
    }
    public synchronized void  setMessage(byte[] message){

        //Mensagem H são mensagens para decidir o Host, ele fica à espera que todos mandem uma mensagem H, o que escolher o maior numero é considerado o Host, a thread que é acordada está na classe LoadingScreenMultiplayer
        if (message[0]=='H'){
            Log.d("Message","Host message received");
            host_deciders.add((int) message[1]);
            if (host_deciders.size()==mParticipants.size()-1){
                notifyAll();
            }
        }

        if(message[0]=='R'){
            Log.d("Ready Message","Ready Message received");
            playersReady.add("r");
            notifyAll();
        }

        else{
         if(settingUpQuestions){

             BigInteger bi = new BigInteger(message);
             int question_number = bi.intValue();
             Log.d("This Question",""+question_number);
             questionsPicked.add(question_number);
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



    public synchronized void decideHost() {
        while (host_deciders.size() != mParticipants.size() - 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("Decider", "Host is being decided");
        Log.d("My Number", Integer.toString(myNumberHost));
        for (Integer i : host_deciders) {
            Log.d("other_number", Integer.toString(i));
            if (i > myNumberHost) {
                host = false;
            }
        }
        if(host==false){
            settingUpQuestions=true;
        }
    }

    public synchronized void setUpQuestions(){
        while (questionsPicked.size()<5){
            try {
                wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        Collections.sort(questionsPicked);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("perto").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    multiplayerHandler.newGame();
                    for(int i=0;i<questionsPicked.size();i++){

                        DataSnapshot question_data = dataSnapshot.child(Integer.toString(questionsPicked.get(i)));
                        Question q = question_data.getValue(Question.class);
                        multiplayerHandler.addQuestion(q);


                    }
                    settingUpQuestions=false;
                    setReady(true);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });

        }


    public synchronized void waitForEveryoneReady(){
        if (!readyToPlay){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
            if (playersReady.size()!= mParticipants.size()-1){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }
}
