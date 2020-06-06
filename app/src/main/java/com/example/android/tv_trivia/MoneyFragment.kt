/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.tv_trivia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.tv_trivia.databinding.FragmentMoneyBinding

class MoneyFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "How many gunshots did Gandia give Nairobi?",
                    answers = listOf("2", "1", "3", "4")),
            Question(text = "Which country was Rio taken to after he was caught?",
                    answers = listOf("Algeria", "Nigeria", "Albania", "Argentina")),
            Question(text = "How many heists had Berlin pulled off before the Royal Mint heist?",
                    answers = listOf("27", "23", "18", "25")),
            Question(text = "What was the name of Alicia's husband?",
                    answers = listOf("German", "Alberto", "Herman", "Antonanzas")),
            Question(text = "What was Marseille's dead dog's name?",
                    answers = listOf("Pamuk", "Miguel", "Sofia", "Matias")),
            Question(text = "What was Helsinki's real name in the show?",
                    answers = listOf("Mirko Dragic", "Dmitri Mirkov", "Darko Peric", "Hovic Keuchkerian")),
            Question(text = "What was the relationship between Moscow and Manila?",
                    answers = listOf("God Son", "Newphew", "God Daughter", "Niece")),
            Question(text = "What was Angel's wife's name?",
                    answers = listOf("Mari", "Laura", "Maria", "Lauren")),
            Question(text = "What was the name of Nairobi's son?",
                    answers = listOf("Axel", "Manuel", "Cincinnati", "Ibiza")),
            Question(text = "How many hostages escaped during the heist?",
                    answers = listOf("16", "10", "14", "8"))
    )



    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentMoneyBinding>(
                inflater, R.layout.fragment_money, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.money = this

        // Set the onClickListener for the submitButton
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // Advance to the next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We've won!  Navigate to the gameWonFragment.
                        view.findNavController()
                                .navigate(MoneyFragmentDirections.actionMoneyFragmentToGameWonFragment(numQuestions, questionIndex))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController()
                            .navigate(MoneyFragmentDirections.actionMoneyFragmentToGameOverFragment(numQuestions, questionIndex))
                }
            }
        }
        return binding.root
    }

    // randomize the questions and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        // and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_money_trivia_question, questionIndex + 1, numQuestions)
    }
}
