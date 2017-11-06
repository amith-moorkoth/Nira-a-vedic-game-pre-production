package ai.amnoid.nira;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Amith Moorkoth on 10/17/2017.
 */

public class GameBot {
    Context context;
    int ball_locker=0;

    GameBot(Context c){
        context=c;
    }

    //***************here we planned to fetch data from table in device sqlite
    String play(int[] game_state,int color){
        //do write code here by replacing me as in below{for sqlite}
        return "0";
    }
    /*String play(int[] game_state,int color){
        DBHelper db = new DBHelper(context);
        int[] arr= game_state;
        String output ="";
        for(int str: arr)
            output=output+","+str;
        if(color==1){
            String val = db.check_table_value("b_ball",output.substring(1,output.length()-6));
        }else{
            String val = db.check_table_value("a_ball",output.substring(1,output.length()-6));
        }

        //Log.d(output.substring(1,output.length()-6)+" valplay,", Arrays.toString(ints));
        /*if(val=="0"){
            val = db.check_table_value("a_ball",output.substring(1,output.length()-6));
        }*/
        /*
        db=null;
        return "0";
    }*/

    int game_rule(int i,int[] gstate){
        //i=number ball
        List<Integer> arr = new ArrayList<Integer>();
        switch (i){
            case 1:
                arr.add(4);
                arr.add(2);
                arr.add(5);
                break;
            case 2:
                arr.add(1);
                arr.add(3);
                arr.add(5);
                break;
            case 3:
                arr.add(2);
                arr.add(6);
                arr.add(5);
                break;
            case 4:
                arr.add(1);
                arr.add(7);
                arr.add(5);
                break;
            case 5:
                arr.add(1);
                arr.add(2);
                arr.add(3);
                arr.add(4);
                arr.add(6);
                arr.add(7);
                arr.add(8);
                arr.add(9);
                break;
            case 6:
                arr.add(3);
                arr.add(9);
                arr.add(5);
                break;
            case 7:
                arr.add(4);
                arr.add(8);
                arr.add(5);
                break;
            case 8:
                arr.add(7);
                arr.add(9);
                arr.add(5);
                break;
            case 9:
                arr.add(6);
                arr.add(8);
                arr.add(5);
                break;
        }
        return check_valid_number(arr,gstate);
    }

    String check_grab_opp2(String gstate1,final String  color){
        gstate1 = gstate1.substring(1, gstate1.length() - 1);
        String[] parts = gstate1.split(",");
        int[] gstate={0,1,2,3,4,5,6,7,8};
        int nball=0;
        for (int i=0; i < parts.length; i++) {
            int val1 = Integer.parseInt(parts[i].replaceAll(" ",""));
            gstate[i]=val1;
        }
        String g_stat1e="0";
        if(color=="1"){
            g_stat1e=check_los_case2(gstate,3);
        }else{
            g_stat1e=check_los_case2(gstate,0);
        }
        return g_stat1e;
    }
    String check_los_case2(int[] gstate,int i) {
        int[] me={3,4,5};
        if(i>=3){me[0]=0;me[1]=1;me[2]=2;}
        int change = 0;int index=0;int index2=0;
        if(gstate[me[0]]==5){
            if(game_rule(gstate[me[1]],gstate)!=0){index=me[1];index2=game_rule(gstate[me[1]],gstate);change=1;}
            else if(game_rule(gstate[me[2]],gstate)!=0){index=me[2];index2=game_rule(gstate[me[2]],gstate);change=1;}
        }else if(gstate[me[1]]==5){
            if(game_rule(gstate[me[0]],gstate)!=0){index=me[0];index2=game_rule(gstate[me[0]],gstate);change=1;}
            else if(game_rule(gstate[me[2]],gstate)!=0){index=me[2];index2=game_rule(gstate[me[2]],gstate);change=1;}
        }else if(gstate[me[2]]==5){
            if(game_rule(gstate[me[1]],gstate)!=0){index=me[1];index2=game_rule(gstate[me[1]],gstate);change=1;}
            else if(game_rule(gstate[me[0]],gstate)!=0){index=me[0];index2=game_rule(gstate[me[0]],gstate);change=1;}
        }
        String string = "";
        if(change==1){
            int tmp=gstate[index2];gstate[index2]=gstate[index];gstate[index]=tmp;
            for (int ii = 0; ii < gstate.length; ii++) {
                string = string + gstate[ii]+",";
            }string = string.substring(0, string.length() - 1);
        }
        return string;
    }
    String check_grab_opp(String gstate1,final String  color){
        gstate1 = gstate1.substring(1, gstate1.length() - 1);
        String g_state="0";
        if(color=="1"){
            g_state=check_los_case(gstate1,0);
        }else{
            g_state=check_los_case(gstate1,3);
        }
        return g_state;
    }

