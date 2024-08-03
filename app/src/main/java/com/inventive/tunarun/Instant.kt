package com.inventive.tunarun

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.Fish.Objects.EntityState
import com.inventive.tunarun.Fish.Objects.ObjectClient
import com.inventive.tunarun.FishAdapter.TagAdapter
import com.inventive.tunarun.Instant.Companion.done
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


        fun <T> selectionDialog(callback: FishAdapter.RecyclerViewAdapter.Callback<T>) {
            val dialog = Dialog(callback.activity)
            with(dialog) {
                setCancelable(true)
                setContentView(R.layout.activity_search_recycler_list)
            }
            val title: TextView = dialog.findViewById(R.id.text_title)
            val listView: RecyclerView = dialog.findViewById(R.id.list_item_view)
            listView.setLayoutManager(LinearLayoutManager(callback.activity))
            val searchView: SearchView = dialog.findViewById(R.id.search_item)

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

            val dismiss: Button = dialog.findViewById(R.id.button_dismiss)
            dismiss.setOnClickListener { dialog.dismiss() }

            callback.searchTextChanged(listView, "")
            callback.setClickListener(object : FishAdapter.RecyclerViewAdapter.ItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    Log.i("TUNA RUN > COUNT", "${callback.items.size.toString()}")
                    Log.i("TUNA RUN > SELECT", "${position.toString()}")
                    Log.i("TUNA RUN > SELECT", callback.items[position].toString())
                    callback.onItemSelected(callback.items[position])
                    dialog.dismiss()
                }
            })

            Log.i("TUNA RUN > DIALOG_ITEMS_SIZE:", callback.items.size.toString())
            dialog.show()
        }

        fun TextView.done() {
            this.setBackgroundColor(resources.getColor(R.color.Light_Green))
        }

        fun TextView.done(text: String) {
            this.text = text
            this.setBackgroundColor(resources.getColor(R.color.Light_Green))
        }

        fun TextView.clear() {
            this.text = ""
            this.setBackgroundColor(resources.getColor(R.color.Blue_Gray_050))
        }

        fun TextView.cancel() {
            this.setBackgroundColor(resources.getColor(R.color.Blue_Gray_050))
        }

        fun EditText.clear() {
            this.setText("")
            this.setBackgroundColor(resources.getColor(R.color.Blue_Gray_050))
        }

        fun EditText.done() {
            this.setBackgroundColor(resources.getColor(R.color.Light_Green))
        }

        fun EditText.done(text: String) {
            this.setText(text)
            this.setBackgroundColor(resources.getColor(R.color.Light_Green))
        }


        fun EditText.typing() {
            this.setBackgroundColor(resources.getColor(R.color.Amber_A100))
        }

        fun EditText.cancel() {
            this.setBackgroundColor(resources.getColor(R.color.Blue_Gray_050))
        }

        fun EditText.error() {
            this.setBackgroundColor(resources.getColor(R.color.Red_200))
        }

        fun EditText.focusThenSelectionEnd() {
            this.requestFocus()
            this.setSelection(this.length())
        }

        fun EditText.focusThenSelectionAll() {
            this.requestFocus()
            this.selectAll()
        }

        fun EditText.showShortTime(date: Date) {
            this.setText(date.stringShortTime())
        }

        fun TextView.showShortTime(date: Date) {
            this.text = date.stringShortTime()
        }

        fun String?.toIntOrDefault(default: Int = 0): Int {
            return this?.toIntOrNull() ?: default
        }


        fun String.getDecimalWeight(): Double {
            return this.uppercase().replace(" ", "")
                .replace("K", "")
                .replace("G", "").toDouble()
        }


        fun Date.queryDateString(): String {
            return (SimpleDateFormat("yyyy-MM-dd").format(this))
        }

        fun Date.stringShortTime(): String {
            return (SimpleDateFormat("dd/MM/yyyy HH:mm").format(this))
        }


        fun TextView.clearResult() {
            this.showResult(EntityState.NEW, "")
        }

        fun TextView.warningResult(message: String) {
            this.showResult(EntityState.WARNING, message)
        }

        fun TextView.errorResult(message: String) {
            this.showResult(EntityState.ERROR, message)
        }

        fun TextView.showResult(entity: ObjectClient) {
            this.showResult(entity.state, entity.entityMessage)
        }


        fun TextView.showResult(state: EntityState, message: String) {
            this.text = message
            when (state) {
                EntityState.OK -> this.setBackgroundColor(resources.getColor(R.color.Light_Green_A100))
                EntityState.WARNING -> this.setBackgroundColor(resources.getColor(R.color.Amber_A200))
                EntityState.ERROR -> this.setBackgroundColor(resources.getColor(R.color.Red_A100))
                EntityState.NEW -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.EDIT -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.CHANGE -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.SUCCESS -> this.setBackgroundColor(resources.getColor(R.color.Light_Green_A100))
                EntityState.FAILURE -> this.setBackgroundColor(resources.getColor(R.color.Red_A200))
                EntityState.DUPLICATE -> this.setBackgroundColor(resources.getColor(R.color.Amber_A200))
                EntityState.DELETED -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.FOUND -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.NOT_FOUND -> this.setBackgroundColor(resources.getColor(R.color.Amber_A200))
                EntityState.EXISTED -> this.setBackgroundColor(resources.getColor(R.color.Amber_A200))
                EntityState.REJECT -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.NOTHING -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.UNCHANGE -> this.setBackgroundColor(Color.TRANSPARENT)
                EntityState.NOT_ALLOW_NULL -> this.setBackgroundColor(resources.getColor(R.color.Red_A100))
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
                    after: Int,
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

