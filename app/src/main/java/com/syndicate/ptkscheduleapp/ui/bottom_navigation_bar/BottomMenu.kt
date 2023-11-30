package com.syndicate.ptkscheduleapp.ui.bottom_navigation_bar

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.syndicate.ptkscheduleapp.R
import com.syndicate.ptkscheduleapp.data.model.BottomNavItem
import com.syndicate.ptkscheduleapp.data.model.PanelState
import com.syndicate.ptkscheduleapp.navigation.ScreenRoute
import com.syndicate.ptkscheduleapp.ui.theme.ActiveNavItemColor
import com.syndicate.ptkscheduleapp.ui.theme.InactiveNavItemColor
import kotlinx.coroutines.delay

@Composable
fun BottomMenu(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    panelState: MutableState<PanelState> = mutableStateOf(PanelState.WeekPanel),
    currentRoute: String? = ScreenRoute.ScheduleScreen.route
) {

    var transition by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = transition) {
        if (!transition) {
            delay(200)

            transition = true
        }
    }

    val colorShadow by animateColorAsState(
        targetValue = if (panelState.value == PanelState.CalendarPanel)
            Color.Black.copy(alpha = 0.35f) else Color.Transparent,
        animationSpec = tween(300, easing = LinearOutSlowInEasing),
        label = ""
    )

    Log.d("checkTransition", transition.toString())

    val items = listOf(
        BottomNavItem(
            name = "schedule",
            route = ScreenRoute.ScheduleScreen.route,
            icon = ImageVector.vectorResource(id = R.drawable.svg_schedule)
        ),
        BottomNavItem(
            name = "setting",
            route = ScreenRoute.SettingScreen.route,
            icon = ImageVector.vectorResource(id = R.drawable.svg_setting)
        )
    )

    var selectedItemIndex by remember {
        mutableIntStateOf(
            if (!currentRoute.isNullOrEmpty() && currentRoute == ScreenRoute.SettingScreen.route)
                1
            else 0
        )
    }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 4.dp,
                    bottom = 4.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = selectedItemIndex,
                label = "",
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 400)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 400))
                }
            ) { itemIndex ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEachIndexed { index, bottomNavItem ->
                        BottomMenuItem(
                            item = bottomNavItem,
                            isSelected = itemIndex == index,
                            onClick = { navItem ->
                                if (selectedItemIndex != index && transition) {
                                    selectedItemIndex = index


                                    if (bottomNavItem.name == "setting")
                                        panelState.value = PanelState.WeekPanel

                                    navController.navigate(navItem.route) {
                                        popUpTo(0)
                                    }

                                    transition = false
                                }
                            }
                        )
                        if (index != items.lastIndex)
                            Spacer(
                                modifier = Modifier
                                    .width(100.dp)
                            )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                /*.background(
                    color = colorShadow
                )*/
        )
    }
}

@Composable
fun BottomMenuItem(
    item: BottomNavItem,
    isSelected: Boolean = false,
    onClick: (BottomNavItem) -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) { onClick(item) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(35.dp),
            imageVector = item.icon,
            contentDescription = null,
            tint = if (isSelected) ActiveNavItemColor else InactiveNavItemColor
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewBottomMenu() {
    BottomMenu()
}