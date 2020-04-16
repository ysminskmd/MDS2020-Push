package com.yandex.academy.mobdev.push

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.push.YandexMetricaPush
import kotlinx.android.synthetic.main.fragment_poll.view.*

const val keyGreeting = "greeting"

@Suppress("unused")
class PollFragment : Fragment(R.layout.fragment_poll) {

    private var questionIndex = 0
    private val questions = listOf(
        Triple(R.string.question_1, R.string.answer_1_1, R.string.answer_1_2),
        Triple(R.string.question_2, R.string.answer_2_1, R.string.answer_2_2),
        Triple(R.string.question_3, R.string.answer_3_1, R.string.answer_3_2),
        Triple(R.string.question_4, R.string.answer_4_1, R.string.answer_4_2),
        Triple(R.string.question_5, R.string.answer_5_1, R.string.answer_5_2)
    )

    private val items = arrayListOf<PollItem>()
    private val adapter = PollAdapter { answer ->
        YandexMetrica.reportEvent(getString(answer))
        items[items.lastIndex] = PollItem.Answer(questionIndex * 3L + 2, answer)
        nextQuestion()
    }

    init {
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.poll.adapter = adapter
        val toolbar = view.toolbar
        toolbar.inflateMenu(R.menu.menu_main)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.clear -> {
                    items.clear()
                    questionIndex = 0
                    nextQuestion()
                    true
                }
                else -> false
            }
        }
        if (savedInstanceState == null) {
            val context = view.context.applicationContext
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            activity?.intent?.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD)?.let { greeting ->
                items.add(PollItem.Question(-2, greeting))
            }
            prefs.getString(keyGreeting, null)?.let { greeting ->
                prefs.edit().remove(keyGreeting).apply()
                items.add(PollItem.Question(-1, greeting))
            }
            nextQuestion()
        }
    }

    private fun nextQuestion() {
        val question = questions[questionIndex.coerceAtMost(questions.lastIndex)]
        items.add(PollItem.Question(questionIndex * 3L, requireContext().getString(question.first)))
        items.add(PollItem.Select(questionIndex * 3L + 1, question.second, question.third))
        ++questionIndex
        adapter.items = items.toList()
        requireView().poll.smoothScrollToPosition(items.lastIndex)
    }
}