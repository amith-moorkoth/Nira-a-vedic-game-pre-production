package ai.amnoid.nira;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * Created by Amith Moorkoth on 10/13/2017.
 */

public class CanvasDrawer {
    int w,h;
    LinearGradient red[]={new LinearGradient(0,0,0,0,Color.parseColor("#6d0019"), Color.parseColor("#A90329"), Shader.TileMode.MIRROR),get_gradiant(1,1),get_gradiant(2,1),get_gradiant(3,1),get_gradiant(4,1),get_gradiant(5,1),get_gradiant(6,1),get_gradiant(7,1),get_gradiant(8,1),get_gradiant(9,1)};
    LinearGradient green[]={new LinearGradient(0,0,0,0,Color.parseColor("#6d0019"), Color.parseColor("#A90329"), Shader.TileMode.MIRROR),get_gradiant(1,2),get_gradiant(2,2),get_gradiant(3,2),get_gradiant(4,2),get_gradiant(5,2),get_gradiant(6,2),get_gradiant(7,2),get_gradiant(8,2 ),get_gradiant(9,2)};

    public CanvasDrawer(int w, int h) {
        this.w=w;this.h=h;
    }
    Paint line = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint circle = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint circle_small = new Paint();

    public void Draw_line(int x1,int y1,int x2,int y2,int window_pad,int dis,int color,Canvas canvas){
        int dx=x2-x1;
        int dy=y2-y1;
        float step;
        Paint line1=line;
        line1.setColor(color);
        if(Math.abs(dx)>Math.abs(dy)){
            step=Math.abs(dx);
        }else{
            step=Math.abs(dy);
        }
        float Xincrement = dx / (float) (step/(window_pad/3));
        float Yincrement = dy / (float) (step/(window_pad/3));
        for(int v=0; v < step; v+=10)
        {
            float rnd=randomWithRange(Xincrement-10,Xincrement+10);
            x1 += Xincrement;
            y1 += Yincrement;
            if(dis==1){canvas.drawPoint(Math.round(x1+rnd), Math.round(y1), line1);}
            else if(dis==2){canvas.drawPoint(Math.round(x1), Math.round(y1+rnd), line1);}
        }
    }

    DashPathEffect dashPathEffect1 = new DashPathEffect(new float[]{10, 20}, 0);

    public void Draw_outline(Canvas canvas){
        //circle_small.setShader(new LinearGradient(0, w, 0, h, Color.parseColor("#ef7197"), Color.parseColor("#0e8581"), Shader.TileMode.MIRROR));
        //line.setShadowLayer(20, 0, 0, Color.WHITE);
        int setw=w;
        if(w>h){setw=h;}
        int window_pad=setw/8;
        int cr=setw/21;
        circle.setColor(Color.parseColor("#FFFFFF"));
        circle.setStyle(Paint.Style.STROKE);
        circle.setStrokeWidth(setw/70);
        line.setColor(Color.parseColor("#FFFFFF"));
        line.setStyle(Paint.Style.STROKE);
        line.setStrokeWidth(window_pad/50);

        canvas.drawLine(w/2, window_pad, w/2, h-window_pad, line);
        canvas.drawLine(window_pad, h/2, w-window_pad, h/2, line);
        canvas.drawLine(window_pad, window_pad, w-window_pad, h-window_pad, line);
        canvas.drawLine(window_pad, h-window_pad, w-window_pad, window_pad, line);
        canvas.drawRect(window_pad, window_pad, w-window_pad, h-window_pad, line);
        /*1 st row*/
        canvas.drawCircle(window_pad, window_pad , cr, circle);
        canvas.drawCircle(window_pad, window_pad , cr, circle_small);
        canvas.drawCircle(w/2, window_pad , cr, circle);
        canvas.drawCircle(w/2, window_pad , cr, circle_small);
        canvas.drawCircle(w-window_pad, window_pad , cr, circle);
        canvas.drawCircle(w-window_pad, window_pad , cr, circle_small);
        /*2 nd row*/
        canvas.drawCircle(window_pad, h/2 , cr, circle);
        canvas.drawCircle(window_pad, h/2 , cr, circle_small);
        canvas.drawCircle(w/2, h/2 , cr, circle);
        canvas.drawCircle(w/2, h/2 , cr, circle_small);
        canvas.drawCircle(w-window_pad, h/2 , cr, circle);
        canvas.drawCircle(w-window_pad, h/2 , cr, circle_small);
        /*3 nd row*/
        canvas.drawCircle(window_pad, h-window_pad , cr, circle);
        canvas.drawCircle(window_pad, h-window_pad , cr, circle_small);
        canvas.drawCircle(w/2, h-window_pad , cr, circle);
        canvas.drawCircle(w/2, h-window_pad , cr, circle_small);
        canvas.drawCircle(w-window_pad, h-window_pad , cr, circle);
        canvas.drawCircle(w-window_pad, h-window_pad , cr, circle_small);
    }

    public LinearGradient get_gradiant(int pos,int color){
        int bx=0,by=0;
        switch (pos){
            case 1:bx=w/10;by=w/10;break;
            case 4:bx=w/2;by=w/10;break;
            case 7:bx=w-w/10;by=w/10;break;
            case 2:bx=w/10;by=h/2;break;
            case 5:bx=w/2;by=h/2;break;
            case 8:bx=w-w/10;by=h/2;break;
            case 3:bx=w/10;by=h-w/10;break;
            case 6:bx=w/2;by=h-w/10;break;
            case 9:bx=w-w/10;by=h-w/10;break;
        }
        //default red
        LinearGradient tmp_grad=new LinearGradient(0, bx/2, by/2, 0, Color.parseColor("#6d0019"), Color.parseColor("#A90329"), Shader.TileMode.MIRROR);
        switch (color){
            case 2:
                tmp_grad=new LinearGradient(0, bx/2, by/2, 0, Color.parseColor("#006E2E"), Color.parseColor("#0D6D35"), Shader.TileMode.MIRROR);
                break;
        }
        return tmp_grad;
    }


    float randomWithRange(float min, float max)
    {
        float range = (max - min) + 1;
        return (float)(Math.random() * range) + min;
    }
}
