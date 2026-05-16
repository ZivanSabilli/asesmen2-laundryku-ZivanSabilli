package com.zivansabilli3153.laundryku.ui.screen

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zivansabilli3153.laundryku.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onAboutClick: () -> Unit) {
    val context = LocalContext.current

    var customerName by rememberSaveable { mutableStateOf("") }
    var weightText by rememberSaveable { mutableStateOf("") }
    var serviceType by rememberSaveable { mutableStateOf("regular") }
    var usePickup by rememberSaveable { mutableStateOf(false) }

    var nameError by rememberSaveable { mutableStateOf("") }
    var weightError by rememberSaveable { mutableStateOf("") }

    var totalPriceText by rememberSaveable { mutableStateOf("") }
    var estimateText by rememberSaveable { mutableStateOf("") }
    var summaryText by rememberSaveable { mutableStateOf("") }

    var expandedMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    Box {
                        TextButton(onClick = { expandedMenu = true }) {
                            Text(text = stringResource(R.string.menu_label))
                        }

                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(R.string.menu_about))
                                },
                                onClick = {
                                    expandedMenu = false
                                    onAboutClick()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.laundry_banner),
                contentDescription = stringResource(R.string.cd_laundry_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = customerName,
                onValueChange = {
                    customerName = it
                    nameError = ""
                },
                label = {
                    Text(text = stringResource(R.string.customer_name))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            if (nameError.isNotEmpty()) {
                Text(
                    text = nameError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weightText,
                onValueChange = {
                    weightText = it
                    weightError = ""
                },
                label = {
                    Text(text = stringResource(R.string.weight_kg))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            if (weightError.isNotEmpty()) {
                Text(
                    text = weightError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.choose_service),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = serviceType == "regular",
                    onClick = { serviceType = "regular" }
                )
                Text(text = stringResource(R.string.service_regular))

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = serviceType == "express",
                    onClick = { serviceType = "express" }
                )
                Text(text = stringResource(R.string.service_express))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.pickup_service),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = usePickup,
                    onCheckedChange = { usePickup = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val weight = weightText.toDoubleOrNull()

                    nameError = if (customerName.isBlank()) {
                        context.getString(R.string.error_name)
                    } else {
                        ""
                    }

                    weightError = when {
                        weightText.isBlank() -> context.getString(R.string.error_weight_empty)
                        weight == null -> context.getString(R.string.error_weight_invalid)
                        weight <= 0 -> context.getString(R.string.error_weight_invalid)
                        else -> ""
                    }

                    if (nameError.isEmpty() && weightError.isEmpty()) {
                        val pricePerKg = if (serviceType == "express") 10000 else 7000
                        val pickupFee = if (usePickup) 5000 else 0
                        val total = (weight!! * pricePerKg + pickupFee).toInt()
                        val days = if (serviceType == "express") 1 else 3

                        val serviceLabel = if (serviceType == "express") {
                            context.getString(R.string.service_express)
                        } else {
                            context.getString(R.string.service_regular)
                        }

                        val pickupLabel = if (usePickup) {
                            context.getString(R.string.yes)
                        } else {
                            context.getString(R.string.no)
                        }

                        totalPriceText = context.getString(R.string.result_total_price, total)
                        estimateText = context.getString(R.string.result_estimate, days)

                        summaryText = context.getString(
                            R.string.share_message,
                            customerName,
                            weightText,
                            serviceLabel,
                            pickupLabel,
                            total,
                            days
                        )
                    } else {
                        totalPriceText = ""
                        estimateText = ""
                        summaryText = ""
                    }
                }
            ) {
                Text(text = stringResource(R.string.calculate))
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (summaryText.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.result_title),
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = totalPriceText)
                        Text(text = estimateText)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, summaryText)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(
                            sendIntent,
                            context.getString(R.string.share_result)
                        )

                        context.startActivity(shareIntent)
                    }
                ) {
                    Text(text = stringResource(R.string.share_result))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}