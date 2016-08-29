package cn.lemon.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 参考了部分代码：http://blog.csdn.net/yanzhenjie1003/article/details/51889239
 * Created by linlongxin on 2016/8/29.
 */

public class CountdownView extends TextView {

    private int mOutLineColor = 0xFF888888;
    private int mOutLineWidth = 4;

    private int mCircleColor = 0x99888888;
    private int mCircleRadius;

    private int mTextColor = Color.WHITE;

    private int mProgressLineColor = Color.RED;
    private int mProgressLineWidth = 4;
    private int mProgress = 0;

    private int mCenterX;
    private int mCenterY;

    private int mTime = 2000;
    private int mChangTimes;
    private int mCurrentTime;

    private Paint mPaint;
    private Rect mBounds;
    private RectF mArcRectF;

    private Timer mTimer;
    private Action mEndAction;
    private String mText = "跳过";

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mBounds = new Rect();
        mArcRectF = new RectF();
        mTimer = new Timer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width > height) {
            height = width;
        } else {
            width = height;
        }
        mCircleRadius = width / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getDrawingRect(mBounds); //找到view的边界

        mCenterX = mBounds.centerX();
        mCenterY = mBounds.centerY();

        //画大圆
        mPaint.setAntiAlias(true);  //防锯齿
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mCircleRadius, mPaint);

        //画外边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mOutLineWidth);
        mPaint.setColor(mOutLineColor);
        canvas.drawCircle(mBounds.centerX(), mBounds.centerY(), mCircleRadius - mOutLineWidth, mPaint);

        //画字
        Paint paint = getPaint();
        paint.setColor(mTextColor);
        paint.setAntiAlias(true);  //防锯齿
        paint.setTextAlign(Paint.Align.CENTER);
        float textY = mCenterY - (paint.descent() + paint.ascent()) / 2;
        canvas.drawText(mText, mCenterX, textY, paint);

        //画进度条
        mPaint.setStrokeWidth(mProgressLineWidth);
        mPaint.setColor(mProgressLineColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRectF.set(mBounds.left + mProgressLineWidth, mBounds.top + mProgressLineWidth,
                mBounds.right - mProgressLineWidth, mBounds.bottom - mProgressLineWidth);
        canvas.drawArc(mArcRectF, -90, mCurrentTime * (360 / mChangTimes), false, mPaint);
    }

    public void setText(String text) {
        mText = text;
    }

    //应该被500整除
    public void setTime(int time) {
        mTime = time;
        mChangTimes = time / 500;
    }

    public void setProgressColor(int color) {
        mProgressLineColor = color;
    }
    public void setCircleBackgroundColor(int color){
        mCircleColor = color;
    }
    public void setTextColor(int color){
        mTextColor = color;
    }

    public void star() {
        final int changePer = 100 / mChangTimes;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mCurrentTime++;
                mProgress += changePer;
                postInvalidate();
                if (mProgress == 100) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            mEndAction.onAction();
                        }
                    });
                    mTimer.cancel();
                }
            }
        }, 500, 500);
    }

    public void setEndAction(Action action) {
        mEndAction = action;
    }
}
