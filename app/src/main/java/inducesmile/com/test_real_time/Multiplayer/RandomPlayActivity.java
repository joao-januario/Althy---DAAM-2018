package inducesmile.com.test_real_time.Multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import inducesmile.com.test_real_time.AppNav.MenuActivity;
import inducesmile.com.test_real_time.Helper.MultiplayerLogin;
import inducesmile.com.test_real_time.R;

public class RandomPlayActivity extends AppCompatActivity {

    int open_intent;
    //ON ACTIVITY RESULT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_play_activty);

        //transition
        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        startQuickGame();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }


    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();
    ArrayList<Participant> mParticipants = null;
    String mRoomId = null;
    MultiplayerLogin login = MultiplayerLogin.getInstance();
    RealTimeMultiplayerClient mRealTimeMultiplayerClient;
    final static int RC_WAITING_ROOM = 10002;
    RoomConfig mroomConfig;
    String mPlayerId;
    String mMyId = null;

    public void startQuickGame() {
        updatePlayerInfo();
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1,1,0);

        // build the room config:
        mroomConfig =
                RoomConfig.builder(mRoomUpdateCallback)
                        .setOnMessageReceivedListener(mReceivedListener)
                        .setRoomStatusUpdateCallback(mRoomStatusUpdateCallback)
                        .setAutoMatchCriteria(autoMatchCriteria)
                        .build();

        // create room:
        Games.getRealTimeMultiplayerClient(this, login.getSignedInAccount())
                .create(mroomConfig);


        Log.d("TesteMultiplayer","asfasfas");

    }





    //Qualquer coisa da sala

    private RoomUpdateCallback mRoomUpdateCallback = new RoomUpdateCallback() {

        // Called when room has been created
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }

            // save room ID so we can leave cleanly before the game starts.
            roomConfigLocal.setRoomID(room.getRoomId());
            mRoomId = room.getRoomId();

            // show the waiting room UI
            showWaitingRoom(room);
        }

        // Called when room is fully connected.
        @Override
        public void onRoomConnected(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }
            updateRoom(room);
        }

        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            if (statusCode != GamesCallbackStatusCodes.OK) {
                showGameError();
                return;
            }

            // show the waiting room UI
            showWaitingRoom(room);
        }

        // Called when we've successfully left the room (this happens a result of voluntarily leaving
        // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
        @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId) {
            // we have left the room; return to main screen.
            // To do
        }
    };




    //Mensagens Real Time


            OnRealTimeMessageReceivedListener mReceivedListener =  new OnRealTimeMessageReceivedListener() {
                @Override
                public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
                    Log.d("Message","mensagem recebida");
                    roomConfigLocal.setMessage(realTimeMessage.getMessageData(),realTimeMessage.getSenderParticipantId());
        }
    };

    void showGameError() {
        new AlertDialog.Builder(this)
                .setMessage(this.getString(R.string.game_problem))
                .setNeutralButton(android.R.string.ok, null).create();
        //Falta mandar para o menu principal

    }

    // Show the waiting room UI to track the progress of other players as they enter the
    // room and get connected.
    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        mRealTimeMultiplayerClient.getWaitingRoomIntent(room, MIN_PLAYERS)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                });

    }
    // show waiting room UI

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_WAITING_ROOM) {
            // we got the result from the "waiting room" UI.
            if (resultCode == Activity.RESULT_OK) {
                // ready to start playing
                startGame(true);
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player indicated that they want to leave the room
                leaveRoom();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Dialog was cancelled (user pressed back key, for instance). In our game,
                // this means leaving the room too. In more elaborate games, this could mean
                // something else (like minimizing the waiting room UI).
                Intent menuIntent = new Intent(this, MenuActivity.class);
                startActivity(menuIntent);
            }
        }

    }





    private RoomStatusUpdateCallback mRoomStatusUpdateCallback = new RoomStatusUpdateCallback() {

        @Override
        public void onRoomConnecting(@Nullable Room room) {
            updateRoom(room);
        }

        @Override
        public void onRoomAutoMatching(@Nullable Room room) {
            updateRoom(room);
        }

        @Override
        public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onConnectedToRoom(@Nullable Room room) {
            //roomConfigLocal.setParticipants(room.getParticipants());
            //mParticipants = room.getParticipants();

            mMyId = room.getParticipantId(mPlayerId);
            roomConfigLocal.setMyId(mMyId);
            if (mRoomId == null) {
                roomConfigLocal.setRoomID(room.getRoomId());
                mRoomId = room.getRoomId();
            }
        }


        @Override
        public void onDisconnectedFromRoom(@Nullable Room room) {
            roomConfigLocal.setRoomID(null);
            mRoomId=null;
            mroomConfig=null;
            showGameError();
        }

        @Override
        public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
            updateRoom(room);
        }

        @Override
        public void onP2PConnected(@NonNull String s) {
            // Não sei o que é isto
        }

        @Override
        public void onP2PDisconnected(@NonNull String s) {
            //Não sei o que é isto
        }
    };














    //Funções Auxiliares

    private void startGame(boolean multiplayer){
        Intent intent = new Intent(this,LoadingScreenMultiplayerActivity.class);

        startActivity(intent);
    }

    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }
        if (mParticipants != null) {
            for (Participant p: mParticipants){
                roomConfigLocal.addUsername(p.getParticipantId(),p.getDisplayName());
            }
            ArrayList<String> participants23= room.getParticipantIds();
            roomConfigLocal.setParticipants(participants23);
            Log.d("idPlayer",room.getParticipantIds().get(0));


        }
    }

    void leaveRoom() {
        //To do
    }

    void updatePlayerInfo(){
        mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, login.getSignedInAccount());

        // get the playerId from the PlayersClient
        PlayersClient playersClient = Games.getPlayersClient(this, login.getSignedInAccount());
        playersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        mPlayerId = player.getPlayerId();

                    }
                });
    }


}