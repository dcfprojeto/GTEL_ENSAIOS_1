package br.com.gtel.ensaios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.gtel.ensaios.data.AppDatabase
import br.com.gtel.ensaios.data.ReportEntity
import br.com.gtel.ensaios.ui.theme.GTELBlue
import br.com.gtel.ensaios.ui.theme.GTELOrange
import br.com.gtel.ensaios.ui.theme.GTELTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GTELTheme { GTELApp() } }
    }
}

sealed class Route(val path: String, val title: String) {
    data object Splash : Route("splash", "Splash")
    data object Home : Route("home", "Início")
    data object Reports : Route("reports", "Relatórios Salvos")
    data object NewReport : Route("new_report", "Novo Relatório")
    data object ProjectData : Route("project_data", "Dados da Obra")
    data object Equipment : Route("equipment", "Dados do Equipamento")
    data object Tree : Route("tree", "Estrutura em Árvore")
    data object Instruments : Route("instruments", "Instrumentos")
    data object Environment : Route("environment", "Condições Ambientais")
    data object Standards : Route("standards", "Normas Aplicáveis")
    data object Megger : Route("megger", "Ensaio Megger")
    data object Hipot : Route("hipot", "Ensaio HIPOT")
    data object Vlf : Route("vlf", "Ensaio VLF")
    data object Photos : Route("photos", "Relatório Fotográfico")
    data object Signatures : Route("signatures", "Assinaturas")
    data object Preview : Route("preview", "Pré-visualização")
    data object Settings : Route("settings", "Configurações")
    data object About : Route("about", "Sobre")
}

private val formRoutes = listOf(
    Route.ProjectData,
    Route.Equipment,
    Route.Tree,
    Route.Instruments,
    Route.Environment,
    Route.Standards,
    Route.Megger,
    Route.Hipot,
    Route.Vlf,
    Route.Photos,
    Route.Signatures,
    Route.Preview
)

@Composable
fun GTELApp() {
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = Route.Splash.path) {
            composable(Route.Splash.path) { SplashScreen(navController) }
            composable(Route.Home.path) { HomeScreen(navController) }
            composable(Route.Reports.path) { ReportsScreen(navController) }
            composable(Route.NewReport.path) { NewReportScreen(navController) }
            composable(Route.ProjectData.path) { SimpleFormScreen(navController, Route.ProjectData, listOf("Cliente", "Obra / Projeto", "Contrato", "Local", "Número do relatório")) }
            composable(Route.Equipment.path) { SimpleFormScreen(navController, Route.Equipment, listOf("Tipo de item", "TAG", "Origem / De", "Destino / Para", "Tensão de ensaio")) }
            composable(Route.Tree.path) { PlaceholderScreen(navController, Route.Tree, "Base criada para estrutura de raiz: Transformador → QGBT → QF → QL.") }
            composable(Route.Instruments.path) { SimpleFormScreen(navController, Route.Instruments, listOf("Tipo de instrumento", "Fabricante", "Modelo", "Número de série", "Validade da calibração")) }
            composable(Route.Environment.path) { SimpleFormScreen(navController, Route.Environment, listOf("Temperatura ambiente", "Umidade relativa do ar", "Condição climática", "Local do ensaio")) }
            composable(Route.Standards.path) { StandardsScreen(navController) }
            composable(Route.Megger.path) { TestScreen(navController, Route.Megger, listOf("R / Terra", "S / Terra", "T / Terra", "R / S", "S / T", "T / R")) }
            composable(Route.Hipot.path) { TestScreen(navController, Route.Hipot, listOf("R / Terra", "S / Terra", "T / Terra", "R / S", "S / T", "T / R")) }
            composable(Route.Vlf.path) { TestScreen(navController, Route.Vlf, listOf("Tempo", "Tensão", "Corrente", "Resultado")) }
            composable(Route.Photos.path) { PlaceholderScreen(navController, Route.Photos, "Na próxima etapa serão adicionadas câmera, galeria, legenda e matriz 1x1 a 4x4.") }
            composable(Route.Signatures.path) { PlaceholderScreen(navController, Route.Signatures, "Na próxima etapa serão adicionadas assinaturas do Executante, Fiscalização e Cliente.") }
            composable(Route.Preview.path) { PlaceholderScreen(navController, Route.Preview, "Resumo do relatório. Geração de PDF será implementada após validação do APK da Etapa 1.") }
            composable(Route.Settings.path) { SettingsScreen(navController) }
            composable(Route.About.path) { AboutScreen(navController) }
        }
    }
}

