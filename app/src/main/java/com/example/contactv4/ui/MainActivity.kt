package com.example.contactv4.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactv4.R
import com.example.contactv4.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    ListeContactsFragment()
                )
                .commit()
        }
    }

    fun ouvrirParametres() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ParametresFragment()
            )
            .addToBackStack(null)
            .commit()
    }
}