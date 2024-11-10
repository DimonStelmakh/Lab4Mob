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


class Task3Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task3)

        val modeSpinner = findViewById<Spinner>(R.id.operatingMode)
        val modeOptions = arrayOf(
            "Нормальний режим",
            "Мінімальний режим",
            "Аварійний режим (не передбачений)"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, modeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeSpinner.adapter = adapter

        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resistanceR = findViewById<TextView>(R.id.resistanceR)
        val resistanceX = findViewById<TextView>(R.id.resistanceX)
        val resistanceZ = findViewById<TextView>(R.id.resistanceZ)
        val currentThreePhase = findViewById<TextView>(R.id.currentThreePhase)
        val currentTwoPhase = findViewById<TextView>(R.id.currentTwoPhase)

        calculateButton.setOnClickListener {
            try {
                when (modeSpinner.selectedItemPosition) {
                    0 -> { // нормальний режим
                        val rShN = 0.1
                        val xShN = 2.31
                        val rD = 7.91
                        val xD = 4.49

                        val r = rD + rShN
                        val x = xD + xShN
                        val z = sqrt(r * r + x * x)

                        val i3 = (11.0 * 1000) / (sqrt(3.0) * z)
                        val i2 = i3 * sqrt(3.0) / 2

                        displayResults(resistanceR, resistanceX, resistanceZ,
                            currentThreePhase, currentTwoPhase,
                            r, x, z, i3, i2)
                    }
                    1 -> { // мінімальний режим
                        val rShNMin = 0.31
                        val xShNMin = 2.69
                        val rD = 7.91
                        val xD = 4.49

                        val r = rD + rShNMin
                        val x = xD + xShNMin
                        val z = sqrt(r * r + x * x)

                        val i3 = (11.0 * 1000) / (sqrt(3.0) * z)
                        val i2 = i3 * sqrt(3.0) / 2

                        displayResults(resistanceR, resistanceX, resistanceZ,
                            currentThreePhase, currentTwoPhase,
                            r, x, z, i3, i2)
                    }
                    2 -> { // аварійний режим
                        Toast.makeText(this, "Аварійний режим не передбачений для цієї підстанції",
                            Toast.LENGTH_LONG).show()
                        clearResults(resistanceR, resistanceX, resistanceZ,
                            currentThreePhase, currentTwoPhase)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Помилка у розрахунках!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayResults(
        resistanceR: TextView,
        resistanceX: TextView,
        resistanceZ: TextView,
        currentThreePhase: TextView,
        currentTwoPhase: TextView,
        r: Double,
        x: Double,
        z: Double,
        i3: Double,
        i2: Double
    ) {
        resistanceR.text = "Активний опір R = %.2f Ом".format(r)
        resistanceX.text = "Реактивний опір X = %.2f Ом".format(x)
        resistanceZ.text = "Повний опір Z = %.2f Ом".format(z)
        currentThreePhase.text = "Струм трифазного КЗ = %.0f А".format(i3)
        currentTwoPhase.text = "Струм двофазного КЗ = %.0f А".format(i2)
    }

    private fun clearResults(
        resistanceR: TextView,
        resistanceX: TextView,
        resistanceZ: TextView,
        currentThreePhase: TextView,
        currentTwoPhase: TextView
    ) {
        resistanceR.text = ""
        resistanceX.text = ""
        resistanceZ.text = ""
        currentThreePhase.text = ""
        currentTwoPhase.text = ""
    }
}