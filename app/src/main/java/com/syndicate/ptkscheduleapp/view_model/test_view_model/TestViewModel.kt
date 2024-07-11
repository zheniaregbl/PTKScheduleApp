package com.syndicate.ptkscheduleapp.view_model.test_view_model

import androidx.lifecycle.ViewModel
import com.syndicate.ptkscheduleapp.data.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: TestRepository
) : ViewModel() {

    val data = repository.fetchData()
}