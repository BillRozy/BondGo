package com.fd.gobondg0;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class ForecastView extends View{

    static final private float PADDING = 100;
    static final private float ARROW_SIZE = 50;
    static final private float AXIS_WEIGHT = 5;
    static final private float POLYLINE_WEIGHT = 4;
    static final private float TEXT_SIZE = 14;
    static final private int INTERVALS = 5;
    static final private int INTERVAL_SIZE = 10;
    static final private Paint mAxisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    static final private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    static final private Paint mArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int xSteps = 0;
    private int ySteps = 0;
    private int xStep = 0;
    private int yStep = 0;
    private String xLegend = "X Axis";
    private String yLegend = "Y Axis";

    private int viewWidth = 1;
    private int viewHeight = 1;

    private float mMaxX = 0;
    private float mMinX = 0;
    private float mMaxY = 0;
    private float mMinY = 0;

    private float mYFactor;
    private float mXFactor;

    private float[] mXIntervals = new float[5];
    private float[] mYIntervals = new float[5];

    private ArrayList<PlotPolyline> mPolylines = new ArrayList<>();
    private ArrayList<PlotPoint> mPoints = new ArrayList<>();


    public ForecastView(Context context){
        this(context, null);
    }

    public ForecastView(Context context, AttributeSet attrs){
        super(context, attrs);
        mAxisPaint.setColor(Color.BLACK);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setStrokeWidth(AXIS_WEIGHT);

        mArrowPaint.setColor(Color.BLACK);
        mArrowPaint.setStyle(Paint.Style.FILL);

        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(14);
    }

    public void prepareToDrawCurves(float startX, float endX, float startY, float endY){
        mMinX = startX;
        mMaxX = endX;
        mMinY = startY;
        mMaxY = endY;
        for(int i = 0; i < INTERVALS; i++){
            mXIntervals[i] = (mMaxX - mMinX)/INTERVALS * i;
            mYIntervals[i] = (mMaxY - mMinY)/INTERVALS * i;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
    }

    public void createPolyline(float[] x, float[] y, String name, int color){
        mPolylines.add(new PlotPolyline(name, color, POLYLINE_WEIGHT, x, y));
    }

    public void createPoint(float x, float y, float radius, String name, int color){
        mPoints.add(new PlotPoint(name, color, radius, x, y));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mYFactor = (viewHeight - 2 * PADDING)/(mMaxY - mMinY);
        mXFactor = (viewWidth -  2 * PADDING)/(mMaxX - mMinX);
        drawAxis(canvas);
        drawIntervals(canvas);
        for(PlotPolyline p : mPolylines){
            p.draw(canvas, mXFactor, mYFactor);
        }

        for(PlotPoint p : mPoints){
            p.draw(canvas, mXFactor, mYFactor);
        }

    }

    private void drawIntervals(Canvas canvas){
        float xLength = viewWidth - PADDING - PADDING;
        float xStep = xLength/INTERVALS;
        float yLength = viewHeight - PADDING - PADDING;
        float yStep = yLength/INTERVALS;
        for(int i = 1; i < INTERVALS; i++){
            // draw x axis interval
            canvas.drawLine(PADDING + i * xStep, viewHeight -  PADDING - INTERVAL_SIZE/2, PADDING + i * xStep, viewHeight - PADDING + INTERVAL_SIZE/2, mAxisPaint);
            canvas.drawText(String.format("%.1f", mXIntervals[i]),PADDING + i * xStep - TEXT_SIZE/2, viewHeight - PADDING + INTERVAL_SIZE / 2 + TEXT_SIZE * 2, mTextPaint);
            // draw y axis interval
            canvas.drawLine(PADDING - INTERVAL_SIZE/2, viewHeight -  PADDING - i * yStep, PADDING + INTERVAL_SIZE/2,viewHeight - PADDING - i * yStep, mAxisPaint);
            canvas.drawText(String.format("%.1f", mYIntervals[i]),PADDING - INTERVAL_SIZE/2  - TEXT_SIZE * 4, viewHeight -  PADDING - i * yStep - TEXT_SIZE/2, mTextPaint);
        }
    }

    private void drawAxis(Canvas canvas){
        // draw x axis
        canvas.drawLine(PADDING, viewHeight -  PADDING, viewWidth - PADDING, viewHeight - PADDING, mAxisPaint);

        // draw y axis
        canvas.drawLine(PADDING, viewHeight -  PADDING, PADDING, PADDING, mAxisPaint);

        Path arrow = createArrow(ARROW_SIZE);
        canvas.save();
        canvas.translate(viewWidth - PADDING, viewHeight -  PADDING);
        canvas.rotate(90);
        canvas.drawPath(arrow, mArrowPaint);
        canvas.restore();
        canvas.save();
        canvas.translate(PADDING, PADDING);
        canvas.drawPath(arrow, mArrowPaint);
        canvas.restore();

    }

    private Path createArrow(float size){
        Path path = new Path();
        path.moveTo(-size/2, size/2);
        path.lineTo(0, -size/2);
        path.lineTo(size/2, size/2);
        path.close();
        return  path;
    }

    private class PlotPolyline {

        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] mX;
        private float[] mY;
        private String mLegend;
        private int mColor;

        public PlotPolyline(String name, int color, float strokewidth ,float[] inputX, float[] inputY){
            mLegend = name;
            mColor = color;
            mX = inputX;
            mY = inputY;
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokewidth);
        }

        public void draw(Canvas canvas, float wFactor, float hFactor){
            Path path = new Path();
            path.moveTo(mX[0] * wFactor + PADDING, -mY[0] * hFactor + viewHeight - PADDING);
            for(int i = 1; i < mX.length; i++){
                path.lineTo(mX[i] * wFactor + PADDING, -mY[i] * hFactor + viewHeight - PADDING);
            }
            canvas.drawPath(path, mPaint);
        }
    }

    private class PlotPoint{

        private static final float STROKE_WIDTH = 3;
        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private float mX;
        private float mY;
        private float mRadius = 10;
        private String mLegend;
        private int mColor;

        public PlotPoint(String name, int color, float radius ,float inputX, float inputY){
            mLegend = name;
            mColor = color;
            mX = inputX;
            mY = inputY;
            mRadius = radius;
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(STROKE_WIDTH);
        }

        public void draw(Canvas canvas, float wFactor, float hFactor){
            canvas.drawCircle(mX * wFactor + PADDING, -mY * hFactor + viewHeight - PADDING, mRadius,  mPaint);
        }
    }
}
