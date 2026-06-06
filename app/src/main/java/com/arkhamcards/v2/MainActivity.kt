package com.arkhamcards.v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import com.arkhamcards.v2.ui.theme.ArkhamCardsTheme
import com.arkhamcards.v2.ui.theme.CustomTheme
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.crashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            val lang = Locale.current.toLanguageTag().take(2)
            val langTag = if (lang.equals("zh-CN", ignoreCase = true)) {
                lang
            } else {
                lang.substringBefore("-")
            }
            ArkhamCardsTheme(lang = langTag) {
                Scaffold(modifier = Modifier.fillMaxSize().safeContentPadding()) { innerPadding ->
                    Greeting(
                        name = "Android${CustomTheme.language.colon} ${CustomTheme.language.languageTag}" ,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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