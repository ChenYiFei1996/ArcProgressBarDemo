package com.cyf.arcprogressbar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.cyf.arcprogressbar.R;

/**
 * Description: 圆形进度条
 * Author: cyf 2020/12/8 9:22
 * Version: 1.0
 * UpdateUser: cyf 2021/1/18 18:06
 * UpdateRemark: 绘制圆弧从直接使用canvas的drawArc方法改为绘制arcpath，
 * 因为实测发现drawArc方法存在角度偏差，绘制两段连接的圆弧时中间会出现空白（安卓10上未发现此问题）
 * Version: 1.1
 */
public class ArcProgressBar extends ProgressBar {
    public static final int STYLE_TICK = 1;                        //刻度
    public static final int STYLE_ARC = 0;                         //填充
    private final int DEFAULT_BORDER_WIDTH = dp2px(15);     //默认进度条宽度
    private final int DEFAULT_TICK_WIDTH = dp2px(2);        //默认刻度的宽度
    private final int DEFAULT_RADIUS = dp2px(72);           //默认圆形半径
    private final int DEFAULT_ARC_BG_COLOR = Color.WHITE;          //默认进度条背景颜色
    private final int DEFAULT_UNPROGRESS_COLOR = Color.WHITE;      //默认进度条无进度部分颜色
    private final int DEFAULT_PROGRESS_COLOR = Color.YELLOW;       //默认进度条进度颜色
    private final int DEFAULT_OFFSET_DEGREE = 60;                  //默认圆角偏移角度
    private final int DEFAULT_TICK_DENSITY = 4;                    //刻度间的间隔角度
    private final int MIN_DENSITY = 2;                             //刻度间的间隔最小角度
    private final int MAX_DENSITY = 8;                             //刻度间的间隔最大角度

    private int mStyleProgress = STYLE_TICK;                       //进度条类型
    private boolean mBgShow = false;                               //是否显示进度条背景，刻度进度条生效
    private int mRadius = DEFAULT_RADIUS;                        //进度条半径
    private int mBoardWidth = DEFAULT_BORDER_WIDTH;                //进度条宽度
    private int mDegree = DEFAULT_OFFSET_DEGREE;                   //圆形中无进度条部分的角度
    private int mArcBgColor = DEFAULT_ARC_BG_COLOR;                //进度条背景颜色，刻度进度条生效
    private int mUnProgressColor = DEFAULT_UNPROGRESS_COLOR;      //进度条无进度部分颜色
    private int mProgressColor = DEFAULT_PROGRESS_COLOR;           //进度条进度颜色
    private int mTickWidth = DEFAULT_TICK_WIDTH;                   //刻度宽度
    private int mTickDensity = DEFAULT_TICK_DENSITY;               //默认每个刻度相较上一个的旋转角度
    private boolean capRound = false;                              //笔帽形状
    private RectF mArcRect;                                        //绘制圆弧的矩形区域
    private Paint mLinePaint;                                      //刻度型进度条画笔
    private Paint mArcPaint;                                       //填充型进度条画笔
    private Path unProgressPath = new Path();                      //无进度条部分圆弧的绘制路径
    private Path progressPath = new Path();                        //有进度条部分圆弧的绘制路径
    private Path bgPath = new Path();                              //刻度进度条背景绘制路径