@Composable
fun SplashScreen(nav: NavHostController) {
    LaunchedEffect(Unit) { kotlinx.coroutines.delay(900); nav.navigate(Route.Home.path) { popUpTo(Route.Splash.path) { inclusive = true } } }
    Box(Modifier.fillMaxSize().background(GTELBlue), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("GTEL", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black)
            Text("⚡ Ensaios Elétricos", color = GTELOrange, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(color = GTELOrange)
        }
    }
}

@Composable
fun HomeScreen(nav: NavHostController) {
    BaseScaffold(title = "GTEL Ensaios Elétricos", showBack = false, nav = nav) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("GTEL", color = GTELBlue, fontSize = 44.sp, fontWeight = FontWeight.Black)
            Text("Relatórios de Megger, HIPOT e VLF", color = GTELOrange, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))
            BigButton("Novo Relatório") { nav.navigate(Route.NewReport.path) }
            BigButton("Continuar Relatório") { nav.navigate(Route.Reports.path) }
            BigButton("Relatórios Salvos") { nav.navigate(Route.Reports.path) }
            BigButton("Configurações") { nav.navigate(Route.Settings.path) }
            OutlinedButton(onClick = { nav.navigate(Route.About.path) }, modifier = Modifier.fillMaxWidth().height(54.dp)) { Text("Sobre") }
        }
    }
}

