package com.example.poemflipgame.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.poemflipgame.model.PoemCard
import com.example.poemflipgame.model.WoodColors

@Composable
fun WoodCard(
    card: PoemCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isFlipped) 180f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        ),
        label = "card_flip"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(enabled = !card.isFlipped && !card.isMatched, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (card.isFlipped || card.isMatched) {
                    WoodColors.woodLight
                } else {
                    WoodColors.cardBack
                }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (card.isFlipped || card.isMatched) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    WoodColors.woodLight,
                                    WoodColors.woodMedium.copy(alpha = 0.8f)
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    WoodColors.cardBack,
                                    WoodColors.woodDark
                                )
                            )
                        }
                    )
                    .border(
                        width = 3.dp,
                        color = if (card.isFlipped || card.isMatched) {
                            WoodColors.woodDark
                        } else {
                            WoodColors.goldAccent
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                if (card.isFlipped || card.isMatched) {
                    // 翻转后显示汉字
                    Text(
                        text = card.char.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = WoodColors.woodText,
                        modifier = Modifier.graphicsLayer {
                            rotationY = 180f
                        }
                    )
                } else {
                    // 翻转前显示木纹图案
                    Text(
                        text = "詩",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = WoodColors.goldAccent.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}