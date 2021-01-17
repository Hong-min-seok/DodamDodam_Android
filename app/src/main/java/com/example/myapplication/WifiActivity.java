package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Tasks.DeviceTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class WifiActivity extends AppCompatActivity {
    private final int REQUEST_BLUETOOTH_ENABLE = 100;

    //--Gone
    private TextView mConnectionStatus;
    private EditText mInputEditText;

    ConnectedTask mConnectedTask = null;
    static BluetoothAdapter mBluetoothAdapter;
    private String mConnectedDeviceName = null;
    private ArrayAdapter<String> mConversationArrayAdapter;
    static boolean isConnectionError = false;
    private static final String TAG = "BluetoothClient";
    //Gone--

    //Visible--
    private EditText edtWiFiID;
    private EditText edtWiFiPW;
    private TextView tvConnectedDevice;
    Button btnRegist;
    Button btnSend;
    Button btnReset;
    //--Visible

    String id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        Intent inIntent = getIntent();
        id = inIntent.getStringExtra("userid");
        Log.d("ddd", id);

        edtWiFiID = findViewById(R.id.edtWifiId);
        edtWiFiPW = findViewById(R.id.edtWifiPw);
        tvConnectedDevice = findViewById(R.id.tvConnectedDevice);

        //와이파이 아이디/비밀번호 전송
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {

                    String wifiConnectMessage = "1-";
                    String wifiId = edtWiFiID.getText().toString().trim();
                    String wifiPw = edtWiFiPW.getText().toString().trim();

                    wifiConnectMessage += wifiId + "-" + wifiPw;

                    if (wifiConnectMessage.equals("1--")) {
                        throw new NullPointerException();
                    }

                    if (wifiConnectMessage.length() > 0) {
                        sendMessage(wifiConnectMessage);
                    }

                } catch (NullPointerException npe) {
                    Toast.makeText(getApplicationContext(), "아이디, 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        });

        //와이파이 연결 해제
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage(String.valueOf(0));
            }
        });

        mConnectionStatus = (TextView) findViewById(R.id.connection_status_textview);
        mInputEditText = (EditText) findViewById(R.id.input_string_edittext);

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

            showPairedDevicesListDialog();
        }

        //기기 등록
        btnRegist = findViewById(R.id.btnRegist);
        btnRegist.setEnabled(false);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();

//                editor.putString("device_id", mConnectedDeviceName.trim());
//                editor.commit();

                try {

                    DeviceTask deviceTask = new DeviceTask(handler);
                    Map<String, String> params = new HashMap<String, String>();
                    Log.d("ddd", id);
                    params.put("user_id", id);
                    Log.d("ddd", mConnectedDeviceName);
                    params.put("device_id", mConnectedDeviceName.trim());

                    deviceTask.execute(params);

                    String prefFnick = pref.getString("fnick", null);

                    if (prefFnick == null) {
                        throw new NullPointerException();
                    }

                    Intent intent = new Intent(WifiActivity.this, MainActivity.class);
                    intent.putExtra("userid", id);
                    intent.putExtra("fnick", prefFnick);
                    startActivity(intent);

                } catch (NullPointerException npe) {

                    Intent intent = new Intent(WifiActivity.this, ListActivity.class);
                    intent.putExtra("userid", id);
                    startActivity(intent);

                } catch (Exception e) {
                    Log.d(TAG, "error : " + e.getMessage());
                } finally {

                    finish();

                }

            }
        });
    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case -1:

                    Toast.makeText(getApplicationContext(), "기기 등록 실패", Toast.LENGTH_SHORT).show();
                    break;
                case 0:

                    Toast.makeText(getApplicationContext(), "기기 등록 성공", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };

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

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mBluetoothSocket.connect();
            } catch (IOException e) {
                // Close the socket
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
            Toast.makeText(getApplicationContext(), "기기 " + mConnectedDeviceName + "에 연결되었습니다", Toast.LENGTH_SHORT).show();
            tvConnectedDevice.setText(tvConnectedDevice.getText().toString() + mConnectedDeviceName);
            mConnectionStatus.setText("connected to " + mConnectedDeviceName);
        }


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

//                            byte[] encodedBytes = new byte[readBufferPosition];
//                            System.arraycopy(readBuffer, 0, encodedBytes, 0,
//                                    encodedBytes.length);
//                            String recvMessage = new String(encodedBytes, "UTF-8");
//
//                            readBufferPosition = 0;
//
//                            Log.d(TAG, "recv message: " + recvMessage);
//                            publishProgress(recvMessage);
                            if (b == '\n') {
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0,
                                        encodedBytes.length);
                                String recvMessage = new String(encodedBytes, "UTF-8");

                                readBufferPosition = 0;

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

//            mConversationArrayAdapter.insert(mConnectedDeviceName + ": " + recvMessage[0], 0);
            Log.d(TAG, "onProgressUpdate: " + mConnectedDeviceName + ": " + recvMessage[0]);

            if (recvMessage[0].contains("connected")){
                Toast.makeText(getApplicationContext(), "연결 되었습니다.", Toast.LENGTH_SHORT).show();
                edtWiFiID.setEnabled(false);
                edtWiFiPW.setEnabled(false);
                btnSend.setEnabled(false);
                btnReset.setEnabled(false);
                btnRegist.setEnabled(true);
            }else{
                Toast.makeText(getApplicationContext(), "연결 실패했습니다.\n 와이파이 아이디/비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }


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

            mInputEditText.setText(" ");
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
                    showPairedDevicesListDialog();
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
                showPairedDevicesListDialog();
            }
        });
        builder.create().show();
    }

    void sendMessage(String msg) {

        if (mConnectedTask != null) {
            mConnectedTask.write(msg);
            Log.d(TAG, "send message: " + msg);
//            mConversationArrayAdapter.insert("Me:  " + msg, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == RESULT_OK) {
                //BlueTooth is now Enabled
                showPairedDevicesListDialog();
            }
            if (resultCode == RESULT_CANCELED) {
                showQuitDialog("You need to enable bluetooth");
            }
        }
    }


}
