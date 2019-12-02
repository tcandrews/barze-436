package com.example.barze

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
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

            holder.setOnClick(model)

            when (model.name) {
                "R.J. Bentley's Filling Station" -> holder.mImage.setImageResource(R.drawable.bentleys)
                "Cornerstone Grill & Loft" -> holder.mImage.setImageResource(R.drawable.cornerstone)
                "Looney's Pub" -> holder.mImage.setImageResource(R.drawable.looneys)
                "MilkBoy ArtHouse" -> holder.mImage.setImageResource(R.drawable.milkboy)
                "Terrapin's Turf" -> holder.mImage.setImageResource(R.drawable.turf)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_bars, parent, false)
//
            return BarViewHolder(view)
        }

        override fun onError(e: FirebaseFirestoreException) {
            super.onError(e)
            Log.e("BarsFragment", e.toString())
        }
    }

    private inner class BarViewHolder internal constructor(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mName: TextView = mView.name
        var mCoverVal: TextView = mView.coverVal
        var mWaitVal: TextView = mView.waitVal
        var mAtmosphereVal: TextView = mView.atmosphereVal
        var mImage: ImageView = mView.imageView

        fun setOnClick(bar: Bar) {
            mView.setOnClickListener {
                val dialog = BottomSheetDialog(context!!)
                val bottomSheetLayout = LayoutInflater.from(context!!).inflate(R.layout.bottom_sheet, container, false)

                bottomSheetLayout.detailsName.text = bar.name
                bottomSheetLayout.detailsCoverVal.text = NumberFormat.getCurrencyInstance().format(bar.cover)
                bottomSheetLayout.detailsWaitVal.text = String.format(resources.getString(R.string.minutes_abbreviation), bar.wait)
                bottomSheetLayout.detailsAtmosphereVal.text = bar.atmosphere
                bottomSheetLayout.detailsDealsVal.text = bar.deals
                bottomSheetLayout.detailsEventsVal.text = bar.events
                bottomSheetLayout.detailsHappyHourVal.text = bar.happyHour
                bottomSheetLayout.detailsHoursVal.text = bar.hours

                bottomSheetLayout.updateButton.setOnClickListener {
                    val updateIntent = Intent(context, UpdateBarActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("BAR", bar)
                    updateIntent.putExtra("BUNDLE", bundle)
                    startActivity(updateIntent)
                }
                bottomSheetLayout.menuButton.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bar.menu)))
                }
                bottomSheetLayout.deleteButton.setOnClickListener {
                    val deleteIntent = Intent(context, DeleteBarActivity::class.java)
                    val bundle = Bundle()
                    bundle.putParcelable("BAR", bar)
                    deleteIntent.putExtra("BUNDLE", bundle)
                    startActivity(deleteIntent)

                }

                dialog.setContentView(bottomSheetLayout)
                dialog.setCanceledOnTouchOutside(true)
                dialog.show()
            }
            mView.setOnLongClickListener {
                val updateIntent = Intent(context, UpdateBarActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("BAR", bar)
                updateIntent.putExtra("BUNDLE", bundle)
                startActivity(updateIntent)
                true
            }
        }
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
