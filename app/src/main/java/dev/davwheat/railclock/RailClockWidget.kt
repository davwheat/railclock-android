package dev.davwheat.railclock

import android.content.Context
import android.view.LayoutInflater
import android.widget.RemoteViews
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.times
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.appwidget.AndroidRemoteViews
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.padding
import androidx.glance.layout.size
import dev.davwheat.railclock.databinding.RailClockDigitalBinding
import dev.davwheat.railclock.databinding.RailClockSecondsBinding

class RailClockWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        provideContent {
            Content()
        }
    }
}

@Composable
fun InAppWidgetDemo(modifier: Modifier=Modifier) {
    BoxWithConstraints(modifier) {
        val smallestSide = minOf(maxWidth, maxHeight)

        androidx.compose.foundation.layout.Box(
            Modifier
                .size(smallestSide)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            AndroidViewBinding(
                RailClockSecondsBinding::inflate,
                modifier = Modifier.size(smallestSide),
            )
            AndroidViewBinding(
                RailClockDigitalBinding::inflate,
                modifier = Modifier.padding(smallestSide * 0.25f),
            )
        }
    }
}


@Composable
private fun Content() {
    val size = LocalSize.current
    val smallestSide = minOf(size.width, size.height)

    Box(
        GlanceModifier
            .size(smallestSide)
            .background(Color.Black)
            .cornerRadius(smallestSide / 2),
        contentAlignment = Alignment.Center,
    ) {
        Seconds(modifier = GlanceModifier.size(smallestSide))
        DigitalTime(modifier = GlanceModifier.padding(0.25f * smallestSide))
    }
}

@Composable
private fun DigitalTime(modifier: GlanceModifier = GlanceModifier) {
    val packageName = LocalContext.current.packageName

    AndroidRemoteViews(
        RemoteViews(packageName, R.layout.rail_clock_digital), modifier
    )
}

@Composable
private fun Seconds(modifier: GlanceModifier = GlanceModifier) {
    val packageName = LocalContext.current.packageName

    AndroidRemoteViews(
        RemoteViews(packageName, R.layout.rail_clock_seconds), modifier
    )
}
