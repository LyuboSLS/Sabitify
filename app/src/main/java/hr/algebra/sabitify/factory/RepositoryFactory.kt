package hr.algebra.sabitify.factory

import android.content.Context
import hr.algebra.sabitify.dao.SabitifySqlHelper

fun getSabitifyRepository(context: Context?) = SabitifySqlHelper(context)

