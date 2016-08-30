#CountDownView -- 启动页倒计时

 - gradle

```
compile 'cn.lemon:countdownview:0.1.0'
```

##使用
```xml
<cn.lemon.view.CountdownView
    android:id="@+id/countdown"
    android:layout_width="50dp"
    android:layout_height="50dp" />
```

```
mCountdownView = (CountdownView) findViewById(R.id.countdown);
mCountdownView.setText("计时");
mCountdownView.setTime(5000);
mCountdownView.star();
mCountdownView.setEndAction(new Action() {
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

##demo

<image src="demo.png" width="320" heigh="564"/>