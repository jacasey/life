package com.jcasey.life;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jcasey on 23/02/2019.
 */
public class WorldSimulation {

    private final boolean transparency;
    long generation = 0;

    boolean[]  next = null;
    boolean[] current = null;

    float cellWidth;
    float cellHeight;

    int arrayWidth;
    int arrayHeight;

    private boolean grid;

    private float screenWidth;
    private float screenHeight;

    public WorldSimulation(int arrayWidth, int arrayHeight, float screenWidth, float screenHeight, boolean grid, boolean transparency)
    {
        this.transparency = transparency;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.grid = grid;

        this.arrayWidth = arrayWidth;
        this.arrayHeight = arrayHeight;

        next = new boolean[(arrayWidth +2)*(arrayHeight +2)];
        current = new boolean[(arrayWidth +2)*(arrayHeight +2)];

        cellWidth = (screenWidth / this.arrayWidth);
        cellHeight = (screenHeight / this.arrayHeight);
    }

    public void paint(Canvas canvas, Paint paint) {
        for(int i = 1; i < this.arrayWidth +1; i++)
        {
            for(int j = 1; j < this.arrayHeight +1; j++)
            {
                float left = (i * cellWidth);
                float top = (j * cellHeight);
                float right = (left + cellWidth);
                float bottom = (top + cellHeight);

                boolean status = getCell(i, j);

                int neighbours = livingNeighbours(i, j);

                float variance = (neighbours / 5.0f) * 255.0f;

                int colour = (int) variance;

                if(status) {
                    if(transparency) {
                        paint.setColor(Color.rgb(0, colour, 0));
                    }
                    else {
                        paint.setColor(Color.GREEN);
                    }
                }
                else
                {
                    paint.setColor(Color.BLACK);
                }
                canvas.drawRect(left - cellWidth, top - cellHeight, right, bottom, paint);
            }
        }
        if(grid) {
            paint.setColor(Color.WHITE);
            for (float i = 0; i < screenHeight; i += cellHeight) {
                canvas.drawLine(0, i, screenWidth, i, paint);
            }
            for (float i = 0; i < screenWidth; i += cellWidth) {
                canvas.drawLine(i, 0, i, screenHeight, paint);
            }
        }
    }

    public void timeStep()
    {
        generation ++;

        System.arraycopy(next, 0, current, 0, next.length);

        for(int i = 1; i < this.arrayWidth +1; i++)
        {
            for(int j = 1; j < this.arrayHeight +1; j++)
            {
                boolean status = getCell(i, j);

                int neighbours = livingNeighbours(i, j);

                if(status) {
                    if (neighbours < 2) {
                        setCell(i,j,false);
                    }
                    else if(neighbours >3)
                    {
                        setCell(i,j,false);
                    }
                }
                else
                {
                    if(neighbours == 3) {
                        setCell(i,j,true);
                    }
                }
            }
        }
    }

    public boolean getCell(int row, int col) {

        int idx = (row * (arrayHeight +2 )) + col ;

        return current[idx];
    }

    private int livingNeighbours(int row, int col) {
        int alive = 0 ;

        for(int i = row -1; i <= row+1; i ++)
        {
            for(int j = col -1; j <= col+1; j ++)
            {
                if(getCell(i, j) && !(i == row && j == col))
                {
                    alive ++;
                }
            }
        }
        return alive;
    }

    public void setCell(int row, int col, boolean alive)
    {
        int idx = (row * (arrayHeight +2 )) + col ;
        next[idx] =  alive;
    }

    public void setCurrentCell(int row, int col, boolean alive)
    {
        int idx = (row * (arrayHeight +2 )) + col ;

        current[idx] = alive;

    }

}
