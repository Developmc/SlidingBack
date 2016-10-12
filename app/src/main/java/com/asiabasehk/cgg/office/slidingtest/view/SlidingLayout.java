package com.asiabasehk.cgg.office.slidingtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**自定义可以滑动的RelativeLayout,类似IOS滑动删除页面效果
 * Created by clement on 16/10/11.
 */

public class SlidingLayout extends RelativeLayout {

    /**
     * SlidingLayout布局的父布局
     */
    private ViewGroup mParentView ;
    //滑动的最小距离
    private int mTouchSlop;
    //按下点的X坐标
    private int downX;
    //按下点的Y坐标
    private int downY;
    //临时的X坐标
    private int tempX;
    //滑动类
    private Scroller mScroller ;
    //SlidingLayout的宽度
    private int viewWidth;
    //是否正在滑动
    private boolean isSliding;
    //滑动监听实例
    private OnSlidingListener onSlidingListener;
    //是否已经完成
    private boolean isFinish ;
    //检测滑动速度
    private VelocityTracker mVelocityTracker;
    //最大速度
    private int mMaxVelocity;
    private int currentVelocity;
    //预定的滑动速度,超过这个值,就认为滑动生效
    private final int THRESHOLD_VELOCITY=300 ;
    //限定点击滑动的点击区域,在区域内才响应
    private final int THRESHOLD_X = 200 ;
    //按下点的ID(用于滑动速度检测)
    private int downPointerId;
    public SlidingLayout(Context context) {
        this(context,null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化参数
        init(context);
    }

    /**
     * 初始化参数
     */
    private void init(Context context){
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //初始化滑动类
        mScroller = new Scroller(context);
        //获取默认的最大滑动速度
        mMaxVelocity = ViewConfiguration.get(context).getMaximumFlingVelocity();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            //获取SlidingLayout所在布局的父布局
            mParentView = (ViewGroup) this.getParent();
            //获取该view的宽度
            viewWidth = this.getWidth();
        }
    }

    /**设置监听器
     * @param onSlidingListener
     */
    public void setOnSlidingListener(OnSlidingListener onSlidingListener){
        this.onSlidingListener = onSlidingListener ;
    }

    /**
     * 距离超过一半,向右滚动出界面
     */
    private void scrollRight(){
        final int delta = viewWidth+mParentView.getScrollX();
        if(mScroller!=null){
            //滚动界面
            mScroller.startScroll(mParentView.getScrollX(),0,-delta+1,0,Math.abs(delta));
            //更新界面
            postInvalidate();
        }
        isFinish = true;
    }
    /**
     * 速度较快,向右滚动出界面,固定划出时间是200
     */
    private void scrollRightByVelocity(){
        final int delta = viewWidth+mParentView.getScrollX();
        if(mScroller!=null){
            //滚动界面
            mScroller.startScroll(mParentView.getScrollX(),0,-delta+1,0,200);
            //更新界面
            postInvalidate();
        }
        isFinish = true;
    }

    /**
     * 回滚到起始位置
     */
    private void scrollOrigin(){
        int delta = mParentView.getScrollX();
        if(mScroller!=null){
            mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0,
                    Math.abs(delta));
            postInvalidate();
        }
        isFinish = false;
    }

    /**事件拦截操作
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) event.getRawX();
                downY = (int) event.getRawY();
                Log.d("downX: ",""+downX);
                break;
            case MotionEvent.ACTION_MOVE:
                //限定滑动关闭的点击区域(左侧THRESHOLD_X像素的位置)
                if(downX<=THRESHOLD_X){
                    int moveX = (int) event.getRawX();
                    // 满足此条件(即发生水平滑动)屏蔽SlidingLayout里面子类的touch事件
                    if (Math.abs(moveX-downX) > mTouchSlop && Math.abs((int) event.getRawY() - downY)<mTouchSlop) {
                        //拦截事件
                        return true;
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //不在滑动范围的滑动,无效
        if(downX>THRESHOLD_X){
            return super.onTouchEvent(event);
        }
        //添加tracker,用于检测滑动速度
        initVelocityTracker(event);
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获得用于测量速度的点的ID(触摸的点可能有多个,这里取第一个)
                downPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int)event.getRawX();
                int deltaX = tempX - moveX ;
                //更新tempX的值
                tempX = moveX ;
                //判断是否发生了水平方向的滑动,mTouchSlop默认值是8
                if(Math.abs(moveX-downX)>mTouchSlop && Math.abs((int)event.getRawY()-downY)<mTouchSlop){
                    isSliding = true ;
                }
                //如果正在向右滑动
                if(moveX-downX>=0 && isSliding){
                    //求伪瞬时速度
                    mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    //移动mParentView
                    mParentView.scrollBy(deltaX,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false ;
                //当前X方向上滑动的速度
                currentVelocity = (int)Math.abs(mVelocityTracker.getXVelocity(downPointerId)) ;
                //先判断速度,如果速度很快,大于1000,认为动作完成
                if(currentVelocity>THRESHOLD_VELOCITY){
                    scrollRightByVelocity();
                }
                //速度较慢时,通过滑动距离判断
                else{
                    //滑动距离超过一半
                    if(mParentView.getScrollX()<= -viewWidth/2){
                        scrollRight();
                    }
                    else{
                        scrollOrigin();
                    }
                }
                //释放触摸速度检测对象
                releaseVelocityTracker();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 调用startScroll的时候scroller.computeScrollOffset()返回true
        if(mScroller.computeScrollOffset()){
            mParentView.scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
            //滑动成功结束后回调
            if(mScroller.isFinished()){
                if(onSlidingListener!=null&&isFinish){
                    onSlidingListener.onSliding();
                }
            }
        }
    }

    /**添加tracker,用于检测滑动速度
     * @param event
     */
    private void initVelocityTracker(MotionEvent event){
        if(mVelocityTracker==null){
            //获取VelocityTracker类的实例对象
            mVelocityTracker = VelocityTracker.obtain();
        }
        //在onTouchEvent回调函数中,使用addMovement(MotionEvent)函数将当前的移动事件传递给VelocityTracker对象
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放对象
     */
    private void releaseVelocityTracker() {
        if(null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
    /**
     * 滑动监听接口
     */
    public interface OnSlidingListener{
        void onSliding();
    }
}
