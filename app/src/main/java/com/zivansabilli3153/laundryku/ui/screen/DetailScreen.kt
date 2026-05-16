package com.zivansabilli3153.laundryku.ui.screen

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zivansabilli3153.laundryku.R
import com.zivansabilli3153.laundryku.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    idPesanan: Long? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var customerName by rememberSaveable { mutableStateOf("") }
    var weightText by rememberSaveable { mutableStateOf("") }
    var serviceType by rememberSaveable { mutableStateOf("regular") }
    var usePickup by rememberSaveable { mutableStateOf(false) }
    var noteText by rememberSaveable { mutableStateOf("") }

    var expandedMenu by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(idPesanan) {
        if (idPesanan != null) {
            val pesanan = viewModel.getPesanan(idPesanan)

            if (pesanan != null) {
                customerName = pesanan.namaPelanggan
                weightText = pesanan.beratKg.toString()
                serviceType = if (pesanan.jenisLayanan.equals("Express", ignoreCase = true)) {
                    "express"
                } else {
                    "regular"
                }
                usePickup = pesanan.antarJemput
                noteText = pesanan.catatan
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            if (idPesanan == null) {
                                R.string.add_order_title
                            } else {
                                R.string.edit_order_title
                            }
                        )
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(text = stringResource(R.string.up_label))
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val weight = weightText.toDoubleOrNull()

                            when {
                                customerName.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_name),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                weightText.isBlank() -> {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_weight_empty),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                weight == null || weight <= 0 -> {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_weight_invalid),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {
                                    if (idPesanan == null) {
                                        viewModel.insert(
                                            namaPelanggan = customerName,
                                            beratKg = weight,
                                            serviceType = serviceType,
                                            antarJemput = usePickup,
                                            catatan = noteText
                                        )
                                    } else {
                                        viewModel.update(
                                            id = idPesanan,
                                            namaPelanggan = customerName,
                                            beratKg = weight,
                                            serviceType = serviceType,
                                            antarJemput = usePickup,
                                            catatan = noteText
                                        )
                                    }

                                    onSaveClick()
                                }
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.save_order))
                    }

                    if (idPesanan != null) {
                        Box {
                            TextButton(onClick = { expandedMenu = true }) {
                                Text(text = "⋮")
                            }

                            DropdownMenu(
                                expanded = expandedMenu,
                                onDismissRequest = { expandedMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = stringResource(R.string.delete_order))
                                    },
                                    onClick = {
                                        expandedMenu = false
                                        showDeleteDialog = true
                                    }
                                )
                            }
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = {
                    Text(text = stringResource(R.string.customer_name))
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = weightText,
                onValueChange = { weightText = it },
                label = {
                    Text(text = stringResource(R.string.weight_kg))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.choose_service),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
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
            }

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

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = {
                    Text(text = stringResource(R.string.order_form_note))
                },
                placeholder = {
                    Text(text = stringResource(R.string.order_form_note_hint))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4
            )
        }
    }

    if (showDeleteDialog) {
        DisplayAlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            onConfirmation = {
                showDeleteDialog = false
                if (idPesanan != null) {
                    viewModel.delete(idPesanan)
                }
                onSaveClick()
            }
        )
    }
}