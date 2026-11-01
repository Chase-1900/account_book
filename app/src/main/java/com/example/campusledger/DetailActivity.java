package com.example.campusledger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/** 详情页：同时用于保存前确认和查看已经保存的历史记录。 */
public class DetailActivity extends Activity {
    private boolean saved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        MainActivity.showIdentity(this);
        Entry entry = Entry.fromIntent(getIntent()); // 接收首页传过来的值。
        boolean readOnly = getIntent().getBooleanExtra("readOnly", false);
        saved = savedInstanceState != null && savedInstanceState.getBoolean("saved");
        TextView amount = findViewById(R.id.tvAmount);
        amount.setText(("收入".equals(entry.type) ? "+ ¥ " : "- ¥ ") + Money.format(entry.cents));
        TextView detail = findViewById(R.id.tvDetail);
        detail.setText("类型：" + entry.type + "\n分类：" + entry.category
                + "\n时间：" + entry.time + "\n重要记录：" + (entry.important ? "是" : "否")
                + "\n备注：" + (entry.note.isEmpty() ? "无" : entry.note));
        Button save = findViewById(R.id.btnSave);
        Button back = findViewById(R.id.btnBack);
        if (readOnly) {
            ((TextView) findViewById(R.id.tvTitle)).setText("记录详情");
            ((TextView) findViewById(R.id.tvHint)).setText("已保存的收支明细");
            save.setVisibility(android.view.View.GONE);
            back.setText("返回历史记录");
        }
        back.setOnClickListener(v -> finish()); // 关闭当前页，返回上一页。
        save.setEnabled(!saved);
        save.setOnClickListener(v -> {
            if (saved) return; // 防止快速连点导致重复保存。
            try {
                new RecordStore(this).add(entry);
                saved = true;
                save.setEnabled(false);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HistoryActivity.class));
                finish();
            } catch (IllegalStateException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("saved", saved);
        super.onSaveInstanceState(outState);
    }
}
