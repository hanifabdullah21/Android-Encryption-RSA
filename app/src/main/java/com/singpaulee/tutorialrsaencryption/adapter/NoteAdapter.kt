package com.singpaulee.tutorialrsaencryption.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import com.singpaulee.tutorialrsaencryption.helper.ConverterDate
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import com.singpaulee.tutorialrsaencryption.main.DetailNoteActivity
import com.singpaulee.tutorialrsaencryption.main.UpdateNoteActivity
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.dialog_input_password.view.*
import kotlinx.android.synthetic.main.item_note.view.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.selector


class NoteAdapter(val context: Context, var listNote: MutableList<NoteContract>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    lateinit var itemView: View

//    var listNote: ArrayList<NoteContract>
//    var filteredListNote: ArrayList<NoteContract>

//    init {
//        this.listNote = listNote as ArrayList<NoteContract>
//        this.filteredListNote  = listNote
//    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_note, parent, false)
        return NoteAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bind(listNote[p1])
        holder.itemView.onClick {
            val choice = listOf("Lihat Detail", "Hapus")
            itemView.context.selector("", choice) { dialog: DialogInterface, i: Int ->
                when (i) {
                    0 -> {
                        var i = Intent(itemView.context, DetailNoteActivity::class.java)
                        i.putExtra("NOTE", listNote[p1])
                        itemView.context.startActivity(i)
                    }
                    1 -> {
                        openDialog(p1)
                    }
                }
            }
        }
    }

    private fun openDialog(position: Int) {
        //Get password value from sharedpreference
        var sp = SharedPrefManager(context)
        var password = sp.getPassword()
        Log.d("DetailNote", "$password")

        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var alertDialog: AlertDialog? = null

        var layoutInflater: LayoutInflater = LayoutInflater.from(context)
        var dialog: View = layoutInflater.inflate(R.layout.dialog_input_password, null)

        dialog.dip_btn_dekrip.text = "HAPUS"

        dialog.dip_btn_dekrip.onClick {
            if (dialog.dip_edt_password.text.isBlank()) {
                dialog.dip_edt_password.error = "Tidak boleh kosong"
                dialog.dip_edt_password.requestFocus()
                return@onClick
            } else if (dialog.dip_edt_password.text.toString() != password) {
                dialog.dip_edt_password.error = "Kata sandi salah"
                dialog.dip_edt_password.requestFocus()
                return@onClick
            } else if (dialog.dip_edt_password.text.toString() == password) {
                itemView.context.database.use {
                    delete(
                        NoteContract.TABLE_NOTE, "(${NoteContract.ID}={id})",
                        "id" to listNote[position].id!!.toLong()
                    )
                    listNote.removeAt(position)
                    notifyDataSetChanged()
                    alertDialog?.dismiss()
                }
            }
        }

        builder.setView(dialog)
        alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    /** Fugsi ini digunakan untuk mendapatkan daftar note
     * yang sudah tersimpan di dalam sqlite
     *
     * */
    fun getListFromSqlite() {
        context.database.use {
            val result = select(NoteContract.TABLE_NOTE)
            listNote = result.parseList(classParser<NoteContract>()) as ArrayList<NoteContract>
            listNote = result.parseList(classParser<NoteContract>()) as ArrayList<NoteContract>
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
            notifyDataSetChanged()
        }
    }

//    fun getFilter(): Filter {
//        return doFilter
//    }
//
//    private val doFilter = object : Filter() {
//        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
//            filteredListNote = ArrayList()
//
//            if (constraint == null || constraint.isEmpty()) {
//                filteredListNote.addAll(listNote)
//            } else {
//                filteredListNote = ArrayList()
//                val filterPattern = constraint.toString().toLowerCase()
//
//                for (item in listNote) {
//                    if (item.title?.toLowerCase().toString().contains(filterPattern)) {
//                        filteredListNote.add(item)
//                    }
//                }
//            }
//
//            val results = Filter.FilterResults()
//            results.values = filteredListNote
//
//            Log.d("SEARCH", "" + filteredListNote.size)
//
//            return results
//        }
//
//        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
//            notifyDataSetChanged()
//        }
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(noteContract: NoteContract) {
            var listDate = ConverterDate().parseDatePart(noteContract.createdAt)
            itemView.in_tv_day.text = listDate[0]
            itemView.in_tv_date.text = listDate[1]
            itemView.in_tv_monthyear.text = listDate[2] + " " + listDate[3]
            itemView.in_tv_time.text = listDate[4]
            itemView.in_tv_title_notes.text = noteContract.title
            itemView.in_tv_message_notes.text = noteContract.message
        }
    }

}