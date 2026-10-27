package kz.muallimi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MuallimiApp() }
    }
}

private enum class Page { HOME, SARF, NAHW, VOCAB, QUIZ, TEACHER }

@Composable
fun MuallimiApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var quizAnswer by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var teacherReply by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("مُعَلِّمِي", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Text("Арабский язык: сарф и нахв", fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text("Учись постепенно • تَعَلَّمْ بِالتَّدْرِيجِ", fontSize = 14.sp)
                Spacer(Modifier.height(22.dp))
                when (page) {
                    Page.HOME -> {
                        Text("Главное меню", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        MenuButton("📗 Сарф — الصَّرْفُ") { page = Page.SARF }
                        MenuButton("📘 Нахв — النَّحْوُ") { page = Page.NAHW }
                        MenuButton("📚 Словарь") { page = Page.VOCAB }
                        MenuButton("📝 Проверка знаний") { page = Page.QUIZ; quizAnswer = "" }
                        MenuButton("🤖 Вопрос преподавателю") { page = Page.TEACHER }
                        Spacer(Modifier.height(12.dp))
                        Text("Первая версия работает офлайн. Онлайн-ИИ пока не подключён.", fontSize = 13.sp)
                    }
                    Page.SARF -> LessonPage(
                        "САРФ — الصَّرْفُ",
                        listOf(
                            "Что изучает сарф?",
                            "Сарф изучает строение слова и изменение его формы.",
                            "Корень ك ت ب связан с письмом:",
                            "كَتَبَ — он написал",
                            "يَكْتُبُ — он пишет",
                            "اُكْتُبْ — напиши!",
                            "كَاتِبٌ — пишущий; писатель",
                            "مَكْتُوبٌ — написанное"
                        )
                    )
                    Page.NAHW -> LessonPage(
                        "НАХВ — النَّحْوُ",
                        listOf(
                            "Нахв изучает роль слова в предложении и окончания.",
                            "جَاءَ الطَّالِبُ — Пришёл студент.",
                            "رَأَيْتُ الطَّالِبَ — Я увидел студента.",
                            "مَرَرْتُ بِالطَّالِبِ — Я прошёл мимо студента.",
                            "الطَّالِبُ — дамма: подлежащее/فاعل в примере.",
                            "الطَّالِبَ — фатха: прямое дополнение.",
                            "بِالطَّالِبِ — касра после предлога بِ."
                        )
                    )
                    Page.VOCAB -> LessonPage(
                        "СЛОВАРЬ — الْمُفْرَدَاتُ",
                        listOf(
                            "كِتَابٌ — kitābun — книга",
                            "طَالِبٌ — ṭālibun — студент",
                            "مُعَلِّمٌ — muʿallimun — учитель",
                            "دَرْسٌ — darsun — урок",
                            "عِلْمٌ — ʿilmun — знание",
                            "مَدْرَسَةٌ — madrasatun — школа",
                            "قَلَمٌ — qalamun — ручка",
                            "بَيْتٌ — baytun — дом",
                            "قَرَأَ — qara'a — он прочитал",
                            "ذَهَبَ — dhahaba — он пошёл"
                        )
                    )
                    Page.QUIZ -> {
                        Text("Мини-тест", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        Text("Что означает كِتَابٌ ?", fontSize = 20.sp)
                        listOf("Студент", "Книга", "Учитель").forEach { option ->
                            Button(
                                onClick = { quizAnswer = if (option == "Книга") "صَحِيحٌ — Верно! Это «книга»." else "حَاوِلْ مَرَّةً أُخْرَى — Попробуй ещё раз." },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) { Text(option) }
                        }
                        if (quizAnswer.isNotEmpty()) Text(quizAnswer, Modifier.padding(12.dp), fontSize = 18.sp)
                    }
                    Page.TEACHER -> {
                        Text("Вопрос преподавателю", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Напиши вопрос по-арабски, сарфу или нахву. В этой версии ответ формируется только для примеров ниже; интернет-ИИ ещё не подключён.")
                        OutlinedTextField(
                            value = question, onValueChange = { question = it },
                            label = { Text("Твой вопрос") }, modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                        Button(onClick = {
                            teacherReply = when {
                                question.contains("كَتَبَ") || question.contains("كتب") ->
                                    "كَتَبَ — فعل ماضٍ: глагол прошедшего времени, «он написал». Настоящее время: يَكْتُبُ."
                                question.contains("الطالب") || question.contains("الطَّالِب") ->
                                    "الطَّالِبُ — студент (именительный падеж); الطَّالِبَ — винительный; الطَّالِبِ — родительный. Точное окончание зависит от роли слова."
                                question.contains("сарф", true) ->
                                    "Сарф изучает формы слов: كَتَبَ، يَكْتُبُ، اُكْتُبْ. Выбери раздел «Сарф» для первого урока."
                                question.contains("нахв", true) ->
                                    "Нахв изучает роль слов в предложении и их окончания. Выбери раздел «Нахв» для примеров."
                                else ->
                                    "Пока доступен встроенный справочник без онлайн-ИИ. Попробуй спросить о كَتَبَ или الطَّالِبُ, либо открой разделы «Сарф» и «Нахв»."
                            }
                        }, modifier = Modifier.fillMaxWidth()) { Text("Объяснить") }
                        if (teacherReply.isNotEmpty()) Text(teacherReply, Modifier.padding(top = 12.dp), fontSize = 17.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
                OutlinedButton(onClick = { page = Page.HOME }, modifier = Modifier.fillMaxWidth()) {
                    Text("⌂ Главное меню")
                }
                Spacer(Modifier.height(8.dp))
                Text("مُعَلِّمِي • Версия 0.1", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun MenuButton(label: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text(label, fontSize = 17.sp)
    }
}

@Composable
private fun LessonPage(title: String, lines: List<String>) {
    Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    lines.forEachIndexed { index, line ->
        Text(
            line,
            modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
            fontSize = if (index == 0) 18.sp else 17.sp,
            fontWeight = if (index == 0) FontWeight.SemiBold else FontWeight.Normal,
            lineHeight = 25.sp
        )
        if (index == 0) HorizontalDivider(Modifier.padding(vertical = 5.dp))
    }
}