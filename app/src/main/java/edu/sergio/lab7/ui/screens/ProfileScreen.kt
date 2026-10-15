package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import edu.sergio.lab7.R

@Composable
fun ProfileScreen(userName: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            modifier = Modifier.size(128.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_person),
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier.padding(28.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(Modifier.height(28.dp))
        Text(userName, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(stringResource(R.string.student_id), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(28.dp))
        OutlinedButton(onClick = onLogout) { Text(stringResource(R.string.logout)) }
    }
}
