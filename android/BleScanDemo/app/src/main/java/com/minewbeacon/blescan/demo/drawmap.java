package com.minewbeacon.blescan.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/*
public class drawmap extends View {
    int unitA;
    public drawmap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void setUnit(int s){
        this.unitA = s;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        for(int i=0;i<MainActivity.i;i++){
            for(int j=0;j<MainActivity.j;j++){
                switch(MainActivity.lmap[i][j]){
                    case 0: paint.setColor(Color.rgb(103,103,103));break;
                    case 1: paint.setColor(Color.BLACK);break;
                    case 2: paint.setColor(Color.RED);break;
                    case 3: paint.setColor(Color.GREEN);break;
                    case 4: paint.setColor(Color.rgb(0,255,255));break;

                }
                canvas.drawRect(10+j*unitA,10+i*unitA,10+(j+1)*unitA,10+(i+1)*unitA,paint);
            }
        }
    }
}
*/

public class drawmap extends View {

    MHandler mHandler;
    boolean flag = false;
    int mapi, mapj;
    int[][] lmap;
    int dw,dh;
    float unitA;
    int W,H;
    public drawmap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void setUnit(int s){
        this.unitA = (float) (s*0.95);
    }

    public void setupHandler() {

        mHandler = new MHandler();
    }

    public void setupDrawmap(int mapi, int mapj,Point size) {

        this.mapi = mapi;
        this.mapj = mapj;
        dw = size.x/mapj;
        this.W = size.x;
        this.H = size.y;
        dh = (size.y)/mapi;
        if(dw<dh)this.setUnit(dw);
        else this.setUnit(dh);
        flag = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!flag) {
            return;
        }

        Paint paint = new Paint();



        for(int i=0;i<mapi;i++){
            for(int j=0;j<mapj;j++){
                switch(lmap[i][j]){
                    case 0: paint.setColor(Color.rgb(214, 212, 212));break;
                    case 1: paint.setColor(Color.rgb(45, 45, 44));break;
                    case 3: paint.setColor(Color.rgb(0, 255, 38));break;
                    case 4: paint.setColor(Color.rgb(25, 255, 228));break;
                    case 5: paint.setColor(Color.rgb(255, 83, 10));break;
                    default: paint.setColor(Color.rgb(214, 212, 212));break;

                }
                canvas.drawRect((this.W-mapj*unitA)/2+j*unitA,i*unitA,(this.W-mapj*unitA)/2+(j+1)*unitA,(i+1)*unitA,paint);
                if(lmap[i][j]==2){
                    paint.setColor(Color.YELLOW);
                    canvas.drawCircle((float)((this.W-mapj*unitA)/2+(j+0.5)*unitA),(float)((i+0.5)*unitA),unitA/2,paint);
                }
            }
        }



    }


    // 배열로 만들어주는거 처리 핸들러
    class MHandler extends Handler {
        @Override

        public void handleMessage(Message msg){

            if(msg.what == 1){
                Log.d("handler : ", "ok");
                lmap = (int[][]) msg.obj;
//                int[][] temp01 = (int[][]) msg.obj;

                invalidate();

            }else {
                Log.d("handler : ", "error");
            }
        }


    }


}
