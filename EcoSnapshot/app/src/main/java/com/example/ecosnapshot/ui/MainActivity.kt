package com.example.ecosnapshot.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ecosnapshot.data.repository.SpeciesRepository
import com.example.ecosnapshot.databinding.ActivityMainBinding

/**
 * Activity principal que aloja o NavHostFragment.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar o repositório de dados antes da UI
        SpeciesRepository.initialize(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar a Toolbar com o Navigation Component
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController

        binding.toolbar.setupWithNavController(navController)
    }
}
