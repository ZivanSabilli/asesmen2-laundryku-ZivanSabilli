package com.zivansabilli3153.laundryku.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zivansabilli3153.laundryku.R
import com.zivansabilli3153.laundryku.model.Pesanan
import com.zivansabilli3153.laundryku.util.SettingsDataStore
import com.zivansabilli3153.laundryku.util.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAboutClick: () -> Unit,
    onAddClick: () -> Unit = {},
    onItemClick: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)

    val settingsDataStore = remember { SettingsDataStore(context) }
    val scope = rememberCoroutineScope()

    val dataPesanan by viewModel.dataPesanan.collectAsState()
    val showList by settingsDataStore.layoutFlow.collectAsState(initial = true)

    var expandedMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                actions = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                settingsDataStore.saveLayout(!showList)
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(
                                if (showList) {
                                    R.string.view_grid
                                } else {
                                    R.string.view_list
                                }
                            )
                        )
                    }

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick
            ) {
                Text(
                    text = stringResource(R.string.add_order),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }
    ) { innerPadding ->
        if (dataPesanan.isEmpty()) {
            EmptyOrderState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            )
        } else {
            if (showList) {
                PesananList(
                    dataPesanan = dataPesanan,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onItemClick = onItemClick
                )
            } else {
                PesananGrid(
                    dataPesanan = dataPesanan,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
private fun EmptyOrderState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

        Text(
            text = stringResource(R.string.empty_order_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.empty_order_desc),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PesananList(
    dataPesanan: List<Pesanan>,
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = dataPesanan,
            key = { it.id }
        ) { pesanan ->
            PesananListItem(
                pesanan = pesanan,
                onClick = {
                    onItemClick(pesanan.id)
                }
            )
        }
    }
}

@Composable
private fun PesananGrid(
    dataPesanan: List<Pesanan>,
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 96.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        gridItems(
            items = dataPesanan,
            key = { it.id }
        ) { pesanan ->
            PesananGridItem(
                pesanan = pesanan,
                onClick = {
                    onItemClick(pesanan.id)
                }
            )
        }
    }
}

@Composable
private fun PesananListItem(
    pesanan: Pesanan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = pesanan.namaPelanggan,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.order_weight,
                    pesanan.beratKg.toString()
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_service,
                    pesanan.jenisLayanan
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_pickup,
                    if (pesanan.antarJemput) {
                        stringResource(R.string.yes)
                    } else {
                        stringResource(R.string.no)
                    }
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_total_price,
                    pesanan.totalHarga
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_estimate,
                    pesanan.estimasiHari
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_date,
                    pesanan.tanggal
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (pesanan.catatan.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = pesanan.catatan,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PesananGridItem(
    pesanan: Pesanan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = pesanan.namaPelanggan,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = pesanan.jenisLayanan,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_weight,
                    pesanan.beratKg.toString()
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = stringResource(
                    R.string.order_total_price,
                    pesanan.totalHarga
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (pesanan.catatan.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = pesanan.catatan,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}