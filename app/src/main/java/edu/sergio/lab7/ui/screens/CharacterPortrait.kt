package edu.sergio.lab7.ui.screens

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import edu.sergio.lab7.R
import edu.sergio.lab7.data.Character

@Composable
fun CharacterPortrait(character: Character, modifier: Modifier = Modifier) {
    // Coil descarga por HTTPS y conserva las imágenes en caché.
    AsyncImage(
        model = character.image,
        contentDescription = stringResource(R.string.image_description, character.name),
        placeholder = painterResource(R.drawable.ic_person),
        error = painterResource(R.drawable.ic_person),
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(CircleShape),
    )
}
