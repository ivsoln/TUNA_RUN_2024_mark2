package com.inventive.tunarun

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.inventive.tunarun.Fish.Objects.EntityState
import java.text.SimpleDateFormat
import java.util.Date


class Instant {
    companion object {
        var dateFormat = "dd/MM/yyyy"
        fun Activity.popupText(text: String) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this, text, duration) // in Activity
            toast.show()
        }

        fun selectionDialog(callback: ListItem.Callback) {
            val dialog = Dialog(callback.activity)
            with(dialog) {
                setCancelable(true)
                setContentView(R.layout.activity_search_view_list)
            }
            val title: TextView = dialog.findViewById(R.id.text_title) as TextView
            val listView: ListView = dialog.findViewById(R.id.list_item_view) as ListView
            val searchView = dialog.findViewById(R.id.search_item) as SearchView

            title.text = callback.title
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // do something on text submit
                    if (query != null) {
                        callback.searchTextChanged(listView, query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // do something when text changes
                    if (newText != null) {
                        //callback.searchTextChanged(listView, newText)
                    }
                    return false
                }
            })

            val dismiss = dialog.findViewById(R.id.button_dismiss) as Button
            dismiss.setOnClickListener { dialog.dismiss() }

            callback.searchTextChanged(listView, "")
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                Log.e("TUNA RUN > SELECT", callback.items[position].toString())
                callback.onItemSelected(callback.items[position])
                dialog.dismiss()
            }
            dialog.show()
        }

        fun EditText.focusThenSelectionEnd(){
            this.requestFocus()
            this.setSelection(this.length())
        }
        fun EditText.focusThenSelectionAll(){
            this.requestFocus()
            this.selectAll()
        }
        fun EditText.showShortTime(date: Date){
            this.setText(date.stringShortTime())
        }
        fun TextView.showShortTime(date: Date){
            this.text = date.stringShortTime()
        }

        fun Date.stringShortTime(): String {
           return (SimpleDateFormat("dd/MM/yyyy HH:mm").format(this))
        }

        fun TextView.clearResult() {
            this.showResult(EntityState.NEW, "")
        }

        fun TextView.showResult(state: EntityState, message: String) {
            this.text = message
            when (state) {
                EntityState.OK -> this.setBackgroundColor(resources.getColor(R.color.Light_Green_A100))
                EntityState.WARNING -> this.setBackgroundColor(resources.getColor(R.color.Amber_A200))
                EntityState.ERROR ->  this.setBackgroundColor(resources.getColor(R.color.Red_A200))
                EntityState.NEW -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.EDIT -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.CHANGE -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.SUCCESS -> this.setBackgroundColor(resources.getColor(R.color.Light_Green_A100))
                EntityState.FAILURE -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.DUPLICATE -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.DELETED -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.FOUND -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.NOT_FOUND -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.EXISTED ->  this.setBackgroundColor(resources.getColor(R.color.Amber_A200))
                EntityState.REJECT -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.NOTHING -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.UNCHANGE -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.NOT_ALLOW_NULL -> this.setBackgroundColor(Color.TRANSPARENT)
            }
        }

        fun TextView.showVCColor(vcColor: Fish.Skipjack.Masters.VCColor) {
            this.setBackgroundColor(Color.parseColor(vcColor.color_hex))
            this.text = vcColor.color_description
            this.setTextColor(Color.BLACK)
        }

        fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }


                override fun afterTextChanged(editable: Editable?) {
                    afterTextChanged.invoke(editable.toString())
                }
            })
        }


        fun EditText.afterKeyEntered(afterKeyEntered: (String) -> Unit) {
            this.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        afterKeyEntered.invoke("")
                        return true
                    }
                    return false
                }
            })
        }

        fun EditText.afterKeyEnteredThenCloseKeyboard(afterKeyEnteredThenCloseKeyboard: (String) -> Unit) {
            this.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                        afterKeyEnteredThenCloseKeyboard.invoke("")
                        val imm =
                            v.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm?.hideSoftInputFromWindow(v.windowToken, 0)
                        return true
                    }
                    return false
                }
            })
        }


    }
}

class ListItem {
    override fun toString(): String {
        return caption.toString()
    }

    var id: Int = 0
    var caption: String = ""
    var description: String = ""

    class Adapter(context: Activity, var resources: Int, var items: List<ListItem>) :
        ArrayAdapter<ListItem>(context, resources, items) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
            val view: View = layoutInflater.inflate(resources, null)
            val textCaption: TextView = view.findViewById(R.id.text_caption)
            val textDescription: TextView = view.findViewById(R.id.text_description)
            var item: ListItem = items[position]


            textCaption.text = item.caption;
            textDescription.text = item.description;
            return view
        }
    }

    abstract class Callback(activity: Activity, title: String) {
        var title: String = title
        val activity: Activity = activity
        var items: List<ListItem> = listOf()
        open fun onItemSelected(result: ListItem) {}
        open fun searchTextChanged(listView: ListView, text: String) {}
    }
}