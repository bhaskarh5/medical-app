package com.emergency.medical;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private MaterialButton btnFilter;
    private TextInputEditText etSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnFilter = findViewById(R.id.btnFilter);
        etSearch = findViewById(R.id.etSearch);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFilter.setOnClickListener(v ->
                Toast.makeText(this,
                        R.string.history_filter_placeholder,
                        Toast.LENGTH_SHORT).show());

        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                Toast.makeText(this,
                        R.string.history_search_placeholder,
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
