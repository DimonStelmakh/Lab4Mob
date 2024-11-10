package com.example.lab4

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Button
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

        val inputAverageDayPower = findViewById<EditText>(R.id.averageDayPower)
        val inputForecastRootMeanSquareDeviation = findViewById<EditText>(R.id.forecastRootMeanSquareDeviation)
        val inputTargetForecastRootMeanSquareDeviation = findViewById<EditText>(R.id.targetForecastRootMeanSquareDeviation)
        val inputElectricityPrice = findViewById<EditText>(R.id.electricityPrice)

        val calculateButton = findViewById<Button>(R.id.calculateButton)

        val moneyBalance = findViewById<TextView>(R.id.moneyBalance)
        val newMoneyBalance = findViewById<TextView>(R.id.newMoneyBalance)

        calculateButton.setOnClickListener {
            try {
                val allowedMistakePercentage = 5

                val averageDayPower = inputAverageDayPower.text.toString().toDouble()
                val forecastRootMeanSquareDeviation = inputForecastRootMeanSquareDeviation.text.toString().toDouble()
                val targetForecastRootMeanSquareDeviation = inputTargetForecastRootMeanSquareDeviation.text.toString().toDouble()
                val electricityPrice = inputElectricityPrice.text.toString().toDouble()

                val shareWithoutImbalancesCalculated = round(calculateShareWithoutImbalance(averageDayPower, forecastRootMeanSquareDeviation, allowedMistakePercentage.toDouble()/100)*100)/100

                val profit = calculateElectricityValue(calculateElectricityQuantity(averageDayPower, shareWithoutImbalancesCalculated), electricityPrice)
                val loss = calculateElectricityValue(calculateElectricityQuantity(averageDayPower, (1-shareWithoutImbalancesCalculated)), electricityPrice)

                val initiateMoneyBalanceCalculated = profit - loss

                moneyBalance.text = "Баланс доходу/втрати, грн: %.0f".format(initiateMoneyBalanceCalculated)

                val newShareWithoutImbalancesCalculated = round(calculateShareWithoutImbalance(averageDayPower, targetForecastRootMeanSquareDeviation, allowedMistakePercentage.toDouble()/100)*100)/100

                val newProfit = calculateElectricityValue(calculateElectricityQuantity(averageDayPower, newShareWithoutImbalancesCalculated), electricityPrice)
                val newLoss = calculateElectricityValue(calculateElectricityQuantity(averageDayPower, (1-newShareWithoutImbalancesCalculated)), electricityPrice)

                val newMoneyBalanceCalculated = newProfit - newLoss

                newMoneyBalance.text = "Баланс доходу/втрати, грн: %.0f".format(newMoneyBalanceCalculated)


            } catch (e: Exception) {
                Toast.makeText(this, "Будь ласка, введіть правильні числові значення!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun calculatePd(p: Double, pC: Double, sigma1: Double): Double {
    val exponent = -((p - pC).pow(2)) / (2 * sigma1.pow(2))
    return (1 / (sigma1 * sqrt(2 * PI))) * exp(exponent)
}

fun calculateShareWithoutImbalance(pC: Double, sigma1: Double, delta: Double): Double {
    return trapezoidalIntegral(pC, sigma1, pC-pC*delta, pC+pC*delta, 1000)
}

fun trapezoidalIntegral(
    pC: Double,
    sigma1: Double,
    start: Double,
    end: Double,
    steps: Int
): Double {
    val stepSize = (end - start) / steps
    var integral = 0.0
    for (i in 0 until steps) {
        val p1 = start + i * stepSize
        val p2 = start + (i + 1) * stepSize
        val pd1 = calculatePd(p1, pC, sigma1)
        val pd2 = calculatePd(p2, pC, sigma1)
        integral += (pd1 + pd2) / 2 * stepSize
    }
    return integral
}

fun calculateElectricityQuantity(pC: Double, deltaW: Double): Double {
    return pC * 24 * deltaW
}

fun calculateElectricityValue(W: Double, cost: Double): Double {
    return W*1000 * cost
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