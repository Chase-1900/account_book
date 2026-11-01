package com.example.campusledger;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

// 使用 SharedPreferences 保存收支记录。
public class RecordStore {
    private final SharedPreferences preferences;

    public RecordStore(Context context) {
        preferences = context.getSharedPreferences("ledger", Context.MODE_PRIVATE);
    }

    public ArrayList<Entry> load() {
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(preferences.getString("records", "[]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Entry entry = new Entry();
                entry.type = obj.getString("type");
                entry.category = obj.getString("category");
                entry.cents = obj.getLong("cents");
                entry.note = obj.getString("note");
                entry.time = obj.getString("time");
                entry.important = obj.getBoolean("important");
                entries.add(entry);
            }
        } catch (JSONException e) {
            // 读取失败时保留原数据并提示错误。
            throw new IllegalStateException("读取本地记录失败", e);
        }
        return entries;
    }

    public void add(Entry entry) {
        try {
            JSONArray array = new JSONArray(preferences.getString("records", "[]"));
            JSONObject obj = new JSONObject();
            obj.put("type", entry.type);
            obj.put("category", entry.category);
            obj.put("cents", entry.cents);
            obj.put("note", entry.note);
            obj.put("time", entry.time);
            obj.put("important", entry.important);
            array.put(obj);
            // apply 会先更新内存，再异步保存到应用私有目录。
            preferences.edit().putString("records", array.toString()).apply();
        } catch (JSONException e) {
            throw new IllegalStateException("保存本地记录失败", e);
        }
    }
}
