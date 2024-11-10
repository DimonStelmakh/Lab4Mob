package com.example.lab4

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity

import kotlin.math.sqrt


class Task2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task2)

        val voltageSpinner = findViewById<Spinner>(R.id.nominalVoltage)
        val voltageOptions = arrayOf("10.5 кВ")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, voltageOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        voltageSpinner.adapter = adapter

        val inputShortCircuitPower = findViewById<EditText>(R.id.shortCircuitPower)
        val inputTransformerPower = findViewById<EditText>(R.id.transformerPower)
        val inputTransformerVoltage = findViewById<EditText>(R.id.transformerVoltage)

        val calculateButton = findViewById<Button>(R.id.calculateButton)

        val systemResistance = findViewById<TextView>(R.id.systemResistance)
        val transformerResistance = findViewById<TextView>(R.id.transformerResistance)
        val totalResistance = findViewById<TextView>(R.id.totalResistance)
        val shortCircuitCurrent = findViewById<TextView>(R.id.shortCircuitCurrent)

        calculateButton.setOnClickListener {
            try {
                val selectedVoltageText = voltageSpinner.selectedItem.toString()
                val nominalVoltage = selectedVoltageText.split(" ")[0].toDouble()  // 10.5
                val shortCircuitPower = inputShortCircuitPower.text.toString().toDouble()  // 200
                val transformerPower = inputTransformerPower.text.toString().toDouble()  // 6.3
                val transformerVoltagePercent = inputTransformerVoltage.text.toString().toDouble()

                val xC = (nominalVoltage * nominalVoltage) / shortCircuitPower

                val xT = (transformerVoltagePercent * nominalVoltage * nominalVoltage) / (100 * transformerPower)

                val xSum = xC + xT

                val iP0 = nominalVoltage / (sqrt(3.0) * xSum)

                systemResistance.text = "Опір системи, Ом: %.2f".format(xC)
                transformerResistance.text = "Опір трансформатора, Ом: %.2f".format(xT)
                totalResistance.text = "Сумарний опір, Ом: %.2f".format(xSum)
                shortCircuitCurrent.text = "Початковий струм КЗ, кА: %.2f".format(iP0)

            } catch (e: Exception) {
                Toast.makeText(this, "Будь ласка, введіть правильні числові значення!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
