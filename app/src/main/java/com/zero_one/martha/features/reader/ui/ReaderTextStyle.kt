package com.zero_one.martha.features.reader.ui

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun readerTextStyle(
    fontSize: Int,
): TextStyle {
    return LocalTextStyle.current.copy(
        textAlign = TextAlign.Justify,
        textIndent = TextIndent(
            firstLine = 35.sp,
        ),
        hyphens = Hyphens.Auto,
        lineBreak = LineBreak.Paragraph.copy(
            strategy = LineBreak.Strategy.HighQuality,
            strictness = LineBreak.Strictness.Strict,
            wordBreak = LineBreak.WordBreak.Phrase,
        ),
        platformStyle = PlatformTextStyle(
            includeFontPadding = false,
        ),
        fontSize = fontSize.sp,
        lineHeight = fontSize.plus(2).sp,
        letterSpacing = TextUnit.Unspecified,
    )
}
