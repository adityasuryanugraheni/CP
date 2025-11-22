package com.example.cp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TaskPref {

    private const val PREF_NAME = "task_pref"
    private const val KEY_TASK = "task_list"

    fun saveTasks(context: Context, list: List<TaskModel>) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()

        val json = Gson().toJson(list)
        editor.putString(KEY_TASK, json)
        editor.apply()
    }

    fun loadTasks(context: Context): MutableList<TaskModel> {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = pref.getString(KEY_TASK, null) ?: return mutableListOf()

        val type = object : TypeToken<MutableList<TaskModel>>() {}.type
        return Gson().fromJson(json, type)
    }
}
