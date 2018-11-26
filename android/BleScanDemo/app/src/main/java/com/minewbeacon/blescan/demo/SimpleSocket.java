package com.minewbeacon.blescan.demo;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

class SimpleSocket extends Thread {


    // 소켓 선언
    private Socket mSocket;

    // 수신, 송신 변수 선언
    private BufferedReader buffRecv;
    private BufferedWriter buffSend;

    private String mAddr ;
    private int mPort;
    private boolean mConnected = false;
    private Handler mHandler = null;
    private Handler handler = new Handler();

    // 메시지의 타입 선언
    static class MessageTypeClass {
        public static final int SIMSOCK_CONNECTED = 1;
        public static final int SIMSOCK_DATA = 2;
        public static final int SIMSOCK_DISCONNECTED = 3;
    };

    public enum MessageType {
        SIMSOCK_CONNECTED, SIMSOCK_DATA, SIMSOCK_DISCONNECTED
    };

    // 넣어준 매개변수를 값으로 가지는 소켓 생성
    public SimpleSocket (String addr, int port, Handler handler){
        mAddr = addr;
        mPort = port;
        mHandler = handler;
    }

    // ??
    private void makeMessage (MessageType what, Object obj){
        Message msg = Message.obtain();
        msg.what = what.ordinal();
        msg.obj = obj;

        // 핸들러에게 메시지 전송
        mHandler.sendMessage(msg);
    }

    private boolean connect (String addr, int port) {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress (InetAddress.getByName(addr), port);
            mSocket = new Socket();
            mSocket.connect (socketAddress, 5000);
        } catch (IOException e) {

            Log.e("sdf", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void run() {
        // 연결이 실패하면 종료
        Log.d("sdf","send");
        try {
            mSocket = new Socket(mAddr, mPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("sdf", "2");
        // 소켓이 null이면 종료
        if (mSocket == null) return;
        Log.e("sdf", "3");
        // 연결 시작
        try {
            buffRecv = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            buffSend = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("sdf", "4");
        mConnected = true;

        // 연결되었다는 메시지 만들고 loop 시작 로그 출력
        makeMessage(MessageType.SIMSOCK_CONNECTED, "");
        Log.d("SimpleSocket","socket_thread loop started");

        // while문에서 사용될 변수 선언
        String aLine = null;

        // 전역변수 사용을 위한 클래스 선언
        //MyApplication myApp = new MyApplication();

        // 쓰레드가 인터럽트 되지 않으면 계속해서 반복
        while (!Thread.interrupted()) {
            try {
                // buffRecv로부터 한 줄 씩 읽어서 aLine에 저장
                aLine = buffRecv.readLine();
                //back = aLine;

                // 전역 변수에 저장
                //myApp.setState(aLine);


                // aLine이 null이 아니면
                if(aLine != null) {
                    //txtView에 표시
                    //txtView.setText(aLine);
                    //output.setText(aLine);

                    // aLine을 데이터로 가지는 메시지 만들기
                    makeMessage(MessageType.SIMSOCK_DATA, aLine);

                }
                else break;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        // 연결이 끊겼다는 메시지 만들고 loop 종료 로그 출력
        makeMessage(MessageType.SIMSOCK_DISCONNECTED, "");
        Log.d("SimpleSocket","socket_thread loop terminated");

        // 연결 종료
        try {
            buffRecv.close();
            buffSend.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mConnected = false;
    }

    // 연결되었는지 아닌지 출력해주는 클래스
    synchronized public boolean isConnected() {
        return mConnected;
    }

    // sendString 스레드
    public void sendString (final String str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(buffSend, true);
                    out.println(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void recvString(){
        new Thread(new Runnable() {
            public void run() {
                Log.d("asd","asdf");
                try{

                    buffRecv = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    String aLine = buffRecv.readLine();
                    Log.d("TCP",aLine);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // 서버로 부터 다시 메시지 받아서 띄워주기.
    //output.setText(aLine);

}