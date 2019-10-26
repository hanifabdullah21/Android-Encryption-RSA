package com.singpaulee.tutorialrsaencryption.fragment


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.adapter.NoteAdapter
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import com.singpaulee.tutorialrsaencryption.main.UpdateNoteActivity
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.dialog_input_password.view.*
import kotlinx.android.synthetic.main.fragment_list_note.view.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk25.coroutines.onClick

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListNoteFragment : Fragment() {

    lateinit var parentView: View

    lateinit var listNote: List<NoteContract>

    lateinit var layoutManager: LinearLayoutManager
    var adapterNote: NoteAdapter? = null

    var isAscending = true
    var sortingAction = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        parentView = inflater.inflate(R.layout.fragment_list_note, container, false)

        var bundle = arguments
        if (bundle != null) {
            sortingAction = bundle.getString("SORT").toString()
            switchSorting()
        } else {
            //TODO Get List of Notes from SQLite
            getListFromSqlite()
            setRecyclerView()
        }


//        parentView.lnf_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
//            android.support.v7.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapterNote?.getFilter()?.filter(newText)
//                return false
//            }
//        })

        return parentView
    }

    private fun setRecyclerView() {
        //TODO prepare setting recyclerview
        layoutManager = LinearLayoutManager(parentView.context, LinearLayoutManager.VERTICAL, false)
        adapterNote = NoteAdapter(parentView.context, listNote.toMutableList())

        //TODO setting recyclerview
        parentView.lnf_rv_notes.layoutManager = layoutManager
        parentView.lnf_rv_notes.adapter = adapterNote
    }

    /** Fugsi ini digunakan untuk mendapatkan daftar note
     * yang sudah tersimpan di dalam sqlite
     *
     * */
    fun getListFromSqlite() {
        parentView.context.database.use {
            val result = select(NoteContract.TABLE_NOTE)
            listNote = result.parseList(classParser<NoteContract>())
            if (!listNote.isEmpty()) {
                for (i in 0 until listNote.size) {
                    Log.d(
                        "SQLITE",
                        "SELECT LIST ${listNote.get(i).id} ${listNote.get(i).title} ${listNote.get(i).message} ${listNote.get(
                            i
                        ).createdAt}"
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        switchSorting()
    }

    fun switchSorting() {
        getListFromSqlite()
        if (sortingAction == "ASC") {
            listNote = listNote.reversed()
        } else if (sortingAction == "DESC") {
            listNote = listNote
        }
        setRecyclerView()
        Log.d("LIST", "" + listNote.toString())
    }
}
