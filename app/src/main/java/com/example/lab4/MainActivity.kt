package com.example.lab4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainMenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val buttonCalculator1 = findViewById<Button>(R.id.buttonCalculator1)
        buttonCalculator1.setOnClickListener {
            val intent = Intent(this, Task1Activity::class.java)
            startActivity(intent)
        }
    }
}
