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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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