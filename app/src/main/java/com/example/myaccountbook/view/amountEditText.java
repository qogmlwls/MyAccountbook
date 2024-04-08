package com.example.myaccountbook.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.myaccountbook.RecordActivity;

import java.text.DecimalFormat;

public class amountEditText {

    public void setTextWatcher(EditText 금액입력창){
        amountEditText.NumberTextWatcher textWatcher = new amountEditText.NumberTextWatcher(금액입력창);
        금액입력창.addTextChangedListener(textWatcher);

    }

    public int 금액쉼표제거(EditText 금액입력창){

        String originalString = 금액입력창.getText().toString();

        String 금액쉼표제거 = originalString.replace(",", "");

        return Integer.parseInt(금액쉼표제거);

    }

    class NumberTextWatcher implements TextWatcher {
        private EditText editText;
        private DecimalFormat decimalFormat;
        private String current = "";

        public NumberTextWatcher(EditText editText) {
            this.editText = editText;
            this.decimalFormat = new DecimalFormat("#,###");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No implementation needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No implementation needed
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null && !editable.toString().equals(current)) {
                editText.removeTextChangedListener(this);

                String userInput = editable.toString().replace(",", "");
                if (!userInput.isEmpty()) {
                    try {
                        long parsed = Long.parseLong(userInput);
                        String formatted = decimalFormat.format(parsed);
                        current = formatted;
                        editText.setText(formatted);
                        editText.setSelection(formatted.length());
                    } catch (NumberFormatException ex) {
                        // Handle the exception as needed
                    }
                }

                editText.addTextChangedListener(this);
            }
        }
    }
}
