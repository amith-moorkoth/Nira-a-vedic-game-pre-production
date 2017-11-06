package ai.amnoid.nira;

import android.media.Image;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

import java.util.Random;

/**
 * Created by Amith Moorkoth on 10/16/2017.
 */

public class Getpoints {
    int w,h,cr,window_pad;
    int rect_pad;
    Getpoints(int width,int height,int win_pad,int cr){
        w=width;h=height;window_pad=win_pad;this.cr=cr;
        rect_pad=win_pad-cr;
    }
    public float[][] Getpoint(){

        float[][] points=new float[10][2];
        points[0][0]=0;
        points[0][1]=0;
        points[1][0]=rect_pad;points[1][1]=rect_pad;
        points[2][0]=rect_pad;points[2][1]=h/2-((float)(rect_pad/1.75));
        points[3][0]=rect_pad;points[3][1]=h-(float)(rect_pad*2.2);

        points[4][0]=w/2-(float)(rect_pad/1.73);points[4][1]=rect_pad;
        points[5][0]=w/2-(float)(rect_pad/1.73);points[5][1]=h/2-(float)(rect_pad/1.75);
        points[6][0]=w/2-(float)(rect_pad/1.73);points[6][1]=h-(float)(rect_pad*2.2);

        points[7][0]=w-(float)(rect_pad*2.2);points[7][1]=rect_pad;
        points[8][0]=w-(float)(rect_pad*2.2);points[8][1]=h/2-(float)(rect_pad/1.75);
        points[9][0]=w-(float)(rect_pad*2.2);points[9][1]=h-(float)(rect_pad*2.2);
        return points;
    }
    public void Setpoint(ImageButton imgbut){
        TranslateAnimation animation = new TranslateAnimation(0, w/2-cr, 0, h/2-cr);
        animation.setFillAfter(true);
        animation.setDuration(0);
        ViewGroup.LayoutParams params = imgbut.getLayoutParams();
        params.width = 2*cr;
        params.height = 2*cr;
        imgbut.setLayoutParams(params);
        imgbut.startAnimation(animation);
    }

    public void Setpoint(ImageButton imgbut,float width,float height){
        TranslateAnimation animation = new TranslateAnimation( w/2-cr,width, h/2-cr,height);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        imgbut.startAnimation(animation);
    }

    public void Setpoint(ImageButton imgbut,float prev_width,float prev_height,float new_width,float new_height){
        TranslateAnimation animation = new TranslateAnimation( prev_width,new_width, prev_height,new_height);
        animation.setFillAfter(true);
        animation.setDuration(1000);
        imgbut.startAnimation(animation);
    }

    static int[] shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    public int detect_ball(float x,float y) {
        int nowball=0;
        /*first row*/
        if((((x-w/10)*(x-w/10)))+(((y-w/10)*(y-w/10)))<=((w/20)*(w/20))){nowball=1;}
        else if((((x-w/2)*(x-w/2)))+(((y-w/10)*(y-w/10)))<=((w/20)*(w/20))){nowball=4;}
        else if((((x-(w-w/10))*(x-(w-w/10))))+(((y-w/10)*(y-w/10)))<=((w/20)*(w/20))){nowball=7;}
        /*second row*/
        else if((((x-w/10)*(x-w/10)))+(((y-h/2)*(y-h/2)))<=((w/20)*(w/20))){nowball=2;}
        else if((((x-w/2)*(x-w/2)))+(((y-h/2)*(y-h/2)))<=((w/20)*(w/20))){nowball=5;}
        else if((((x-(w-w/10))*(x-(w-w/10))))+(((y-h/2)*(y-h/2)))<=((w/20)*(w/20))){nowball=8;}
        /*second row*/
        else if((((x-w/10)*(x-w/10)))+(((y-(h-w/10))*(y-(h-w/10))))<=((w/20)*(w/20))){nowball=3;}
        else if((((x-w/2)*(x-w/2)))+(((y-(h-w/10))*(y-(h-w/10))))<=((w/20)*(w/20))){nowball=6;}
        else if((((x-(w-w/10))*(x-(w-w/10))))+(((y-(h-w/10))*(y-(h-w/10))))<=((w/20)*(w/20))){nowball=9;}
        return nowball;
    }

    public int detect_location_move(int direction,int cur_ball){
        int loc=0;
        switch (cur_ball){
            case 1:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_RIGHT :
                            loc=4;
                        break;
                    case SimpleGestureFilter.SWIPE_DOWN :
                        loc=2;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT_DOWN :
                        loc=5;
                        break;
                }
                break;
            case 2:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_UP :
                        loc=1;
                        break;
                    case SimpleGestureFilter.SWIPE_DOWN :
                        loc=3;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT :
                        loc=5;
                        break;
                }
                break;
            case 3:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_UP :
                        loc=2;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT_UP:
                        loc=5;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT :
                        loc=6;
                        break;
                }
                break;
            case 4:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_LEFT :
                        loc=1;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT:
                        loc=7;
                        break;
                    case SimpleGestureFilter.SWIPE_DOWN :
                        loc=5;
                        break;
                }
                break;
            case 5:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_LEFT_UP :
                        loc=1;
                        break;
                    case SimpleGestureFilter.SWIPE_LEFT:
                        loc=2;
                        break;
                    case SimpleGestureFilter.SWIPE_LEFT_DOWN :
                        loc=3;
                        break;
                    case SimpleGestureFilter.SWIPE_UP :
                        loc=4;
                        break;
                    case SimpleGestureFilter.SWIPE_DOWN :
                        loc=6;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT_UP:
                        loc=7;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT :
                        loc=8;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT_DOWN :
                        loc=9;
                        break;
                }
                break;
            case 6:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_LEFT :
                        loc=3;
                        break;
                    case SimpleGestureFilter.SWIPE_RIGHT:
                        loc=9;
                        break;
                    case SimpleGestureFilter.SWIPE_UP:
                        loc=5;
                        break;
                }
                break;
            case 7:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_LEFT :
                        loc=4;
                        break;
                    case SimpleGestureFilter.SWIPE_DOWN:
                        loc=8;
                        break;
                    case SimpleGestureFilter.SWIPE_LEFT_DOWN:
                        loc=5;
                        break;
                }
                break;
            case 8:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_UP :
                        loc=7;
                        break;
                    case SimpleGestureFilter.SWIPE_DOWN:
                        loc=9;
                        break;
                    case SimpleGestureFilter.SWIPE_LEFT:
                        loc=5;
                        break;
                }
                break;
            case 9:
                switch (direction){
                    case SimpleGestureFilter.SWIPE_UP :
                        loc=8;
                        break;
                    case SimpleGestureFilter.SWIPE_LEFT:
                        loc=6;
                        break;
                    case SimpleGestureFilter.SWIPE_LEFT_UP:
                        loc=5;
                        break;
                }
                break;
        }
        return loc;
    }
}
