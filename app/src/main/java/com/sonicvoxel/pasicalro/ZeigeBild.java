package com.sonicvoxel.pasicalro;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ZeigeBild extends AppCompatActivity {
    private Handler mHandler = new Handler();

    //LinearLayout mLinearLayout;
    Random random=null;
    byte[][] karte;
    double T=10.,en0=1,ratio=0.5;
    public int anzx, anzy;
    int i, j, dumx, dumy;
    MyView SimBild=null;
    int farbschema;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeige_bild);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        Intent intent = getIntent();
        ratio = intent.getDoubleExtra(MainActivity.DOUBLE_RATIO,0.5);
        en0 = intent.getDoubleExtra(MainActivity.DOUBLE_EN0,1);
        T = intent.getDoubleExtra(MainActivity.DOUBLE_TEMP,1);
        farbschema = intent.getIntExtra(MainActivity.INT_FARBSCHEMA,1);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        float verh = (float) (height*1.0/width);
        anzx = 50;
        anzy = (int) (anzx*verh);
        int grx = width/anzx;
        int gry = height/anzy;
        karte = new byte[anzy][anzx];
        random = new Random();
        for (int i=0;i<anzx;i++){
            for (int j=0;j<anzy;j++){
                karte[j][i]=0;
            }
        }

        while (i < ratio * (anzx * anzy-1)) {
            dumx = (int) (random.nextDouble() * anzx);
            dumy = (int) (random.nextDouble() * anzy);
            if (karte[dumy][dumx] == 0) {
                karte[dumy][dumx] = 1;
                i++;
            }
        }


        SimBild= new MyView(this,anzx,anzy,grx,gry,karte);
        setContentView(SimBild);



    }

    @Override
    protected void onResume(){
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }



    public void step(){
        int dumx,dumy;
        double en;

        dumx = (int) (random.nextDouble() * anzx);
        dumy = (int) (random.nextDouble() * anzy);
        en=karte[(dumy+1)%anzy][dumx]+karte[(dumy-1+anzy)%anzy][dumx] + karte[dumy][(dumx+1)%anzx] + karte[dumy][(dumx-1+anzx)%anzx];
        en=en+(karte[(dumy+1)%anzy][(dumx+1)%anzx]+karte[(dumy+1)%anzy][(dumx-1+anzx)%anzx]+karte[(dumy-1+anzy)%anzy][(dumx-1+anzx)%anzx]+karte[(dumy-1+anzy)%anzy][(dumx+1)%anzx]);
        en=(en-4);
        if (karte[dumy][dumx]==1){
            if ( random.nextDouble() < (1/(1+Math.exp((en+en0)/T))) ) {
                karte[dumy][dumx]=0;
            }
        }
        else {
            if ( random.nextDouble() > (1/(1+Math.exp((en-en0)/T))) ) {
                karte[dumy][dumx]=1;
            }
        }


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    T=T*1.1;
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    T=T*0.9;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public class MyView extends View
    {
        private class UpdateViewRunnable implements Runnable {
            public void run() {
                for (int i=0;i<1000;i++){
                    step();
                }
                invalidate();

                if (updateView) {
                    //postDelayed(this, DELAY_TIME_MILLIS);
                    post(this);
                }
            }
        }

        Paint paintUp;
        Paint paintDown;
        Paint paintBack;
        int colorUp=Color.WHITE,colorDown=Color.BLUE;
        int anzx,anzy, grx,gry;
        byte [][] karte;
        Canvas defCanvas;
        private boolean updateView = false;
        private static final long DELAY_TIME_MILLIS = 100L;
        private UpdateViewRunnable updateViewRunnable = new UpdateViewRunnable();

        public MyView(Context context,int AnzX,int AnzY,int GrX,int GrY,byte [][] Karte)
        {
            super(context);
            switch (farbschema){
                case (0):{
                    colorUp=Color.WHITE;
                    colorDown=Color.BLUE;
                    break;
                }
                case (1):{
                    colorUp=0xffe9a11b;//Orange
                    colorDown=Color.WHITE;
                    break;
                }
                case (2):{
                    colorUp=0xFF009b3a;//Grün
                    colorDown=0xFFFEDF00;//Gelb
                    break;
                }
                case (3):{
                    colorUp=Color.WHITE;
                    colorDown=0xFFC70025;//Japan Rot
                    break;
                }
                case (4):{
                    colorUp=Color.WHITE;
                    colorDown=0xFF006c35;//Saudi Arabien Grün
                    break;
                }
                case (5):{
                    colorUp=0xFFE0162B;//USA Rot
                    colorDown=0xFF0052A5;//USA Blau
                    break;
                }
                default: break;

            }
            paintUp = new Paint();
            paintUp.setStyle(Paint.Style.FILL);
            paintUp.setColor(colorUp);
            paintDown=new Paint();
            paintDown.setStyle(Paint.Style.FILL);
            paintDown.setColor(colorDown);
            paintBack=new Paint();
            paintBack.setStyle(Paint.Style.FILL);
            paintBack.setColor(Color.BLACK);
            anzx=AnzX;
            anzy=AnzY;
            grx=GrX;
            gry=GrY;
            karte=Karte;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            defCanvas=canvas;
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int offsetx,offsety;
            int startx,starty;
            canvas.drawPaint(paintBack);
            offsetx= (Math.round((x-anzx*grx)/2));
            offsety= (Math.round((y-anzy*gry)/2));



            for (int i=0;i<anzy;i++){
                for (int j=0;j<anzx;j++){
                    startx=(j)*grx+offsetx;
                    starty=(i)*gry+offsety;
                    if (karte[i][j]>=1) {
                        canvas.drawRect(startx, starty, startx + grx, starty + gry,paintUp);
                    }
                    else{
                        canvas.drawRect(startx, starty, startx + grx, starty + gry,paintDown);
                    }
                }
            }

        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateView = true;
            postDelayed(updateViewRunnable, DELAY_TIME_MILLIS);
        }

        @Override
        public void onDetachedFromWindow() {
            updateView = false;
            super.onDetachedFromWindow();
        }


    }

}
