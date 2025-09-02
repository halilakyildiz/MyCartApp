package com.example.productcardlist.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productcardlist.R
import com.example.productcardlist.model.*
import com.example.productcardlist.viewmodel.MyViewModel
import coil.compose.AsyncImage

@Composable
fun CartCard(cart: Cart,navController:NavController) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Row(verticalAlignment = Alignment.CenterVertically
            ) {
                val maxVisible = 2
                var visibleProducts:List<Product>?=null
                if(cart.products.size>=maxVisible)
                    visibleProducts = cart.products.take(maxVisible)
                else
                    visibleProducts=cart.products
                Row {
                    visibleProducts.forEachIndexed { index, product ->
                        AsyncImage(
                            model = product.thumbnail,
                            contentDescription = product.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        )
                    }
                    if (cart.products.size > maxVisible) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(20.dp).padding(start = 2.dp)
                            ) {
                                Text(
                                    "${cart.products.size - maxVisible}",
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp // ihtiyaca göre
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "User ${cart.userId}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Total: ${cart.total}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.LineThrough,
                    )
                )
                Text(
                    text = "Discounted Total: ${cart.discountedTotal}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
                )
                Button(
                    onClick = {
                        navController.navigate("cart_detail/${cart.id}")
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Go to Cart")
                }
            }
        }
    }
}

@Composable
fun CartDetails(cart:Cart?){
    Column(
    ) {
        cart?.let{
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(cart.products) {product->
                    CartProduct(product)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total: ${cart.total}$",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.LineThrough,
                        )
                    )
                    Text(
                        text = "Discounted Total: ${cart.discountedTotal}$",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
                    )
                }
                Button(onClick = {

                }) {
                    Text("Payment")
                }
            }
        }?:run{
            Text("Data Not Found", textAlign = TextAlign.Center)
        }

    }
}

@Composable
fun CartProduct(product:Product){
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment =Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            var isLoading by remember { mutableStateOf(true) }
            Box(
                modifier = Modifier.size(64.dp),
            ){
                AsyncImage(
                    model = "${product.thumbnail}",
                    contentDescription = "${product.title}",
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.imgsvg), // Show this if img not loaded
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false }
                )
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        strokeWidth = 2.dp
                    )
                }
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text("${product.title}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Row{
                    Text("${product.price}$",
                        style = MaterialTheme.typography.labelSmall,
                        textDecoration = TextDecoration.LineThrough)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text("${product.discountPercentage}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.Blue
                        ))
                }
                val result =product.discountedTotal.toDouble()/product.quantity.toDouble()
                Text("${String.format("%.2f", result)}$",
                    style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.padding(10.dp))
                Text("${product.discountedTotal}$",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    ))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                IconButton(
                    onClick = { /* azalt */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Azalt",
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = "${product.quantity}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                IconButton(
                    onClick = { /* azalt */ },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Azalt",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CartList(root:Root?,navController:NavController){
    LazyColumn {
        root?.let {
            items(it.carts) { cart ->
                CartCard(cart = cart,navController)
            }
        }?:run{
            item{
                Text("Data not found",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            }
        }

    }
}