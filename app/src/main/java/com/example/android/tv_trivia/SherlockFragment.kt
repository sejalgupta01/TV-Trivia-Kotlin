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
import com.example.android.tv_trivia.databinding.FragmentSherlockBinding

class SherlockFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Moriarty favours suits by which British designer?",
                    answers = listOf("Vivienne Westwood", "Alexander McQueen", "Paul Smith", "Burberry")),
            Question(text = "Sherlock can never remember Lestrade's first name. But what is it??",
                    answers = listOf("Greg", "Graham", "Gary", "Gavin")),
            Question(text = "What is the last name of the man who introduces Dr. Watson to Sherlock?",
                    answers = listOf("Stamford", "Welsey", "Anderson", "Scott")),
            Question(text = "In what episode does Sherlock first don the Deepstalker Hat?",
                    answers = listOf("A Scandal in Belgravia", "The Great Game", "The Hounds Of Baskerville", "The Reichenbach Fall")),
            Question(text = "In the beginning of \"The Blind Banker\" John runs into trouble with which device?",
                    answers = listOf("Self checkout at the Grocery Store", "ATM", "Rotary Telephone", "Vending Machine")),
            Question(text = "What is the name of the fictional Gentlemen's Club featured in the series?",
                    answers = listOf("The Diogenes Club", "The Commonwealth Club", "The Reform Club", "The Reformers")),
            Question(text = "What is the name of the security prison facility which holds Eurus Holmes in Season 4?",
                    answers = listOf("Sherrinford", "Azkaban", "Derrington", "Aschecliffe")),
            Question(text = "What name does Moriarty use for his actor persona in \"The Reichenbach Fall\"?",
                    answers = listOf("Richard Brook", "Andrew Scott", "Bill Wiggins", "Henry Knight")),
            Question(text = "What does the \"O\" in \"H. O. U. N. D\" stand for in \"The Hounds of Baskerville\'?",
                    answers = listOf("O'Mara", "O\'Reilly", "Olsen", "O\'Brien")),
            Question(text = "What was the name of Charles Augustus Magnussen\'s mansion?",
                    answers = listOf("Appledore", "Redbeard", "Butterbear", "Manderley"))
    )



    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSherlockBinding>(
                inflater, R.layout.fragment_sherlock, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.sherlock = this

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
                                .navigate(SherlockFragmentDirections.actionSherlockFragmentToGameWonFragment(numQuestions, questionIndex))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController()
                            .navigate(SherlockFragmentDirections.actionSherlockFragmentToGameOverFragment(numQuestions, questionIndex))
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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_sherlock_trivia_question, questionIndex + 1, numQuestions)
    }
}
