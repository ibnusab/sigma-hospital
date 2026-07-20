package com.sigma.hospital;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sigma.hospital.ui.AiAssistantFragment;
import com.sigma.hospital.ui.AppointmentFragment;
import com.sigma.hospital.ui.DashboardFragment;
import com.sigma.hospital.ui.DoctorListFragment;
import com.sigma.hospital.ui.LoginFragment;
import com.sigma.hospital.ui.MedicalRecordFragment;
import com.sigma.hospital.ui.NotificationsFragment;
import com.sigma.hospital.ui.PatientListFragment;
import com.sigma.hospital.ui.ReportsFragment;
import com.sigma.hospital.ui.ScheduleFragment;
import com.sigma.hospital.ui.SettingsFragment;
import com.sigma.hospital.ui.SplashFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String userRole = "Admin";
    public static String userEmail = "admin@sigmahospital.com";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;

    public void updateSession(String role, String email) {
        userRole = role;
        userEmail = email;
        
        if (navigationView != null && navigationView.getHeaderCount() > 0) {
            View headerView = navigationView.getHeaderView(0);
            android.widget.TextView titleText = headerView.findViewById(R.id.nav_header_title);
            android.widget.TextView emailText = headerView.findViewById(R.id.nav_header_email);
            
            if (emailText != null) {
                emailText.setText(email);
            }
            if (titleText != null) {
                if ("Admin".equals(role)) {
                    titleText.setText("Sigma Administrator");
                } else if ("Doctor".equals(role)) {
                    titleText.setText("Sigma Doctor Portal");
                } else if ("Patient".equals(role)) {
                    titleText.setText("Sigma Patient Portal");
                } else {
                    titleText.setText("Sigma Hospital");
                }
            }
        }
        configureMenusForRole(role);
    }

    public void configureMenusForRole(String role) {
        if (bottomNavigationView != null) {
            android.view.Menu bottomMenu = bottomNavigationView.getMenu();
            if ("Admin".equals(role)) {
                bottomMenu.findItem(R.id.nav_dashboard).setVisible(true);
                bottomMenu.findItem(R.id.nav_doctors).setVisible(true);
                bottomMenu.findItem(R.id.nav_patients).setVisible(true);
                bottomMenu.findItem(R.id.nav_appointments).setVisible(true);
                bottomMenu.findItem(R.id.nav_ai_assistant).setVisible(true);
            } else if ("Doctor".equals(role)) {
                bottomMenu.findItem(R.id.nav_dashboard).setVisible(true);
                bottomMenu.findItem(R.id.nav_doctors).setVisible(true);
                bottomMenu.findItem(R.id.nav_patients).setVisible(true);
                bottomMenu.findItem(R.id.nav_appointments).setVisible(true);
                bottomMenu.findItem(R.id.nav_ai_assistant).setVisible(true);
            } else if ("Patient".equals(role)) {
                bottomMenu.findItem(R.id.nav_dashboard).setVisible(true);
                bottomMenu.findItem(R.id.nav_doctors).setVisible(true);
                bottomMenu.findItem(R.id.nav_patients).setVisible(false);
                bottomMenu.findItem(R.id.nav_appointments).setVisible(true);
                bottomMenu.findItem(R.id.nav_ai_assistant).setVisible(true);
            }
        }

        if (navigationView != null) {
            android.view.Menu drawerMenu = navigationView.getMenu();
            if ("Admin".equals(role)) {
                drawerMenu.findItem(R.id.drawer_dashboard).setVisible(true);
                drawerMenu.findItem(R.id.drawer_doctors).setVisible(true);
                drawerMenu.findItem(R.id.drawer_patients).setVisible(true);
                drawerMenu.findItem(R.id.drawer_appointments).setVisible(true);
                drawerMenu.findItem(R.id.drawer_schedules).setVisible(true);
                drawerMenu.findItem(R.id.drawer_records).setVisible(true);
                drawerMenu.findItem(R.id.drawer_ai).setVisible(true);
                drawerMenu.findItem(R.id.drawer_notifications).setVisible(true);
                drawerMenu.findItem(R.id.drawer_reports).setVisible(true);
                drawerMenu.findItem(R.id.drawer_settings).setVisible(true);
            } else if ("Doctor".equals(role)) {
                drawerMenu.findItem(R.id.drawer_dashboard).setVisible(true);
                drawerMenu.findItem(R.id.drawer_doctors).setVisible(true);
                drawerMenu.findItem(R.id.drawer_patients).setVisible(true);
                drawerMenu.findItem(R.id.drawer_appointments).setVisible(true);
                drawerMenu.findItem(R.id.drawer_schedules).setVisible(true);
                drawerMenu.findItem(R.id.drawer_records).setVisible(true);
                drawerMenu.findItem(R.id.drawer_ai).setVisible(true);
                drawerMenu.findItem(R.id.drawer_notifications).setVisible(true);
                drawerMenu.findItem(R.id.drawer_reports).setVisible(false);
                drawerMenu.findItem(R.id.drawer_settings).setVisible(true);
            } else if ("Patient".equals(role)) {
                drawerMenu.findItem(R.id.drawer_dashboard).setVisible(true);
                drawerMenu.findItem(R.id.drawer_doctors).setVisible(true);
                drawerMenu.findItem(R.id.drawer_patients).setVisible(false);
                drawerMenu.findItem(R.id.drawer_appointments).setVisible(true);
                drawerMenu.findItem(R.id.drawer_schedules).setVisible(false);
                drawerMenu.findItem(R.id.drawer_records).setVisible(false);
                drawerMenu.findItem(R.id.drawer_ai).setVisible(true);
                drawerMenu.findItem(R.id.drawer_notifications).setVisible(true);
                drawerMenu.findItem(R.id.drawer_reports).setVisible(false);
                drawerMenu.findItem(R.id.drawer_settings).setVisible(false);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.toolbar);

        // Setup custom Material Toolbar
        setSupportActionBar(toolbar);

        // Setup Drawer Toggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Register Navigation Listeners
        navigationView.setNavigationItemSelectedListener(this);
        setupBottomNavigation();

        // Load SplashFragment on initial launch
        if (savedInstanceState == null) {
            replaceFragment(new SplashFragment(), false);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.nav_dashboard) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_doctors) {
                selectedFragment = new DoctorListFragment();
            } else if (itemId == R.id.nav_patients) {
                selectedFragment = new PatientListFragment();
            } else if (itemId == R.id.nav_appointments) {
                selectedFragment = new AppointmentFragment();
            } else if (itemId == R.id.nav_ai_assistant) {
                selectedFragment = new AiAssistantFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment, false);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        boolean isBottomNavTab = false;

        if (id == R.id.drawer_dashboard) {
            bottomNavigationView.setSelectedItemId(R.id.nav_dashboard);
            isBottomNavTab = true;
        } else if (id == R.id.drawer_doctors) {
            bottomNavigationView.setSelectedItemId(R.id.nav_doctors);
            isBottomNavTab = true;
        } else if (id == R.id.drawer_patients) {
            bottomNavigationView.setSelectedItemId(R.id.nav_patients);
            isBottomNavTab = true;
        } else if (id == R.id.drawer_appointments) {
            bottomNavigationView.setSelectedItemId(R.id.nav_appointments);
            isBottomNavTab = true;
        } else if (id == R.id.drawer_schedules) {
            fragment = new ScheduleFragment();
        } else if (id == R.id.drawer_records) {
            fragment = new MedicalRecordFragment();
        } else if (id == R.id.drawer_ai) {
            bottomNavigationView.setSelectedItemId(R.id.nav_ai_assistant);
            isBottomNavTab = true;
        } else if (id == R.id.drawer_notifications) {
            fragment = new NotificationsFragment();
        } else if (id == R.id.drawer_reports) {
            fragment = new ReportsFragment();
        } else if (id == R.id.drawer_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.drawer_logout) {
            Toast.makeText(this, "Administrator session logged out securely.", Toast.LENGTH_SHORT).show();
            fragment = new LoginFragment();
            hideNavigationBars();
        }

        if (fragment != null && !isBottomNavTab) {
            replaceFragment(fragment, true);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // --- NAVIGATION MANAGEMENT CONTROLS ---

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        // Add subtle polished cross-fade animation transitions
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.fragment_container, fragment);
        
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void hideNavigationBars() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }
        // Lock sliding drawer so it can't be swiped open
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void showNavigationBars() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
        }
        // Unlock sliding drawer
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        configureMenusForRole(userRole);
    }

    public void updateActionBarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
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
