# SlidingBack
### 作用
手势滑动，返回上一个activity/fragment。
### 截图
![Image](https://github.com/Developmc/SlidingBack/blob/master/app/src/main/res/drawable/show.gif) 

### 如何使用
1.在activity/fragmentUI根布局使用SlidingLayout，如下：
``` groovy
<?xml version="1.0" encoding="UTF-8"?>
<com.asiabasehk.cgg.office.slidingtest.view.SlidingLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/sildingFinishLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#556677" >
    <ListView
        android:id="@+id/listView"
        android:cacheColorHint="@android:color/transparent"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
    </ListView>
</com.asiabasehk.cgg.office.slidingtest.view.SlidingLayout>
```
2.在activity中，获取SlidingLayout实例，监听右侧滑动事件即可：
``` groovy
slidingLayout.setOnSlidingListener(new SlidingLayout.OnSlidingListener() {
            @Override
            public void onSliding() {
                XXXActivity.finish();
            }
        });
``` 
3.注意事项：需要将对应的activity的背景设为透明。如可在主题中声明：
``` groovy
<item name="android:windowIsTranslucent">true</item>
<item name="android:windowBackground">@color/translucent_background</item>
```
