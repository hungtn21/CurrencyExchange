package com.example.currencyexchange

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    private var isUpdatingSource = false
    private var isUpdatingDestination = false

    // Biến để lưu trạng thái đồng tiền nguồn và đích
    private var isSourceAmountActive = true // Mặc định là nguồn

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khai báo các EditText và Spinner
        val etSourceAmount = findViewById<EditText>(R.id.etSourceAmount)
        val etDestinationAmount = findViewById<EditText>(R.id.etDestinationAmount)
        val spinnerSourceCurrency = findViewById<Spinner>(R.id.spinnerSourceCurrency)
        val spinnerDestinationCurrency = findViewById<Spinner>(R.id.spinnerDestinationCurrency)

        // Khởi tạo danh sách và adapter cho spinner
        val currencyList = listOf("USD", "EUR", "JPY", "VND", "GBP") // Thêm GBP vào danh sách
        val sourceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSourceCurrency.adapter = sourceAdapter

        val destinationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDestinationCurrency.adapter = destinationAdapter

        etSourceAmount.addTextChangedListener {
            if (isSourceAmountActive && !isUpdatingSource) {
                isUpdatingSource = true
                convertCurrency(etSourceAmount, etDestinationAmount, spinnerSourceCurrency, spinnerDestinationCurrency)
                isUpdatingSource = false
            }
        }

        etDestinationAmount.addTextChangedListener {
            if (!isSourceAmountActive && !isUpdatingDestination) {
                isUpdatingDestination = true
                convertCurrency(etDestinationAmount, etSourceAmount, spinnerDestinationCurrency, spinnerSourceCurrency)
                isUpdatingDestination = false
            }
        }

        etSourceAmount.setOnClickListener {
            isSourceAmountActive = true
            setActiveFields(etSourceAmount, spinnerSourceCurrency, etDestinationAmount, spinnerDestinationCurrency)
        }

        etDestinationAmount.setOnClickListener {
            isSourceAmountActive = false
            setActiveFields(etDestinationAmount, spinnerDestinationCurrency, etSourceAmount, spinnerSourceCurrency)
        }

        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency(etSourceAmount, etDestinationAmount, spinnerSourceCurrency, spinnerDestinationCurrency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerDestinationCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertCurrency(etSourceAmount, etDestinationAmount, spinnerSourceCurrency, spinnerDestinationCurrency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun convertCurrency(
        sourceAmountField: EditText,
        destinationAmountField: EditText,
        sourceCurrencySpinner: Spinner,
        destinationCurrencySpinner: Spinner
    ) {
        val amount = sourceAmountField.text.toString().toDoubleOrNull() ?: 0.0
        val sourceCurrency = sourceCurrencySpinner.selectedItem.toString()
        val destinationCurrency = destinationCurrencySpinner.selectedItem.toString()

        val conversionRates = mapOf(
            "USD" to mapOf("USD" to 1.0, "EUR" to 0.85, "JPY" to 110.0, "VND" to 23000.0, "GBP" to 0.75),
            "EUR" to mapOf("USD" to 1.18, "EUR" to 1.0, "JPY" to 129.0, "VND" to 27000.0, "GBP" to 0.88),
            "JPY" to mapOf("USD" to 0.0091, "EUR" to 0.0077, "JPY" to 1.0, "VND" to 210.0, "GBP" to 0.0065),
            "VND" to mapOf("USD" to 0.000043, "EUR" to 0.000037, "JPY" to 0.0048, "VND" to 1.0, "GBP" to 0.000043),
            "GBP" to mapOf("USD" to 1.33, "EUR" to 1.14, "JPY" to 153.0, "VND" to 30000.0, "GBP" to 1.0)
        )

        val rate = conversionRates[sourceCurrency]?.get(destinationCurrency) ?: 1.0
        val convertedAmount = amount * rate

        destinationAmountField.setText(convertedAmount.toString())
    }

    // Hàm để thiết lập trạng thái cho các trường
    private fun setActiveFields(activeEditText: EditText, activeSpinner: Spinner, inactiveEditText: EditText, inactiveSpinner: Spinner) {

        activeEditText.isEnabled = true
        activeSpinner.isEnabled = true

        inactiveEditText.isEnabled = false
        inactiveSpinner.isEnabled = false

        inactiveEditText.text.clear()
    }
}
