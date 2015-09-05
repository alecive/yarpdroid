package example.yarpglass;

import android.content.Context;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public abstract class GlassGestureDetector extends GestureDetector {

	public GlassGestureDetector(Context context) {
		super(context);
		
		setBaseListener(new BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				switch (gesture) {
				case SWIPE_DOWN:
					onSwipeDown();
					break;
				case SWIPE_LEFT:
					onSwipeLeft();
					break;
				case SWIPE_RIGHT:
					onSwipeRight();
					break;
				case SWIPE_UP:
					onSwipeUp();
					break;
				case LONG_PRESS:
					onLongPress();
					break;
				case TAP:
					onTap();
					break;
				case THREE_LONG_PRESS:
					onThreeFingerLongPress();
					break;
				case TWO_LONG_PRESS:
					onTwoFingerLongPress();
					break;
				case TWO_SWIPE_DOWN:
					onTwoFingerSwipeDown();
					break;
				case TWO_SWIPE_LEFT:
					onTwoFingerSwipeLeft();
					break;
				case TWO_SWIPE_RIGHT:
					onTwoFingerSwipeRight();
					break;
				case TWO_SWIPE_UP:
					onTwoFingerSwipeUp();
					break;
				case TWO_TAP:
					onTwoFingerTap();
					break;
				case THREE_TAP:
					onThreeFingerTap();
					break;
				}
				return true;
			}
		});

		setFingerListener(new FingerListener() {
			@Override
			public void onFingerCountChanged(int previousCount, int currentCount) {
			}
		});

		setScrollListener(new ScrollListener() {
			@Override
			public boolean onScroll(float displacement, float delta, float velocity) {
				return true;
			}
		});		
	}

	protected abstract void onTap();

	protected abstract void onSwipeUp();
	
	protected abstract void onSwipeDown();
	
	protected abstract void onSwipeLeft();
	
	protected abstract void onSwipeRight();
	
	protected abstract void onLongPress();
		
	protected abstract void onTwoFingerTap();

	protected abstract void onTwoFingerSwipeUp();
	
	protected abstract void onTwoFingerSwipeDown();
	
	protected abstract void onTwoFingerSwipeLeft();
	
	protected abstract void onTwoFingerSwipeRight();
	
	protected abstract void onTwoFingerLongPress();
	
	protected abstract void onThreeFingerTap();

	protected abstract void onThreeFingerLongPress();

}
