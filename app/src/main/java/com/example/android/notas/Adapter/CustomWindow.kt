package com.example.android.notas.Adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.aplicacao.android.notas.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomWindow(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.info_window, null)

    private fun rendowWindowText(marker: Marker, view: View) {
        val title = view.findViewById<TextView>(R.id.titleLabl)
        val problem = view.findViewById<TextView>(R.id.problemLbl)
        val type=view.findViewById<TextView>(R.id.textView3)

        title.text = view.context.getString(R.string.problemLbl)
        problem.text = marker.title
        type.text=marker.snippet
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}
