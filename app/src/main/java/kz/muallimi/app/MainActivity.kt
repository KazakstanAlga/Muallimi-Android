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

private data class QuizQuestion(
    val prompt: String,
    val options: List<String>,
    val correct: Int,
    val explanation: String
)

private val quizQuestions = listOf(
    QuizQuestion("Что означает كِتَابٌ؟", listOf("Студент", "Книга", "Учитель"), 1,
        "كِتَابٌ означает «книга»."),
    QuizQuestion("كَتَبَ — это…", listOf("Он написал", "Он пишет", "Напиши!"), 0,
        "كَتَبَ — глагол прошедшего времени: «он написал»."),
    QuizQuestion("Как сказать «он пишет»?", listOf("اُكْتُبْ", "يَكْتُبُ", "كَاتِبٌ"), 1,
        "يَكْتُبُ — «он пишет»; اُكْتُبْ — «напиши!»."),
    QuizQuestion("Что означает طَالِبٌ؟", listOf("Студент", "Школа", "Дом"), 0,
        "طَالِبٌ означает «студент, учащийся»."),
    QuizQuestion("Какое окончание у слова после предлога بِ в примере بِالطَّالِبِ؟",
        listOf("Дамма ـُ", "Фатха ـَ", "Касра ـِ"), 2,
        "После بِ имя стоит в родительном падеже: بِالطَّالِبِ.")
)

