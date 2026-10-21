package kz.muallimi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MuallimiApp() }
    }
}

private enum class Section(val label: String) {
    HOME("Басты бет"), SARF("Сарф"), NAHW("Нахв"), TUTOR("ИИ-ұстаз"), QUIZ("Тест")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MuallimiApp() {
    var section by remember { mutableStateOf(Section.HOME) }
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("مُعَلِّمِي", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                            Text("Араб тілі: сарф және нахв", fontSize = 12.sp)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    listOf(Section.HOME, Section.SARF, Section.NAHW, Section.TUTOR, Section.QUIZ).forEach {
                        NavigationBarItem(
                            selected = section == it,
                            onClick = { section = it },
                            icon = { Text(when (it) {
                                Section.HOME -> "⌂"
                                Section.SARF -> "صرف"
                                Section.NAHW -> "نحو"
                                Section.TUTOR -> "AI"
                                Section.QUIZ -> "✓"
                            }) },
                            label = { Text(it.label, fontSize = 10.sp) }
                        )
                    }
                }
            }
        ) { padding ->
            when (section) {
                Section.HOME -> HomeScreen(Modifier.padding(padding))
                Section.SARF -> SarfScreen(Modifier.padding(padding))
                Section.NAHW -> NahwScreen(Modifier.padding(padding))
                Section.TUTOR -> TutorScreen(Modifier.padding(padding))
                Section.QUIZ -> QuizScreen(Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Ассаламу алейкум!", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("Қош келдің! Мұнда араб тілінің сарфын, нахвын және ислам кітаптарындағы мәтіндерді кезең-кезеңімен үйренесің.")
        InfoCard("📗 Сарф — الصَّرْفُ", "Етістіктің формалары, түбірі және сөз жасау үлгілері.")
        InfoCard("📘 Нахв — النَّحْوُ", "Сөйлем құрылысы, иғраб және грамматикалық қызметтер.")
        InfoCard("🤖 ИИ-ұстаз", "Бұл бөлім әзірге дайындық режимінде. ИИ сервисі әлі қосылған жоқ.")
        Text("Алғашқы нұсқа · 0.1.0", color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
private fun SarfScreen(modifier: Modifier = Modifier) {
    val lessons = listOf(
        Triple("فِعْلٌ مَاضٍ", "كَتَبَ", "Өткен шақ: ол жазды."),
        Triple("فِعْلٌ مُضَارِعٌ", "يَكْتُبُ", "Осы/келер шақ: ол жазады."),
        Triple("فِعْلُ أَمْرٍ", "اُكْتُبْ", "Бұйрық рай: жаз!"),
        Triple("مَصْدَرٌ", "كِتَابَةٌ", "Қимыл атауы: жазу.")
    )
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Сарф — الصَّرْفُ", fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        item { Text("Мысал: كَتَبَ — يَكْتُبُ — اُكْتُبْ — كِتَابَةٌ") }
        items(lessons) { item -> InfoCard(item.first + " · " + item.second, item.third) }
        item { Text("Ескерту: бұл бастапқы оқу мысалдары; әр сөздің формасы контекстке қарай түсіндіріледі.") }
    }
}

@Composable
private fun NahwScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Нахв — النَّحْوُ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        InfoCard("مُبْتَدَأٌ", "Бастауыш есімді сөйлемнің басында келетін мүбтәдә. Мысал: الطَّالِبُ مُجْتَهِدٌ")
        InfoCard("خَبَرٌ", "Хабар — мүбтәдә туралы мәлімет. Мысал: الطَّالِبُ مُجْتَهِدٌ — студент ынталы.")
        InfoCard("فَاعِلٌ", "Іс-әрекетті орындаушы. Мысал: كَتَبَ الطَّالِبُ — студент жазды.")
        Text("Толық иғраб пен күрделі ережелер келесі нұсқаларда кеңейтіледі.")
    }
}

@Composable
private fun TutorScreen(modifier: Modifier = Modifier) {
    var question by remember { mutableStateOf("") }
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("ИИ-ұстаз", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Мұнда сарф немесе нахв бойынша сұрақ жаза аласың. Бұл бастапқы нұсқада сұрақ телефоннан шықпайды және ИИ сервисіне жіберілмейді.")
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Сұрағыңды жаз…") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
        Button(onClick = { }, enabled = false, modifier = Modifier.fillMaxWidth()) {
            Text("ИИ-ден сұрау (әлі қосылмаған)")
        }
        Text("ИИ қосу үшін қауіпсіз серверлік делдал және API сервисін баптау қажет. Құпия API кілтін APK ішіне салуға болмайды.")
    }
}

@Composable
private fun QuizScreen(modifier: Modifier = Modifier) {
    var answer by remember { mutableStateOf<Int?>(null) }
    var checked by remember { mutableStateOf(false) }
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Шағын тест", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("كَتَبَ сөзінің мағынасы қандай?")
        listOf("Ол жазады", "Ол жазды", "Жаз!").forEachIndexed { index, option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = answer == index, onClick = { answer = index; checked = false })
                Text(option)
            }
        }
        Button(onClick = { checked = true }, enabled = answer != null) { Text("Тексеру") }
        if (checked) {
            Text(if (answer == 1) "Дұрыс! كَتَبَ — ол жазды." else "Қайта байқап көр: كَتَبَ — өткен шақ.")
        }
        OutlinedButton(onClick = { answer = null; checked = false }) { Text("Қайта бастау") }
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(body)
        }
    }
}
