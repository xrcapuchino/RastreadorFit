package com.example.rastreadorfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar); // Asegúrate de tener un Toolbar con id 'toolbar' en activity_main.xml
        setSupportActionBar(toolbar);

        // 2. Configurar Drawer (Menú Lateral)
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 3. Cargar fragmento inicial (Home) si es la primera vez
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new Home())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    // --- Manejo de clics en el menú ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new Home();
        } else if (id == R.id.nav_nueva) {
            selectedFragment = new NuevaComida();
        } else if (id == R.id.nav_info) {
            selectedFragment = new Informacion(); // Asegúrate de tener la clase Informacion.java (aunque sea vacía)
        } else if (id == R.id.nav_salir) {
            // Lógica de cerrar sesión (puedes crear un fragmento o cerrar la activity)
            Toast.makeText(this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la App
        }

        // Cambiar fragmento si se seleccionó uno
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, selectedFragment)
                    .addToBackStack(null) // Para poder volver atrás con el botón físico
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}