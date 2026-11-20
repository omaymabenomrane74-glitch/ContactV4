package com.example.contactv4.utils

import android.content.Context
import android.graphics.Color
import androidx.preference.PreferenceManager
import com.example.contactv4.data.OrdreTri
class PreferencesManager(context: Context) {

    private val preferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun getReglages(): ReglagesContacts {

        val ordre = when (
            preferences.getString("ordre_tri", "nom")
        ) {
            "prenom" -> OrdreTri.PRENOM
            "favoris" -> OrdreTri.FAVORIS
            else -> OrdreTri.NOM
        }

        return ReglagesContacts(
            taillePolice = preferences.getFloat("taille_police", 16f),
            couleur = preferences.getInt(
                "couleur_preferee", Color.BLACK
            ),
            appelDepuisListe = preferences.getBoolean(
                "appel_depuis_liste", false
            ),
            ordreTri = ordre
        )
    }

    fun enregistrerReglages(reglages: ReglagesContacts) {

        preferences.edit()
            .putFloat("taille_police", reglages.taillePolice)
            .putInt("couleur_preferee", reglages.couleur)
            .putBoolean(
                "appel_depuis_liste",
                reglages.appelDepuisListe
            )
            .putString(
                "ordre_tri",
                reglages.ordreTri.toString().lowercase()
            )
            .apply()
    }
}
