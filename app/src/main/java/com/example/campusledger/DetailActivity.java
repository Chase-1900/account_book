package com.example.campusledger;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/** 第三步：接收首页通过 Intent 传来的数据，并显示在详情页面。 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        MainActivity.showIdentity(this);
        Entry entry = Entry.fromIntent(getIntent());
        ((TextView) findViewById(R.id.tvAmount)).setText(
                ("收入".equals(entry.type) ? "+ ¥ " : "- ¥ ") + Money.format(entry.cents));
        ((TextView) findViewById(R.id.tvDetail)).setText("类型：" + entry.type
                + "\n分类：" + entry.category + "\n时间：" + entry.time
                + "\n重要记录：" + (entry.important ? "是" : "否")
                + "\n备注：" + (entry.note.isEmpty() ? "无" : entry.note));
        // finish 关闭当前页，返回首页；此时尚未保存。
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
