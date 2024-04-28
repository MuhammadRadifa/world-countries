package com.dicoding.worldcountries.ui.screen.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dicoding.worldcountries.R
import com.dicoding.worldcountries.data.network.local.FavoriteEntity
import com.dicoding.worldcountries.data.network.remote.DataItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(viewModel:FavoriteViewModel = koinViewModel(),navigateToDetail:(DataItem)->Unit) {

    val data = viewModel.getAllFavorites().collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Your Favorite Countries", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        if(data.value.isNotEmpty()){
            LazyColumn {
                items(data.value){
                    country ->
                    CardItem(country = country,navigateToDetail)
                }
            }
        }else{
            Spacer(modifier = Modifier.fillMaxHeight(0.45f))
            Text(text = "No Favorite Countries", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(country: FavoriteEntity,navigateToDetail:(DataItem)->Unit) {

    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context)
            .data(country.flag)
            .decoderFactory(SvgDecoder.Factory())
            .error(R.drawable.empty_image)
            .placeholder(R.drawable.empty_image)
            .build()
    )

    Card(
        onClick = {
            navigateToDetail(DataItem(
                name = country.name,
                flag = country.flag,
            ))
        },
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Row(
            Modifier
                .background(color = Color.White)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painter,
                contentDescription = "image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(16.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .border(1.5.dp, Color.Black)
                    .height(50.dp)
                    .width(70.dp)
            )
            Text(
                text = country.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }

}
