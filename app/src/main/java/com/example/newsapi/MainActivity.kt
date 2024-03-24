package com.example.newsapi

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.databinding.ActivityMainBinding
import com.example.newsapi.utils.PreferenceManager
import com.example.newsapi.viewModels.NewsViewModel
import com.example.newsapi.ui.ArticlesFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private var isDarkTheme :Boolean = false

    /**
     * The [viewModel] we used so that we can maintain the single source of data all over the app
     * And this help to maintain the changes and state over the application.
     * */

    private val viewModel by lazy {
        ViewModelProvider(this)[NewsViewModel::class.java]
    }
    private val coroutines = CoroutineScope(Dispatchers.IO)

    override fun onApplyThemeResource(theme: Resources.Theme?, resid: Int, first: Boolean) {
        super.onApplyThemeResource(theme, resid, first)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * This [applyTheme] function helps us to apply the dark mode and light mode
         * through the switch button present in [drawerLayout]
         * We Have to apply [applyTheme] function before [onCreate] otherwise it won't work
         *
         * The texts color we puts default right now so it will work for both dark mode and light mode
         *
         * */
        applyTheme()
        super.onCreate(savedInstanceState)

        /**
         *
         * I have used [binding] for binding the data with the layouts
         * for using this binding feature we have to allow [ viewBinding = true ] in Build Script
         *
         * */

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         *  [requestNotificationPermissions] is the function that is taking the permission for the
         *  notification have to allowed by this app in there mobile phone because after android 12
         *  we have to take permission from user itself for posting notification to them
         * */

        requestNotificationPermissions()

        drawerLayout = binding.drawerLayout
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        /**
         * This [SwitchCompat] and [PreferenceManager] both are used to handle the dark mode and light mode
         * [PreferenceManager] helps us to store the choice of the user for dark mode or light mode
         * and every time when we open the app the mode should be open accordingly the last preference set
         * by the user itself.
         * */
        val switchDarkMode: SwitchCompat? = binding.navView.menu.findItem(R.id.switch_theme_mode).actionView?.findViewById(R.id.dark_mode)
        // Initialize Switch state based on current theme
        switchDarkMode?.isChecked = isDarkTheme

        switchDarkMode?.setOnCheckedChangeListener { _, isChecked ->
            PreferenceManager.setThemeMode(this,isChecked)
            // After switching the theme mode we have to re-create the activity
            recreate()
        }

        /**
         * Default Article Fragment is Hosted when the MainActivity will launch
         * [replaceFragment] is function that helps to replacing the fragment in the
         * [MainActivity] and [R.id.container_fragment] that is Frame layout inside the MainActivity
         * that work as container where we can just replaces our fragment and worked less with the
         * activities
         * */
        replaceFragment(ArticlesFragment())


        /**
         * [coroutines] helps us to call [suspend] functions so it help to work in the background
         * thread and Ui thread would not have to worry about or wait fore the network call completions
         * here we call our Articles for first time that can be used listed in the list in Articles Fragment
         * */

        coroutines.launch {
            viewModel.getNewsList()
        }
    }

    private fun applyTheme() {
        isDarkTheme = PreferenceManager.getThemeMode(this)

        if (isDarkTheme) {
            setTheme(R.style.AppTheme_Night)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            setTheme(R.style.AppTheme_Light)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    private fun replaceFragment(fragment: Fragment, addTobackStack:Boolean = false, tag:String? = null) {
        supportFragmentManager.commit {
            replace(R.id.container_fragment,fragment)
            if (addTobackStack)
                addToBackStack(tag)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // Handle the permission results
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    // Handle the case where a permission is not granted
                    // You may display a message or take appropriate action
                    return
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermissions() : Boolean{
        val permissionsToRequest = mutableListOf<String>()

        // Check if POST_NOTIFICATIONS permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Check if BIND_NOTIFICATION_LISTENER_SERVICE permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)
        }

        // Check if ACCESS_NOTIFICATION_POLICY permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }else{
            return true
        }
        return false
    }

    companion object{
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 123
//        private const val TAG = "MainActivity"
    }

}
