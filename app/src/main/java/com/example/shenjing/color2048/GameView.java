package com.example.shenjing.color2048;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shenjing on 2016/5/5.
 */
public class GameView extends GridLayout implements Serializable{
    DisplayMetrics dm = getResources().getDisplayMetrics();
    public int width = dm.widthPixels;
    public int height = dm.heightPixels;
    public Block blocks[][] = new Block[4][4];
    public ArrayList<Point> points = new ArrayList<>();
    private int flag2048 = 0;
    private boolean flagInit = false;
    public int score = 4;
    public int maxScore = 4;
    private TextView tv,tv2;
    SharedPreferences sf;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGame();
    }

    public GameView(Context context) {
        super(context);

        initGame();
    }

    private void initGame() {
        setColumnCount(4);
        this.setBackgroundResource(R.drawable.gridshape);
        sf = getContext().getSharedPreferences("score", Context.MODE_PRIVATE);
        //loadGame();

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                blocks[x][y] = new Block(getContext());
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)(width*0.225),(int)(width*0.225));
                addView(blocks[x][y],lp);
                points.add(new Point(x,y));
            }
        }

        for(int i=0; i<2; i++){
            Point p =points.remove((int)((Math.random())*points.size()));
            blocks[p.x][p.y].setNum(2);
        }


        setOnTouchListener(new OnTouchListener() {

            private float startX,startY,offestX,offestY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX=event.getX();
                        startY=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offestX = event.getX()-startX;
                        offestY = event.getY()-startY;
                        if (Math.abs(offestX)>Math.abs(offestY)+10){
                            if (offestX<-10)
                                swipeLeft();
                            else if (offestX>10)
                                swipeRight();
                        }
                        else{
                            if (offestY<-10)
                                swipeUp();
                            else if (offestY>10)
                                swipeDown();
                        }
                        break;
                }
                return true;
            }
        });


    }


    public void retryGame(){
        points.clear();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                blocks[x][y].setNum(0);
                points.add(new Point(x,y));
            }
        }

        for(int i=0; i<2; i++){
            Point p =points.remove((int)((Math.random())*points.size()));
            blocks[p.x][p.y].setNum(2);
        }
        score = 4;
        tv.setText(score+"");
    }


    public void swipeLeft(){
        boolean flag = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                if(blocks[x][y].getNum()!=0){

                    //最左为0的block
                    int m;
                    for (m=y; m > 0; m--) {
                        if (blocks[x][m-1].getNum()!=0)
                            break;
                    }
                    //左移
                    if (m!=y) {
                        //blocks[x][y].move(x,x,y,m);
                        blocks[x][m].setNum(blocks[x][y].getNum());
                        points.remove(new Point(x,m));
                        blocks[x][y].setNum(0);
                        points.add(new Point(x,y));
                        flag = true;
                        y = m;
                    }

                    //相同的合并
                    for (int n = y+1; n < 4; n++){
                        if (blocks[x][n].getNum()==0) {
                            continue;
                        }
                        else if (blocks[x][y].equals(blocks[x][n])) {
                            blocks[x][y].merge(blocks[x][n]);
                            if(flag2048==0)
                                if(blocks[x][y].getNum()==2048)
                                    flag2048=1;
                            flag = true;
                            points.add(new Point(x,n));
                            y=n;
                        }
                        break;
                    }

                }

            }
        }
        if (flag)
        addNum();
    }


    public void swipeRight(){
        boolean flag = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y > -1; y--) {

                if(blocks[x][y].getNum()!=0){


                    int m;
                    for (m=y; m < 3; m++) {
                        if (blocks[x][m+1].getNum()!=0)
                            break;
                    }
                    if (m!=y) {
                        blocks[x][m].setNum(blocks[x][y].getNum());
                        points.remove(new Point(x,m));
                        blocks[x][y].setNum(0);
                        points.add(new Point(x,y));
                        flag = true;
                        y = m;
                    }

                    for (int n = y-1; n > -1; n--){
                        if (blocks[x][n].getNum()==0) {
                            continue;
                        }
                        else if (blocks[x][y].equals(blocks[x][n])) {
                            blocks[x][y].merge(blocks[x][n]);
                            if(flag2048==0)
                                if(blocks[x][y].getNum()==2048)
                                    flag2048=1;
                            flag = true;
                            points.add(new Point(x,n));
                            y=n;
                        }
                        break;
                    }

                }

            }
        }
        if (flag)
        addNum();
    }


    public void swipeUp(){
        boolean flag = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                if(blocks[x][y].getNum()!=0){

                    int m;
                    for (m=x; m > 0; m--) {
                        if (blocks[m-1][y].getNum()!=0)
                            break;
                    }
                    if (m!=x) {
                        blocks[m][y].setNum(blocks[x][y].getNum());
                        points.remove(new Point(m,y));
                        blocks[x][y].setNum(0);
                        points.add(new Point(x,y));
                        flag = true;
                        x = m;
                    }

                    for (int n = x+1; n < 4; n++){
                        if (blocks[n][y].getNum()==0) {
                            continue;
                        }
                        else if (blocks[x][y].equals(blocks[n][y])) {
                            blocks[x][y].merge(blocks[n][y]);
                            if(flag2048==0)
                                if(blocks[x][y].getNum()==2048)
                                    flag2048=1;
                            flag = true;
                            points.add(new Point(n,y));
                            x=n;
                        }
                        break;
                    }

                }

            }
        }
        if (flag)
        addNum();
    }


    public void swipeDown(){
        boolean moveFlag = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x > -1; x--) {

                if(blocks[x][y].getNum()!=0){

                    int m;
                    for (m=x; m < 3; m++) {
                        if (blocks[m+1][y].getNum()!=0)
                            break;
                    }
                    if (m!=x) {
                        blocks[m][y].setNum(blocks[x][y].getNum());
                        points.remove(new Point(m,y));
                        blocks[x][y].setNum(0);
                        points.add(new Point(x,y));
                        moveFlag = true;
                        x = m;
                    }

                    for (int n = x-1; n > -1; n--){
                        if (blocks[n][y].getNum()==0) {
                            continue;
                        }
                        else if (blocks[x][y].equals(blocks[n][y])) {
                            blocks[x][y].merge(blocks[n][y]);
                            if(flag2048==0)
                                if(blocks[x][y].getNum()==2048)
                                    flag2048=1;
                            moveFlag = true;
                            points.add(new Point(n,y));
                            x=n;
                        }
                        break;
                    }

                }

            }
        }
        if (moveFlag)
        addNum();
    }


