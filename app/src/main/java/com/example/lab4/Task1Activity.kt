package com.example.lab4

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab4.ui.theme.Lab4Theme

import kotlin.math.pow
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.math.PI
import kotlin.math.round


class Task1Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task1)

        val voltageSpinner = findViewById<Spinner>(R.id.enterpriseVoltage)
        val voltageOptions = arrayOf("6 кВт", "10 кВт")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, voltageOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        voltageSpinner.adapter = adapter

        val inputShortCircuitKiloAmperage = findViewById<EditText>(R.id.shortCircuitKiloAmperage)
        val inputFictitiousPowerOffTime = findViewById<EditText>(R.id.fictitiousPowerOffTime)
        val inputTransformerSubstationPower = findViewById<EditText>(R.id.transformerSubstationPower)
        val inputCalculatedLoad = findViewById<EditText>(R.id.calculatedLoad)
        val inputMaxLoadTime = findViewById<EditText>(R.id.maxLoadTime)

        val calculateButton = findViewById<Button>(R.id.calculateButton)

        val minimalCableSection = findViewById<TextView>(R.id.minimalCableSection)
        val cableSection = findViewById<TextView>(R.id.cableSection)

        calculateButton.setOnClickListener {
            try {
                val thermalCoefficient = 92

                val selectedVoltageText = voltageSpinner.selectedItem.toString()
                val selectedVoltage = selectedVoltageText.split(" ")[0].toDouble()

                val shortCircuitKiloAmperage = inputShortCircuitKiloAmperage.text.toString().toDouble()
                val fictitiousPowerOffTime = inputFictitiousPowerOffTime.text.toString().toDouble()
                val transformerSubstationPower = inputTransformerSubstationPower.text.toString().toDouble()
                val calculatedLoad = inputCalculatedLoad.text.toString().toDouble()
                val maxLoadTime = inputMaxLoadTime.text.toString().toInt()

                val calculatedAmperageForNormalRegime = calculateNormalRegimeAmperage(calculatedLoad, selectedVoltage)
                val calculatedAmperageForPostAccidentRegime = calculatePostAccidentRegimeAmperage(calculatedAmperageForNormalRegime)

                // відповідно до таблиці
                val economicalCurrentDensity = when {
                    maxLoadTime in 1001..3000 -> 1.6
                    maxLoadTime in 3001..5000 -> 1.4
                    maxLoadTime > 5000 -> 1.2
                    else -> 1.6
                }

                val calculatedEconomicSection = calculateEconomicSection(calculatedAmperageForNormalRegime, economicalCurrentDensity)
                val calculatedMinimalSection = calculateMinimalSection(shortCircuitKiloAmperage, fictitiousPowerOffTime, thermalCoefficient)

                val chosenStandardSection = chooseStandardSection(calculatedMinimalSection)

                minimalCableSection.text = "Мінімальний переріз кабеля, мм^2: %.2f".format(calculatedMinimalSection)
                cableSection.text = "Переріз кабеля, який варто обрати, мм^2: %.2f".format(chosenStandardSection)

            } catch (e: Exception) {
                Toast.makeText(this, "Будь ласка, введіть правильні числові значення!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun calculateNormalRegimeAmperage(sM: Double, uNom: Double): Double {
    return (sM / 2) / (sqrt(3.0)*uNom)
}

fun calculatePostAccidentRegimeAmperage(normalRegimeAmperage: Double): Double {
    return normalRegimeAmperage * 2
}

fun calculateEconomicSection(iM: Double, jEk: Double): Double {
    return iM / jEk
}

fun calculateMinimalSection(iK: Double, tF: Double, cT: Int): Double {
    return iK*1000 * sqrt(tF) / cT
}

fun chooseStandardSection(sMin: Double): Double {
    val standardSections = listOf(10.0, 16.0, 25.0, 35.0, 50.0, 70.0, 95.0, 120.0, 150.0, 185.0, 240.0)
    return standardSections.first { it >= sMin }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab4Theme {
        Greeting("Android")
    }
}