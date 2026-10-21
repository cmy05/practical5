package com.example.practicalfive;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main screen of the Chinese Municipalities application.
 *
 * Shows the title, a map of China and an input field. When the user presses
 * the Go action (or the Enter key), the input is validated with
 * {@link TextProcessor}. Valid names are passed to the {@link Municipality}
 * activity through an explicit Intent; invalid input stays on this screen
 * and shows an error.
 */
public class MainActivity extends AppCompatActivity {

    /** The EditText used to type the municipality name. */
    private EditText editTextMunicipality;

    /** Shared validation helper - validation logic is not duplicated. */
    private final TextProcessor textProcessor = new TextProcessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMunicipality = findViewById(R.id.editTextMunicipality);

        // Pressing the Go/Done action on the keyboard submits the input.
        editTextMunicipality.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO
                                || actionId == EditorInfo.IME_ACTION_DONE
                                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                                && event.getAction() == KeyEvent.ACTION_DOWN)) {
                            openMunicipality();
                            return true;
                        }
                        return false;
                    }
                });
    }

    /**
     * Reads the user input, validates it and either shows an error on the
     * main screen or starts the {@link Municipality} activity.
     */
    private void openMunicipality() {
        String input = editTextMunicipality.getText().toString();

        // Distinguish the two error messages required by the test plan.
        if (input.trim().isEmpty()) {
            editTextMunicipality.setError(getString(R.string.error_empty_input));
            return;
        }

        String validated = textProcessor.processMunicipality(input);
        if (validated == null) {
            // Unsupported name such as "Guangzhou".
            editTextMunicipality.setError(getString(R.string.error_invalid_municipality));
            return;
        }

        // Explicit Intent: navigate to the Municipality activity and pass
        // the normalised name as an extra.
        Intent intent = new Intent(MainActivity.this, Municipality.class);
        intent.putExtra(Municipality.EXTRA_MUNICIPALITY, validated);
        startActivity(intent);
    }
}
