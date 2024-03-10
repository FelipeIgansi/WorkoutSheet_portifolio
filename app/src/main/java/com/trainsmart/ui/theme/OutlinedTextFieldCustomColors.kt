package com.trainsmart.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun outlinedCustomColors ()= OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SystemRed,
    focusedLabelColor = Color.White,
    unfocusedBorderColor = Color.White,
    unfocusedLabelColor = Color.White,
    cursorColor = SystemRed,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White
)