package com.code.pokedex.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaughtViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is caught Fragment"
    }
    val text: LiveData<String> = _text
}