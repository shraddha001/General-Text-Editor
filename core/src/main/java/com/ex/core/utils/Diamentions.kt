package com.ex.core.utils

import android.content.Context
import android.view.View

fun View.dip(value: Int): Int = context.dip(value)

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()