    public ArcProgressBar(Context context) {
        this(context, null);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context context
     * @param attrs   属性
     */
    @SuppressLint("CustomViewStyleable")
    private void init(final Context context, final AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar);
            try {
                mBoardWidth = a.getDimensionPixelOffset(R.styleable.ArcProgressBar_borderWidth, DEFAULT_BORDER_WIDTH);
                mUnProgressColor = a.getColor(R.styleable.ArcProgressBar_unProgressColor, DEFAULT_UNPROGRESS_COLOR);
                mProgressColor = a.getColor(R.styleable.ArcProgressBar_progressColor, DEFAULT_PROGRESS_COLOR);
                mArcBgColor = a.getColor(R.styleable.ArcProgressBar_arcBgColor, DEFAULT_ARC_BG_COLOR);
                mTickWidth = a.getDimensionPixelOffset(R.styleable.ArcProgressBar_tickWidth, DEFAULT_TICK_WIDTH);
                mTickDensity = a.getInt(R.styleable.ArcProgressBar_tickDensity, DEFAULT_TICK_DENSITY);
                mRadius = a.getDimensionPixelOffset(R.styleable.ArcProgressBar_barRadius, DEFAULT_RADIUS);
                mTickDensity = Math.max(Math.min(mTickDensity, MAX_DENSITY), MIN_DENSITY);
                mBgShow = a.getBoolean(R.styleable.ArcProgressBar_bgShow, false);
                mDegree = a.getInt(R.styleable.ArcProgressBar_degree, DEFAULT_OFFSET_DEGREE);
                mStyleProgress = a.getInt(R.styleable.ArcProgressBar_progressStyle, STYLE_TICK);
                capRound = a.getBoolean(R.styleable.ArcProgressBar_arcCapRound, false);
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (capRound) {
            mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        mArcPaint.setColor(mArcBgColor);
        mArcPaint.setStrokeWidth(mBoardWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mTickWidth);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //计算实际绘制区域大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            int widthSize = (int) (mRadius * 2 + mBoardWidth);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            int heightSize = (int) (mRadius * 2 + mBoardWidth);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        float progressProportion = getProgress() * 1.0f / getMax(); //进度部分所占比例
//        float x = mArcRect.right / 2 + ((float) mBoardWidth) / 2;//圆心x
//        float y = mArcRect.bottom / 2 + ((float) mBoardWidth) / 2;//圆心y
        float x = mArcRect.width() / 2 + mArcRect.left;//圆心x
        float y = mArcRect.height() / 2 + mArcRect.top;//圆心y
        float angle = ((float) mDegree) / 2;    //进度条开始绘制的角度，90+此值，即圆形无进度条部分总是朝下
        int count = (360 - mDegree) / mTickDensity; //进度条总刻度数量
        int target = (int) (progressProportion * count); //有进度部分的刻度数量
        if (mStyleProgress == STYLE_ARC) {
            float targetDegree = (360 - mDegree) * progressProportion;//有进度部分扫过的角度
            //绘制完成部分
            mArcPaint.setColor(mProgressColor);
            //改为使用绘制路径的方式绘制圆弧，因为实际测试直接使用canvas绘制圆弧会出现角度偏差
            progressPath.reset();
            progressPath.addArc(mArcRect, 90 + angle, targetDegree);
            canvas.drawPath(progressPath, mArcPaint);
            //drawArc的参数含义：绘制的矩形区域，开始绘制的角度，圆弧扫过的角度，是否经过圆心，画笔
//            canvas.drawArc(mArcRect, 90 + angle, targetDegree, false, mArcPaint);

            //绘制未完成部分
            mArcPaint.setColor(mUnProgressColor);
            unProgressPath.reset();
            unProgressPath.addArc(mArcRect, 90 + angle + targetDegree, 360 - mDegree - targetDegree);
            canvas.drawPath(unProgressPath, mArcPaint);
//            canvas.drawArc(mArcRect, 90 + angle + targetDegree, 360 - mDegree - targetDegree, false, mArcPaint);
        } else {
            if (mBgShow) {//绘制圆弧背景
                bgPath.reset();
                bgPath.addArc(mArcRect, 90 + angle, 360 - mDegree);
                canvas.drawPath(bgPath, mArcPaint);
//                canvas.drawArc(mArcRect, 90 + angle, 360 - mDegree, false, mArcPaint);
            }
            canvas.rotate(180 + angle, x, y);//绕圆心旋转坐标轴
            for (int i = 0; i <= count; i++) {
                if (target == count) {
                    mLinePaint.setColor(mProgressColor);
                } else {
                    if (i < target) {
                        mLinePaint.setColor(mProgressColor);
                    } else {
                        mLinePaint.setColor(mUnProgressColor);
                    }
                }

                canvas.drawLine(x, mArcRect.top + ((float) mBoardWidth) / 2, x, mArcRect.top - ((float) mBoardWidth) / 2, mLinePaint);
                canvas.rotate(mTickDensity, x, y);//绕圆心旋转坐标轴
            }
        }
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //在控件中心区域生成绘制圆形的矩形区域
        mArcRect = new RectF(((float) w) / 2 - mRadius,
                ((float) h) / 2 - mRadius,
                ((float) w) / 2 + mRadius,
                ((float) h) / 2 + mRadius);
    }

    /**
     * dp转px
     *
     * @param dpVal dp值
     * @return px值
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}