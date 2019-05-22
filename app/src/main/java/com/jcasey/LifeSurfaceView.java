package com.jcasey;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

public class LifeSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback, View.OnTouchListener
{
    public static final int SCREEN_DIAGONAL_METRES = 80;

    final Paint paint = new Paint();
    boolean running = false;

    Thread animation;
    WorldSimulation world;

    private float width;
    private float height;

    float scale;

    public LifeSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public void setup(final SurfaceHolder holder) {
        paint.setAntiAlias(true);

        Rect surfaceFrame = holder.getSurfaceFrame();

        height = surfaceFrame.height();
        width = surfaceFrame.width();

        float hyp = (float) Math.hypot(width, height); // get screen diagonal

        scale = SCREEN_DIAGONAL_METRES / hyp;

        float scaledWidth = (float) Math.floor(width * scale);
        float scaledHeight = (float)Math.floor(height * scale);

        world = new WorldSimulation((int)(scaledWidth), (int)(scaledHeight), width, height);

        Random random = new Random();

        for(int i =0 ;i<800; i++)
        {
            int x = random.nextInt((int)scaledWidth)+1;
            int y = random.nextInt((int)scaledHeight)+1;

            world.setCell(x,y,true);
        }

//
//
//        world.setCell(1,5,true);
//        world.setCell(1,6,true);
//        world.setCell(2,5,true);
//        world.setCell(2,6,true);
//        world.setCell(11,5,true);
//        world.setCell(11,6,true);
//        world.setCell(11,7,true);
//        world.setCell(12,4,true);
//        world.setCell(12,8,true);
//        world.setCell(13,3,true);
//        world.setCell(13,9,true);
//        world.setCell(14,3,true);
//        world.setCell(14,9,true);
//        world.setCell(15,6,true);
//        world.setCell(16,4,true);
//        world.setCell(16,8,true);
//        world.setCell(17,5,true);
//        world.setCell(17,6,true);
//        world.setCell(17,7,true);
//        world.setCell(18,6,true);
//        world.setCell(21,3,true);
//        world.setCell(21,4,true);
//        world.setCell(21,5,true);
//        world.setCell(22,3,true);
//        world.setCell(22,4,true);
//        world.setCell(22,5,true);
//        world.setCell(23,2,true);
//        world.setCell(23,6,true);
//        world.setCell(25,1,true);
//        world.setCell(25,2,true);
//        world.setCell(25,6,true);
//        world.setCell(25,7,true);
//        world.setCell(35,3,true);
//        world.setCell(35,4,true);
//        world.setCell(36,3,true);
//        world.setCell(36,4,true);

        animation = new Thread(this,"life");
        running = true;

        this.setOnTouchListener(this);

        animation.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        setup(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//        surfaceDestroyed(holder);
//        setup(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        animation.interrupt();
    }

    @Override
    public void run() {

        paint.setColor(Color.GREEN);

        while(running)
        {
            Canvas canvas;

            SurfaceHolder holder = getHolder();
            synchronized(holder)
            {
                canvas = holder.lockCanvas();

                if(canvas != null) {
                    world.timeStep(canvas, paint);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        world.setCell((int)(motionEvent.getX()*scale),(int)(motionEvent.getY()*scale),true);
        return true;
    }
}
