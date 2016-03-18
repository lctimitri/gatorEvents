package com.seven.actionbar;

import android.view.animation.Animation;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created on 11/4/15.
 */
public class SlideDownAnim extends Animation {

    public final static int COLLAPSE = 1;
    public final static int EXPAND = 0;

    private View mView;
    private int mEndHeight;
    private int mType;
    private LinearLayout.LayoutParams layoutParams;

    public SlideDownAnim(View view, int duration, int type)
    {
        setDuration(duration);
        mView = view;
        mEndHeight = mView.getHeight();
        layoutParams = ((LinearLayout.LayoutParams) view.getLayoutParams());
        mType = type;
        if(mType == EXPAND)
        {
            layoutParams.height = 0;
        }
        else
        {
            layoutParams.height = LayoutParams.WRAP_CONTENT;
//            layoutParams.height = mEndHeight;
        }
        view.setVisibility(View.VISIBLE);
    }

    public int getHeight()
    {
        return mView.getHeight();
    }

    public void setHeight(int height)
    {
        mEndHeight = height;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f)
        {
            if (mType == EXPAND)
            {
                layoutParams.height = (int)(mEndHeight *  interpolatedTime);
            }
            else
            {
                layoutParams.height = (int)(mEndHeight * (1 - interpolatedTime));
//                layoutParams.height = mEndHeight;
            }

            mView.requestLayout();
        }
        else
        {
            if (mType == EXPAND)
            {
                layoutParams.height = LayoutParams.WRAP_CONTENT;
//                mView.setVisibility(View.VISIBLE);
            }
            else
            {
                mView.setVisibility(View.GONE);
//                layoutParams.height = mEndHeight;
            }
            mView.requestLayout();

        }
    }
}
