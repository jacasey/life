package com.jcasey.life;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by jcasey on 23/02/2019.
 */
public class WorldSimulation {

    long generation = 0;

    int[][]  next = null;
    int[][] current = null;

    float cellWidth;
    float cellHeight;

    int width;
    int height;

    public WorldSimulation(int width, int height, float w, float h)
    {
        this.width = width;
        this.height = height;

        next = new int[width+2][height+2];
        current = new int[width+2][height+2];

        cellWidth = (w / this.width);
        cellHeight = (h / this.height);
    }

    public void timeStep(Canvas canvas, Paint paint)
    {
        generation ++;

        for (int i = 0; i < next.length; i++) {
            System.arraycopy(next[i], 0, current[i], 0, next[0].length);
        }

        for(int i = 1; i < this.width+1; i++)
        {
            for(int j = 1; j < this.height+1; j++)
            {
                float left = (i * cellWidth);
                float top = (j * cellHeight);
                float right = (left + cellWidth);
                float bottom = (top + cellHeight);

                int status = current[i][j];

                int neighbours = livingNeighbours(i, j);

                if(status == 255) {
                    paint.setColor(Color.rgb(0, 255, 0));

                    canvas.drawRect(left-cellWidth,top-cellHeight,right,bottom,paint);

                    if (neighbours < 2) {
                        next[i][j]=0;
                    }
                    else if(neighbours >3)
                    {
                        next[i][j]=0;
                    }
                }
                else if(status!=255)
                {
                    paint.setColor(Color.rgb(0, status--, 0));
                    canvas.drawRect(left-cellWidth,top-cellHeight,right,bottom,paint);

                    if(neighbours == 3)
                    {
                        next[i][j] =  255;
                    }
                }
            }
        }
    }

    public int livingNeighbours(int i, int j) {
        int alive = 0 ;

        for(int kernelRow = i -1; kernelRow <= i+1; kernelRow ++)
        {
            for(int kernelCol = j -1; kernelCol <= j+1; kernelCol ++)
            {
                if(current[kernelRow][kernelCol]== 255 && !(kernelRow == i && kernelCol == j))
                {
                    alive ++;
                }
            }
        }
        return alive;
    }

    public void setCell(int i, int j, boolean alive)
    {
        next[i][j] =  255;
    }

}
