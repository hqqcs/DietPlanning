package com.example.a.dietplanning.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.example.a.dietplanning.R;


public class ScoreView extends View {

    private int startColor = 0xff67a4f5;
    private int endColor = 0xFF3abbe2;
    private int defaultColor = 0x00F5F5F5;
    private int defaultColor1 = 0xFFF5F5F5;

    private int percentEndColor;

    private int strokeWidth;
    private float percent = 0.0006f;
    private float mFootStep = 0;

    // 用于渐变
    private Paint paint;

    private int deltaR, deltaB, deltaG;
    private int startR, startB, startG;

    private ValueAnimator mAnimator;
    private long mAnimTime = 1000;

    private Paint mBackgroundCirclePaint;
    private Paint startPaint;
    private Paint endPaint;

    private final RectF rectF = new RectF();

    private int[] customColors;
    private int[] fullColors;
    private int[] emptyColors;
    private float[] customPositions;
    private float[] extremePositions;

    private float mTextOffsetPercentInRadius = 0.5f;
    private float mValueFix = 0.5f;
    private float mTarget = 100.0f;
    private Context mContext;
    private float mDefaultTarget = 100.0f;
    private static final String TAG = "ScoreView";


    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(final Context context, final AttributeSet attrs) {
        float defaultPercent = -1;
        mContext = context;

        final int strokeWdithDefaultValue = (int) (10 * getResources().getDisplayMetrics().density + 0.5f);

        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SportStepCountView);
            percent = typedArray.getFloat(R.styleable.SportStepCountView_mpc_percent, defaultPercent);
            strokeWidth = (int) typedArray.getDimension(R.styleable.SportStepCountView_mpc_stroke_width, strokeWdithDefaultValue);
            startColor = typedArray.getColor(R.styleable.SportStepCountView_mpc_start_color, startColor);
            endColor = typedArray.getColor(R.styleable.SportStepCountView_mpc_end_color, endColor);
            defaultColor = typedArray.getColor(R.styleable.SportStepCountView_mpc_default_color, defaultColor);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        ;

        int samllCircleSize = (int) (8 * getResources().getDisplayMetrics().density + 0.5f);

        mBackgroundCirclePaint = new Paint();
        mBackgroundCirclePaint.setAntiAlias(true);
        mBackgroundCirclePaint.setColor(context.getResources().getColor(R.color.pregnancy_color_5a5a66));
        mBackgroundCirclePaint.setStyle(Paint.Style.STROKE);
        mBackgroundCirclePaint.setStrokeJoin(Paint.Join.ROUND);
        mBackgroundCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mBackgroundCirclePaint.setStrokeWidth(samllCircleSize);

        startPaint = new Paint();
        startPaint.setColor(startColor);
        startPaint.setAntiAlias(true);
        startPaint.setStyle(Paint.Style.FILL);


        endPaint = new Paint();
        endPaint.setAntiAlias(true);
        endPaint.setStyle(Paint.Style.FILL);

        refreshDelta();

        customColors = new int[]{startColor, percentEndColor, defaultColor, defaultColor};
        fullColors = new int[]{startColor, endColor};
        emptyColors = new int[]{defaultColor, defaultColor};

        customPositions = new float[4];
        customPositions[0] = 0;
        customPositions[3] = 1;

        extremePositions = new float[]{0, 1};

