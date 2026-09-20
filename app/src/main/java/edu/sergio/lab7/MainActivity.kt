package edu.sergio.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.sergio.lab7.navigation.LabNavigation
import edu.sergio.lab7.ui.theme.LabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { LabTheme { LabNavigation(onExit = { finishAndRemoveTask() }) } }
    }
}
