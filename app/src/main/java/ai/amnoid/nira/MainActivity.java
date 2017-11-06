package ai.amnoid.nira;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.AlphaModifier;
import com.plattysoft.leonids.modifiers.ScaleModifier;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends Activity  implements SimpleGestureFilter.SimpleGestureListener{
    /** Called when the activity is first created. */

    private Handler mHandler = new Handler();
    TextView tv;
    DrawCanvas canvasView;
    int w,h;
    private SimpleGestureFilter detector;
    Context context;
    ImageButton[] rball=new ImageButton[3];
    ImageButton[] gball=new ImageButton[3];
    float[][] points;

    //game state store
    int[] game_state={1,2,3,7,8,9,4,5,6};
    JSONObject game_state_mem = new JSONObject();
    JSONObject game_state_mem2 = new JSONObject();
    private int cur_ball=0,prev_ball=0;
    int lock=0;
    //get point constructor
    Getpoints g;
    TextView txtview;
    int mycolor=1;//redfirst

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new SimpleGestureFilter(this,this);
        context=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        try{if(getIntent().getStringExtra("key").equals("two")){this.mycolor=2;}}catch (Exception e){Log.d("msg",e.toString());}
        try{if(getIntent().getStringExtra("bot").equals("1")){mHandler.postDelayed(game_bot, 100);}}catch (Exception e){Log.d("msg",e.toString());}

        rball[0] =(ImageButton) findViewById(R.id.rball1);
        rball[1] =(ImageButton) findViewById(R.id.rball2);
        rball[2] =(ImageButton) findViewById(R.id.rball3);
        gball[0] =(ImageButton) findViewById(R.id.gball1);
        gball[1] =(ImageButton) findViewById(R.id.gball2);
        gball[2] =(ImageButton) findViewById(R.id.gball3);
        txtview =(TextView) findViewById(R.id.textView);
        RelativeLayout wheel = (RelativeLayout) findViewById(R.id.wheel);
        DisplayMetrics metrics = wheel.getResources().getDisplayMetrics();
        w = metrics.widthPixels;
        h = metrics.heightPixels;
        canvasView = new DrawCanvas(this,w,h);

        wheel.addView(canvasView);

        mHandler.postDelayed(add_ui_button, 10);
        mHandler.postDelayed(mUpdateBackground, 100);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        canvasView.onResumeMySurfaceView();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        canvasView.onPauseMySurfaceView();
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        long start = SystemClock.uptimeMillis();
        public void run() {
            long millis = SystemClock.uptimeMillis() - start;
            canvasView.slice = millis/20000.0;
            //tv.setText(String.valueOf(canvasView.slice));
            if(millis<20000) mHandler.postAtTime(this, start + millis);
            else{
                mHandler.removeCallbacks(mUpdateTimeTask);
            }
        }
    };


    int anim_smoke=0;
    final int[] dust = {R.drawable.animated_confetti,R.drawable.animated_confetti2,R.drawable.animated_confetti3,R.drawable.animated_confetti4};

    public void anim_background(){
        anim_smoke+=1;
        Random random = new Random();
        int index = random.nextInt(dust.length);

        new ParticleSystem(this, 4, dust[index], 7000)
                .setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.1f)
                .setInitialRotationRange(0, 360)
                .setRotationSpeedRange(90, 180)
                .setFadeOut(7000)
                .addModifier(new ScaleModifier(0f, 2f, 0, 3000))
                .oneShot(findViewById(R.id.wheel), 1);
        if(anim_smoke==6){anim_smoke=0;
            new ParticleSystem(this, 2,dust[index], 7000)
                    .setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.1f)
                    .setInitialRotationRange(0, 360)
                    .setRotationSpeedRange(90, 180)
                    .addModifier(new ScaleModifier(0f, 2f, 0, 7000))
                    .setFadeOut(7000)
                    .oneShot(findViewById(R.id.wheel), 1);
        }
        random=null;

    }
