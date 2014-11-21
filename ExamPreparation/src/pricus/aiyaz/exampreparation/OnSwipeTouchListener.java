package pricus.aiyaz.exampreparation;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeTouchListener implements OnTouchListener {
	
	public int x,y;
	int flag=0;
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight(int x, int y) {
    	
    }

    public boolean onTouch(View v, MotionEvent event) {
    	
    	
    	if(flag==0)
    	{
    		x=(int) event.getX();
    		y=(int) event.getY();
    		flag=1;
    	}
    	
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                {  	System.out.println("x:---------"+x+" y:-------------"+y);
                    onSwipeRight(x,y);
                    flag=0;
                }
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }
}