package com.syndicate.ptkscheduleapp.ui.bottom_navigation_bar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@Composable
fun BottomMenu(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    currentRoute: String? = "",
    panelState: MutableState<PanelState> = mutableStateOf(PanelState.WeekPanel),
    selectedItemIndex: MutableState<Int> = mutableIntStateOf(0),
    isDarkTheme: Boolean = false
) {

    LaunchedEffect(currentRoute) {
        if (currentRoute == ScreenRoute.ScheduleScreen.route)
            selectedItemIndex.value = 0
    }

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

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 4.dp,
                    bottom = 4.dp
                )
                .background(
                    color = Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, bottomNavItem ->

                    BottomMenuItem(
                        item = bottomNavItem,
                        isDarkTheme = isDarkTheme,
                        isSelected = selectedItemIndex.value == index,
                        onClick = { navItem ->
                            if (selectedItemIndex.value != index) {

                                selectedItemIndex.value = index

                                if (bottomNavItem.name == "setting")
                                    panelState.value = PanelState.WeekPanel

                                navController.navigate(navItem.route) {
                                    popUpTo(0)
                                }
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
}

@Composable
fun BottomMenuItem(
    item: BottomNavItem,
    isDarkTheme: Boolean = false,
    isSelected: Boolean = false,
    onClick: (BottomNavItem) -> Unit
) {

    val buttonColor by animateColorAsState(
        targetValue = if (isSelected) if (isDarkTheme) InactiveNavItemColor else ActiveNavItemColor
        else if (isDarkTheme) ActiveNavItemColor else InactiveNavItemColor,
        label = "",
        animationSpec = tween(200, easing = LinearEasing)
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .clickable { onClick(item) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(35.dp),
            imageVector = item.icon,
            contentDescription = null,
            tint = buttonColor
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