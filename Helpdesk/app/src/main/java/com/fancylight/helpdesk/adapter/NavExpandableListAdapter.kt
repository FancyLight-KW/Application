package com.fancylight.helpdesk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.model.NavListItem

class NavExpandableListAdapter(private val itemList: Array<NavListItem>) : BaseExpandableListAdapter() {


    override fun getGroupCount(): Int {
        return itemList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return itemList[groupPosition].subList.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return itemList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return itemList[groupPosition].subList[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        // 레이아웃 생성
        val view = LayoutInflater
                .from(parent?.context)
                .inflate(R.layout.item_nav_list_group, parent, false)

        // 텍스트 뷰 채우기
        val textView = view.findViewById<TextView>(R.id.txt_title)
        textView.text = itemList[groupPosition].title

        // 이미지 뷰 설정 : 열리고 닫힌 상태에 따라 drawable 다르게 설정
        val imageView = view.findViewById<ImageView>(R.id.img_indicator)
        val indicatorRes = if (isExpanded) R.drawable.ic_collapse else R.drawable.ic_expand
        imageView.setImageResource(indicatorRes)

        return view
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        // 레이아웃 생성
        val view = LayoutInflater
            .from(parent?.context)
            .inflate(R.layout.item_nav_list_child, parent, false)

        // 텍스트 뷰 채우기
        val textView = view.findViewById<TextView>(R.id.txt_title)
        textView.text = itemList[groupPosition].subList[childPosition]

        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}