package com.example.cp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object NotesStorage {

    private const val PREFS_NAME = "notes_prefs"
    private const val KEY_NOTES = "notes_list"
    private const val KEY_PRIVATE_NOTES = "private_notes_list"

    fun saveNotes(context: Context, notes: List<Note>) {
        val json = Gson().toJson(notes)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_NOTES, json).apply()
    }

    fun loadNotes(context: Context): MutableList<Note> {
        val json = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_NOTES, null) ?: return mutableListOf()
        return Gson().fromJson(json, object : TypeToken<MutableList<Note>>(){}.type)
    }

    fun savePrivateNotes(context: Context, notes: List<Note>) {
        val json = Gson().toJson(notes)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_PRIVATE_NOTES, json).apply()
    }

    fun loadPrivateNotes(context: Context): MutableList<Note> {
        val json = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_PRIVATE_NOTES, null) ?: return mutableListOf()
        return Gson().fromJson(json, object : TypeToken<MutableList<Note>>(){}.type)
    }
}

