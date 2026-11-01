package com.example.campusledger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import java.util.*;

/** 历史页：用 SimpleAdapter 将数据集合绑定到 ListView，并计算合计。 */
public class HistoryActivity extends Activity {
    private ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        MainActivity.showIdentity(this);
        findViewById(R.id.btnAdd).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次回到当前页面重新加载，确保显示最新数据。
        try {
            entries = new RecordStore(this).load();
        } catch (IllegalStateException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            entries = new ArrayList<>();
        }
        Collections.reverse(entries); // 新记录排在前面。
        long income = 0, expense = 0;
        ArrayList<Map<String, String>> rows = new ArrayList<>();
        for (Entry entry : entries) {
            boolean isIncome = "收入".equals(entry.type);
            if (isIncome) income += entry.cents;
            else expense += entry.cents;
            Map<String, String> row = new HashMap<>();
            row.put("title", entry.category + "  " + (isIncome ? "+" : "-")
                    + Money.format(entry.cents) + " 元" + (entry.important ? "  [重要]" : ""));
            row.put("subtitle", entry.time + (entry.note.isEmpty() ? "" : "  ·  " + entry.note));
            rows.add(row);
        }
        ((TextView) findViewById(R.id.tvSummary)).setText("共 " + entries.size() + " 笔记录"
                + "\n收入：" + Money.format(income) + " 元    支出：" + Money.format(expense)
                + " 元\n结余：" + Money.format(income - expense) + " 元");
        ListView list = findViewById(R.id.lvRecords);
        list.setEmptyView(findViewById(R.id.tvEmpty));
        SimpleAdapter adapter = new SimpleAdapter(this, rows, R.layout.row_record,
                new String[]{"title", "subtitle"}, new int[]{R.id.rowTitle, R.id.rowSubtitle});
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            entries.get(position).putInto(intent);
            intent.putExtra("readOnly", true); // 查看旧记录时隐藏保存按钮。
            startActivity(intent);
        });
    }
}
