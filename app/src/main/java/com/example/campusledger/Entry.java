package com.example.campusledger;

import android.content.Intent;

/** 一条收支记录。字段少，直接使用普通 Java 类，方便理解。 */
public class Entry {
    public String type, category, note, time;
    public long cents;
    public boolean important;

    // 发送方用 putExtra 写入数据。每个键名要与接收方保持一致。
    public void putInto(Intent intent) {
        intent.putExtra("type", type);
        intent.putExtra("category", category);
        intent.putExtra("cents", cents);
        intent.putExtra("note", note);
        intent.putExtra("time", time);
        intent.putExtra("important", important);
    }

    // 接收方通过 getIntent 获取同一个 Intent 携带的数据。
    public static Entry fromIntent(Intent intent) {
        Entry entry = new Entry();
        entry.type = intent.getStringExtra("type");
        entry.category = intent.getStringExtra("category");
        entry.cents = intent.getLongExtra("cents", 0);
        entry.note = intent.getStringExtra("note");
        entry.time = intent.getStringExtra("time");
        entry.important = intent.getBooleanExtra("important", false);
        return entry;
    }
}
