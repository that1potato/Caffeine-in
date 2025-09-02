package com.example.caffeine_in.ui.info.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caffeine_in.R

@Composable
fun CaffeineStudy() {
    Text(
        text = "\n" + "Caffeine in Human Body",
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = Color(0xFF38220F)
    )
    Text(
        text = buildAnnotatedString {
            append("\n")
            append("The FDA suggests up to")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(" 400mg/day ") }
            append("of caffeine intake for most adults.\n\n")
            append("This app uses the pharmacokinetic principle of caffeine metabolism based on peer-reviewed research:\n\n")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Half-life\n") }
            append("Mean value of 5 hours for healthy adults¹².\n\n")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("Individual variation\n") }
            append("1.5~9.5 hours depending on factors like genetics, smoking, pregnancy².\n")
        },
        fontSize = 14.sp,
        color = Color(0xFF38220F)
    )
    Image(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF38220F),
                shape = RoundedCornerShape(16.dp)
            ),
        painter = painterResource(id = R.drawable.caffeine_decay_chart),
        contentDescription = "Caffeine Decay Chart",
        contentScale = ContentScale.FillWidth
    )
    Spacer(modifier = Modifier.height(24.dp))
    References()
}

@Composable
fun References() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color(0xFFC5B5A6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "References",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF38220F)
                )
                Text(
                    text = buildAnnotatedString{
                        append("1. ")
                        withLink(
                            LinkAnnotation.Url(
                                "https://academic.oup.com/ajcp/article-abstract/73/3/390/1771986",
                                TextLinkStyles(
                                    style = SpanStyle(
                                        color = Color(0xFF38220F),
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            )
                        ) {append("American Journal of Clinical Pathology\n")}
                        append("2. ")
                        withLink(
                            LinkAnnotation.Url(
                                "https://www.ncbi.nlm.nih.gov/books/NBK223808/",
                                TextLinkStyles(
                                    style = SpanStyle(
                                        color = Color(0xFF38220F),
                                        textDecoration = TextDecoration.Underline
                                    )
                                )
                            )
                        ) {append("NCBI Bookshelf")}
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF38220F)
                )
            }
        }
    }
}
