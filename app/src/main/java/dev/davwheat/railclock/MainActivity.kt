package dev.davwheat.railclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.glance.appwidget.GlanceAppWidgetManager
import dev.davwheat.railclock.theme.AppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                Scaffold { innerPadding ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        HomeContent()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeContent() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(R.string.home_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(32.dp))

        val windowSizeInfo = LocalWindowInfo.current.containerSize
        val smallestSize = minOf(windowSizeInfo.width, windowSizeInfo.height)
        val sizeDp = with(LocalDensity.current) { smallestSize.toDp() }
        InAppWidgetDemo(
            Modifier
                .widthIn(max = min(400.dp, sizeDp))
                .fillMaxWidth(0.75f),
        )
        Spacer(Modifier.height(36.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
                        receiver = RailClockWidgetReceiver::class.java,
                        preview = RailClockWidget(),
                        previewState = DpSize(200.dp, 200.dp)
                    )
                }
            },
            shapes = ButtonDefaults.shapes(),
            modifier = Modifier.heightIn(min = ButtonDefaults.MediumContainerHeight),
            contentPadding = ButtonDefaults.contentPaddingFor(ButtonDefaults.MediumContainerHeight),
        ) {
            Text(stringResource(R.string.add_widget_button))
        }

        Spacer(Modifier.height(8.dp))

        var showInfoSheet by rememberSaveable { mutableStateOf(false) }
        TextButton(onClick = { showInfoSheet = true }) {
            Text(stringResource(R.string.more_info_button))
        }

        if (showInfoSheet) {
            val sheetState =
                rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showInfoSheet = false
                        }
                    }
                },
                sheetState = sheetState,
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 18.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        stringResource(R.string.info_sheet_title),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        stringResource(R.string.info_sheet_1),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        AnnotatedString.fromHtml(
                            stringResource(R.string.info_sheet_2_html)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        stringResource(R.string.info_sheet_3),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