//    public void move(final int fromX, final int toX, final int fromY, final int toY){
//        //AnimationSet as = new AnimationSet(true);
//        //final Block b = new Block(getContext(),blocks[fromX][fromY].getNum());
//        TranslateAnimation ta = new TranslateAnimation(0,(toY-fromY)*width/4,0,(toX-fromX)*width/4);
//        ta.setDuration(500);
//        ta.setFillBefore(true);
//        ta.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                blocks[fromX][fromY].setNum(blocks[toX][toY].getNum());
//                blocks[toX][toY].setNum(0);
//                points.remove(new Point(toX,toY));
//                points.add(new Point(fromX,fromY));
//                blocks[fromX][fromY].setAnimation(null);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        //as.addAnimation(ta);
//        blocks[fromX][fromY].startAnimation(ta);
//
//    }

    public void addNum(){
        if(!flagInit){
            tv = (TextView) ((View)getParent()).findViewById(R.id.textScore);
            tv2 = (TextView) ((View)getParent()).findViewById(R.id.textMaxScore);
            flagInit=true;
        }
        Point p =points.remove((int)((Math.random())*points.size()));
        int i=Math.random()>0.1?2:4;
        blocks[p.x][p.y].setNum(i);
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(0,1.1f,0,1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(150);
        sa.getFillBefore();
        as.addAnimation(sa);
        blocks[p.x][p.y].startAnimation(as);
        score += i;
        tv.setText(score+"");

        gameOver();
    }


    public void gameOver(){
        boolean overFlag = true;

        if (points.size()>0)
            overFlag = false;
        else {
            for (int i=0; i<4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i != 3 && j != 3) {
                        if (blocks[i][j].equals(blocks[i + 1][j]) || blocks[i][j].equals(blocks[i][j + 1]))
                            overFlag = false;
                    } else if (i == 3 && j != 3) {
                        if (blocks[i][j].equals(blocks[i][j + 1]))
                            overFlag = false;
                    } else if (i != 3 && j == 3) {
                        if (blocks[i][j].equals(blocks[i + 1][j]))
                            overFlag = false;
                    }
                }
            }
        }

        if (flag2048==1){
            new AlertDialog.Builder(getContext()).setTitle(R.string.congratulation).setMessage(R.string.get2048)
                    .setPositiveButton(R.string.continueGame, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            flag2048 = 2;
        }

        if (overFlag){
            String title = getResources().getString(R.string.gameOverTitle);
            String message = getResources().getString(R.string.gameOverMessage);
            if (score>maxScore){
                title = getResources().getString(R.string.congratulation);
                message = getResources().getString(R.string.getMaxScore);
                maxScore = score;
                tv2.setText(maxScore+"");
                SharedPreferences.Editor ed = sf.edit();
                ed.putInt("maxScore",maxScore);
                ed.commit();
            }
            else{
            }
            new AlertDialog.Builder(getContext()).setTitle(title).setMessage(message)
                    .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            retryGame();
                        }
                    }).show();
        }
    }

    public void saveGame(){
        SharedPreferences.Editor ed = sf.edit();
        for(int i=0;i<4;i++)
            for (int j=0;j<4;j++) {
                ed.putInt("blocks"+(i*4+j),blocks[i][j].getNum());
            }
        ed.putInt("score",score);
        ed.putInt("maxScore",maxScore);
        ed.putInt("flag2048",flag2048);
        ed.commit();
        Toast.makeText(getContext(),R.string.save_s,Toast.LENGTH_SHORT).show();

    }

    public void loadGame(){
        if(!flagInit){
            tv = (TextView) ((View)getParent()).findViewById(R.id.textScore);
            tv2 = (TextView) ((View)getParent()).findViewById(R.id.textMaxScore);
            flagInit=true;
        }
        points.clear();
        for(int i=0;i<4;i++)
            for (int j=0;j<4;j++) {
                int num=sf.getInt("blocks"+(i*4+j),0);
                blocks[i][j].setNum(num);
                if(num==0)
                    points.add(new Point(i,j));
            }
        score = sf.getInt("score",4);
        tv.setText(score+"");
        maxScore = sf.getInt("maxScore",4);
        tv2.setText(maxScore+"");
        flag2048 = sf.getInt("flag2048",0);
        Toast.makeText(getContext(),R.string.load_s,Toast.LENGTH_SHORT).show();
    }

}
