package com.example.campusledger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** 首页：读取表单，检查金额，再用 Intent 传给详情页。 */
public class MainActivity extends Activity {
    private EditText amountInput, noteInput;
    private RadioGroup typeGroup;
    private Spinner categorySpinner;
    private CheckBox importantBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showIdentity(this);
        amountInput = findViewById(R.id.etAmount);
        noteInput = findViewById(R.id.etNote);
        typeGroup = findViewById(R.id.rgType);
        categorySpinner = findViewById(R.id.spCategory);
        importantBox = findViewById(R.id.cbImportant);
        updateCategories();
        // RadioGroup 保证收入、支出只能选一个，选项变化时更新分类。
        typeGroup.setOnCheckedChangeListener((group, checkedId) -> updateCategories());
        findViewById(R.id.btnPreview).setOnClickListener(v -> preview());
        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        // 系统旋转屏幕后，Spinner 的位置需要在设置适配器后恢复。
        if (savedInstanceState != null) {
            typeGroup.check(savedInstanceState.getInt("typeId", R.id.rbExpense));
            updateCategories();
            categorySpinner.setSelection(savedInstanceState.getInt("categoryIndex", 0));
        }
    }

    private void updateCategories() {
        int arrayId = typeGroup.getCheckedRadioButtonId() == R.id.rbIncome
                ? R.array.income_categories : R.array.expense_categories;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void preview() {
        long cents;
        try {
            cents = Money.parseCents(amountInput.getText().toString().trim());
        } catch (IllegalArgumentException e) {
            amountInput.setError(e.getMessage());
            amountInput.requestFocus();
            return;
        }
        Entry entry = new Entry();
        entry.type = typeGroup.getCheckedRadioButtonId() == R.id.rbIncome ? "收入" : "支出";
        entry.category = categorySpinner.getSelectedItem().toString();
        entry.cents = cents;
        entry.note = noteInput.getText().toString().trim();
        entry.important = importantBox.isChecked();
        entry.time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date());
        Intent intent = new Intent(this, DetailActivity.class); // 显式 Intent 指定目标页面。
        entry.putInto(intent); // 把金额、分类、备注等放进 Intent。
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("typeId", typeGroup.getCheckedRadioButtonId());
        outState.putInt("categoryIndex", categorySpinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    // 显示固定的姓名、学号和班级。
    public static void showIdentity(Activity activity) {
        TextView view = activity.findViewById(R.id.tvIdentity);
        view.setText(activity.getString(R.string.identity_line,
                activity.getString(R.string.student_name),
                activity.getString(R.string.student_id),
                activity.getString(R.string.student_class)));
    }
}
