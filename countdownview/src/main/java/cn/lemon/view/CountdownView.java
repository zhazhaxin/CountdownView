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

    private int mTime = 2000;   //默认计时
    private int mDrawTimes = 4;  //总的绘制次数
    private int mCurrentDrawTimes;  //已经绘制的次数
    private int mEachDrawAngle = 90; //默认每次绘制90度

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
        canvas.drawArc(mArcRectF, -90,
                (mCurrentDrawTimes + 1) * mEachDrawAngle, false, mPaint);
    }

    public void setText(String text) {
        mText = text;
    }

    //倒计时时间应该被500整除，每隔500毫秒更新一次UI
    public void setTime(int time) {
        mTime = time;
        mDrawTimes = time / 500;
        mEachDrawAngle = 360 / mDrawTimes;
    }

    public int getTime() {
        return mTime;
    }

    public void setProgressColor(int color) {
        mProgressLineColor = color;
    }

    public void setCircleBackgroundColor(int color) {
        mCircleColor = color;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void star() {
        final int changePer = 100 / mDrawTimes;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                postInvalidate();
                mCurrentDrawTimes++;
                mProgress += changePer;
                mTime -= 500;
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

    public void setOnFinishAction(Action action) {
        mEndAction = action;
    }
}
