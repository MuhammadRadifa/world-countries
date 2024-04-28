package com.dicoding.worldcountries.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dicoding.worldcountries.R
import com.dicoding.worldcountries.data.network.remote.DataItem
import com.dicoding.worldcountries.ui.screen.home.CardItem
import com.dicoding.worldcountries.ui.screen.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(country: DataItem, viewModel: HomeViewModel = koinViewModel()) {

    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context = context)
            .data(country.flag)
            .decoderFactory(SvgDecoder.Factory())
            .error(R.drawable.empty_image)
            .placeholder(R.drawable.empty_image)
            .build()
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painter,
            contentDescription = country.name,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(200.dp)
                .width(300.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(5.dp, Color.Black)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = country.name, fontSize = 30.sp, fontWeight = FontWeight.Bold)

        viewModel.getCityByCountry(country.name)

        val cityState = viewModel.city.value

        when {
            (cityState.error != null) -> Text(
                text = cityState.error,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            (cityState.loading) -> {
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                Image(
                    painter = painterResource(id = R.drawable.earth),
                    contentDescription = "Earth",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Loading", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            else -> LazyColumn {
                items(cityState.list.size) {
                    country ->
                    CardCityItem(city = cityState.list[country])
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardCityItem(city:String) {

    Card(onClick = { /*TODO*/ }, modifier = Modifier.padding(vertical = 6.dp, horizontal = 6.dp)) {
        Row(
            Modifier
                .background(color = Color.White)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_maps_home_work_24),
                contentDescription = "image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(16.dp)
                    .height(60.dp)
                    .width(60.dp)
            )
            Text(
                text = city,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }

}