    String check_los_case(String gstate1,int i){
        String[] parts = gstate1.split(",");
        int[] gstate={0,1,2,3,4,5,6,7,8};
        int nball=0;
        for (int ii=0; ii < parts.length; ii++) {
            int val1 = Integer.parseInt(parts[ii].replaceAll(" ",""));
            gstate[ii]=val1;
        }
        int[] me={0,1,2};
        if(i>=3){me[0]=3;me[1]=4;me[2]=5;}
        int change=0;

        int[] lossindex=check_los_case2(gstate[i],gstate[i+1],gstate[i+2],gstate[me[0]],gstate[me[1]],gstate[me[2]]);
        int[] winindex=check_los_case2(gstate[me[0]],gstate[me[1]],gstate[me[2]],gstate[i],gstate[i+1],gstate[i+2]);
        if(lossindex[0]!=-1){
            Log.d("lossindex:",Arrays.toString(lossindex));
            if(lossindex[1]==5){
                int lossindex2=0;
                if(gstate[6]==lossindex[1]){lossindex2=6;}
                if(gstate[7]==lossindex[1]){lossindex2=7;}
                if(gstate[8]==lossindex[1]){lossindex2=8;}
                if(lossindex2!=0){
                    int mee1=0;
                    if(game_rule(me[0],gstate)!=0){mee1=0;}
                    if(game_rule(me[1],gstate)!=0){mee1=1;}
                    if(game_rule(me[2],gstate)!=0){mee1=2;}
                    if(mee1!=0){
                        int tmp=gstate[me[mee1]];
                        gstate[me[mee1]]=gstate[lossindex2];
                        gstate[lossindex2]=tmp;change=1;
                    }
                }
            }else{
                int lossindex2=0;
                if(gstate[6]==lossindex[1] || gstate[6]==lossindex[2]){lossindex2=6;}
                if(gstate[7]==lossindex[1] || gstate[7]==lossindex[2]){lossindex2=7;}
                if(gstate[8]==lossindex[1] || gstate[8]==lossindex[2]){lossindex2=8;}
                if(lossindex2!=0){
                    int mee1=0;
                    if(game_rule(me[0],gstate)!=0){mee1=0;}
                    else if(game_rule(me[1],gstate)!=0){mee1=1;}
                    else if(game_rule(me[2],gstate)!=0){mee1=2;}
                    if(mee1!=0) {
                        int tmp=gstate[me[mee1]];
                        gstate[me[mee1]]=gstate[lossindex2];
                        gstate[lossindex2]=tmp;change=1;
                    }
                }
            }
        }else if(winindex[0]!=-1){
            Log.d("winindex:",Arrays.toString(winindex));
            if(winindex[1]==5) {
                int winindex2=0;
                if(gstate[6]==winindex[1]){winindex2=6;}
                if(gstate[7]==winindex[1]){winindex2=7;}
                if(gstate[8]==winindex[1]){winindex2=8;}
                if(winindex2!=0){
                    int tmp=gstate[winindex[0]];
                    gstate[winindex[0]]=gstate[winindex2];
                    gstate[winindex2]=tmp;change=1;
                }
            }else{
                int winindex2=0;
                if(gstate[6]==winindex[1] || gstate[6]==winindex[2]){winindex2=6;}
                if(gstate[7]==winindex[1] || gstate[7]==winindex[2]){winindex2=7;}
                if(gstate[8]==winindex[1] || gstate[8]==winindex[2]){winindex2=8;}
                if(winindex2!=0){
                    int tmp=gstate[winindex[0]];
                    gstate[winindex[0]]=gstate[winindex2];
                    gstate[winindex2]=tmp;change=1;
                }
            }
            }

        String string = "";
        if(change==1){
            for (int ii = 0; ii < gstate.length; ii++) {
                string = string + gstate[ii]+",";
            }string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    int[] check_los_case2(int a,int b,int c,int a1,int b1,int c1){
        int five=0;
        if(a1==5 ){five=1;}if(b1==5 ){five=1;}if(c1==5 ){five=1;}
        //start1
        if(a==2 || a==4){if(b==5){if(c==9){return new int[]{0,2,4};}}}
        if(a==1){if(five==0){if(c==9){return new int[]{1,5};}}}
        if(a==1){if(b==5){if(c==8 || c==6){return new int[]{2,8,6};}}}
        //end1
        //start2
        if(a==1 || a==3){if(b==5){if(c==8){return new int[]{0,1,3};}}}
        if(a==2){if(five==0){if(c==8){return new int[]{1,5};}}}
        if(a==2){if(b==5){if(c==7 || c==9){return new int[]{2,7,9};}}}
        //end
        //start
        if(a==2 || a==6){if(b==5){if(c==7){return new int[]{0,2,6};}}}
        if(a==3){if(five==0){if(c==7){return new int[]{1,5};}}}
        if(a==3){if(b==5){if(c==8 || c==4){return new int[]{2,8,4};}}}
        //end
        //start
        if(a==1 || a==7){if(b==5){if(c==6){return new int[]{0,1,7};}}}
        if(a==4){if(five==0){if(c==6){return new int[]{1,5};}}}
        if(a==4){if(b==5){if(c==3 || c==9){return new int[]{2,3,9};}}}
        //end
        if(a==5){
            if(a==6 || a==8){if(c==1){return new int[]{1,6,8};}}
            if(a==1){if(c==6 || c==8){return new int[]{2,6,8};}}
            if(a==7 || a==9){if(c==2){return new int[]{1,7,9};}}
            if(a==2){if(c==7 || c==9){return new int[]{2,7,9};}}
            if(a==4 || a==8){if(c==3){return new int[]{1,4,8};}}
            if(a==3){if(c==4 || c==8){return new int[]{2,4,8};}}
            if(a==3 || a==9){if(c==4){return new int[]{1,3,9};}}
            if(a==4){if(c==3 || c==9){return new int[]{2,3,9};}}
            if(a==1 || a==7){if(c==6){return new int[]{1,1,7};}}
            if(a==6){if(c==1 || c==7){return new int[]{2,1,7};}}
            if(a==2 || a==6){if(c==7){return new int[]{1,2,6};}}
            if(a==7){if(c==2 || c==6){return new int[]{2,2,6};}}
            if(a==1 || a==3){if(c==8){return new int[]{1,1,3};}}
            if(a==8){if(c==1 || c==3){return new int[]{2,1,3};}}
            if(a==2 || a==4){if(c==9){return new int[]{1,2,4};}}
            if(a==9){if(c==2 || c==4){return new int[]{2,2,4};}}
        }
        //start
        if(a==3 || a==9){if(b==5){if(c==4){return new int[]{0,3,9};}}}
        if(a==6){if(five==0){if(c==4){return new int[]{1,5};}}}
        if(a==6){if(b==5){if(c==1 || c==7){return new int[]{2,1,7};}}}
        //end
        //start
        if(a==4 || a==8){if(b==5){if(c==3){return new int[]{0,4,8};}}}
        if(a==7){if(five==0){if(c==3){return new int[]{1,5};}}}
        if(a==7){if(b==5){if(c==2 || c==6){return new int[]{2,7,5};}}}
        //end
        //start
        if(a==7 || a==9){if(b==5){if(c==2){return new int[]{0,7,9};}}}
        if(a==8){if(five==0){if(c==2){return new int[]{1,5};}}}
        if(a==8){if(b==5){if(c==1 || c==3){return new int[]{2,1,3};}}}
        //end
        //start
        if(a==8 || a==6){if(b==5){if(c==1){return new int[]{0,8,6};}}}
        if(a==9){if(five==0){if(c==1){return new int[]{1,5};}}}
        if(a==9){if(b==5){if(c==2 || c==4){return new int[]{2,2,4};}}}
        //end
        return new int[]{-1};
    }

    int check_valid_number(List<Integer> arr,int[] gstate) {
        List<Integer> arr2 = new ArrayList<Integer>();
        for (Integer integer : arr)
        {
            if(integer==gstate[6]){arr2.add(6);}
            if(integer==gstate[7]){arr2.add(7);}
            if(integer==gstate[8]){arr2.add(8);}
        }
        if(arr2.size()==0){
            return 0;
        }else{
            Random r = new Random();
            return arr2.get(r.nextInt(arr2.size()));
        }
    }

    //**************************check wins
    Boolean check_won(int[] oppp){
        switch(oppp[0]){
            case 1:
                if(oppp[1]==5&&oppp[2]==9){return true;}
                if(oppp[1]==9&&oppp[2]==5){return true;}
                break;
            case 2:
                if(oppp[1]==5&&oppp[2]==8){return true;}
                if(oppp[1]==8&&oppp[2]==5){return true;}
                break;
            case 3:
                if(oppp[1]==5&&oppp[2]==7){return true;}
                if(oppp[1]==7&&oppp[2]==5){return true;}
                break;
            case 4:
                if(oppp[1]==5&&oppp[2]==6){return true;}
                if(oppp[1]==6&&oppp[2]==5){return true;}
                break;
            case 5:
                if(oppp[1]==6 && oppp[2]==4){return true;}
                if(oppp[1]==4 && oppp[2]==6){return true;}
                if(oppp[1]==3 && oppp[2]==7){return true;}
                if(oppp[1]==7 && oppp[2]==3){return true;}
                if(oppp[1]==8 && oppp[2]==2){return true;}
                if(oppp[1]==2 && oppp[2]==8){return true;}
                if(oppp[1]==9 && oppp[2]==1){return true;}
                if(oppp[1]==1 && oppp[2]==9){return true;}
                break;
            case 6:
                if(oppp[1]==5&&oppp[2]==4){return true;}
                if(oppp[1]==4&&oppp[2]==5){return true;}
                break;
            case 7:
                if(oppp[1]==5&&oppp[2]==3){return true;}
                if(oppp[1]==3&&oppp[2]==5){return true;}
                break;
            case 8:
                if(oppp[1]==5&&oppp[2]==2){return true;}
                if(oppp[1]==2&&oppp[2]==5){return true;}
                break;
            case 9:
                if(oppp[1]==5&&oppp[2]==1){return true;}
                if(oppp[1]==1&&oppp[2]==5){return true;}
                break;
        }
        return false;
    }
}
