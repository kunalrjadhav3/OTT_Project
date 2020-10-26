package com.example.diagnal_assignment.views.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.diagnal_assignment.R
import com.example.diagnal_assignment.model.Content
import com.example.diagnal_assignment.utils.getImageFromName
import java.lang.Exception

class PostersGridAdapter(val context: Context, val posterList:ArrayList<Content>):BaseAdapter() {
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        val mView=if(view==null){
            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            mInflater.inflate(R.layout.grid_item,viewGroup,false)
        }else
            view

        val ivPoster: ImageView = mView.findViewById(R.id.iv_poster)
        val tvName: TextView = mView.findViewById(R.id.tv_name)
        val poster= getItem(position)
        tvName.setText(poster.name)
        try {
            ivPoster.setImageDrawable(getImageFromName(poster.poster_image))
        }catch (e:Exception){ // Exception handled while trying to read drawable with filePath that does not exist. (in PageThree.Json)
            ivPoster.setImageResource(R.drawable.placeholder_for_missing_posters)

        }
        return mView
    }

    override fun getItem(position: Int)=posterList.get(position)

    override fun getItemId(position: Int)=position.toLong()

    override fun getCount()=posterList.size
}