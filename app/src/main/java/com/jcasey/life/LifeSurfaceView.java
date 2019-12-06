package com.jcasey.life;

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
    public static final int SCREEN_DIAGONAL_METRES = 100;

    private final Paint paint = new Paint();
    private boolean running = false;

    private Thread animation;
    private WorldSimulation world;

    private float width;
    private float height;

    private float scale;

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

        int scaledWidth = (int) Math.floor(width * scale);
        int scaledHeight = (int)Math.floor(height * scale);

        world = new WorldSimulation(scaledWidth, scaledHeight, width, height);

        Random random = new Random();

        int generate = (int)(width * height*0.001f);

        for(int i =0 ;i<generate; i++)
        {
            int x = random.nextInt(scaledWidth)+1;
            int y = random.nextInt(scaledHeight)+1;

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
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.equals(this)) {
            int x = (int) (motionEvent.getX() * scale) + 1;
            int y = (int) (motionEvent.getY() * scale) + 1;

            x = Math.min(x,world.width);
            y = Math.min(y,world.height);

            world.setCell(x, y, true);
            return true;
        }
        return false;
    }
}