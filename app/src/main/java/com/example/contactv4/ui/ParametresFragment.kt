package com.example.contactv4.ui
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.contactv4.R

class ParametresFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(
            R.xml.preferences,
            rootKey
        )
    }
}
