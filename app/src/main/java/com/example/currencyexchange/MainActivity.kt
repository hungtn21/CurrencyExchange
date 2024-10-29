package com.example.currencyexchange

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.widget.addTextChangedListener
import com.example.currencyexchange.ui.theme.CurrencyExchangeTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khai báo các EditText và Spinner
        val etSourceAmount = findViewById<EditText>(R.id.etSourceAmount)
        val etDestinationAmount = findViewById<EditText>(R.id.etDestinationAmount)
        val spinnerSourceCurrency = findViewById<Spinner>(R.id.spinnerSourceCurrency)
        val spinnerDestinationCurrency = findViewById<Spinner>(R.id.spinnerDestinationCurrency)

        // Khởi tạo danh sách và adapter cho spinner
        val currencyList = listOf("USD", "EUR", "JPY", "VND")
        val sourceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSourceCurrency.adapter = sourceAdapter

        val destinationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDestinationCurrency.adapter = destinationAdapter

        // Thiết lập listener cho EditText và Spinner để gọi hàm convertCurrency khi thay đổi
        etSourceAmount.addTextChangedListener {
            convertCurrency()
        }

        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerDestinationCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        etSourceAmount.setOnClickListener {
            etSourceAmount.isEnabled = true
            etDestinationAmount.isEnabled = false
        }

        etDestinationAmount.setOnClickListener {
            etSourceAmount.isEnabled = false
            etDestinationAmount.isEnabled = true
        }


    }

    // Hàm chuyển đổi tiền tệ
    private fun convertCurrency() {
        val etSourceAmount = findViewById<EditText>(R.id.etSourceAmount)
        val etDestinationAmount = findViewById<EditText>(R.id.etDestinationAmount)
        val spinnerSourceCurrency = findViewById<Spinner>(R.id.spinnerSourceCurrency)
        val spinnerDestinationCurrency = findViewById<Spinner>(R.id.spinnerDestinationCurrency)
        val amount = etSourceAmount.text.toString().toDoubleOrNull() ?: 0.0
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val destinationCurrency = spinnerDestinationCurrency.selectedItem.toString()

        // Tỷ giá mẫu, trong thực tế bạn có thể lấy từ một API
        val conversionRates = mapOf(
            "USD" to mapOf("USD" to 1.0, "EUR" to 0.85, "JPY" to 110.0, "VND" to 23000.0),
            "EUR" to mapOf("USD" to 1.18, "EUR" to 1.0, "JPY" to 129.0, "VND" to 27000.0),
            "JPY" to mapOf("USD" to 0.0091, "EUR" to 0.0077, "JPY" to 1.0, "VND" to 210.0),
            "VND" to mapOf("USD" to 0.000043, "EUR" to 0.000037, "JPY" to 0.0048, "VND" to 1.0)
        )

        val rate = conversionRates[sourceCurrency]?.get(destinationCurrency) ?: 1.0
        val convertedAmount = amount * rate

        etDestinationAmount.setText(convertedAmount.toString())
    }


}