        initRed();
    }

    public void setValue(float value,float target) {
        if(target == 0) {
            target = mTarget;
        }

        //如果是恢复期，target=0的情况,这个时候mDefaultTarget=0
        if(target == 0) {
            mFootStep =  value;
            startAnimator(0, 1, mAnimTime);
            return;
        }

        float start = 0;
        float end = value / (target*(1.0f));

        if(value > target) {
            end = 1;
        }
        if (value < mValueFix) {
            end = mValueFix / target;
        }
        if (mFootStep != 0) {
            percent = end;
            mFootStep = value;
            startAnimator(start, end, mAnimTime);
        } else {
            mFootStep =  value;
            startAnimator(start, end, mAnimTime);
        }

    }

    public void setValueDuringRefresh(float value, float target) {
        if(target == 0) {
            target = mTarget;
        }

        //如果是恢复期，target=0的情况,这个时候mDefaultTarget=0
        if(target == 0) {
            percent = 1;
            mFootStep = value;
            invalidate();
            return;
        }

        float start = 0;
        float end = value / target;

        if(value > target) {
            end = 1;
        }
        if (value < mValueFix) {
            end = mValueFix / target;
        }

        if (mFootStep != 0) {
            percent = end;
            mFootStep = value;
            invalidate();

        } else {
            mFootStep =  value;
            startAnimator(start, end, mAnimTime);
        }
    }


    private void initRed() {
        float end = mValueFix / mTarget;
        if(mValueFix >= mTarget) {
            end = mValueFix / 5000.0f;
        }
        percent = end;
        mFootStep = 0;
        invalidate();
    }


    private void startAnimator(float start, float end, long animTime) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(animTime);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }


    private void calculatePercentEndColor(final float percent) {
        percentEndColor = ((int) (deltaR * percent + startR) << 16) +
                ((int) (deltaG * percent + startG) << 8) +
                ((int) (deltaB * percent + startB)) + 0xFF000000;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.rectF.left = getMeasuredWidth() / 2 - strokeWidth / 2;
        this.rectF.top = 0;
        this.rectF.right = getMeasuredWidth() / 2 + strokeWidth / 2;
        this.rectF.bottom = strokeWidth;
    }


    // 目前由于SweepGradient赋值只在构造函数，无法pre allocate & reuse instead
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int restore = canvas.save();

        final int cx = getMeasuredWidth() / 2;
        final int cy = getMeasuredHeight() / 2;
        final int radius = getMeasuredWidth() / 2 - strokeWidth / 2;

        float drawPercent = percent;
        if (drawPercent > 0.97 && drawPercent < 1) {
            drawPercent = 0.97f;
        }

        // 画渐变圆
        canvas.save();
        canvas.rotate(-90, cx, cy);
        int[] colors;
        float[] positions;

        if (drawPercent < 1 && drawPercent > 0) {
            calculatePercentEndColor(drawPercent);
            customColors[1] = percentEndColor;
            colors = customColors;
            customPositions[1] = drawPercent;
            customPositions[2] = drawPercent;
            positions = customPositions;
        } else if (drawPercent == 1) {
            colors = fullColors;
            positions = extremePositions;
        } else {
            colors = emptyColors;
            positions = extremePositions;
        }

        //这个是灰色的大圆环
        canvas.drawCircle(cx, cy, radius, mBackgroundCirclePaint);

        final SweepGradient sweepGradient = new SweepGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, colors, positions);
        paint.setShader(sweepGradient);
        //paint.setShadowLayer(13.5f,0,0.5f,getResources().getColor(R.color.circle_shadow_color));
        canvas.drawCircle(cx, cy, radius, paint);

        canvas.restore();

        if (drawPercent > 0) {
            // 绘制结束的半圆
            if (drawPercent < 1) {
                canvas.save();
                endPaint.setColor(percentEndColor);
                canvas.rotate((int) Math.floor(360.0f * drawPercent) - 1, cx, cy);
                canvas.drawArc(rectF, -90f, 180f, true, endPaint);
                canvas.restore();
            }

            canvas.save();
            // 绘制开始的半圆
            canvas.drawArc(rectF, 90f, 180f, true, startPaint);
            canvas.restore();
        }

        canvas.restoreToCount(restore);
    }
    private void refreshDelta() {
        int endR = (endColor & 0xFF0000) >> 16;
        int endG = (endColor & 0xFF00) >> 8;
        int endB = (endColor & 0xFF);

        this.startR = (startColor & 0xFF0000) >> 16;
        this.startG = (startColor & 0xFF00) >> 8;
        this.startB = (startColor & 0xFF);

        deltaR = endR - startR;
        deltaG = endG - startG;
        deltaB = endB - startB;
    }


}