/*Button b = (Button) findViewById(R.id.button2);
            TranslateAnimation animation = new TranslateAnimation(0, w, 0, h);

            animation.setFillAfter(true);

            animation.setDuration(2000);

            b.startAnimation(animation);
*/

    public void gamebot_ball_anim(int index,int index2){
        GameBot gb=new GameBot(context);
        int cball=game_state[index];
        int nball=game_state[index2];
        int nowcolor=1;if(index>2){nowcolor=2;}
        int j=index;
        ImageButton[] but=rball;
        if(index>2){but=gball;j-=3;}
        String prev_gamestate=Arrays.toString(game_state);
        game_state[index]=nball;
        game_state[index2]=cball;
        try{game_state_mem.put(prev_gamestate,Arrays.toString(game_state));}catch (Exception e){}
        g.Setpoint(but[j],points[cball][0],points[cball][1],points[nball][0],points[nball][1]);
        gb.ball_locker=0;
        gb=null;
    }

    public void ball_anim(int cball,int nball){
        GameBot gb=new GameBot(context);
        if(lock==0 && cball>0 && gb.ball_locker==0){

            int index = -1;int index2 = -1;int stopper=0;
            int nowcolor=1;
            for (int i=0;i<game_state.length;i++) {
                if(i<6){if(game_state[i]==nball){stopper=1;}}
                if (game_state[i]==cball) {index = i;if(i>2){nowcolor=2;}}
                if (game_state[i]==nball) {index2 = i;}
            }

            if(index<6 && stopper==0 && nowcolor==mycolor && index>-1 && index2>-1){
                int j=index;
                ImageButton[] but=rball;
                if(index>2){but=gball;j-=3;}
                String prev_gamestate=Arrays.toString(game_state);
                game_state[index]=nball;
                game_state[index2]=cball;
                try{game_state_mem2.put(prev_gamestate,Arrays.toString(game_state));}catch (Exception e){}
                g.Setpoint(but[j],points[cball][0],points[cball][1],points[nball][0],points[nball][1]);
                gb.ball_locker=1;
                int[] aa={game_state[0],game_state[1],game_state[2]};
                if(mycolor==1){aa[0]=game_state[3];aa[1]=game_state[4];aa[2]=game_state[5];}
                if(gb.check_won(aa)){
                    try{
                        Log.d("hh",game_state_mem2.toString());

                        txtview.setText("You Won");
                        txtview.setVisibility(View.VISIBLE);
                        //new GetMethodDemo(this).execute("http://w3console.com/table_adder.php?&table=abot&data="+ URLEncoder.encode(game_state_mem2.toString()));
                        /*Form form = new Form()
                                .add("table", "b_ball")
                                .add("data", game_state_mem2);
                        Bridge.get("http://w3console.com/table_adder.php")
                                .body(form)
                                .request();*/
                    }catch(Exception e){}
                }else{
                    mHandler.postDelayed(game_bot, 10);
                }
            }
        }
        gb=null;
    }

    private Runnable game_bot = new Runnable() {
        public void run() {
            GameBot gb=new GameBot(context);
            String var2="";
            try {
                String string = "";
                for (int i = 0; i < game_state.length; i++) {
                    string = string + game_state[i]+",";
                }string = string.substring(0, string.length() - 1);
                before_ball_anim2(mycolor,string,gb);
                /*if(mycolor==1){
                    //var2=downloadUrl(gb.play(game_state,mycolor),new URL("http://w3console.com/table_fetcher.php?&table=abot&data="+string));
                }else{
                    //var2=downloadUrl(gb.play(game_state,mycolor),new URL("http://w3console.com/table_fetcher.php?&table=bbot&data="+string));
                }*/
            }catch (Exception e){}
            //gb.play(game_state);
            //Arrays.sort(game_state, 0, 2);
            //Arrays.sort(game_state, 3, 5);
            //ball_anim2(gb.play(game_state,mycolor),val2);
            gb=null;
        }
    };

    public void before_ball_anim2(int mycolor,String string,GameBot gb){
        //new GetMethodDemo(this).execute("http://w3console.com/table_adder.php?&table=bbot&data="+URLEncoder.encode(game_state_mem.toString()));
        String var2="";
        if(mycolor==1){
            new RetrieveFeedTask(this).execute("http://w3console.com/table_fetcher.php?&table=bbot&data="+string,gb.play(game_state,mycolor),String.valueOf(mycolor));
            //ball_anim2(gb.play(game_state,mycolor),var2);
        }else{
            new RetrieveFeedTask(this).execute("http://w3console.com/table_fetcher.php?&table=abot&data="+string,gb.play(game_state,mycolor),String.valueOf(mycolor));
        }
    }
    public void before_ball_anim2(final String val,final String  val2,final String  color){
        Runnable start_ball_anim2 = new Runnable() {
            public void run() {
                String gstate=Arrays.toString(game_state);
                ball_anim2(val,val2,new GameBot(context).check_grab_opp(gstate,color),new GameBot(context).check_grab_opp2(gstate,color),color);
            }
        };mHandler.postDelayed(start_ball_anim2, 1);
    }
    public void ball_anim2(String val,String val2,String val3,String val4,final String  color){
        GameBot gb=new GameBot(context);
        //Log.d(val+" val,ii ",Arrays.toString(game_state));
        int index=-1;int index2=-1;
        Log.d("new "+val," : new "+val2+"   :"+"val4:"+val4+"      val3:"+val3);
        //String val2=downloadUrl(new URL("http://"));
        if(val4!="") {
            String[] parts = val4.split(",");
            int nball=0;
            if(mycolor==1){
                for (int i=3; i < 6; i++) {
                    int val1 = Integer.parseInt(parts[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" ==val "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }else{
                for (int i=0; i < 3; i++) {
                    int val1 = Integer.parseInt(parts[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" == "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }
        }else if(val3!="") {
            String[] parts3 = val3.split(",");
            int nball=0;
            Log.d("val3:parts",parts3.length+" "+String.valueOf(parts3)+"  "+String.valueOf(game_state));

            if(mycolor==1){
                for (int i=3; i < 6; i++) {
                    int val1 = Integer.parseInt(parts3[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" ==val "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }else{
                for (int i=0; i < 3; i++) {
                    int val1 = Integer.parseInt(parts3[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" == "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }
        }else if(val2!=""){
            String[] parts = val2.split(",");
            int nball=0;
            if(mycolor==1){
                for (int i=3; i < 6; i++) {
                    int val1 = Integer.parseInt(parts[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" ==val "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }else{
                for (int i=0; i < 3; i++) {
                    int val1 = Integer.parseInt(parts[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" == "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }
        } else if(val!="0"){
            String[] parts = val.split(",");
            int nball=0;
            if(mycolor==1){
                for (int i=3; i < 6; i++) {
                    int val1 = Integer.parseInt(parts[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" ==val "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }else{
                for (int i=0; i < 3; i++) {
                    int val1 = Integer.parseInt(parts[i]);
                    if(val1!=game_state[i]){if(i<6){index=i;nball=val1;}}
                }for (int i=6; i < game_state.length; i++) {
                    Log.d(nball+" == "+index+"    ",String.valueOf(game_state[i]));

                    if(nball!=0){if(nball==game_state[i]){index2=i;}}
                }
            }
        }
        Log.d("indes == "+index+"    ","index2 =="+index2);
        if(index2!=-1){gamebot_ball_anim(index,index2);}
        else{
            int nball=0;
            if(mycolor==1){
                if(gb.game_rule(game_state[3],game_state)!=0){index=3;index2=gb.game_rule(game_state[3],game_state);}
                else if(gb.game_rule(game_state[4],game_state)!=0){index=4;index2=gb.game_rule(game_state[4],game_state);}
                else if(gb.game_rule(game_state[5],game_state)!=0){index=5;index2=gb.game_rule(game_state[5],game_state);}
            }else{
                if(gb.game_rule(game_state[0],game_state)!=0){index=0;index2=gb.game_rule(game_state[0],game_state);}
                else if(gb.game_rule(game_state[1],game_state)!=0){index=1;index2=gb.game_rule(game_state[1],game_state);}
                else if(gb.game_rule(game_state[2],game_state)!=0){index=2;index2=gb.game_rule(game_state[2],game_state);}
            }
            gamebot_ball_anim(index,index2);
        }
        int[] aa={game_state[3],game_state[4],game_state[5]};
        if(mycolor==2){aa[0]=game_state[0];aa[1]=game_state[1];aa[2]=game_state[2];}
        if(gb.check_won(aa)){
            try{

                Log.d("",game_state_mem.toString());
                txtview.setText("You Lost");
                txtview.setVisibility(View.VISIBLE);
                //new GetMethodDemo(this).execute("http://w3console.com/table_adder.php?&table=bbot&data="+URLEncoder.encode(game_state_mem.toString()));
                /*Form form = new Form()
                        .add("table", "a_ball")
                        .add("data", game_state_mem);
                Bridge.get("http://w3console.com/table_adder.php")
                        .body(form)
                        .request();*/
            }catch(Exception e){}
        }else{
            gb.ball_locker=0;
        }
        gb=null;//
    }

    private Runnable add_ui_button = new Runnable() {
        public void run() {
            int setw=w;
            if(w>h){setw=h;}
            int window_pad=setw/8;
            int cr=setw/21;
            g=new Getpoints(w,h,window_pad,cr);
            points=g.Getpoint();
            g.Setpoint(rball[0]);g.Setpoint(rball[1]);g.Setpoint(rball[2]);
            g.Setpoint(gball[0]);g.Setpoint(gball[1]);g.Setpoint(gball[2]);
            //if want to shuffle points
            //game_state=g.shuffleArray(game_state);
            mHandler.removeCallbacks(add_ui_button);
            mHandler.postDelayed(set_game_plan, 500);
        }
    };

    private Runnable set_game_plan = new Runnable() {
        public void run() {
            for(int i=0;i<6;i++){
                int j=i;
                ImageButton[] but=rball;
                if(i>2){but=gball;j-=3;}
                int x=game_state[i];
                g.Setpoint(but[j],points[x][0],points[x][1]);
            }
            mHandler.removeCallbacks(set_game_plan);
        }
    };

    private Runnable mUpdateBackground = new Runnable() {
        long start = SystemClock.uptimeMillis();
        public void run() {
            anim_background();
            mHandler.removeCallbacks(mUpdateBackground);
            mHandler.postDelayed(mUpdateBackground, 800);
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @Override
    public void onSwipe(int direction) {
        lock=0;
        int loc=g.detect_location_move(direction,cur_ball);
        ball_anim(cur_ball,loc);
        //Toast.makeText(this, String.valueOf(cur_ball+"  cur  "+loc), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, String.valueOf(g.detect_location_move(direction,cur_ball)), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onDoubleTap() {
        //Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(lock<3){
            lock+=1;
            float x,y;
            x = ev.getX();
            y = ev.getY();
            int nowball=g.detect_ball(x,y);
            if(nowball!=0){
                if(nowball!=cur_ball){
                    if(prev_ball!=cur_ball){prev_ball=cur_ball;}
                    cur_ball=nowball;
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    /*public void go(View view){
        mHandler.removeCallbacks(mUpdateTimeTask);  //Remove old timer
        mHandler.postDelayed(mUpdateTimeTask, 100);

    }*/

    /*if(nowball!=0){
            if(nowball!=cur_ball){
                if(prev_ball!=cur_ball){prev_ball=cur_ball;}
                cur_ball=nowball;
            }
        }*/

//***********************************************internet functions

    private void downloadUrl(String val2,final URL url){
        try {
            Log.d("url:", url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000 /* milliseconds */);
            conn.setConnectTimeout(1000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            //return conn.getInputStream().toString();
            //return convertStreamToString(conn.getInputStream());
            //ball_anim2(convertStreamToString(conn.getInputStream()), val2,new GameBot(context).check_grab_opp(game_state));
        }catch (Exception e){
            Log.d("exceptioninternet:",e.toString());
        }
    }


    public String convertStreamToString(InputStream is)
            throws IOException {
            /*
             * To convert the InputStream to String we use the
             * Reader.read(char[] buffer) method. We iterate until the
    35.         * Reader return -1 which means there's no more data to
    36.         * read. We use the StringWriter class to produce the string.
    37.         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try
            {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1)
                {
                    writer.write(buffer, 0, n);
                }
            }
            finally
            {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
