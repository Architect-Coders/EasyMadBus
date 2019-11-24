package com.developer.ivan.easymadbus.core

sealed class ScreenState<out T>
{
    object Loading : ScreenState<Nothing>()
    class Render<T>(state: T) : ScreenState<T>()
}