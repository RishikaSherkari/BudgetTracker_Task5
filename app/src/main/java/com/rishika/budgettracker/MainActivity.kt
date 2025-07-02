package com.rishika.budgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rishika.budgettracker.ui.theme.BudgetTrackerAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.collectAsState
import com.rishika.budgettracker.ui.theme.DatabaseProvider
import com.rishika.budgettracker.TransactionEntity
import com.rishika.budgettracker.TransactionDao


@Composable
fun ButtonBlock(text: String, bgColor: Color, textColor: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(bgColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFDFFFD6)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BUDGET TRACKER",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            ButtonBlock("BALANCE", Color.White, Color.Black) {
                navController.navigate(Screen.Balance.route)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonBlock("INCOME", Color.White, Color.Black) {
                navController.navigate(Screen.Income.route)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonBlock("EXPENSE", Color(0xFFFFD6D6), Color.Red) {
                navController.navigate(Screen.Expense.route)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonBlock("+ ADD TRANSACTION", Color(0xFF2D6A4F), Color.White) {
                navController.navigate(Screen.AddTransaction.route)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonBlock("+ ADD INCOME", Color(0xFF2D6A4F), Color.White) {
                navController.navigate(Screen.AddIncome.route)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ButtonBlock("VIEW TRANSACTIONS", Color(0xFF2D6A4F), Color.White) {
                navController.navigate(Screen.TransactionList.route)
            }
        }
    }
}

@Composable
fun AddTransactionScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { DatabaseProvider.getDatabase(context).transactionDao() }

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDFFFD6))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Transaction", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount", fontSize = 20.sp) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category", fontSize = 20.sp) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date", fontSize = 20.sp) },
            placeholder = { Text("DD/MM/YY") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)", fontSize = 20.sp) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertTransaction(
                        TransactionEntity(
                            amount = amount,
                            category = category,
                            date = date,
                            note = note,
                            isIncome = false
                        )
                    )
                }
                navController.popBackStack()
            }) {
                Text("Save", fontSize = 18.sp)
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun AddIncomeScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { DatabaseProvider.getDatabase(context).transactionDao() }

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDFFFD6))
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Income", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount", fontSize = 20.sp) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category", fontSize = 20.sp) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date", fontSize = 20.sp) },
            placeholder = { Text("DD/MM/YY") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)", fontSize = 20.sp) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertTransaction(
                        TransactionEntity(
                            amount = amount,
                            category = category,
                            date = date,
                            note = note,
                            isIncome = true
                        )
                    )
                }
                navController.popBackStack()
            }) {
                Text("Save", fontSize = 18.sp)
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("Cancel", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun TransactionListScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { DatabaseProvider.getDatabase(context).transactionDao() }
    val transactions by dao.getAllTransactions().collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDFFFD6))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Transaction History", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(transactions) { transaction ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(transaction.category, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Text(transaction.date, fontSize = 16.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "₹${transaction.amount}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (transaction.isIncome) Color(0xFF2D6A4F) else Color(0xFFD62828)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Delete",
                                fontSize = 14.sp,
                                color = Color.Red,
                                modifier = Modifier.clickable {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        dao.deleteTransaction(transaction)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SummaryScreen(title: String, amount: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDFFFD6))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "$${amount}",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2D6A4F),
            modifier = Modifier.padding(bottom = 40.dp)
        )
        ButtonBlock("← Back", Color(0xFF2D6A4F), Color.White, onBack)
    }
}

@Composable
fun BalanceScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { DatabaseProvider.getDatabase(context).transactionDao() }
    val transactions by dao.getAllTransactions().collectAsState(initial = emptyList())

    val balance = transactions.sumOf {
        if (it.isIncome) it.amount.toDoubleOrNull() ?: 0.0 else -(it.amount.toDoubleOrNull() ?: 0.0)
    }
    SummaryScreen("Total Balance", String.format("%.2f", balance)) {
        navController.popBackStack()
    }
}

@Composable
fun IncomeScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { DatabaseProvider.getDatabase(context).transactionDao() }
    val transactions by dao.getAllTransactions().collectAsState(initial = emptyList())

    val income = transactions.filter { it.isIncome }
        .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    SummaryScreen("Total Income", String.format("%.2f", income)) {
        navController.popBackStack()
    }
}

@Composable
fun ExpenseScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = remember { DatabaseProvider.getDatabase(context).transactionDao() }
    val transactions by dao.getAllTransactions().collectAsState(initial = emptyList())

    val expense = transactions.filter { !it.isIncome }
        .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
    SummaryScreen("Total Expense", String.format("%.2f", expense)) {
        navController.popBackStack()
    }
}


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object AddIncome : Screen("add_income")
    object TransactionList : Screen("transaction_list")
    object Balance : Screen("balance")
    object Income : Screen("income")
    object Expense : Screen("expense")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTrackerAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) { HomeScreen(navController) }
                    composable(Screen.AddTransaction.route) { AddTransactionScreen(navController) }
                    composable(Screen.AddIncome.route) { AddIncomeScreen(navController) }
                    composable(Screen.TransactionList.route) { TransactionListScreen(navController) }
                    composable(Screen.Balance.route) { BalanceScreen(navController) }
                    composable(Screen.Income.route) { IncomeScreen(navController) }
                    composable(Screen.Expense.route) { ExpenseScreen(navController) }
                }
            }
        }
    }
}
