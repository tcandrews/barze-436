package com.example.barze

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.example.barze.BarsFragment.OnListFragmentInteractionListener
import com.example.barze.data.model.BarContent
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.fragment_bars.view.*

/**
 * [RecyclerView.Adapter] that can display a [Bar] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyBarsRecyclerViewAdapter(
    private val mValues: List<BarContent.Bar>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyBarsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val db = FirebaseFirestore.getInstance()
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
        holder.mIdView.text = item.name
        holder.mContentView.text = item.wait.toString()

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