@Composable
fun MuallimiApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var quizIndex by remember { mutableIntStateOf(0) }
    var quizScore by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var answered by remember { mutableStateOf(false) }
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
                Text("Учись постепенно • تَعَلَّمْ بِالتَّدْرِيجِ", fontSize = 14.sp)
                Spacer(Modifier.height(22.dp))
                when (page) {
                    Page.HOME -> {
                        Text("Главное меню", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        MenuButton("📗 Сарф — الصَّرْفُ") { page = Page.SARF }
                        MenuButton("📘 Нахв — النَّحْوُ") { page = Page.NAHW }
                        MenuButton("📚 Словарь") { page = Page.VOCAB }
                        MenuButton("📝 Тест из 5 вопросов") {
                            page = Page.QUIZ
                            quizIndex = 0
                            quizScore = 0
                            selectedAnswer = -1
                            answered = false
                        }
                        MenuButton("🤖 Вопрос преподавателю") { page = Page.TEACHER }
                        Spacer(Modifier.height(12.dp))
                        Text("Уроки и тест работают офлайн. Онлайн-ИИ пока не подключён.", fontSize = 13.sp)
                    }
                    Page.SARF -> LessonPage(
                        "САРФ — الصَّرْفُ",
                        listOf(
                            "Урок 1. Одна основа — разные формы",
                            "Сарф изучает строение слова и изменение его формы.",
                            "Возьмём корень ك ت ب, связанный с письмом.",
                            "كَتَبَ — он написал (فِعْلٌ مَاضٍ — прошедшее время).",
                            "يَكْتُبُ — он пишет (فِعْلٌ مُضَارِعٌ — настоящее/будущее).",
                            "اُكْتُبْ — напиши! (فِعْلُ الْأَمْرِ — повелительная форма).",
                            "كِتَابٌ — книга; كَاتِبٌ — пишущий, писатель.",
                            "مَكْتُوبٌ — написанное; كِتَابَةٌ — письмо, написание.",
                            "Запомни: форма слова меняется, а общий смысловой корень остаётся связанным с письмом.",
                            "Самопроверка: какая форма означает «напиши!»? Ответ: اُكْتُبْ."
                        )
                    )
                    Page.NAHW -> LessonPage(
                        "НАХВ — النَّحْوُ",
                        listOf(
                            "Урок 1. Роль слова и окончания",
                            "Нахв изучает положение слова в предложении и его грамматическое окончание.",
                            "جَاءَ الطَّالِبُ — Пришёл студент.",
                            "رَأَيْتُ الطَّالِبَ — Я увидел студента.",
                            "مَرَرْتُ بِالطَّالِبِ — Я прошёл мимо студента.",
                            "В этих примерах слово الطالب меняет окончание в зависимости от роли.",
                            "الطَّالِبُ — дамма ـُ: именительный падеж в первом примере.",
                            "الطَّالِبَ — фатха ـَ: винительный падеж во втором примере.",
                            "بِالطَّالِبِ — касра ـِ после предлога بِ.",
                            "Обрати внимание: предлог بِ влияет на падеж следующего имени."
                        )
                    )
                    Page.VOCAB -> LessonPage(
                        "СЛОВАРЬ — الْمُفْرَدَاتُ",
                        listOf(
                            "Арабский — транскрипция — русский",
                            "كِتَابٌ — kitābun — книга",
                            "طَالِبٌ — ṭālibun — студент",
                            "مُعَلِّمٌ — muʿallimun — учитель",
                            "دَرْسٌ — darsun — урок",
                            "عِلْمٌ — ʿilmun — знание",
                            "مَدْرَسَةٌ — madrasatun — школа",
                            "قَلَمٌ — qalamun — ручка",
                            "بَيْتٌ — baytun — дом",
                            "كَتَبَ — kataba — он написал",
                            "يَكْتُبُ — yaktubu — он пишет",
                            "قَرَأَ — qara'a — он прочитал",
                            "ذَهَبَ — dhahaba — он пошёл",
                            "Читайте арабские формы с харакатами и повторяйте их вслух."
                        )
                    )
                    Page.QUIZ -> {
                        if (quizIndex < quizQuestions.size) {
                            val q = quizQuestions[quizIndex]
                            Text("Мини-тест", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text("Вопрос ${quizIndex + 1} из ${quizQuestions.size} • Баллы: $quizScore")
                            Spacer(Modifier.height(12.dp))
                            Text(q.prompt, fontSize = 22.sp, lineHeight = 30.sp)
                            Spacer(Modifier.height(8.dp))
                            q.options.forEachIndexed { index, option ->
                                OutlinedButton(
                                    onClick = {
                                        if (!answered) {
                                            selectedAnswer = index
                                            answered = true
                                            if (index == q.correct) quizScore++
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    enabled = !answered
                                ) { Text(option, fontSize = 17.sp) }
                            }
                            if (answered) {
                                Text(
                                    (if (selectedAnswer == q.correct) "صَحِيحٌ — Верно! " else "Не совсем. ") + q.explanation,
                                    Modifier.padding(vertical = 12.dp),
                                    fontSize = 17.sp,
                                    lineHeight = 24.sp
                                )
                                Button(onClick = {
                                    quizIndex++
                                    selectedAnswer = -1
                                    answered = false
                                }, modifier = Modifier.fillMaxWidth()) {
                                    Text(if (quizIndex == quizQuestions.lastIndex) "Результат" else "Следующий вопрос")
                                }
                            }
                        } else {
                            Text("Тест завершён!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                            Text("Твой результат: $quizScore из ${quizQuestions.size}", fontSize = 22.sp)
                            Text(
                                when (quizScore) {
                                    5 -> "مُمْتَازٌ — Отлично! Все ответы верны."
                                    in 3..4 -> "جَيِّدٌ — Хорошая работа! Повтори ошибки."
                                    else -> "لَا تَيْأَسْ — Не сдавайся. Повтори уроки и попробуй ещё раз."
                                },
                                Modifier.padding(12.dp), fontSize = 18.sp
                            )
                            Button(onClick = {
                                quizIndex = 0
                                quizScore = 0
                                selectedAnswer = -1
                                answered = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Пройти ещё раз") }
                        }
                    }
                    Page.TEACHER -> {
                        Text("Вопрос преподавателю", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Напиши вопрос по арабскому языку, сарфу или нахву. Сейчас ответ работает офлайн и распознаёт несколько тем; свободный онлайн-ИИ ещё не подключён.")
                        OutlinedTextField(
                            value = question, onValueChange = { question = it },
                            label = { Text("Твой вопрос") }, modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                        Button(onClick = {
                            teacherReply = when {
                                question.contains("كَتَبَ") || question.contains("كتب") ->
                                    "كَتَبَ — فعل ماضٍ: глагол прошедшего времени, «он написал». Настоящее время: يَكْتُبُ. Повелительная форма: اُكْتُبْ."
                                question.contains("الطالب") || question.contains("الطَّالِب") ->
                                    "الطَّالِبُ — именительный падеж; الطَّالِبَ — винительный; الطَّالِبِ — родительный. Точное окончание зависит от роли слова."
                                question.contains("сарф", true) ->
                                    "Сарф изучает формы слов. Пример: كَتَبَ — он написал; يَكْتُبُ — он пишет; اُكْتُبْ — напиши!"
                                question.contains("нахв", true) ->
                                    "Нахв изучает роль слов в предложении и их окончания. Сравни: جَاءَ الطَّالِبُ، رَأَيْتُ الطَّالِبَ، بِالطَّالِبِ."
                                question.contains("كِتَاب") || question.contains("كتاب") ->
                                    "كِتَابٌ означает «книга». Слово из того же корня: كَتَبَ — «он написал»."
                                else ->
                                    "В этой версии пока нет подключения к онлайн-ИИ. Попробуй спросить о كَتَبَ, كِتَابٌ, الطَّالِبُ, сарфе или нахве."
                            }
                        }, modifier = Modifier.fillMaxWidth()) { Text("Объяснить") }
                        if (teacherReply.isNotEmpty()) Text(teacherReply, Modifier.padding(top = 12.dp), fontSize = 17.sp, lineHeight = 25.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
                OutlinedButton(onClick = { page = Page.HOME }, modifier = Modifier.fillMaxWidth()) {
                    Text("⌂ Главное меню")
                }
                Spacer(Modifier.height(8.dp))
                Text("مُعَلِّمِي • Версия 0.2", fontSize = 12.sp)
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
            lineHeight = 26.sp
        )
        if (index == 0) HorizontalDivider(Modifier.padding(vertical = 5.dp))
    }
}