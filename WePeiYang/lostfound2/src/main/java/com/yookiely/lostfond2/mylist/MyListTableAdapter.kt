package com.yookiely.lostfond2.mylist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.lostfond2.R
import com.twt.wepeiyang.commons.mta.mtaClick
import com.yookiely.lostfond2.detail.DetailActivity
import com.yookiely.lostfond2.release.ReleaseActivity
import com.yookiely.lostfond2.service.MyListDataOrSearchBean
import com.yookiely.lostfond2.service.Utils
import org.jetbrains.anko.textColor

class MyListTableAdapter(val myListBean: MutableList<MyListDataOrSearchBean>, var context: FragmentActivity?, var lostOrFound: String, var mylistView: MyListService.MyListView) : RecyclerView.Adapter<MyListTableAdapter.MyListViewHolder>() {

    class MyListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var myListItemTitle: TextView = view.findViewById(R.id.tv_mylist_item_title)
        var myListItemStatus: TextView = view.findViewById(R.id.tv_mylist_item_status)
        var myListItemType: TextView = view.findViewById(R.id.tv_mylist_item_type)
        var myListItemTime: TextView = view.findViewById(R.id.tv_mylist_item_time)
        var myListItemPlace: TextView = view.findViewById(R.id.tv_mylist_item_place)
        var myListItemPic: ImageView = view.findViewById(R.id.iv_mylist_item_pic)
        var myListItemPencilTouch: TextView = view.findViewById(R.id.tv_mylist_item_pencil_touch)
        var myListItemOutDate: ImageView = view.findViewById(R.id.iv_mylist_detail_isoutdate)
        var myListItemButton: Button = view.findViewById(R.id.bn_mylist_isback)
        var myListItemButtonOutDate: Button = view.findViewById(R.id.bn_mylist_item_outdate)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val tempList = myListBean[position]
        val intent = Intent()
        val bundle = Bundle()

        holder.apply {
            myListItemTitle.text = tempList.title
            myListItemType.text = Utils.getType(tempList.detail_type)
            myListItemTime.text = tempList.time
            if (tempList.campus == 1) {
                this.myListItemPlace.text = "北洋园 - ${tempList.place}"
            } else {
                this.myListItemPlace.text = "卫津路 - ${tempList.place}"
            }

            if (tempList.isExpired == 1) {
                // 0是未过期,1是已过期
                myListItemButtonOutDate.visibility = View.VISIBLE
                myListItemButton.visibility = View.GONE
                myListItemButtonOutDate.text = "重新编辑"
                myListItemStatus.textColor = Color.parseColor("#999999")
                myListItemStatus.text = "已过期"
                myListItemOutDate.visibility = View.VISIBLE
            } else {
                holder.myListItemOutDate.visibility = View.GONE
                holder.myListItemButtonOutDate.visibility = View.GONE
                holder.myListItemButton.visibility = View.VISIBLE
                if (lostOrFound == "found") {
                    if (tempList.isback == 1) {
                        myListItemStatus.text = "已归还"
                        myListItemStatus.textColor = Color.parseColor("#999999")
                        myListItemButton.text = "取消归还"
                    } else {
                        myListItemStatus.text = "未归还"
                        myListItemStatus.textColor = Color.parseColor("#44a0e3")
                        myListItemButton.text = "确认归还"
                    }
                } else {
                    if (tempList.isback == 1) {
                        myListItemStatus.text = "已找到"
                        myListItemStatus.textColor = Color.parseColor("#999999")
                        myListItemButton.text = "取消找到"
                    } else {
                        myListItemStatus.text = "未找到"
                        myListItemStatus.textColor = Color.parseColor("#44a0e3")
                        myListItemButton.text = "确认找到"
                    }
                }
            }

            myListItemButton.setOnClickListener {
                mtaClick("lostfound2_我的 改变物品状态的次数")
                mylistView.turnStatus(tempList.id) }
            myListItemButtonOutDate.setOnClickListener {
                bundle.apply {
                    if (lostOrFound == "lost") {
                        putString(Utils.LOSTORFOUND_KEY, "editLost")
                    } else {
                        putString(Utils.LOSTORFOUND_KEY, "editFound")
                    }
                    putInt(Utils.ID_KEY, tempList.id)
                    putInt(Utils.DETAIL_TYPE, tempList.detail_type)
                }

                intent.apply {
                    putExtras(bundle)
                    setClass(context, ReleaseActivity::class.java)
                    context?.startActivity(this)
                }
            }

            if (tempList.picture != null) {
                Glide.with(context)
                        .load(Utils.getPicUrl(tempList.picture[0]))
                        .placeholder(R.drawable.lf_waterfall_nopic)
                        .error(R.drawable.lf_waterfall_nopic)
                        .into(myListItemPic)
            } else {
                Glide.with(context)
                        .load(Utils.getPicUrl("null"))
                        .placeholder(R.drawable.lf_waterfall_nopic)
                        .error(R.drawable.lf_waterfall_nopic)
                        .into(myListItemPic)
            }

            itemView.setOnClickListener {
                mtaClick("lostfound2_我的 进入详情页的次数")
                bundle.putInt(Utils.ID_KEY, tempList.id)
                intent.putExtras(bundle)
                intent.setClass(context, DetailActivity::class.java)
                context?.startActivity(intent)
            }

            myListItemPencilTouch.setOnClickListener {
                mtaClick("lostfound2_我的 进入编辑页的次数")
                bundle.apply {
                    if (lostOrFound == "lost") {
                        putString(Utils.LOSTORFOUND_KEY, "editLost")
                    } else {
                        putString(Utils.LOSTORFOUND_KEY, "editFound")
                    }
                    putInt(Utils.ID_KEY, tempList.id)
                    putInt(Utils.DETAIL_TYPE, tempList.detail_type)
                }

                intent.apply {
                    putExtras(bundle)
                    setClass(context, ReleaseActivity::class.java)
                    context?.startActivity(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.lf2_item_mylist, parent, false)
        return MyListViewHolder(view)
    }

    override fun getItemCount(): Int = myListBean.size
}
