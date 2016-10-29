package com.example.shenjing.color2048;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import java.util.LinkedList;

/**
 * Created by shenjing on 2016/5/5.
 */
public class GameView extends GridLayout implements Serializable{
    DisplayMetrics dm = getResources().getDisplayMetrics();
    public int width = dm.widthPixels;
    public int height = dm.heightPixels;
    private int BLOCK_WIDTH = (int) (width * 0.225);
    private FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(BLOCK_WIDTH, BLOCK_WIDTH);
    public Block blocks[][] = new Block[4][4];
    public ArrayList<Point> points = new ArrayList<>();
    private LinkedList<Block> recBlocks = new LinkedList<>();
    private int flag2048 = 0;
    private boolean flagInit = false;
    public int score = 4;
    public int maxScore = 4;
    private TextView tv,tv2;
    public FrameLayout frameLayout;
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
        boolean mergeFlag = false;
        boolean moveFlag = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if(blocks[x][y].getNum()!=0){

                    //最左为0的block
                    int tempy = y;
                    int m;
                    for (m=y; m > 0; m--) {
                        if (blocks[x][m-1].getNum()!=0)
                            break;
                    }
                    //合并
                    boolean merge = false;
                    for (int n = y+1; n < 4; n++){
                        if (blocks[x][n].getNum()==0) {
                            continue;
                        }
                        if (blocks[x][y].equals(blocks[x][n])) {
                            if (flag2048 == 0) {
                                if (blocks[x][y].getNum() == 1024) {
                                    flag2048=1;
                                }
                            }
                            mergeFlag = true;
                            merge = true;
                            merge(x, y, x, n, x, m, blocks[x][y].getNum() * 2);
                            tempy = n;
                        }
                        break;
                    }
                    //移动
                    if (!merge && m != y) {
                        moveFlag = true;
                        merge(x, m, x, y, x, m, blocks[x][y].getNum());
                    }
                    y = tempy;
                }

            }
        }
        if (mergeFlag || moveFlag)
            addNum();
    }


    public void swipeRight(){
        boolean mergeFlag = false;
        boolean moveFlag = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y > -1; y--) {

                if(blocks[x][y].getNum()!=0){

                    int tempy = y;
                    int m;
                    for (m=y; m < 3; m++) {
                        if (blocks[x][m+1].getNum()!=0)
                            break;
                    }

                    boolean merge = false;
                    for (int n = y-1; n > -1; n--){
                        if (blocks[x][n].getNum()==0) {
                            continue;
                        }

                        if (blocks[x][y].equals(blocks[x][n])) {

                            if(flag2048==0)
                                if (blocks[x][y].getNum() == 1024)
                                    flag2048=1;
                            mergeFlag = true;
                            merge = true;
                            merge(x, y, x, n, x, m, blocks[x][y].getNum() * 2);
                            tempy = n;
                        }
                        break;
                    }

                    if (!merge && m != y) {
                        moveFlag = true;
                        merge(x, m, x, y, x, m, blocks[x][y].getNum());
                    }
                    y = tempy;

                }

            }
        }
        if (mergeFlag || moveFlag) {
            addNum();
        }
    }


    public void swipeUp(){
        boolean mergeFlag = false;
        boolean moveFlag = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if(blocks[x][y].getNum()!=0){

                    int tempx = x;
                    int m;
                    for (m=x; m > 0; m--) {
                        if (blocks[m-1][y].getNum()!=0)
                            break;
                    }

                    boolean merge = false;
                    for (int n = x+1; n < 4; n++){
                        if (blocks[n][y].getNum()==0) {
                            continue;
                        }
                        if (blocks[x][y].equals(blocks[n][y])) {
                            if (flag2048 == 0) {
                                if (blocks[x][y].getNum() == 1024) {
                                    flag2048=1;
                                }
                            }
                            mergeFlag = true;
                            merge = true;
                            merge(x, y, n, y, m, y, blocks[x][y].getNum() * 2);
                            tempx = n;
                        }
                        break;
                    }

                    if (!merge && m != x) {
                        moveFlag = true;
                        merge(m, y, x, y, m, y, blocks[x][y].getNum());
                    }
                    x = tempx;
                }

            }
        }
        if (mergeFlag || moveFlag) {
            addNum();
        }
    }


    public void swipeDown(){
        boolean mergeFlag = false;
        boolean moveFlag = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x > -1; x--) {

                if(blocks[x][y].getNum()!=0){

                    int tempx = x;
                    int m;

                    for (m=x; m < 3; m++) {
                        if (blocks[m+1][y].getNum()!=0)
                            break;
                    }

                    boolean merge = false;

                    for (int n = x-1; n > -1; n--){
                        if (blocks[n][y].getNum()==0) {
                            continue;
                        }

                        if (blocks[x][y].equals(blocks[n][y])) {
                            if (flag2048 == 0) {
                                if (blocks[x][y].getNum() == 1024) {
                                    flag2048=1;
                                }
                            }
                            mergeFlag = true;
                            merge = true;
                            merge(x, y, n, y, m, y, blocks[x][y].getNum() * 2);
                            tempx = n;
                        }

                        break;
                    }

                    if (!merge && m != x) {
                        moveFlag = true;
                        merge(m, y, x, y, m, y, blocks[x][y].getNum());
                    }
                    x = tempx;

                }

            }
        }
        if (mergeFlag || moveFlag) {
            addNum();
        }
    }


    public Block newBlock(int num) {
        Block b;
        if (recBlocks.isEmpty()) {
            b = new Block(getContext());
            frameLayout.addView(b);
        } else
            b = recBlocks.remove(0);
        b.setNum(num);
        return b;
    }

    public void recBlocks(Block b) {
        b.setVisibility(INVISIBLE);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(b, "translationX", 0, 0).setDuration(10);
        objectAnimatorX.start();
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(b, "translationY", 0, 0).setDuration(10);
        objectAnimatorY.start();
        recBlocks.add(b);
    }

    public void merge(final int fromX1, final int fromY1, final int fromX2, final int fromY2, final int toX, final int toY, final int num) {

        AnimatorSet animatorSet = new AnimatorSet();
        if (fromX1 != toX || fromY1 != toY) {
            animatorSet.play(moveAnimation(fromX2, fromY2, toX, toY)).with(moveAnimation(fromX1, fromY1, toX, toY));
        } else {
            animatorSet.play(moveAnimation(fromX2, fromY2, toX, toY));
        }
        if (fromX1 != toX || fromY1 != toY) {
            blocks[fromX1][fromY1].setNum(0);
        }
        blocks[fromX2][fromY2].setNum(0);
        blocks[toX][toY].setNum(num);
        if (fromX1 != toX || fromY1 != toY) {
            points.add(new Point(fromX1, fromY1));
        }
        points.add(new Point(fromX2, fromY2));
        points.remove(new Point(toX, toY));

        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                //super.onAnimationStart(animation);
                blocks[toX][toY].setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //super.onAnimationEnd(animation);
                blocks[toX][toY].setVisibility(VISIBLE);

                AnimatorSet as = new AnimatorSet();
                ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(blocks[toX][toY], "scaleX", 1.0f, 1.1f, 1.0f).setDuration(100);
                ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(blocks[toX][toY], "scaleY", 1.0f, 1.1f, 1.0f).setDuration(100);
                as.play(scaleAnimatorX).with(scaleAnimatorY);
                as.start();
            }
        });

        animatorSet.start();
    }


    public ObjectAnimator moveAnimation(final int fromX, final int fromY, final int toX, final int toY) {
        final Block tempBlock = newBlock(blocks[fromX][fromY].getNum());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(BLOCK_WIDTH, BLOCK_WIDTH);
        lp.leftMargin = fromY * BLOCK_WIDTH + 20;
        lp.topMargin = fromX * BLOCK_WIDTH + 20;
        tempBlock.setLayoutParams(lp);
        ObjectAnimator objectAnimator;
        if (fromX == toX)
            objectAnimator = ObjectAnimator.ofFloat(tempBlock, "translationX", 0, (toY - fromY) * BLOCK_WIDTH).setDuration(100);
        else if (fromY == toY)
            objectAnimator = ObjectAnimator.ofFloat(tempBlock, "translationY", 0, (toX - fromX) * BLOCK_WIDTH).setDuration(100);
        else
            objectAnimator = ObjectAnimator.ofFloat(tempBlock, "scaleX", 10, 1).setDuration(10);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //super.onAnimationStart(animation);
                tempBlock.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //super.onAnimationEnd(animation);
                recBlocks(tempBlock);
            }
        });
        return objectAnimator;

    }

    public void addNum(){
        if(!flagInit){
            tv = (TextView) ((View) getParent().getParent()).findViewById(R.id.textScore);
            tv2 = (TextView) ((View) getParent().getParent()).findViewById(R.id.textMaxScore);
            frameLayout = (FrameLayout) getParent();
            flagInit=true;
        }
        Point p =points.remove((int)((Math.random())*points.size()));
        int i=Math.random()>0.1?2:4;
        blocks[p.x][p.y].setNum(i);
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(0,1.1f,0,1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(300);
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
            frameLayout = (FrameLayout) getParent();
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
