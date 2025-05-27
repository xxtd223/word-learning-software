package com.peter.landing.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.exam.Exam
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor() : ViewModel() {

    private val _uiState = mutableStateOf<ExamUiState>(ExamUiState.Loading)
    val uiState: State<ExamUiState> = _uiState

    private val _currentExamSet = MutableStateFlow(0)
    val currentExamSet = _currentExamSet.asStateFlow()

    private val _examSets = listOf(
        createCet4ExamSet1(),
        createCet4ExamSet2(),
        createCet6ExamSet1(),
        createCet6ExamSet2()
    )

    init {
        loadExam(0)
    }

    fun loadExam(setIndex: Int) {
        viewModelScope.launch {
            try {
                _currentExamSet.value = setIndex
                _uiState.value = ExamUiState.Success(_examSets[setIndex])
            } catch (e: Exception) {
                _uiState.value = ExamUiState.Error(DataResult.Error.Code.UNKNOWN)
            }
        }
    }

    fun nextExamSet() {
        val nextIndex = (_currentExamSet.value + 1) % _examSets.size
        loadExam(nextIndex)
    }

    fun prevExamSet() {
        val prevIndex = if (_currentExamSet.value > 0) _currentExamSet.value - 1 else _examSets.size - 1
        loadExam(prevIndex)
    }

    private fun createCet4ExamSet1(): Exam {
        return Exam(
            title = "大学英语四级考试真题 - 阅读理解 (Set 1)",
            level = Exam.Level.CET4,
            questions = listOf(
                Exam.Question(
                    id = 1,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "Reading Comprehension - Passage 1\n\nReading is an important skill for academic success. It is the foundation for learning and studying in most academic disciplines. When students read effectively, they can understand and analyze complex information, extract key ideas, and relate new knowledge to what they already know.\n\nWhat is the main idea of this passage?",
                    options = listOf(
                        "Reading is difficult for most students",
                        "Reading is essential for academic achievement",
                        "Students should read more fiction",
                        "Academic disciplines require different reading methods"
                    ),
                    correctAnswers = listOf(1),
                    explanation = "The passage emphasizes the importance of reading for academic success, stating it's a foundation for learning."
                ),
                Exam.Question(
                    id = 2,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "According to the passage, effective reading allows students to:",
                    options = listOf(
                        "Avoid studying other subjects",
                        "Read faster than their peers",
                        "Comprehend and analyze complex information",
                        "Memorize entire textbooks"
                    ),
                    correctAnswers = listOf(2),
                    explanation = "The passage states that effective reading enables students to 'understand and analyze complex information'."
                ),
                Exam.Question(
                    id = 3,
                    type = Exam.QuestionType.MULTIPLE_CHOICE,
                    text = "Reading Comprehension - Passage 2\n\nTechnology has transformed education in numerous ways. Online resources provide instant access to information that previously required trips to the library. Digital tools enable more interactive and personalized learning experiences. However, technology also presents challenges such as digital distractions and concerns about screen time.\n\nBased on the passage, which of the following are benefits of technology in education? (Select all that apply)",
                    options = listOf(
                        "Quick access to information",
                        "Interactive learning experiences",
                        "Reduced need for teachers",
                        "Personalized learning opportunities"
                    ),
                    correctAnswers = listOf(0, 1, 3),
                    explanation = "The passage mentions instant access to information, interactive learning, and personalized learning as benefits. It doesn't suggest technology reduces the need for teachers."
                )
            )
        )
    }

    private fun createCet4ExamSet2(): Exam {
        return Exam(
            title = "大学英语四级考试真题 - 阅读理解 (Set 2)",
            level = Exam.Level.CET4,
            questions = listOf(
                Exam.Question(
                    id = 1,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "Reading Comprehension - Passage 1\n\nClimate change is altering weather patterns worldwide. Scientists have observed rising temperatures, changing precipitation patterns, and more frequent extreme weather events. These changes affect agriculture, water resources, and human health. Addressing climate change requires both mitigation strategies to reduce greenhouse gas emissions and adaptation measures to deal with unavoidable impacts.\n\nWhat is the main focus of this passage?",
                    options = listOf(
                        "Predicting future weather patterns",
                        "The economic costs of climate change",
                        "How climate change is affecting the world",
                        "Solutions to eliminate climate change"
                    ),
                    correctAnswers = listOf(2),
                    explanation = "The passage primarily describes how climate change is altering weather and affecting various aspects of life globally."
                ),
                Exam.Question(
                    id = 2,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "According to the passage, addressing climate change requires:",
                    options = listOf(
                        "Only reducing greenhouse gas emissions",
                        "Only adapting to current changes",
                        "Both mitigation and adaptation strategies",
                        "Changing agricultural practices exclusively"
                    ),
                    correctAnswers = listOf(2),
                    explanation = "The passage states that addressing climate change requires 'both mitigation strategies to reduce greenhouse gas emissions and adaptation measures'."
                ),
                Exam.Question(
                    id = 3,
                    type = Exam.QuestionType.MULTIPLE_CHOICE,
                    text = "Reading Comprehension - Passage 2\n\nNutrition plays a vital role in maintaining good health. A balanced diet provides the body with essential nutrients needed for optimal functioning. Proteins build and repair tissues, carbohydrates supply energy, and fats support cell growth. Vitamins and minerals, though needed in smaller amounts, are crucial for various bodily processes.\n\nBased on the passage, which of the following are functions of nutrients in the body? (Select all that apply)",
                    options = listOf(
                        "Building and repairing tissues",
                        "Providing energy",
                        "Supporting cell growth",
                        "Preventing all diseases"
                    ),
                    correctAnswers = listOf(0, 1, 2),
                    explanation = "The passage mentions that proteins build and repair tissues, carbohydrates supply energy, and fats support cell growth. It doesn't claim nutrients prevent all diseases."
                )
            )
        )
    }

    private fun createCet6ExamSet1(): Exam {
        return Exam(
            title = "大学英语六级考试真题 - 阅读理解 (Set 1)",
            level = Exam.Level.CET6,
            questions = listOf(
                Exam.Question(
                    id = 1,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "Reading Comprehension - Passage 1\n\nArtificial intelligence is rapidly advancing and transforming various industries. Machine learning algorithms now power recommendation systems, autonomous vehicles, and medical diagnostic tools. While these technologies offer tremendous benefits, they also raise important ethical questions about privacy, bias, and the future of work. As AI capabilities continue to grow, society must establish frameworks to ensure these technologies serve human welfare.\n\nWhat is the main purpose of this passage?",
                    options = listOf(
                        "To criticize the development of artificial intelligence",
                        "To explain how machine learning algorithms work",
                        "To discuss AI's impact and associated ethical considerations",
                        "To predict which jobs will be replaced by AI"
                    ),
                    correctAnswers = listOf(2),
                    explanation = "The passage provides an overview of AI's advancement, its applications, benefits, and ethical concerns, with an emphasis on balanced development."
                ),
                Exam.Question(
                    id = 2,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "According to the passage, which of the following is NOT mentioned as an application of machine learning?",
                    options = listOf(
                        "Recommendation systems",
                        "Weather forecasting",
                        "Self-driving vehicles",
                        "Medical diagnostics"
                    ),
                    correctAnswers = listOf(1),
                    explanation = "The passage mentions recommendation systems, autonomous vehicles, and medical diagnostic tools as applications of machine learning, but not weather forecasting."
                ),
                Exam.Question(
                    id = 3,
                    type = Exam.QuestionType.MULTIPLE_CHOICE,
                    text = "Reading Comprehension - Passage 2\n\nGlobalization has connected economies and cultures across the world. International trade has expanded dramatically, allowing consumers access to products from anywhere. Digital communications enable instant connections regardless of distance. While globalization has created economic opportunities and cultural exchange, critics point to growing inequality, cultural homogenization, and environmental concerns.\n\nBased on the passage, which of the following are effects of globalization? (Select all that apply)",
                    options = listOf(
                        "Increased international trade",
                        "Enhanced digital communication",
                        "Greater economic equality worldwide",
                        "Concerns about cultural homogenization"
                    ),
                    correctAnswers = listOf(0, 1, 3),
                    explanation = "The passage mentions expanded international trade, digital communications enabling connections, and concerns about cultural homogenization. It actually states critics point to 'growing inequality', not greater equality."
                )
            )
        )
    }

    private fun createCet6ExamSet2(): Exam {
        return Exam(
            title = "大学英语六级考试真题 - 阅读理解 (Set 2)",
            level = Exam.Level.CET6,
            questions = listOf(
                Exam.Question(
                    id = 1,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "Reading Comprehension - Passage 1\n\nThe concept of sustainable development aims to meet present needs without compromising future generations' ability to meet their own needs. It requires balancing economic growth, environmental protection, and social well-being. Sustainable practices include renewable energy adoption, responsible resource management, and equitable social policies. As global challenges like climate change intensify, sustainability has become central to international policy discussions.\n\nWhat is the central theme of this passage?",
                    options = listOf(
                        "Economic growth is more important than environmental protection",
                        "Renewable energy is the only solution to climate change",
                        "The balance between present needs and future capabilities",
                        "International policies are ineffective at addressing sustainability"
                    ),
                    correctAnswers = listOf(2),
                    explanation = "The passage focuses on sustainable development as meeting present needs while preserving future generations' abilities, emphasizing balance among economic, environmental, and social factors."
                ),
                Exam.Question(
                    id = 2,
                    type = Exam.QuestionType.SINGLE_CHOICE,
                    text = "According to the passage, sustainable development requires balancing:",
                    options = listOf(
                        "Present and past considerations",
                        "Economic, environmental, and social factors",
                        "National and international interests only",
                        "Renewable and non-renewable resources"
                    ),
                    correctAnswers = listOf(1),
                    explanation = "The passage explicitly states that sustainable development 'requires balancing economic growth, environmental protection, and social well-being.'"
                ),
                Exam.Question(
                    id = 3,
                    type = Exam.QuestionType.MULTIPLE_CHOICE,
                    text = "Reading Comprehension - Passage 2\n\nNeuroscience research has revolutionized our understanding of how the brain functions. Advanced imaging techniques allow scientists to observe neural activity in real-time. Studies have revealed the brain's remarkable plasticity—its ability to reorganize by forming new neural connections. This knowledge has implications for education, mental health treatment, and rehabilitation after brain injuries.\n\nBased on the passage, which of the following are true about brain research? (Select all that apply)",
                    options = listOf(
                        "Modern technology enables real-time observation of brain activity",
                        "The brain can form new neural connections",
                        "Neuroscience findings are irrelevant to education",
                        "Brain research has applications in rehabilitation"
                    ),
                    correctAnswers = listOf(0, 1, 3),
                    explanation = "The passage states that advanced imaging allows real-time observation of neural activity, studies have revealed the brain's plasticity (ability to form new connections), and this knowledge has implications for rehabilitation. It specifically mentions relevance to education, contradicting the third option."
                )
            )
        )
    }
}

sealed class ExamUiState {
    object Loading : ExamUiState()
    data class Success(val exam: Exam) : ExamUiState()
    data class Error(val code: DataResult.Error.Code) : ExamUiState()
} 