package com.inventive.tunarun

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inventive.tunarun.Fish.Objects.HashSetClient
import com.inventive.tunarun.Instant.Companion.queryDateString
import com.inventive.tunarun.Instant.Companion.stringShortTime
import com.inventive.tunarun.SkipjackCookPostActivity.Companion.Cook
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date


class FishClient {
    companion object {

        private var apiAddr: String = "99.42.1.48"

        var REQUEST_SHIFT = 1
        var REQUEST_BLIND_RECEIVE = 10

        var REQUEST_TAG_SCAN = 30
        var REQUEST_TAG_COUNT = 31


        class Master {
            companion object {
                var Species = HashSetClient<Fish.Skipjack.Masters.Species>()
                var SpeciesOrigin = HashSetClient<Fish.Skipjack.Masters.SpeciesOrigin>()
                var SpeciesSize = HashSetClient<Fish.Skipjack.Masters.SpeciesSize>()
                var VCColor = HashSetClient<Fish.Skipjack.Masters.VCColor>()
                var TagColor = HashSetClient<Fish.Skipjack.Masters.TagColor>()
                var QueueRanges = HashSetClient<Fish.Skipjack.Masters.QueueRange>()
                var QueueTypes = HashSetClient<Fish.Skipjack.Masters.QueueType>()
            }
        }

        class Skipjack {
            companion object {
                var Identity = Fish.Auth.Identity()
                var Default = Fish.Skipjack.DefaultClient()
                var Shift = Fish.Skipjack.DateShift()
            }
        }

        fun Init(context: Context, callback: InitCallback) {
            var master = MasterClient(context)
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.Species =
                            result as HashSetClient<Fish.Skipjack.Masters.Species>
                        callback.count += 1
                        callback.refresh()
                        Log.e("TUNA RUN > INIT_SPECIES > COUNT", Master.Species.count.toString())
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > INIT_SPECIES > ERROR", result)
                    }
                }
                it.get_species_materials(callback)
            }
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.SpeciesSize =
                            result as HashSetClient<Fish.Skipjack.Masters.SpeciesSize>
                        callback.count += 1
                        callback.refresh()
                        Log.e(
                            "TUNA RUN > INIT_SPECIES_SIZE > COUNT",
                            Master.SpeciesSize.count.toString()
                        )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > INIT_SPECIES_SIZE > ERROR", result)
                    }
                }
                it.get_species_sizes(callback)
            }
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.SpeciesOrigin =
                            result as HashSetClient<Fish.Skipjack.Masters.SpeciesOrigin>
                        callback.count += 1
                        callback.refresh()
                        Log.e(
                            "TUNA RUN > INIT_SPECIES_ORIGIN > COUNT",
                            Master.SpeciesOrigin.count.toString()
                        )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > INIT_SPECIES_ORIGIN > ERROR", result)
                    }
                }
                it.get_species_origins(callback)
            }
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.QueueRanges =
                            result as HashSetClient<Fish.Skipjack.Masters.QueueRange>
                        callback.count += 1
                        callback.refresh()
                        Log.e(
                            "TUNA RUN > LOAD_QUEUE_RANGE > COUNT",
                            Master.QueueRanges.count.toString()
                        )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > LOAD_QUEUE_RANGE > ERROR", result)
                    }
                }
                it.get_queue_ranges(callback)
            }
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.QueueTypes =
                            result as HashSetClient<Fish.Skipjack.Masters.QueueType>
                        callback.count += 1
                        callback.refresh()
                        Log.e(
                            "TUNA RUN > LOAD_QUEUE_TYPE > COUNT",
                            Master.QueueTypes.count.toString()
                        )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > LOAD_QUEUE_TYPE > ERROR", result)
                    }
                }
                it.get_queue_types(callback)
            }
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.TagColor = result as HashSetClient<Fish.Skipjack.Masters.TagColor>
                        callback.count += 1
                        callback.refresh()
                        Log.e(
                            "TUNA RUN > LOAD_TAG_COLOR > COUNT",
                            Master.TagColor.count.toString()
                        )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > LOAD_TAG_COLOR > ERROR", result)
                    }
                }
                it.get_tag_colors(callback)
            }
            master.also {
                val callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Master.VCColor = result as HashSetClient<Fish.Skipjack.Masters.VCColor>
                        callback.count += 1
                        callback.refresh()
                        Log.e(
                            "TUNA RUN > LOAD_VC_COLOR > COUNT",
                            Master.VCColor.count.toString()
                        )
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > LOAD_VC_COLOR > ERROR", result)
                    }
                }
                it.get_virtual_colors(callback)
            }


            var skipjack = SkipjackClient(context)
            skipjack.also {
                var callback = object : ActionRequest.Callback {
                    override fun <T> onSuccess(result: T) {
                        Skipjack.Default = result as Fish.Skipjack.DefaultClient
                        Skipjack.Shift = Skipjack.Default.PresentShift!!
                        callback.count += 1
                        callback.refresh()
                        Log.i("TUNA RUN > INIT_DEFAULT > SUCCESS", "")
                    }

                    override fun onError(result: String) {
                        Log.e("TUNA RUN > INIT_SPECIES > ERROR", result)
                    }
                }
                it.getDefault(callback)
            }
        }


        fun TextView.showShift() {
            var shiftText =
                SimpleDateFormat(Instant.dateFormat).format(Skipjack.Shift.Date)
            shiftText += " - " + Skipjack.Shift.WorkShift?.ShiftCode.toString()
            this.text = shiftText
        }

        fun TextView.showUser() {
            this.text = Skipjack.Identity.UserId.uppercase()
        }


        fun Activity.dialogSpeciesList(callback: ActionRequest.Callback) {
            val popupList = object : FishAdapter.RecyclerViewAdapter.Callback<FishAdapter.ListItem>(
                this,
                "CHOOSE SPECIES"
            ) {
                override fun onItemSelected(result: FishAdapter.ListItem) {
                    var species = Master.Species.Items.first { it.Id == result.id }
                    callback.onSuccess(species)
                }

                override fun searchTextChanged(listView: RecyclerView, text: String) {
                    items = listOf()
                    for (o: Fish.Skipjack.Masters.Species in Master.Species.Items) {
                        val w = FishAdapter.ListItem()
                        w.id = o.Id
                        w.caption = o.species_code
                        if (o.material_code.isNotEmpty()) {
                            w.caption = "${o.species_code} /${o.material_code}"
                        }
                        w.description = o.species_description
                        items = items + w
                    }
                    listView.adapter =
                        FishAdapter.RecyclerViewAdapter(
                            activity, this,
                            R.layout.activity_search_item_desc,
                            items
                        )

                    this.notifyItemsChanged()
                }
            }
            Instant.selectionDialog(popupList)
        }

        fun Activity.dialogSpeciesOriginList(callback: ActionRequest.Callback) {
            val popupList = object : FishAdapter.RecyclerViewAdapter.Callback<FishAdapter.ListItem>(
                this,
                "CHOOSE ORIGIN"
            ) {
                override fun onItemSelected(result: FishAdapter.ListItem) {
                    var origin = Master.SpeciesOrigin.Items.first { it.Id == result.id }
                    callback.onSuccess(origin)
                }

                override fun searchTextChanged(listView: RecyclerView, text: String) {
                    items = listOf()
                    for (o: Fish.Skipjack.Masters.SpeciesOrigin in Master.SpeciesOrigin.Items) {
                        val w = FishAdapter.ListItem()
                        w.id = o.Id
                        w.caption = o.species_origin_code.toString()
                        w.description = o.species_origin_description.toString()
                        items = items + w
                    }
                    listView.adapter =
                        FishAdapter.RecyclerViewAdapter(
                            activity, this,
                            R.layout.activity_search_item_desc,
                            items
                        )
                    this.notifyItemsChanged()
                }
            }
            Instant.selectionDialog(popupList)

        }

        fun Activity.dialogSpeciesSizeList(callback: ActionRequest.Callback) {
            val popupList = object : FishAdapter.RecyclerViewAdapter.Callback<FishAdapter.ListItem>(
                this,
                "CHOOSE SIZE"
            ) {
                override fun onItemSelected(result: FishAdapter.ListItem) {
                    Master.SpeciesSize.Items.firstOrNull { it.Id == result.id }
                        .also {
                            callback.onSuccess(it)
                        }
                }

                override fun searchTextChanged(listView: RecyclerView, text: String) {
                    items = listOf()
                    for (o: Fish.Skipjack.Masters.SpeciesSize in Master.SpeciesSize.Items) {
                        val w = FishAdapter.ListItem()
                        w.id = o.Id
                        w.caption = o.species_size_code.toString()
                        w.description = o.species_size_description.toString()
                        items = items + w
                    }
                    FishAdapter.RecyclerViewAdapter(
                        activity, this,
                        R.layout.activity_search_item,
                        items
                    ).also {
                        listView.adapter = it
                        listView.adapter?.notifyDataSetChanged()

                        this.notifyItemsChanged()
                    }

                }
            }
            Instant.selectionDialog(popupList)
        }

        fun Activity.dialogPreRackList(callback: ActionRequest.Callback) {
            val popupList = object : FishAdapter.RecyclerViewAdapter.Callback<FishAdapter.ListItem>(
                this,
                "CHOOSE PRE-RACK"
            ) {
                var racks = HashSetClient<Fish.Skipjack.Rack>()
                override fun onItemSelected(result: FishAdapter.ListItem) {
                    var rack = racks.Items.first { it.Id == result.id }
                    callback.onSuccess(rack)
                }

                override fun searchTextChanged(listView: RecyclerView, text: String) {
                    var date = Skipjack.Shift.Date
                    var shift = Skipjack.Shift.Shift
                    val skipjack = SkipjackClient(applicationContext)
                    val callback = object : ActionRequest.Callback {
                        override fun <T> onSuccess(result: T) {
                            racks = result as HashSetClient<Fish.Skipjack.Rack>
                            items = listOf()
                            for (o in racks.Items) {
                                val w = FishAdapter.ListItem()
                                w.id = o.Id
                                w.caption = o.rack_no.toString()
                                w.description = o.rack_arrange_start.stringShortTime()
                                items = items + w
                            }
                            listView.adapter = FishAdapter.RecyclerViewAdapter(
                                activity,
                                R.layout.activity_search_item_desc,
                                items
                            ).also {
                                it.setClickListener(object :
                                    FishAdapter.RecyclerViewAdapter.ItemClickListener {
                                    override fun onItemClick(view: View?, position: Int) {
                                        Log.i(
                                            "TUNA RUN:dialogPreRackList",
                                            "onItemClick : $position"
                                        )
                                        onItemClicked(view, position)
                                    }
                                })
                                it.setChangedListener(object :
                                    FishAdapter.RecyclerViewAdapter.ItemsChangedListener {
                                    override fun onItemsChanged() {
                                        notifyItemsChanged()
                                    }
                                })

                                listView.adapter = it
                                listView.adapter?.notifyDataSetChanged()

                                it.notifyItemsChanged()

                            }
                        }

                        override fun onError(result: String) {
                            Log.e("TUNA RUN > GET_PRE_RACKS > ERROR", result)
                        }
                    }
                    skipjack.listPreRacks(date, shift, callback)
                }
            }
            Instant.selectionDialog(popupList)
        }

        fun Activity.dialogQueueList(callback: ActionRequest.Callback) {

            val popupList = object : FishAdapter.RecyclerViewAdapter.Callback<Fish.Skipjack.Queue>(
                this,
                "CHOOSE QUEUE"
            ) {
                var client = HashSetClient<Fish.Skipjack.Queue>()
                override fun onItemSelected(result: Fish.Skipjack.Queue) {
                    var queue = client.Items.first { it.Id == result.Id }
                    callback.onSuccess(queue)
                }

                override fun searchTextChanged(listView: RecyclerView, text: String) {
                    val skipjack = SkipjackClient(applicationContext)
                    val callback = object : ActionRequest.Callback {
                        override fun <T> onSuccess(result: T) {
                            client = result as HashSetClient<Fish.Skipjack.Queue>

                            items = client.Items

                            Log.i("TUNA RUN > GET_QUEUES > COUNT:", items.size.toString())

                            FishAdapter.QueAdapter(
                                activity,
                                items
                            ).also {
                                it.setClickListener(object :
                                    FishAdapter.RecyclerViewAdapter.ItemClickListener {
                                    override fun onItemClick(view: View?, position: Int) {
                                        Log.i(
                                            "TUNA RUN > QUEUES_ADAPTER_CLICK",
                                            position.toString()
                                        )
                                        onItemClicked(view, position)
                                    }
                                })

                                it.setChangedListener(object :
                                    FishAdapter.RecyclerViewAdapter.ItemsChangedListener {
                                    override fun onItemsChanged() {
                                        notifyItemsChanged()
                                    }
                                })

                                listView.adapter = it
                                listView.adapter?.notifyDataSetChanged()

                                it.notifyItemsChanged()
                            }
                        }

                        override fun onError(result: String) {
                            Log.e("TUNA RUN > GET_QUEUES > ERROR", result)
                        }
                    }
                    skipjack.listQueuesAsCompact(callback)
                }
            }
            Instant.selectionDialog(popupList)
        }

        fun Activity.dialogTagList(callback: ActionRequest.Callback) {
            val popupList = object :
                FishAdapter.RecyclerViewAdapter.Callback<Fish.Skipjack.Tag>(this, "CHOOSE TAG") {
                var client = HashSetClient<Fish.Skipjack.Tag>()
                override fun onItemSelected(result: Fish.Skipjack.Tag) {
                    var item = client.Items.first { it.Id == result.Id }
                    callback.onSuccess(item)
                }

                override fun searchTextChanged(listView: RecyclerView, text: String) {
                    val skipjack = SkipjackClient(applicationContext)
                    val callback = object : ActionRequest.Callback {
                        override fun <T> onSuccess(result: T) {
                            client = result as HashSetClient<Fish.Skipjack.Tag>
                            items = client.Items
                            FishAdapter.TagAdapter(activity, items).also {
                                it.setClickListener(object :
                                    FishAdapter.RecyclerViewAdapter.ItemClickListener {
                                    override fun onItemClick(view: View?, position: Int) {
                                        Log.i("TUNA RUN:dialogTagList", "onItemClick :$position")
                                        onItemClicked(view, position)
                                    }
                                })

                                it.setChangedListener(object :
                                    FishAdapter.RecyclerViewAdapter.ItemsChangedListener {
                                    override fun onItemsChanged() {
                                        notifyItemsChanged()
                                    }
                                })

                                listView.adapter = it
                                listView.adapter?.notifyDataSetChanged()

                                it.notifyItemsChanged()
                            }

                        }

                        override fun onError(result: String) {
                            Log.e("TUNA RUN:dialogTagList", "Error :$result")
                        }
                    }
                    skipjack.listTags(callback)
                }
            }
            Instant.selectionDialog(popupList)
        }
    }


    class AuthClient constructor(context: Context) {
        private var baseUrl = "http://$apiAddr/Inventive.APIs/authentication/"
        private var connect: Connect = Connect(context)
        fun login(
            user: com.inventive.tunarun.Fish.Security.User,
            callback: ActionRequest.Callback,
        ) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val request = connect.jObj<Fish.Auth.Identity>(response)
                    callback.run { onSuccess(request) }
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val url = baseUrl + "UserLogin"
            connect.post(url, user, jCall)
        }

    }

    class MasterClient constructor(context: Context) {
        private var baseUrl = "http://$apiAddr/Inventive.Skipjack.Master.APIs/master/"
        private var connect: Connect = Connect(context)

        fun get_species(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.Species>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_species", jCall)
        }

        fun get_species_materials(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {

                    connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.Species>>(
                        response
                    ).also { callback.onSuccess(it) }

                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_species_materials", jCall)
        }

        fun get_species_sizes(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.SpeciesSize>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_species_sizes", jCall)
        }

        fun get_species_bases(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.SpeciesBase>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_species_bases", jCall)
        }

        fun get_shifts(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.WorkShift>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_shifts", jCall)
        }

        fun get_bin_flags(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.BinFlag>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_bin_flags", jCall)
        }

        fun get_queue_types(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.QueueType>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_queue_types", jCall)
        }

        fun get_queue_ranges(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.QueueRange>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_queue_ranges", jCall)
        }

        fun get_queue_remarks(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.QueueRemark>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_queue_remarks", jCall)
        }

        fun get_virtual_colors(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.VCColor>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_virtual_colors", jCall)
        }

        fun get_cook_lines(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.CookLine>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_cook_lines", jCall)
        }


        fun get_scrape_lines(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.ScrapeLine>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_scrape_lines", jCall)
        }

        //get_yield_lines_none_details
        fun get_yield_lines_none_details(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.YieldLine>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_yield_lines_none_details", jCall)
        }

        fun get_yield_lines(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.YieldLine>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_yield_lines", jCall)
        }

        fun get_cook_racks(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.CookRack>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_cook_racks", jCall)
        }

        fun get_yield_racks(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.YieldRack>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_yield_racks", jCall)
        }

        fun get_yield_trays(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.YieldTray>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_yield_trays", jCall)
        }

        fun get_tag_colors(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.TagColor>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_tag_colors", jCall)
        }

        fun get_grades(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.Grade>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_grades", jCall)
        }


        fun get_species_origins(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Masters.SpeciesOrigin>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_species_origins", jCall)
        }


    }

    class SkipjackClient constructor(context: Context) {

        private var baseUrl = "http://$apiAddr/Inventive.Skipjack.Core.APIs/skipjack/"
        private var connect: Connect = Connect(context)

        fun getDefault(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.DefaultClient>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "GetDefault", jCall)
        }

        fun getShift(date: Date, shift: Int, callback: ActionRequest.Callback) {

            var strDate = SimpleDateFormat("yyyy-MM-dd").format(date)
            var strShift = shift.toString()
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.DateShift>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "GetShift?_date=$strDate&_shift=$strShift", jCall)
        }

        fun getBin(serialNo: String, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Bin>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "getBin?_serial_no=$serialNo", jCall)
        }


        fun getBinBySloc(sloc: String, callback: ActionRequest.Callback) {

            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Bin>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = Companion.Skipjack.Shift.Date.queryDateString()
            val strShift = Companion.Skipjack.Shift.Shift
            connect.get(
                baseUrl + "get_bin_by_sloc?_date=$strDate&_shift=$strShift&_sloc=$sloc",
                jCall
            )
        }

        fun listBlindSLoc(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Blind.SLoc>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_blind_slocs", jCall)
        }

        fun listBlindStatus(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Blind.Status>>(
                            response
                        )
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_blind_status", jCall)
        }

        fun listBlindReceive(date: Date, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<Fish.Objects.HashSetClient<Fish.Skipjack.Blind.BR>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = date.queryDateString()
            connect.get(baseUrl + "get_blind_receives?_date=$strDate", jCall)
        }

        fun listBlindReceive(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result =
                        connect.jObj<HashSetClient<Fish.Skipjack.Blind.BR>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = Companion.Skipjack.Shift.Date.queryDateString()
            val strShift = Companion.Skipjack.Shift.Shift
            connect.get(baseUrl + "get_blind_receives?_date=$strDate&&_shift=$strShift", jCall)
        }

        fun getBlindReceive(serialNo: String, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Blind.BR>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_blind_receive?_serial_no=$serialNo", jCall)
        }

        fun createThenGetBin(blind: Fish.Skipjack.Blind.BR, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Bin>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.post(baseUrl + "create_then_get_bin", blind, jCall)
        }

        fun changeBlindReceive(blind: Fish.Skipjack.Blind.BR, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Blind.BR>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.post(baseUrl + "change_blind_receive", blind, jCall)
        }

        fun createQueue(queue: Fish.Skipjack.Queue, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<HashSetClient<Fish.Skipjack.Queue>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.post(baseUrl + "create_queue", queue, jCall)
        }

        fun addQueue(bin: Fish.Skipjack.Bin, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Queue>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.post(baseUrl + "add_queue", bin, jCall)
        }

        fun getQueue(date: Date, shift: Int, queNo: Int, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Queue>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }

            val strDate = date.queryDateString()
            connect.get(baseUrl + "get_queue?_date=$strDate&_shift=$shift&_queue_no=$queNo", jCall)
        }

        fun getQueue(queueNo: Int, callback: ActionRequest.Callback) {
            var date = Companion.Skipjack.Shift.Date
            var shift = Companion.Skipjack.Shift.Shift
            return getQueue(date, shift, queueNo, callback)
        }

        fun listQueuesAsCompact(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<HashSetClient<Fish.Skipjack.Queue>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = Companion.Skipjack.Shift.Date.queryDateString()
            val strShift = Companion.Skipjack.Shift.Shift
            connect.get(baseUrl + "get_queues_as_compact?_date=$strDate&_shift=$strShift", jCall)
        }

        fun listQueuesAsGroup(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<HashSetClient<Fish.Skipjack.Queue>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = Companion.Skipjack.Shift.Date.queryDateString()
            val strShift = Companion.Skipjack.Shift.Shift
            connect.get(
                baseUrl + "get_queues_as_group?_date=$strDate&_shift=$strShift",
                jCall
            )
        }


        fun addTag(tag: Fish.Skipjack.Tag, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Tag>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.post(baseUrl + "add_tag", tag, jCall)
        }

        fun getTag(tagNo: String, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Tag>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_tag?_tag=$tagNo", jCall)
        }

        fun listTags(callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<HashSetClient<Fish.Skipjack.Tag>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = Companion.Skipjack.Shift.Date.queryDateString()
            val strShift = Companion.Skipjack.Shift.Shift
            connect.get(
                baseUrl + "get_tags?_date=$strDate&_shift=$strShift",
                jCall
            )
        }

        fun listTags(callback: ActionRequest.Callback, queueNo: Int) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<HashSetClient<Fish.Skipjack.Tag>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = Companion.Skipjack.Shift.Date.queryDateString()
            val strShift = Companion.Skipjack.Shift.Shift
            connect.get(
                baseUrl + "get_tags?_date=$strDate&_shift=$strShift&_queue_no=$queueNo",
                jCall
            )
        }


        fun addCook(cook: Fish.Skipjack.Cook, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Cook>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.post(baseUrl + "add_cook", cook, jCall)
        }


        fun preRack(date: Date, shift: Int, rackNo: String, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Rack>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = date.queryDateString()
            connect.get(baseUrl + "pre_rack?_date=$strDate&_shift=$shift&_rack_no=$rackNo", jCall)
        }

        fun getPreRack(date: Date, shift: Int, rackNo: String, callback: ActionRequest.Callback) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Rack>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = date.queryDateString()
            connect.get(baseUrl + "get_rack?_date=$strDate&_shift=$shift&_rack_no=$rackNo", jCall)
        }

        fun deletePreRack(
            date: Date,
            shift: Int,
            rackNo: String,
            callback: ActionRequest.Callback,
        ) {
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<Fish.Skipjack.Rack>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            val strDate = date.queryDateString()
            connect.get(
                baseUrl + "delete_rack?_date=$strDate&_shift=$shift&_rack_no=$rackNo",
                jCall
            )
        }

        fun listPreRacks(date: Date, shift: Int, callback: ActionRequest.Callback) {
            var _date = date.queryDateString()
            val jCall = object : JSONCallback {
                override fun onSuccess(response: JSONObject) {
                    val result = connect.jObj<HashSetClient<Fish.Skipjack.Rack>>(response)
                    callback.onSuccess(result)
                }

                override fun onError(error: String) {
                    callback.onError(error)
                }
            }
            connect.get(baseUrl + "get_pre_racks?_date=$_date&_shift=$shift", jCall)
        }
    }

    class SkipjackView {
        class Queue {
            class ListItem {
                override fun toString(): String {
                    return queue.queue_no.toString()
                }

                private var queue: Fish.Skipjack.Queue = Fish.Skipjack.Queue()

                class Adapter(
                    context: Activity,
                    var resources: Int,
                    private var items: List<Fish.Skipjack.Queue>,
                ) :
                    ArrayAdapter<Fish.Skipjack.Queue>(context, resources, items) {
                    override fun getView(
                        position: Int,
                        convertView: View?,
                        parent: ViewGroup,
                    ): View {

                        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                        val itemView: View = layoutInflater.inflate(resources, null)

                        var viewQue: TextView = itemView.findViewById(R.id.view_que)
                        var viewBatchNo: TextView = itemView.findViewById(R.id.view_batchNo)
                        var viewLotNo: TextView = itemView.findViewById(R.id.view_lotNo)
                        var viewItemNo: TextView = itemView.findViewById(R.id.view_itemNo)
                        var viewWeight: TextView = itemView.findViewById(R.id.view_weight)

                        var queue: Fish.Skipjack.Queue = items[position]

                        viewQue.text = queue.queue_no.toString()
                        viewBatchNo.text = queue.batch_no
                        viewLotNo.text = queue.lot_no
                        var itemNo = ""
                        if (queue.material_code != null) {
                            itemNo += queue.material_code
                        }
                        if (queue.species_code != null) {
                            if (itemNo.isNotEmpty()) {
                                itemNo += " /"
                            }
                            itemNo += queue.species_code
                        }
                        viewItemNo.text = itemNo
                        viewWeight.text = "${queue.net_weight} KG."
                        return itemView
                    }
                }
            }
        }
    }
}
