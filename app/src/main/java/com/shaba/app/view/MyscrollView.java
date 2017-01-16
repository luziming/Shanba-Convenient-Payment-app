package com.shaba.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyscrollView extends ScrollView  {
	 private ScrollViewListener scrollViewListener = null;
	private float xDistance;
	private float yDistance;
	private float xStart;
	private float yStart;
	private float xEnd;
	private float yEnd;  
	  
	    public MyscrollView(Context context) {  
	        super(context);  
	    }  
	  
	    public MyscrollView(Context context, AttributeSet attrs,  
	            int defStyle) {  
	        super(context, attrs, defStyle);  
	    }  
	  
	    public MyscrollView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	  
	    public void setScrollViewListener(ScrollViewListener scrollViewListener) {  
	        this.scrollViewListener = scrollViewListener;  
	    }  
	  
	    @Override  
	    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
	        super.onScrollChanged(x, y, oldx, oldy);  
	        if (scrollViewListener != null) {  
	            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
	        }  
	    }  
	    public interface ScrollViewListener {  
	    	  
	        void onScrollChanged(MyscrollView scrollView, int x, int y, int oldx, int oldy);  
	      
	    }  
	    
	    @Override  
	    public boolean onInterceptTouchEvent(MotionEvent ev) {  
	        // TODO Auto-generated method stub  
	        switch (ev.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	            xDistance = yDistance = 0f;  
	            xStart = ev.getX();  
	            yStart = ev.getY();  
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	            xEnd = ev.getX();  
	            yEnd = ev.getY();  
	            break;  
	        default:  
	            break;  
	        }  
	        xDistance = Math.abs(xEnd-xStart);  
	        yDistance = Math.abs(yEnd-yStart);  
	        if(xDistance>yDistance)  
	            return false;  
	        return super.onInterceptTouchEvent(ev);  
	    }  
	}  