@Composable
fun NewReportScreen(nav: NavHostController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()
    var selected by remember { mutableStateOf("Megger") }
    BaseScaffold(title = "Novo Relatório", nav = nav) {
        Text("Selecione o tipo de ensaio e inicie o preenchimento.")
        Spacer(Modifier.height(12.dp))
        listOf("Megger", "HIPOT", "VLF", "Combinado").forEach { type ->
            FilterChip(
                selected = selected == type,
                onClick = { selected = type },
                label = { Text(type) },
                modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        BigButton("Criar relatório e preencher dados") {
            scope.launch {
                db.reportDao().insert(ReportEntity(testType = selected, number = "GTEL-${selected.uppercase()}-0001/2026"))
                nav.navigate(Route.ProjectData.path)
            }
        }
        BigButton("Abrir etapas do relatório") { nav.navigate(Route.ProjectData.path) }
    }
}

@Composable
fun ReportsScreen(nav: NavHostController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val reports by db.reportDao().observeReports().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    BaseScaffold(title = "Relatórios Salvos", nav = nav) {
        if (reports.isEmpty()) {
            Text("Nenhum relatório salvo ainda.")
            Spacer(Modifier.height(12.dp))
            BigButton("Criar primeiro relatório") { nav.navigate(Route.NewReport.path) }
        } else {
            reports.forEach { report ->
                Card(Modifier.fillMaxWidth().padding(bottom = 10.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(14.dp)) {
                        Text(report.number, fontWeight = FontWeight.Bold, color = GTELBlue)
                        Text("Tipo: ${report.testType} | Status: ${report.status}")
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { nav.navigate(Route.ProjectData.path) }) { Text("Editar") }
                            OutlinedButton(onClick = { scope.launch { db.reportDao().delete(report) } }) { Text("Apagar") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleFormScreen(nav: NavHostController, route: Route, fields: List<String>) {
    val values = remember { fields.associateWith { mutableStateOf("") } }
    BaseScaffold(title = route.title, nav = nav) {
        fields.forEach { label ->
            OutlinedTextField(
                value = values[label]?.value ?: "",
                onValueChange = { values[label]?.value = it },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                singleLine = false
            )
        }
        StepButtons(nav, route)
    }
}

@Composable
fun TestScreen(nav: NavHostController, route: Route, rows: List<String>) {
    var appliedVoltage by remember { mutableStateOf("") }
    var testTime by remember { mutableStateOf("") }
    val values = remember { rows.associateWith { mutableStateOf("") } }
    BaseScaffold(title = route.title, nav = nav) {
        OutlinedTextField(value = appliedVoltage, onValueChange = { appliedVoltage = it }, label = { Text("Tensão aplicada") }, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp))
        OutlinedTextField(value = testTime, onValueChange = { testTime = it }, label = { Text("Tempo de ensaio") }, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
        Text("Medições", fontWeight = FontWeight.Bold, color = GTELBlue)
        Spacer(Modifier.height(8.dp))
        rows.forEach { row ->
            Card(Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(12.dp)) {
                    Text(row, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = values[row]?.value ?: "", onValueChange = { values[row]?.value = it }, label = { Text("Valor / Resultado") }, modifier = Modifier.fillMaxWidth())
                }
            }
        }
        StepButtons(nav, route)
    }
}

@Composable
fun StandardsScreen(nav: NavHostController) {
    val standards = listOf("ABNT NBR 5410", "ABNT NBR 14039", "ABNT NBR 7286", "ABNT NBR 7287", "ABNT NBR IEC 60502", "IEEE 400", "IEEE 400.2", "NR-10", "Procedimento interno GTEL")
    BaseScaffold(title = Route.Standards.title, nav = nav) {
        standards.forEach { item ->
            var checked by remember { mutableStateOf(true) }
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked, onCheckedChange = { checked = it })
                Text(item)
            }
        }
        StepButtons(nav, Route.Standards)
    }
}

@Composable
fun PlaceholderScreen(nav: NavHostController, route: Route, message: String) {
    BaseScaffold(title = route.title, nav = nav) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
            Text(message, Modifier.padding(16.dp))
        }
        Spacer(Modifier.height(16.dp))
        StepButtons(nav, route)
    }
}

@Composable
fun SettingsScreen(nav: NavHostController) {
    BaseScaffold(title = Route.Settings.title, nav = nav) {
        SimpleReadOnlyItem("Prefixo padrão", "GTEL-ENS")
        SimpleReadOnlyItem("Ano", "2026")
        SimpleReadOnlyItem("Tema", "Claro - Azul e Laranja GTEL")
        SimpleReadOnlyItem("Etapa", "Base funcional compilável")
    }
}

@Composable
fun AboutScreen(nav: NavHostController) {
    BaseScaffold(title = Route.About.title, nav = nav) {
        Text("GTEL Ensaios Elétricos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = GTELBlue)
        Spacer(Modifier.height(8.dp))
        Text("Aplicativo técnico para relatórios de ensaio Megger, HIPOT e VLF.")
        Spacer(Modifier.height(8.dp))
        Text("Etapa 1: base Android com navegação, tema GTEL, Room inicial e GitHub Actions.")
    }
}

@Composable
fun BaseScaffold(title: String, nav: NavHostController, showBack: Boolean = true, content: @Composable ColumnScope.() -> Unit) {
    Scaffold(
        topBar = {
            Surface(color = GTELBlue, modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.height(62.dp).padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (showBack) TextButton(onClick = { nav.popBackStack() }) { Text("Voltar", color = Color.White) }
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("⚡", color = GTELOrange, fontSize = 22.sp)
                }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            content = content
        )
    }
}

@Composable
fun BigButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp).padding(bottom = 10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GTELBlue)
    ) { Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
}

@Composable
fun StepButtons(nav: NavHostController, route: Route) {
    val index = formRoutes.indexOfFirst { it.path == route.path }
    Spacer(Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedButton(onClick = { if (index > 0) nav.navigate(formRoutes[index - 1].path) else nav.navigate(Route.Home.path) }, modifier = Modifier.weight(1f).height(52.dp)) { Text("Voltar") }
        Button(onClick = { if (index in 0 until formRoutes.lastIndex) nav.navigate(formRoutes[index + 1].path) else nav.navigate(Route.Home.path) }, modifier = Modifier.weight(1f).height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = GTELOrange)) { Text("Avançar") }
    }
}

@Composable
fun SimpleReadOnlyItem(label: String, value: String) {
    Card(Modifier.fillMaxWidth().padding(bottom = 10.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp)) {
            Text(label, fontWeight = FontWeight.Bold, color = GTELBlue)
            Text(value)
        }
    }
}
