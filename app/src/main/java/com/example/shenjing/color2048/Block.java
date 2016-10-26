package com.example.shenjing.color2048;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by shenjing on 2016/5/5.
 */
public class Block extends FrameLayout {

    private TextView t;
    private int num = 0;
    LayoutParams lp;
    DisplayMetrics dm = getResources().getDisplayMetrics();
    public int width = dm.widthPixels/4;
    public GradientDrawable myGrad;


    public Block(Context context) {
        super(context);
        initBlock();
        setNum(0);
    }

//    public Block(Context context,int num) {
//        super(context);
//        initBlock();
//        setNum(num);
//    }

    private void initBlock(){
        this.setBackgroundResource(R.drawable.blockshape);
        t = new TextView(getContext());
        t.setTextSize(30);
        t.setGravity(Gravity.CENTER);
        addView(t);
        myGrad = (GradientDrawable)this.getBackground();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        switch (num){
            case 0:
                myGrad.setColor(getResources().getColor(R.color.block0));
//                t.setBackgroundColor(0x33E1BEE7);
                t.setText("");
                break;
            case 2:
                myGrad.setColor(getResources().getColor(R.color.block2));
//                t.setBackgroundColor(0xFFE1BEE7);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 4:
                myGrad.setColor(getResources().getColor(R.color.block4));
//                t.setBackgroundColor(0xFFCE93D8);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 8:
                myGrad.setColor(getResources().getColor(R.color.block8));
//                t.setBackgroundColor(0xDDBA68C8);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 16:
                myGrad.setColor(getResources().getColor(R.color.block16));
//                t.setBackgroundColor(0xDDAB47BC);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 32:
                myGrad.setColor(getResources().getColor(R.color.block32));
//                t.setBackgroundColor(0xFFAB47BC);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 64:
                myGrad.setColor(getResources().getColor(R.color.block64));
//                t.setBackgroundColor(0xFF9C27B0);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 128:
                myGrad.setColor(getResources().getColor(R.color.block128));
//                t.setBackgroundColor(0xFF8E24AA);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 256:
                myGrad.setColor(getResources().getColor(R.color.block256));
//                t.setBackgroundColor(0xFF7B1FA2);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 512:
                myGrad.setColor(getResources().getColor(R.color.block512));
//                t.setBackgroundColor(0xFF6A1B9A);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 1024:
                myGrad.setColor(getResources().getColor(R.color.block1024));
//                t.setBackgroundColor(0xBB4A148C);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 2048:
                myGrad.setColor(getResources().getColor(R.color.block2048));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 4096:
                myGrad.setColor(getResources().getColor(R.color.block4096));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 8192:
                myGrad.setColor(getResources().getColor(R.color.block8192));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 16384:
                myGrad.setColor(getResources().getColor(R.color.block16384));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 32768:
                myGrad.setColor(getResources().getColor(R.color.block32768));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 65536:
                myGrad.setColor(getResources().getColor(R.color.block65536));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;

            default:
                myGrad.setColor(getResources().getColor(R.color.blockOther));
//                t.setBackgroundColor(0xFFAA00FF);
                t.setText(num+"");
                t.setTextColor(getResources().getColor(R.color.textLight));
                break;
        }
    }

    public boolean equals(Block o) {
        return  (this.num==o.getNum());
    }

    public void merge(Block b){
        this.setNum(b.getNum()*2);
        b.setNum(0);
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f,1.1f,1f,1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(150);
        sa.setFillBefore(true);
        as.addAnimation(sa);
        this.startAnimation(as);
    }

//    public void move(final int fromX, final int toX, final int fromY, final int toY){
//        //final Block b = new Block(getContext(),blocks[fromX][fromY].getNum());
//        TranslateAnimation ta = new TranslateAnimation(0,(toY-fromY)*width/4,0,(toX-fromX)*width/4);
//        ta.setDuration(500);
//        ta.setFillBefore(true);
//        this.startAnimation(ta);
//    }
}
