package com.example.campusledger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/** 第一步：先搭建一个能打开的首页，并显示固定身份。 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showIdentity(this);
        findViewById(R.id.btnStart).setOnClickListener(v ->
                Toast.makeText(this, "下一步将加入记账表单", Toast.LENGTH_SHORT).show());
    }

    public static void showIdentity(Activity activity) {
        TextView view = activity.findViewById(R.id.tvIdentity);
        view.setText(activity.getString(R.string.identity_line,
                activity.getString(R.string.student_name),
                activity.getString(R.string.student_id),
                activity.getString(R.string.student_class)));
    }
}
