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
import com.example.android.tv_trivia.databinding.FragmentBrooklynBinding

class BrooklynFragment : Fragment() {
    data class Question(
            val text: String,
            val answers: List<String>)

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (Or better yet, don't define the questions in code...)
    private val questions: MutableList<Question> = mutableListOf(
            Question(text = "Jake and Amy have a bet about number of arrests. What is the final score?",
                    answers = listOf("Jake: 93 Amy: 84", "Jake: 87 Amy: 78", "Jake: 103 Amy: 94", "Jake: 99 Amy: 90")),
            Question(text = "What is Jake's traditional Thanksgiving food?",
                    answers = listOf("Mayonnaise and peanuts", "Everything bagel", "Floor cheerios", "Peanut butter straight from the jar")),
            Question(text = "How many maximum hours did Rosa continuously sit on a chair?",
                    answers = listOf("32", "16", "24", "48")),
            Question(text = "How does Charles signal Jake if he's going full Boyle?",
                    answers = listOf("Imitates laser guns \"Pew Pew\"", "Hi-Fives", "He yells \"Tippy Toe\"", "He yells \"Jerrico!\"")),
            Question(text = "Which chesspiece did Gina take with her as a remembrance?",
                    answers = listOf("Pawn", "Queen", "King", "Bishop")),
            Question(text = "Who was Amy's worst date?",
                    answers = listOf("Her aunt's dentist", "Her mom's friend's son", "Johnny", "Jake")),
            Question(text = "Where was the cop con held?",
                    answers = listOf("Rochester", "Texas", "LA", "New Jersey")),
            Question(text = "Who was Jake's undercover best friend?",
                    answers = listOf("Derrick", "Kyle", "Austin", "Antonio")),
            Question(text = "What wasn't a name proposed by Charles for the duo??",
                    answers = listOf("The Dusky Knights", "The Dark Stallions", "The Night Boys", "The Midnight Men")),
            Question(text = "What song is playing as Jake walks in slow-mo wearing Doug Judy's father's white suit?",
                    answers = listOf("Mama said knock you out", "Motownphily", "What a man", "So what'cha want"))
    )



    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentBrooklynBinding>(
                inflater, R.layout.fragment_brooklyn, container, false)

        // Shuffles the questions and sets the question index to the first question.
        randomizeQuestions()

        // Bind this fragment class to the layout
        binding.brooklyn = this

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
                                .navigate(BrooklynFragmentDirections.actionBrooklynFragmentToGameWonFragment(numQuestions, questionIndex))
                    }
                } else {
                    // Game over! A wrong answer sends us to the gameOverFragment.
                    view.findNavController()
                            .navigate(BrooklynFragmentDirections.actionBrooklynFragmentToGameOverFragment(numQuestions, questionIndex))
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
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_brooklyn_trivia_question, questionIndex + 1, numQuestions)
    }
}
