# CountDownView -- 启动页倒计时

 - gradle

```
compile 'cn.lemon:countdownview:0.2.0'
```

## 使用
```xml
<cn.lemon.view.CountdownView
    android:id="@+id/countdown"
    android:layout_width="56dp"
    android:layout_height="56dp"
    app:text="跳过"
    app:text_color="@android:color/white"
    app:text_size="13dp"
    app:update_time="500"
    app:total_time="5000"
    app:bg_color="#248742"
    app:progress_width="2dp"
    app:progress_hint_color="#94595b59"/>
```

```java
mCountdownView = (CountdownView) findViewById(R.id.countdown);
mCountdownView.start();
mCountdownView.setOnFinishAction(new Action() {
    @Override
    public void onAction() {
        Toast.makeText(MainActivity.this,"结束",Toast.LENGTH_SHORT).show();
    }
});
mCountdownView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
    }
});
```

## demo

<image src="demo.png" width="320" heigh="564"/>
