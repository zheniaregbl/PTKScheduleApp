package com.syndicate.ptkscheduleapp.view_model.group_selection_view_model

sealed interface GroupSelectionEvent {

    data object FillListGroup: GroupSelectionEvent
}