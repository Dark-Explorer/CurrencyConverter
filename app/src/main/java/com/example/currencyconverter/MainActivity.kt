package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var firstAmount: EditText
    private lateinit var secondAmount: EditText
    private lateinit var firstSpinner: Spinner
    private lateinit var secondSpinner: Spinner
    private var isUpdating = false

    private val exchangeRates = mapOf(
        "United States - Dollar" to 1.0,
        "Europe - Euro" to 0.9262,
        "Great Britain - Pound" to 0.7713,
        "Japan - Yen" to 152.3,
        "Vietnam - Dong" to 25355.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstAmount = findViewById(R.id.firstAmount)
        secondAmount = findViewById(R.id.secondAmount)
        firstSpinner = findViewById(R.id.firstSpinner)
        secondSpinner = findViewById(R.id.secondSpinner)

        setupSpinners()
        setupEditTexts()
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            exchangeRates.keys.toList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        firstSpinner.adapter = adapter
        secondSpinner.adapter = adapter

        secondSpinner.setSelection(1)

        val firstSpinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isUpdating && !firstAmount.text.isNullOrEmpty()) {
                    convertFromFirst()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val secondSpinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isUpdating && !firstAmount.text.isNullOrEmpty()) {
                    convertFromSecond()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        firstSpinner.onItemSelectedListener = firstSpinnerListener
        secondSpinner.onItemSelectedListener = secondSpinnerListener
    }

    private fun setupEditTexts() {
        firstAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    convertFromFirst()
                }
            }
        })

        secondAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    convertFromSecond()
                }
            }
        })
    }

    private fun convertFromFirst() {
        try {
            isUpdating = true
            val fromCurrency = firstSpinner.selectedItem.toString()
            val toCurrency = secondSpinner.selectedItem.toString()

            val inputText = firstAmount.text.toString()
            if (inputText.isEmpty()) {
                secondAmount.setText("")
                isUpdating = false
                return
            }

            val amount = inputText.toDouble()
            val fromRate = exchangeRates[fromCurrency] ?: 1.0
            val toRate = exchangeRates[toCurrency] ?: 1.0
            val result = amount * (toRate / fromRate)

            val formatter = DecimalFormat("###0.00")
            secondAmount.setText(formatter.format(result))

        } catch (e: Exception) {
            secondAmount.setText("")
        } finally {
            isUpdating = false
        }
    }

    private fun convertFromSecond() {
        try {
            isUpdating = true
            val fromCurrency = secondSpinner.selectedItem.toString()
            val toCurrency = firstSpinner.selectedItem.toString()

            val inputText = secondAmount.text.toString()
            if (inputText.isEmpty()) {
                firstAmount.setText("")
                isUpdating = false
                return
            }

            val amount = inputText.toDouble()
            val fromRate = exchangeRates[fromCurrency] ?: 1.0
            val toRate = exchangeRates[toCurrency] ?: 1.0
            val result = amount * (toRate / fromRate)

            val formatter = DecimalFormat("###0.00")
            firstAmount.setText(formatter.format(result))

        } catch (e: Exception) {
            firstAmount.setText("")
        } finally {
            isUpdating = false
        }
    }
}