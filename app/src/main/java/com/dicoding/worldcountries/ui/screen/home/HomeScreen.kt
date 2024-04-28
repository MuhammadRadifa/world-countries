package com.dicoding.worldcountries.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dicoding.worldcountries.R
import com.dicoding.worldcountries.data.common.Screen
import com.dicoding.worldcountries.data.network.remote.DataItem

import com.dicoding.worldcountries.data.state.ResultState
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    navigateToDetail:(DataItem)->Unit) {

    var textUrl by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Column(
        Modifier
            .padding(horizontal = 10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Spacer(modifier = Modifier.height(8.dp))
        MySearchBar(
            text = textUrl,
            onTextChange = {
                textUrl = it
                viewModel.searchCountry(it)
            },
            placeHolder = "Search Country",
            keyboardController = keyboardController
        )
        Spacer(modifier = Modifier.height(8.dp))

        val countriesState = viewModel.countries.value
        when{
            (countriesState.error != null) -> Text(text = countriesState.error, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            (countriesState.loading) -> {
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                Image(painter = painterResource(id = R.drawable.earth), contentDescription = "Earth", modifier = Modifier.size(100.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Loading", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            else -> LazyColumn {
                items(countriesState.list.size) {
                    country ->
                    CardItem(country = countriesState.list[country],navigateToDetail )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardItem(country:DataItem,navigateToDetail:(DataItem)->Unit ) {

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
        onClick = { navigateToDetail(country) },
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    placeHolder: String,
    keyboardController: SoftwareKeyboardController?
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        value = text,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(
                text = placeHolder
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black,
            focusedBorderColor = colorResource(id = R.color.green),
            unfocusedBorderColor = colorResource(id = R.color.gray_dark),
        ),
        shape = RoundedCornerShape(8.dp),
        leadingIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = modifier.size(22.dp)
                )
            }
        },
    )
}