package com.twtstudio.service.tjwhm.exam.user.history

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twt.wepeiyang.commons.experimental.cache.RefreshState
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.startProblemActivity
import com.twtstudio.service.tjwhm.exam.commons.toMode
import com.twtstudio.service.tjwhm.exam.commons.toProblemType
import com.twtstudio.service.tjwhm.exam.problem.ProblemActivity
import com.twtstudio.service.tjwhm.exam.problem.getTestHistory
import com.twtstudio.service.tjwhm.exam.problem.score.ScoreActivity
import com.twtstudio.service.tjwhm.exam.user.OneHistoryBean
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.layoutInflater
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tjwhm@TWTStudio at 5:18 PM,2018/8/9.
 * Happy coding!
 */

class HistoryItem(val context: Context, val oneHistoryBean: OneHistoryBean) : Item {
    override val controller: ItemController
        get() = Controller

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                ItemViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_history, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ItemViewHolder
            item as HistoryItem
            holder.apply {
                tvPracticeType?.text = item.oneHistoryBean.type.toInt().toMode()
                tvDate?.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(item.oneHistoryBean.timestamp.toLong() * 1000L))
                tvTitle?.text = item.oneHistoryBean.course_name
                if (item.oneHistoryBean.type.toInt().toMode() == "模拟测试") {
                    tvProblemType?.visibility = View.GONE
                    itemView.setOnClickListener {
                        getTestHistory(item.oneHistoryBean.time) {
                            when (it) {
                                is RefreshState.Failure -> {
                                    Toasty.error(itemView.context, "网络错误").show()
                                    Log.e("HistoryItem: ", it.throwable.toString())
                                }
                                is RefreshState.Success -> {
                                    val intent = Intent(itemView.context, ScoreActivity::class.java)
                                    intent.putExtra(ScoreActivity.SCORE_BEAN_KEY, it.message.data)
                                    itemView.context.startActivity(intent)
                                }
                            }
                        }
                    }
                } else {
                    tvProblemType?.text = item.oneHistoryBean.ques_type.toInt().toProblemType()
                    itemView.setOnClickListener {
                        itemView.context.startProblemActivity(ProblemActivity.READ_AND_PRACTICE,
                                item.oneHistoryBean.course_id.toInt(),
                                item.oneHistoryBean.ques_type.toInt(),
                                item.oneHistoryBean.done_index.toInt())
                    }
                }
            }
        }

    }

    private class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvPracticeType: TextView? = itemView?.findViewById(R.id.tv_history_item_type)
        val tvProblemType: TextView? = itemView?.findViewById(R.id.tv_history_item_problem_type)
        val tvTitle: TextView? = itemView?.findViewById(R.id.tv_history_item_title)
        val tvDate: TextView? = itemView?.findViewById(R.id.tv_history_date)
    }
}

fun MutableList<Item>.historyItem(context: Context, oneHistoryBean: OneHistoryBean) = add(HistoryItem(context, oneHistoryBean))