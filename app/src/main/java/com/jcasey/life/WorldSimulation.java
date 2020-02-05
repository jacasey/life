package com.jcasey.life;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jcasey on 23/02/2019.
 */
public class WorldSimulation {

    long generation = 0;

    boolean[]  next = null;
    boolean[] current = null;

    float cellWidth;
    float cellHeight;

    int width;
    int height;

    public WorldSimulation(int width, int height, float w, float h)
    {
        this.width = width;
        this.height = height;

        next = new boolean[(width+2)*(height+2)];
        current = new boolean[(width+2)*(height+2)];

        cellWidth = (w / this.width);
        cellHeight = (h / this.height);
    }

    public void timeStep(Canvas canvas, Paint paint)
    {
        generation ++;

        System.arraycopy(next, 0, current, 0, next.length);

        for(int i = 1; i < this.width+1; i++)
        {
            for(int j = 1; j < this.height+1; j++)
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
                    paint.setColor(Color.rgb(0, colour, 0));

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
                    paint.setColor(Color.rgb(0, 0, 0));
                    if(neighbours == 3)
                    {
                        setCell(i,j,true);
                    }
                }

                canvas.drawRect(left - cellWidth, top - cellHeight, right, bottom, paint);
            }
        }
    }

    private boolean getCell(int row, int col) {

        int idx = (row * (height +2 )) + col ;

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
        int idx = (row * (height +2 )) + col ;

        next[idx] =  alive;
    }

}
