package com.example.barze

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.example.barze.BarsFragment.OnListFragmentInteractionListener
import com.example.barze.data.model.BarContent

import kotlinx.android.synthetic.main.fragment_bars.view.*

/**
 * [RecyclerView.Adapter] that can display a [Bar] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyBarsRecyclerViewAdapter(
    private val mValues: List<BarContent.Bar>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyBarsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as BarContent.Bar
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_bars, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mName.text = item.name
        holder.mCoverVal.text = item.cover.toString()
        holder.mWaitVal.text = item.wait.toString()
        holder.mAtmosphereVal.text = item.atmosphere
        Log.i("ADAPTER", item.name)
        Log.i("ADAPTER", item.cover.toString())


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mName: TextView = mView.barName
        var mCoverVal: TextView = mView.coverVal
        var mWaitVal: TextView = mView.waitVal
        var mAtmosphereVal: TextView = mView.atmosphereVal

        override fun toString(): String {
            return super.toString() + " '" + mName.text + "'"
        }
    }
}
