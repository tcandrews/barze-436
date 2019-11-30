package com.example.barze

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.barze.data.model.Bar

import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.fragment_bars.view.*
import java.text.NumberFormat
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.bottom_sheet.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [BarsFragment.OnListFragmentInteractionListener] interface.
 */
class BarsFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var recyclerAdapter: FirestoreRecyclerAdapter<Bar, BarViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bars_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val rootRef = FirebaseFirestore.getInstance()
                val query = rootRef.collection("bars")

                val options = FirestoreRecyclerOptions
                    .Builder<Bar>()
                    .setQuery(query, Bar::class.java)
                    .build()
                recyclerAdapter = MyBarsRecyclerViewAdapter(options)
                recyclerAdapter.startListening()
                adapter = recyclerAdapter

            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        recyclerAdapter.stopListening()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Bar?)
    }

    private inner class MyBarsRecyclerViewAdapter internal constructor(options: FirestoreRecyclerOptions<Bar>) : FirestoreRecyclerAdapter<Bar, BarViewHolder>(options) {
        override fun onBindViewHolder(holder: BarViewHolder, position: Int, model: Bar) {
            holder.mName.text = model.name
            holder.mCoverVal.text = NumberFormat.getCurrencyInstance().format(model.cover)
            holder.mWaitVal.text = String.format(resources.getString(R.string.minutes_abbreviation), model.wait)
            holder.mAtmosphereVal.text = model.atmosphere
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_bars, parent, false)
            view.setOnClickListener {
                val dialog = BottomSheetDialog(context!!)
                val bottomSheet = LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet, parent, false)
                bottomSheet.textView.text = view.barName.text
                dialog.setContentView(bottomSheet)
                dialog.setCanceledOnTouchOutside(true)
                dialog.show()
            }
            return BarViewHolder(view)
        }

        override fun onError(e: FirebaseFirestoreException) {
            super.onError(e)
            Log.e("BarsFragment", e.toString())
        }
    }

    private inner class BarViewHolder internal constructor(mView: View) : RecyclerView.ViewHolder(mView) {
        var mName: TextView = mView.barName
        var mCoverVal: TextView = mView.coverVal
        var mWaitVal: TextView = mView.waitVal
        var mAtmosphereVal: TextView = mView.atmosphereVal
        var mImage: ImageView = mView.imageView
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            BarsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
