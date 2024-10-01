package com.example.tingshingliu_multipaneshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tingshingliu_multipaneshopping.ui.theme.TingShingLiuMultiPaneShoppingTheme
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TingShingLiuMultiPaneShoppingTheme {
                ShoppingApp()
            }
        }
    }
}

data class Product(val name: String, val price: String, val description: String)

@Composable
fun ShoppingApp() {
    val products = listOf(
        Product("Gura Mousepad", "$100", "Equipped with the latest tactile technology, allowing effortless movements "),
        Product("Gura Chopsticks", "$150", "An smart chopsticks that makes your food taste better!"),
        Product("Gura Pillow", "$200", "Luxury pillow of the seas that embraces you in the moment"),
        Product("AmeSame", "$500", "Eternal bliss")
    )

    val navController = rememberNavController()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row {
                ProductList(
                    products = products,
                    onProductSelected = { selectedProduct = it },
                    modifier = Modifier.weight(1f)
                )
                ProductDetails(
                    product = selectedProduct,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        else -> {
            NavHost(navController, startDestination = "productList") {
                composable("productList") {
                    ProductListScreen(
                        products = products,
                        onProductSelected = {
                            selectedProduct = it
                            navController.navigate("productDetails")
                        }
                    )
                }
                composable("productDetails") {
                    ProductDetailsScreen(
                        product = selectedProduct,
                        onBackPressed = {
                            selectedProduct = null  // Reset selected product
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun ProductListScreen(
    products: List<Product>,
    onProductSelected: (Product) -> Unit
) {
    Column {
        Text(
            text = "Product List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        ProductList(
            products = products,
            onProductSelected = onProductSelected
        )
    }
}

@Composable
fun ProductDetailsScreen(
    product: Product?,
    onBackPressed: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable(onClick = onBackPressed)
                    .padding(end = 16.dp)
            )
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        if (product != null) {
            ProductDetails(product = product)
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Select a product to view details")
            }
        }
    }
}
@Composable
fun ProductList(
    products: List<Product>,
    onProductSelected: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onProductSelected(product) }
            ) {
                Text(
                    text = product.name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun ProductDetails(product: Product?, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        if (product != null) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = product.name, style = MaterialTheme.typography.headlineMedium)
                Text(text = product.price, style = MaterialTheme.typography.headlineSmall)
                Text(text = product.description, style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Select a product to view details")
            }
        }
    }
}