package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.R

@Composable
fun LoginScreen(onStart: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.rick_morty_logo),
                        contentDescription = stringResource(R.string.logo_description),
                        modifier = Modifier.widthIn(max = 300.dp).fillMaxWidth().height(110.dp),
                    )
                    Button(
                        onClick = onStart,
                        modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth().heightIn(min = 48.dp),
                    ) { Text(stringResource(R.string.start)) }
                }
            }
            Text(
                text = stringResource(R.string.student_name),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.student_id),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
