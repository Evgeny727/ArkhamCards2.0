package com.arkhamcards.v2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.arkhamcards.v2.ui.theme.ArkhamCardsTheme
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AppViewModel
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var appUpdateManager: AppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var isDataLoaded = false
        splashScreen.setKeepOnScreenCondition { !isDataLoaded }
        super.onCreate(savedInstanceState)

        val isPlayServicesAvailable = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(this)
        if (isPlayServicesAvailable == ConnectionResult.SUCCESS) maybeCheckForUpdate()

        enableEdgeToEdge()
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {}
        val crashlytics = Firebase.crashlytics
//        crashlytics.setCustomKeys {
//            key("current_level", 3)
//            key("last_UI_action", "logged_in")
//        }
        //crashlytics.log("message")
        //crashlytics.setUserId("user_id")
//        crashlytics.recordException(RuntimeException("Test Crash")) {
//            key("string key", "string value")
//            key("boolean key", true)
//        }

        setContent {
            viewModel = hiltViewModel<AppViewModel>(this)
            val lang = Locale.current.toLanguageTag()
            val langTag = if (lang.equals("zh-CN", ignoreCase = true)) lang
                else lang.substringBefore("-")
            val theme = 2 //by viewModel.themeState.collectAsState()
            // Collecting user's theme from shared preferences via viewmodel - false = light, true = dark
            val currentTheme = when(theme) {
                0 -> false
                1 -> true
                2 -> isSystemInDarkTheme()
                else -> null
            }
            if (currentTheme != null) {
                isDataLoaded = true
                ArkhamCardsTheme(currentTheme, langTag) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    ) { innerPadding ->
                        val cardsState by viewModel.cardsSyncState.collectAsState()
                        LaunchedEffect(Unit) {
                            viewModel.checkIfCardsReady()
                            //TODO: Add error collecting
                        }
                        // if (cardsState is CardsSyncState.UpdateAvailable) TODO: Show update dialog
                        Crossfade(cardsState) {
                            when (it) {
                                CardsSyncState.Loading ->
                                    CardsDownloadingCircularProgressIndicator()

                                else -> Greeting(
                                    name = "Android${CustomTheme.language.colon} ${CustomTheme.language.languageTag}",
                                    modifier = Modifier.padding(innerPadding)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun maybeCheckForUpdate() {
        val manager = runCatching { AppUpdateManagerFactory.create(this) }.getOrNull()
            ?: return

        appUpdateManager = manager

        manager.appUpdateInfo
            .addOnSuccessListener { info ->
                val available = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                val allowed = info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                val stalenessDays = info.clientVersionStalenessDays() ?: -1

                val shouldForceUpdate = available && allowed && stalenessDays >= 14
                if (shouldForceUpdate) {
                    manager.startUpdateFlow(
                        info,
                        this,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
    }

    // Checks that the update is not stalled during 'onResume()'.
    override fun onResume() {
        super.onResume()

        val manager = appUpdateManager ?: return

        manager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    manager.startUpdateFlow(
                        appUpdateInfo,
                        this,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                    )
                }
            }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun CardsDownloadingCircularProgressIndicator() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CustomTheme.colors.l30
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
//            Text(
//                text = stringResource(id = R.string.cards_updating),
//                color = CustomTheme.colors.d30,
//                style = CustomTheme.typography.headline
//            )
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = CustomTheme.colors.m)
        }
    }
}