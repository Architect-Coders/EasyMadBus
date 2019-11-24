package com.developer.ivan.core

sealed class ScreenState<out T>
{
    object Loading : ScreenState<Nothing>()
    class Render<T>(state: T) : ScreenState<T>()
}