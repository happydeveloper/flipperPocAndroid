package com.talktomeinkorean.debuggingpocapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier

import com.talktomeinkorean.debuggingpocapplication.MainActivity.Companion.TAG
import com.talktomeinkorean.debuggingpocapplication.ui.theme.DebuggingPoCApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity-test"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DebuggingPoCApplicationTheme {
                // snackbar doc
                // https://developer.android.com/develop/ui/compose/components/snackbar?hl=ko
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                val someDummyList = remember {
                    mutableListOf<String>()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        onClick = {
                            val someValue = 123
                            val someArray = arrayOf(0,1,2,3,4)
                            someDummyList.add("test")

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShowToastButton(onClick: () -> Unit){
    Button(onClick = {
        Log.d(TAG, "ShowToastButton: ")
        onClick.invoke()
    }) {
        Text(text = "testsss button ")
    }
}

@Composable
fun Greeting(name: String,
             modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        ShowToastButton(onClick = {
            onClick.invoke()
        })
    }

}