package com.example.cp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object NotesStorage {

    private const val PREF_NAME = "notes_pref"
    private const val KEY_NOTES = "notes_data"

    fun saveNotes(context: Context, notes: List<Note>) {
        val json = Gson().toJson(notes)
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().putString(KEY_NOTES, json).apply()
    }

    fun loadNotes(context: Context): MutableList<Note> {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = pref.getString(KEY_NOTES, null) ?: return mutableListOf()

        val type = object : TypeToken<MutableList<Note>>() {}.type
        return Gson().fromJson(json, type)
    }
}
