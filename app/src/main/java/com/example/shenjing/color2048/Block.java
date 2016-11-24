package com.example.shenjing.color2048;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by shenjing on 2016/5/5.
 */
public class Block extends FrameLayout {

    private TextView t;
    private int num = 0;
    DisplayMetrics dm = getResources().getDisplayMetrics();
    public int WIDTH = dm.widthPixels / 4;
    public GradientDrawable myGrad;


    public Block(Context context) {
        super(context);
        initBlock();
        setNum(0);
    }

    private void initBlock(){
        this.setBackgroundResource(R.drawable.blockshape);
        t = new TextView(getContext());
        t.setTextSize(30);
        t.setGravity(Gravity.CENTER);
        t.setTextColor(getResources().getColor(R.color.textDeep));
        addView(t);
        myGrad = (GradientDrawable)this.getBackground();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num == 0)
            t.setText("");
        else
            t.setText(num + "");

        switch (num){
            case 0:
                myGrad.setColor(getResources().getColor(R.color.block0));
//                t.setBackgroundColor(0x33E1BEE7);
                break;
            case 2:
                myGrad.setColor(getResources().getColor(R.color.block2));
//                t.setBackgroundColor(0xFFE1BEE7);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 4:
                myGrad.setColor(getResources().getColor(R.color.block4));
//                t.setBackgroundColor(0xFFCE93D8);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 8:
                myGrad.setColor(getResources().getColor(R.color.block8));
//                t.setBackgroundColor(0xDDBA68C8);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 16:
                myGrad.setColor(getResources().getColor(R.color.block16));
//                t.setBackgroundColor(0xDDAB47BC);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 32:
                myGrad.setColor(getResources().getColor(R.color.block32));
//                t.setBackgroundColor(0xFFAB47BC);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 64:
                myGrad.setColor(getResources().getColor(R.color.block64));
//                t.setBackgroundColor(0xFF9C27B0);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 128:
                myGrad.setColor(getResources().getColor(R.color.block128));
//                t.setBackgroundColor(0xFF8E24AA);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 256:
                myGrad.setColor(getResources().getColor(R.color.block256));
//                t.setBackgroundColor(0xFF7B1FA2);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 512:
                myGrad.setColor(getResources().getColor(R.color.block512));
//                t.setBackgroundColor(0xFF6A1B9A);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 1024:
                myGrad.setColor(getResources().getColor(R.color.block1024));
//                t.setBackgroundColor(0xBB4A148C);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 2048:
                myGrad.setColor(getResources().getColor(R.color.block2048));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 4096:
                myGrad.setColor(getResources().getColor(R.color.block4096));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textDeep));
                break;
            case 8192:
                myGrad.setColor(getResources().getColor(R.color.block8192));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 16384:
                myGrad.setColor(getResources().getColor(R.color.block16384));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 32768:
                myGrad.setColor(getResources().getColor(R.color.block32768));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
            case 65536:
                myGrad.setColor(getResources().getColor(R.color.block65536));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;

            default:
                myGrad.setColor(getResources().getColor(R.color.blockOther));
//                t.setBackgroundColor(0xFFAA00FF);
                //t.setTextColor(getResources().getColor(R.color.textLight));
                break;
        }
    }

    public boolean equals(Block o) {
        return  (this.num==o.getNum());
    }

}
