package cn.lemon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 参考了部分代码：http://blog.csdn.net/yanzhenjie1003/article/details/51889239
 * Created by linlongxin on 2016/8/29.
 */

public class CountdownView extends View {

    private static final int MSG_UPDATE = 10;

    private static final int DEFAULT_TIME = 1000;
    private static final int DEFAULT_STOKE_WIDTH = 8;
    private static final int DEFAULT_SIZE = 100;
    private static final float DEFAULT_TEXT_SIZE = 15f;

    private int mProgressHintColor;
    private int mCircleColor;
    private int mCircleRadius;
    private int mTextColor;
    private int mProgressColor;
    private float mProgressWidth;
    private int mProgress = 0;
    private CharSequence mText;
    private float mTextSize;
    private int mTotalTime;
    private int mUpdateTime;

    // 总的绘制次数
    private int mDrawTimes;
    // 已经绘制的次数
    private int mCurrentDrawTimes;
    // 每次绘制角度
    private int mEachDrawAngle;

    private Paint mPaint;
    private TextPaint mTextPaint;
    private Rect mBounds;
    private RectF mArcRectF;

    private Action mEndAction;

    private Handler mHandler;

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountdownView, 0, 0);

        try {
            int defaultColor = Color.parseColor("grey");
            mText = a.getText(R.styleable.CountdownView_text);
            mTextColor = a.getColor(R.styleable.CountdownView_text_color, Color.BLACK);
            mTextSize = a.getDimension(R.styleable.CountdownView_text_size, DEFAULT_TEXT_SIZE);
            mUpdateTime = a.getInteger(R.styleable.CountdownView_update_time, DEFAULT_TIME);
            mTotalTime = a.getInteger(R.styleable.CountdownView_total_time, DEFAULT_TIME);
            mProgressColor = a.getColor(R.styleable.CountdownView_progress_color, Color.RED);
            mProgressWidth = a.getDimension(R.styleable.CountdownView_progress_width, DEFAULT_STOKE_WIDTH);
            mProgressHintColor = a.getColor(R.styleable.CountdownView_progress_hint_color, defaultColor);
            mCircleColor = a.getColor(R.styleable.CountdownView_bg_color, Color.WHITE);
        } finally {
            a.recycle();
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
        mArcRectF = new RectF();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_UPDATE:
                        int changePer = 100 / mDrawTimes;
                        postInvalidate();
                        mCurrentDrawTimes++;
                        mProgress += changePer;
                        mTotalTime -= mUpdateTime;
                        if (mProgress == 100) {
                            mEndAction.onAction();
                        } else {
                            sendEmptyMessageDelayed(MSG_UPDATE, mUpdateTime);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            mCircleRadius = DEFAULT_SIZE;
        } else {
            mCircleRadius = Math.min(width, height) / 2;
        }
        setMeasuredDimension(mCircleRadius * 2, mCircleRadius * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //找到view的边界
        getDrawingRect(mBounds);

        int centerX = mBounds.centerX();
        int centerY = mBounds.centerY();

        //画大圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleColor);
        canvas.drawCircle(centerX, centerY, mCircleRadius, mPaint);

        //画外边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setColor(mProgressHintColor);
        canvas.drawCircle(centerX, centerY, mCircleRadius - mProgressWidth, mPaint);

        //画字
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        float textY = centerY - (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        canvas.drawText(mText.toString(), centerX, textY, mTextPaint);

        //画进度条
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcRectF.set(mBounds.left + mProgressWidth, mBounds.top + mProgressWidth,
                mBounds.right - mProgressWidth, mBounds.bottom - mProgressWidth);
        canvas.drawArc(mArcRectF, -90,
                (mCurrentDrawTimes + 1) * mEachDrawAngle, false, mPaint);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setTotalTime(int time) {
        mTotalTime = time;
    }

    public void setUpdateTime(int updateTime) {
        mUpdateTime = updateTime;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public void setProgressColor(int color) {
        mProgressColor = color;
    }

    public void setCircleBackgroundColor(int color) {
        mCircleColor = color;
    }

    public void setTextColor(int color) {
        mTextColor = color;
    }

    public void start() {
        mDrawTimes = mTotalTime / mUpdateTime;
        mEachDrawAngle = 360 / mDrawTimes;
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE, mUpdateTime);
    }

    public void setOnFinishAction(Action action) {
        mEndAction = action;
    }
}
