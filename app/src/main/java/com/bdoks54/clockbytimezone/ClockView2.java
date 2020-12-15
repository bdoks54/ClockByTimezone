package com.bdoks54.clockbytimezone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Message;
import android.view.View;
import android.os.Handler;

import java.util.Calendar;
import java.util.TimeZone;

public class ClockView2 extends View {
    int hour;
    int minute;
    int second;
    int width, height;
    int selectw;
    int center;

    public ClockView2(Context context) {
        super(context);
        refreshView();
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        setBackgroundColor(Color.WHITE);
        width=getWidth();
        height=getHeight();
        selectw=Math.min(width,height);
        center=selectw/2;
        //바깥 원
        Paint circleOut = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleOut.setColor(Color.DKGRAY);
        Paint circleIn = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleIn.setColor(Color.LTGRAY);
        //시계의 중앙 (center,center-10), 반지름(center-20)
        canvas.drawCircle(center, center-10, center-20, circleOut);
        canvas.drawCircle(center, center-10, center-50, circleIn);

        //각도 변경
        Matrix mt = new Matrix();
        //초침
        Path secondPin = new Path();
        secondPin.moveTo(center,center-10);
        secondPin.lineTo(center,60);
        mt.setRotate(6.0f * second, center, center-10); //초침 각도 변경
        secondPin.transform(mt);    //이동
        Paint secondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPaint.setColor(Color.GREEN);
        secondPaint.setStyle(Paint.Style.STROKE);
        secondPaint.setStrokeWidth(3);
        //분침
        Path minitePin = new Path();
        minitePin.moveTo(center, center-10);
        minitePin.lineTo(center,90);
        mt.setRotate(6.0f*minute+0.1f * second, center, center-10);
        minitePin.transform(mt);
        Paint minitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minitePaint.setColor(Color.BLUE);
        minitePaint.setStyle(Paint.Style.STROKE);
        minitePaint.setStrokeWidth(8);
        //시침
        Path hourPin = new Path();
        hourPin.moveTo(center, center-10);
        hourPin.lineTo(center,120);
        mt.setRotate(30.0f * (hour % 12) + 0.5f * minute, center, center-10); //시침 각도 변경
        hourPin.transform(mt);
        Paint hourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourPaint.setColor(Color.RED);
        hourPaint.setStyle(Paint.Style.STROKE);
        hourPaint.setStrokeWidth(15);
        //시계침을 그리기
        canvas.drawPath(secondPin, secondPaint);
        canvas.drawPath(minitePin, minitePaint);
        canvas.drawPath(hourPin, hourPaint);
        //텍스트 시간
        Paint degitalClockPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        degitalClockPaint.setColor(Color.BLACK);
        degitalClockPaint.setStrokeWidth(10);
        degitalClockPaint.setTextSize(selectw/10);
        canvas.drawText(hour + " : " + minute + " : " + second,
                selectw/3, selectw*2/3, degitalClockPaint);
    }
    public void clockCalc(){
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        Calendar cal=Calendar.getInstance();
        cal.setTimeZone(tz);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        invalidate();   //onDraw()자동호출
    }

    public void refreshView() {
        refreshViewHandler.sendEmptyMessageDelayed(0,1000); //1초 후 handleMessage 호출
        clockCalc();    //시간 구하고 시계 그리기
    }

    private RefreshViewHandler refreshViewHandler = new RefreshViewHandler();
    class RefreshViewHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            refreshView();
        }
    }
}
