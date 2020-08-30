package com.example.remote_assitant.gesture_controller;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import com.example.remote_assitant.connection.TcpClient;
import com.example.remote_assitant.main_application.GlobalRepository;

public class GestureController implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat mDetector;
    private TcpClient mTcpClient;

    private long mOldPosX = -1;
    private long mOldPosY = -1;

    private boolean mHasMoveAction = false;

    public GestureController(Context context, TcpClient client) {
        mDetector = new GestureDetectorCompat(context, this);
        mTcpClient = client;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        boolean isEventConsumed = false;

        long x = (long)event.getX();
        long y = (long)event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN: {

                mOldPosX = x;
                mOldPosY = y;

                isEventConsumed = this.mDetector.onTouchEvent(event);
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                mHasMoveAction = true;

                if(x == mOldPosX && y == mOldPosY) // don't DDOS the server
                    break;

                if(x < 0 && y < 0)
                    break;

                // Това е за да можем да изпратим число което да съответства на boolean
                // 0 - moveDown/Left, 1 - moveUp/Right, -1 Dont move
                short isUp = -1;
                short isRight = -1;

                long offsetX =  mOldPosX - x;
                long offsetY = mOldPosY - y;

                mOldPosX = x;
                mOldPosY = y;

                mTcpClient.sendMessage(GlobalRepository.Actions.getMouseMoveActionCommandText(offsetX, offsetY));

                isEventConsumed = true;

                break;
            }
            case MotionEvent.ACTION_UP: {
                isEventConsumed = !mHasMoveAction && this.mDetector.onTouchEvent(event);

                break;
            }
            default:
                isEventConsumed = this.mDetector.onTouchEvent(event);
                break;
        }
        if(!isEventConsumed)
            v.performClick();

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mTcpClient.sendMessage(GlobalRepository.Actions.getMouseClickActionCommandText());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mTcpClient.sendMessage(GlobalRepository.Actions.getMouseDoubleClickActionCommandText());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mTcpClient.sendMessage(GlobalRepository.Actions.getMouseRightClickActionCommandText());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

}
