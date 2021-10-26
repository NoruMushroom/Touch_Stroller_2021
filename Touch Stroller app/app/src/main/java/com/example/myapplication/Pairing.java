package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.google.firebase.auth.FirebaseAuth;

import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;



@SuppressWarnings("ALL")
public class Pairing extends AppCompatActivity {

    // 온습도
    private LinearLayout weather;
    private TextView temp, humd, baby;
    private String[] array = new String[3];
    private ImageView st_baby;


    // 날씨



    // 블루투스
    private final int REQUEST_BLUETOOTH_ENABLE = 100;
    private TextView mConnectionStatus;
    ConnectedTask mConnectedTask = null;
    static BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    static boolean isConnectionError = false;
    private static final String TAG = "BluetoothClient";

    // 자동 수동
    Switch choice;
    LinearLayout auha;
    MapView mapView;
    LinearLayout map;
    RelativeLayout mapViewContainer;
    Button send_1;


    // 수동 버튼
    ToggleButton bt_sun, bt_led, bt_wind;

    //로그아웃
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // -----------------------------------------------------------------------------------------
        // ---------------------------------------로그아웃-------------------------------------------
        // -----------------------------------------------------------------------------------------

        mFirebaseAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();

                Intent intent = new Intent(Pairing.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });


        // -----------------------------------------------------------------------------------------
        // ---------------------------------------카카오 맵-------------------------------------------
        // -----------------------------------------------------------------------------------------

        map = (LinearLayout)findViewById(R.id.map);
        mapViewContainer = (RelativeLayout)findViewById(R.id.map_view);
        mapView = new MapView(this);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

        // -----------------------------------------------------------------------------------------
        // ---------------------------------------VISIBLE------------------------------------------
        // -----------------------------------------------------------------------------------------

        choice = (Switch)findViewById(R.id.choice);
        LinearLayout pas = findViewById(R.id.pas);

        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choice.isChecked()) {
                    pas.setVisibility(View.VISIBLE);
                    sendMessage("0");
                    Toast.makeText(Pairing.this, "수동 모드 ON", Toast.LENGTH_SHORT).show();
                } else {
                    pas.setVisibility(View.GONE);
                    sendMessage("1");
                    Toast.makeText(Pairing.this, "자동 모드 ON", Toast.LENGTH_SHORT).show();
                }
            }
        });

        send_1 = findViewById(R.id.send_1);
        send_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage("1");
            }
        });

        // -----------------------------------------------------------------------------------------
        // ----------------------------------------블루투스------------------------------------------
        // -----------------------------------------------------------------------------------------

        Button bt_blue = findViewById(R.id.bt_blue);

        bt_led = (ToggleButton) findViewById(R.id.bt_leds);
        bt_led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_led.isChecked()) {
                    sendMessage("2");
                    Toast.makeText(Pairing.this, "LED ON", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage("3");
                    Toast.makeText(Pairing.this, "LED OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_sun = (ToggleButton) findViewById(R.id.bt_suns);
        bt_sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_sun.isChecked()) {
                    sendMessage("4");
                    Toast.makeText(Pairing.this, "선루프 ON", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage("5");
                    Toast.makeText(Pairing.this, "선루프 OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_wind = (ToggleButton) findViewById(R.id.bt_winds);
        bt_wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_wind.isChecked()) {
                    sendMessage("6");
                    Toast.makeText(Pairing.this, "선풍기 ON", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage("7");
                    Toast.makeText(Pairing.this, "선풍기 OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mConnectionStatus = findViewById(R.id.connection_status_textview);
        ListView mMessageListview = findViewById(R.id.message_listview);

        mConversationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        mMessageListview.setAdapter(mConversationArrayAdapter);


        Log.d(TAG, "Initalizing Bluetooth adapter...");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showErrorDialog("This device is not implement Bluetooth.");
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_BLUETOOTH_ENABLE);
        } else {
            Log.d(TAG, "Initialisation successful.");

            bt_blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPairedDevicesListDialog();
                }
            });

        }


    }


    // -----------------------------------------------------------------------------------------
    // ----------------------------------------날씨----------------------------------------------
    // -----------------------------------------------------------------------------------------




    // -----------------------------------------------------------------------------------------
    // ----------------------------------------블루투스------------------------------------------
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnectedTask != null) {

            mConnectedTask.cancel(true);
        }
    }


    private class ConnectTask extends AsyncTask<Void, Void, Boolean> {

        private BluetoothSocket mBluetoothSocket = null;
        private BluetoothDevice mBluetoothDevice = null;

        ConnectTask(BluetoothDevice bluetoothDevice) {
            mBluetoothDevice = bluetoothDevice;
            mConnectedDeviceName = bluetoothDevice.getName();

            //SPP
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            try {
                mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                Log.d(TAG, "create socket for " + mConnectedDeviceName);

            } catch (IOException e) {
                Log.e(TAG, "socket create failed " + e.getMessage());
            }

            mConnectionStatus.setText("connecting...");
        }


        @Override
        protected Boolean doInBackground(Void... params) {

            mBluetoothAdapter.cancelDiscovery();

            try {
                mBluetoothSocket.connect();
            } catch (IOException e) {
                try {
                    mBluetoothSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " +
                            " socket during connection failure", e2);
                }

                return false;
            }

            return true;
        }


        @Override
        protected void onPostExecute(Boolean isSucess) {

            if (isSucess) {
                connected(mBluetoothSocket);
            } else {

                isConnectionError = true;
                Log.d(TAG, "Unable to connect device");
                showErrorDialog("Unable to connect device");
            }
        }
    }


    public void connected(BluetoothSocket socket) {
        mConnectedTask = new ConnectedTask(socket);
        mConnectedTask.execute();
    }


    private class ConnectedTask extends AsyncTask<Void, String, Boolean> {

        private InputStream mInputStream = null;
        private OutputStream mOutputStream = null;
        private BluetoothSocket mBluetoothSocket = null;

        ConnectedTask(BluetoothSocket socket) {

            mBluetoothSocket = socket;
            try {
                mInputStream = mBluetoothSocket.getInputStream();
                mOutputStream = mBluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "socket not created", e);
            }

            Log.d(TAG, "connected to " + mConnectedDeviceName);
            mConnectionStatus.setText("connected to " + mConnectedDeviceName);

            auha = findViewById(R.id.auha);
            weather = findViewById(R.id.weather);
            if(mConnectionStatus != null){
                auha.setVisibility(View.VISIBLE);
                map.setVisibility(View.VISIBLE);
                weather.setVisibility(View.VISIBLE);
            }else{
                auha.setVisibility(View.GONE);
                map.setVisibility(View.GONE);
                weather.setVisibility(View.GONE);
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(Void... params) {

            byte[] readBuffer = new byte[1024];
            int readBufferPosition = 0;


            while (true) {

                if (isCancelled()) return false;

                try {

                    int bytesAvailable = mInputStream.available();

                    if (bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];

                        mInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {

                            byte b = packetBytes[i];
                            if (b == '\n') {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, StandardCharsets.UTF_8);

                                readBufferPosition = 0;

                                // 젯슨 나노로부터 받은 메시지 리스트뷰에 표시
                                Log.d(TAG, "recv message: " + recvMessage);

                                publishProgress(recvMessage);
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (IOException e) {

                    Log.e(TAG, "disconnected", e);
                    return false;
                }
            }

        }

        @Override
        protected void onProgressUpdate(String... recvMessage) {

            mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);


        }

        @Override
        protected void onPostExecute(Boolean isSucess) {
            super.onPostExecute(isSucess);

            if (!isSucess) {


                closeSocket();
                Log.d(TAG, "Device connection was lost");
                isConnectionError = true;
                showErrorDialog("Device connection was lost");
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            closeSocket();
        }

        void closeSocket() {

            try {

                mBluetoothSocket.close();
                Log.d(TAG, "close socket()");

            } catch (IOException e2) {

                Log.e(TAG, "unable to close() " +
                        " socket during connection failure", e2);
            }
        }

        void write(String msg) {

            msg += "\n";

            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                Log.e(TAG, "Exception during send", e);
            }

        }
    }


    public void showPairedDevicesListDialog() {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        final BluetoothDevice[] pairedDevices = devices.toArray(new BluetoothDevice[0]);

        if (pairedDevices.length == 0) {
            showQuitDialog("No devices have been paired.\n"
                    + "You must pair it with another device.");
            return;
        }

        String[] items;
        items = new String[pairedDevices.length];
        for (int i = 0; i < pairedDevices.length; i++) {
            items[i] = pairedDevices[i].getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select device");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                ConnectTask task = new ConnectTask(pairedDevices[which]);
                task.execute();
            }
        });
        builder.create().show();
    }


    public void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isConnectionError) {
                    isConnectionError = false;
                    mConnectionStatus.setText("");
                }
            }
        });
        builder.create().show();
    }


    public void showQuitDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    void sendMessage(String msg) {

        if (mConnectedTask != null) {
            mConnectedTask.write(msg);
            // 젯슨 나노에게 보낸 메시지 표시
            Log.d(TAG, "send message: " + msg);
            mConversationArrayAdapter.insert("Me:  " + msg, 0);
        }
    }

    public void Babyface(){

        st_baby = findViewById(R.id.st_baby);
        baby = findViewById(R.id.baby);

        baby.setText(array[2]);

        if(array[2] == "100"){
            st_baby.setImageResource(R.drawable.smile);
        }
        if(array[2] == "200"){
            st_baby.setImageResource(R.drawable.wake);
        }
        if(array[2] == "300"){
            st_baby.setImageResource(R.drawable.sleep);
        }
        if(array[2] == "400"){
            st_baby.setImageResource(R.drawable.noface);
        }

    }

    public void Temp(){
        temp = findViewById(R.id.temp);
        humd = findViewById(R.id.humd);

        temp.setText(array[0].concat("C"));
        humd.setText(array[1].concat("%"));
    }

    // ---------------------------------------------------------------------------------------------
    //------------------------------------------위치 추적--------------------------------------------
    // ---------------------------------------------------------------------------------------------
}
