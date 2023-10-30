package com.syndicate.ptkscheduleapp.ui.screens.role_selection_screen.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.syndicate.ptkscheduleapp.R

@Composable
fun SelectionRoleSection(
    modifier: Modifier = Modifier,
    radioOptions: List<String> = listOf("Студент", "Преподаватель"),
    radioState: MutableState<String> = mutableStateOf("Студент")
) {
    Column(
        modifier = modifier
            .selectableGroup()
    ) {
        radioOptions.forEach { label ->
            Row(
                modifier = Modifier
                    .height(30.dp)
                    .selectable(
                        selected = (radioState.value == label),
                        onClick = { radioState.value = label },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Crossfade(
                    targetState = radioState.value,
                    animationSpec = tween(225),
                    label = ""
                ) { targetState ->
                    Image(
                        modifier = Modifier
                            .size(30.dp),
                        imageVector = ImageVector.vectorResource(
                            id = if (targetState == label)
                                R.drawable.svg_radio_checked else R.drawable.svg_radio_default
                        ),
                        contentDescription = null
                    )
                }
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewSelectionRoleSection() {
    SelectionRoleSection()
}