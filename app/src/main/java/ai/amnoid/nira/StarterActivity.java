package ai.amnoid.nira;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import java.io.File;

public class StarterActivity extends AppCompatActivity {

    ProgressDialog progress;
    private Handler mHandler;
    Context context;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while Initial Setup...");
        progress.setCancelable(false);
        setContentView(R.layout.activity_starter);
        context=this;
        mHandler = new Handler();
        //mHandler.postDelayed(add_table, 2500);
        try {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.track);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }catch (Exception e){}
    }

    /*private Runnable add_table = new Runnable() {
        public void run() {
            progress.show();
            checkDataBase();
            DBHelper db=new DBHelper(context);
            progress.dismiss();
        }
    };*/

    public void onClickplaywithme(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("key", "one"); //Optional parameters
        myIntent.putExtra("bot", "0"); //Optional parameters
        this.startActivity(myIntent);
    }
    public void onClickplaywithme3(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("key", "one"); //Optional parameters
        myIntent.putExtra("bot", "1"); //Optional parameters
        this.startActivity(myIntent);
    }
    public void onClickplaywithme4(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("key", "two"); //Optional parameters
        myIntent.putExtra("bot", "1"); //Optional parameters
        this.startActivity(myIntent);
    }

    public void onClickplaywithme2(View v){
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("key", "two"); //Optional parameters
        myIntent.putExtra("bot", "0"); //Optional parameters
        this.startActivity(myIntent);
    }

    /*private boolean checkDataBase() {
        //Log.d("file:","DB file is not deployed");

        String dbPath = this.getApplicationContext().getFilesDir().getParentFile().getPath() + "/databases/";
        Log.d("path:",String.valueOf(this.getApplicationContext().getFilesDir().getParentFile().getPath()));
        SQLighterDbImpl db = new SQLighterDbImpl();
        db.setDbPath(dbPath);
        db.setDbName("gamebot.db");
        boolean isDbFileDeployed = db.isDbFileDeployed();
        if(!isDbFileDeployed) {
            //Log.d("file:","DB file is not deployed");
            //System.out.println("DB file is not deployed");
            //Toast.makeText(getBaseContext(), "DB file is not deployed dbPath="+dbPath,Toast.LENGTH_SHORT).show();

        } else {
            //Log.d("file:","DB file is deployed");
            //System.out.println("DB file is deployed");
            //Toast.makeText(getBaseContext(), "DB file is  deployed dbPath="+dbPath,Toast.LENGTH_SHORT).show();

        }
        db.setContext(this.getApplicationContext());
        db.setOverwriteDb(true);
        try {
            db.copyDbOnce();
            db.openIfClosed();
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return  true;
    }*/
}
