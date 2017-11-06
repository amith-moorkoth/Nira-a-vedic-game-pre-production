package ai.amnoid.nira;

/**
 * Created by Amith Moorkoth on 10/13/2017.
 */

import java.util.Random;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.AlphaModifier;
import com.plattysoft.leonids.modifiers.ScaleModifier;

class DrawCanvas extends SurfaceView implements Runnable{

    Thread thread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean running = false;
    public double slice;
    Context c;
    int w,h;
    CanvasDrawer candrawer;
    public DrawCanvas(Context context,int w,int h) {
        super(context);
        this.w=w;this.h=h;
        c=context;
        candrawer=new CanvasDrawer(w,h);
        // TODO Auto-generated constructor stub
        surfaceHolder = getHolder();

    }

    public void onResumeMySurfaceView(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPauseMySurfaceView(){
        boolean retry = true;
        running = false;
        while(retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void run() {
        // TODO Auto-generated method stub
        while(running){
            if(surfaceHolder.getSurface().isValid()){
                surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
                Canvas canvas = surfaceHolder.lockCanvas();
                Paint black = new Paint();
                black.setColor(Color.rgb(5, 8, 38));
                canvas.drawRect(0, 0, w, h, black);
                Paint mPaints = new Paint();
                mPaints.setAntiAlias(true);
                mPaints.setStyle(Paint.Style.FILL);
                mPaints.setColor(0x8800FF00);
                RectF bounds= new RectF( 0, 0, w, h);
                candrawer.Draw_outline(canvas);
                canvas.drawArc(bounds, 0, (float) (slice*360), true, mPaints);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
