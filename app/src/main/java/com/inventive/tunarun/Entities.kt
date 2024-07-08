package com.inventive.tunarun

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class Utils {
    companion object {
        fun getTimeString(date: Date): String {
            val pattern = "dd.MM.yyyy"
            return getTimeString(date, pattern)
        }

        private fun getTimeString(date: Date, pattern: String): String {
            val simpleDateFormat = SimpleDateFormat(pattern)
            return simpleDateFormat.format(date)
        }

        fun getDateTime(unix: Long): Date = Date(unix * 1000)

        fun getTimeString(unix: Long): String {
            val time = getDateTime(unix)
            return getTimeString(time)
        }

        fun getTimeString(unix: Long, pattern: String): String {
            val time = getDateTime(unix)
            return getTimeString(time, pattern)
        }


        private fun getMonthText(month: Int): String {
            if (month == 1) return "JAN"
            if (month == 2) return "FEB"
            if (month == 3) return "MAR"
            if (month == 4) return "APR"
            if (month == 5) return "MAY"
            if (month == 6) return "JUN"
            if (month == 7) return "JUL"
            if (month == 8) return "AUG"
            if (month == 9) return "SEP"
            if (month == 10) return "OCT"
            if (month == 11) return "NOV"
            return if (month == 12) "DEC" else "WRONG NUMBER"
            //default should never happen
        }
    }
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

interface InitCallback {

    var count: Int
    var total: Int
    fun refresh()
    fun success()
    fun onError(error: String)
}

interface DateCallback {
    var init: Date
    fun onSuccess(result: Date)
    fun onError(error: String)
}

interface StringCallback {
    var value: String
    fun onSuccess(result: String)
    fun onError(error: String)
}

interface NumberCallback {
    var value: Number
    fun onSuccess(result: Number)
    fun onError(error: String)
}

interface JSONCallback {
    fun onSuccess(response: JSONObject)
    fun onError(error: String)
}

class ActionArgument {
    var id: Int = 0
    var value: String = ""
    var match: Int = 0
    var shiftId: Int = 0
}

class ActionRequest<T> {
    var id: Int = 0;
    var error: String = ""
    var message: String = ""
    var type: T? = null
    var list: List<T> = listOf<T>()

    interface Callback {
        fun <T> onSuccess(result: T)
        fun onError(result: String)
    }
}


class Fish {
    class Program {
        var id: Int = 0
        var name: String = ""
    }

    class Device {
        var id: Int = 0
        var deviceId: String = ""

        var name: String = ""
        var hostName: String = "PPC"
        var domainName: String = ""

        var description: String = ""
        var interfaceType: String = ""
        var physicalAddress: String = ""

        var ipv4Address: String = ""
        var ipv6Address: String = ""

        var osPlatform: String = ""
        var osServicePack: String = ""
        var osVersion: String = ""
        var osVersionString: String = ""
        var isOSx64: Boolean = false
        var isOSx64Process: Boolean = false

        var timeStamp: Date = Date()

        var state: Fish.Objects.EntityState = Fish.Objects.EntityState.NEW
    }

    class Objects {

        enum class EntityState(val value: Int) {
            NEW(0),
            EDIT(1),
            CHANGE(2),
            SUCCESS(3),
            OK(4),
            WARNING(5),
            FAILURE(6),
            DUPLICATE(7),
            DELETED(8),
            FOUND(9),
            NOT_FOUND(10),
            EXISTED(11),
            REJECT(12),
            NOTHING(13),
            UNCHANGE(14),
            ERROR(15),
            NOT_ALLOW_NULL(16);

            companion object {
                fun fromInt(value: Int) = EntityState.values().first { it.value == value }
            }
        }

        class EntityMessage {
            var Id: Int = 0
            var Method: String = ""
            var Action: String = ""
            var Description: String = ""
            var ErrorMessage: String = ""
            var TimeStamp: Date = Date()
            var _uxTimeStamp: Long = 0
        }

        open class EntityClient<TEntity> where TEntity : EntityClient<TEntity> {
            var Id: Int = 0
            var State: Int = 0
            var EntityMessages: MutableList<EntityMessage> = mutableListOf()
            var TimeStamp: Date = Date()
            var state: EntityState = EntityState.NEW
                get() {
                    return EntityState.fromInt(State)
                }
            val entityMessage: String
                get() {
                    return if (EntityMessages.isNotEmpty()) {
                        val msg = EntityMessages.last()
                        "${msg.Action}, ${msg.Description} (#${state})"
                    } else {
                        ""
                    }
                }
        }

        open class HashSetClient<TEntity> where TEntity : EntityClient<TEntity> {
            var Id: Int = 0
            var State: Int = 0
            var EntityMessages: MutableList<EntityMessage> = mutableListOf()
            var TimeStamp: Date = Date()
            var Items: MutableList<TEntity> = mutableListOf()

            init {
                EntityMessages = mutableListOf()
                Items = mutableListOf()
            }

            val count: Int
                get() = Items.size

            fun add(item: TEntity) {
                Items.add(item)
            }

            val entityMessage: String
                get() {
                    return if (EntityMessages.isNotEmpty()) {
                        val msg = EntityMessages.last()
                        "${msg.Action}, ${msg.Description} (#${State})"
                    } else {
                        ""
                    }
                }
        }

    }

    class Security {
        class User {
            var UserId: String = ""
            var Role: String = ""
            var Password: String = ""
            var Device: Device? = null
            var CreateTime: Date = Date()
        }
    }

    class Auth {
        class Identity {
            var UserId: String = ""
            var SessionId: String = ""
            var IsAuthenticated: Boolean = false
            var RoleId: Int = 0
            var RoleName: String = ""
            var TimeStamp: Date? = null
            var Program: Program? = null
            var Device: Device? = null
            var Description: String = ""
        }
    }

    class Skipjack {
        class DefaultClient : Objects.EntityClient<DefaultClient>() {
            var PresentDate: Date? = null
            var PresentDateTime: Date? = null
            var PresentShift: DateShift? = null
            var DateFormat: String? = null
            var WeightFormat: String? = null
            var VCColorText: String? = null
            var MasterShifts: Objects.HashSetClient<Masters.WorkShift>? = null
        }

        class Masters {
            class WorkHour : Objects.EntityClient<WorkHour>() {
                override fun toString(): String {
                    return this.HourNo.toString()
                }

                var Shift: Int = 0
                var HourNo: Int = 0
                var Description: String = ""
            }

            class WorkShift : Objects.EntityClient<WorkShift>() {
                override fun toString(): String {
                    return ShiftCode.toString()
                }

                var ShiftCode: String? = null
                var Description: String? = null

                var Hours: List<WorkHour>? = null

                init {
                    this.Hours = ArrayList<WorkHour>()
                }
            }

            class Species : Objects.EntityClient<Species>() {
                override fun toString(): String {
                    return species_code.toString()
                }

                var species_base_id: Int = 0
                var species_base_code: String = ""
                var species_base_description: String = ""
                var species_code: String = ""
                var species_description: String = ""
                var is_available: Boolean = false
                var material_code: String = ""
                var material_name: String = ""
                var material_description: String = ""
            }

            class BinFlag : Objects.EntityClient<BinFlag>() {
                var flag_description: String = ""
                var view_index: Int = 0
            }

            class CookLine : Objects.EntityClient<CookLine>() {
                override fun toString(): String {
                    return line_no.toString()
                }

                var line_no: Int = 0
                var line_description: String = ""
            }

            class CookRack : Objects.EntityClient<CookRack>() {
                override fun toString(): String {
                    return rack_code.toString()
                }

                var rack_code: String? = null
                var rack_description: String? = null
            }

            class GradeParameter : Objects.EntityClient<GradeParameter>() {
                var grade_code: String = ""
                var grade_description: String = ""

                var grade_yield_code: String = ""
                var grade_yield_description: String = ""

                var is_yield_recovery: Boolean = false
                var yield_recovery_factor: Double = 0.0

                var is_yield_value: Boolean = false
                var yield_value_factor: Double = 0.0

                var parameter_type: String = ""
                var parameter_value: String = ""
            }

            class GradePeriod : Objects.EntityClient<GradePeriod>() {
                var period_date: Date = Date()
                var period_description: String = ""
                var is_available: Boolean = false
            }

            class GradeYield : Objects.EntityClient<GradeYield>() {
                override fun toString(): String {
                    return grade_yield_code.toString()
                }

                var grade_yield_code: String? = null
                var grade_yield_description: String? = null
                var is_available: Boolean = false
                var is_yield_recovery: Boolean = false
                var yield_recovery_factor: Double = 0.0
                var is_yield_value: Boolean = false
                var yield_value_factor: Double = 0.0
                var Parameters: Objects.HashSetClient<GradeParameter>? = null

                init {
                    this.Parameters = Objects.HashSetClient<GradeParameter>()
                }
            }

            open class Grade : Objects.EntityClient<Grade>() {
                override fun toString(): String {
                    return grade_code.toString()
                }

                var grade_code: String? = null
                var grade_description: String? = null
                var is_available: Boolean = false
                var GradeYields: Objects.HashSetClient<Masters.GradeYield>? = null

                init {
                    this.GradeYields = Objects.HashSetClient<Masters.GradeYield>()
                }
            }

            class GradeRemark : Grade() {
                var grade_remark_code: String = ""
                var grade_remark_description: String = ""

                override fun toString(): String {
                    return grade_remark_code
                }
            }

            class Parameter : Objects.EntityClient<Parameter>() {

                var parameter_type: String = ""

                var parameter_value: String = ""
            }

            class QueueRange : Objects.EntityClient<QueueRange>() {

                override fun toString(): String {
                    return this.queue_range_description.toString()
                }

                var queue_range_description: String? = null

                var min_queue: Int = 0

                var max_queue: Int = 0

                var is_available: Boolean = false
            }

            class QueueRemark : Objects.EntityClient<QueueRemark>() {
                var remark_description: String = ""
                var view_index: Int = 0
            }

            class QueueType : Objects.EntityClient<QueueType>() {

                override fun toString(): String {
                    return queue_type_code.toString()
                }

                var queue_type_code: String? = null
                var queue_type_description: String? = null
                var is_available: Boolean = false
            }

            class ScrapeLine : Objects.EntityClient<ScrapeLine>() {
                override fun toString(): String {
                    return line_no.toString()
                }

                var line_no: Int = 0
                var line_description: String = ""
            }

            class SpeciesBase : Objects.EntityClient<SpeciesBase>() {

                override fun toString(): String {
                    return species_base_code.toString()
                }

                var species_base_code: String? = null
                var species_base_description: String? = null
                var is_available: Boolean = false
                var Species: Objects.HashSetClient<Species>? = null
            }

            class SpeciesOrigin : Objects.EntityClient<SpeciesOrigin>() {

                var species_origin_code: String? = null

                var species_origin_description: String? = null

                var is_available: Boolean = false
            }

            class SpeciesSize : Objects.EntityClient<SpeciesSize>() {

                var species_size_code: String? = null
                var species_size_description: String? = null
                var species_size_quantity: Double = 0.0
                var species_size_weight: Double = 0.0
                var view_index: Int = 0
            }

            class TagColor : Objects.EntityClient<TagColor>() {

                var color_code: String = ""

                var color_type: String = ""

                var color_description: String = ""

                var color_hex: String? = null

            }


            class VCColor : Objects.EntityClient<VCColor>() {

                var color_code: String? = null

                var color_description: String? = null

                var view_index: Int = 0

                var color_hex: String? = null
            }

            class YieldLine : Objects.EntityClient<YieldLine>() {

                override fun toString(): String {
                    return this.line_description
                }

                var line_no: Int = 0
                var line_description: String = ""
                var YieldTable: Objects.HashSetClient<YieldTable> = Objects.HashSetClient()
            }

            class YieldTable : Objects.EntityClient<YieldTable>() {

                override fun toString(): String {
                    return this.table_description
                }

                var line_no: Int = 0
                var line_description: String = ""
                var table_no: Int = 0
                var table_description: String = ""
                var YieldPositions: Objects.HashSetClient<YieldPosition> = Objects.HashSetClient()

                init {
                    YieldPositions = Objects.HashSetClient()
                }
            }

            class YieldPosition : Objects.EntityClient<YieldPosition>() {

                override fun toString(): String {
                    return position.toString()
                }

                var line_no: Int = 0
                var line_description: String = ""
                var table_no: Int = 0
                var table_description: String = ""

                var position: Int = 0
            }

            class YieldRack : Objects.EntityClient<YieldRack>() {

                override fun toString(): String {
                    return rack_code.toString()
                }

                var rack_code: String? = null

                var rack_description: String? = null
            }

            class YieldTray : Objects.EntityClient<YieldTray>() {
                var tray_code: Int = 0
                var tray_description: String = ""
                var tray_weight: Double = 0.0
            }
        }

        class DateShift : Objects.EntityClient<DateShift>() {
            val Date: Date = Date()
            val WorkShift: Masters.WorkShift = Masters.WorkShift()
            var Hour: Int = 0
            val Shift: Int = 0
        }

        class Blind {
            class BR : Objects.EntityClient<BR>() {
                var serial_no: String = ""
                var batch_no: String = ""
                var lot_no: String = ""
                var species: String = ""
                var species_desc: String = ""

                var origin: String = ""
                var origin_desc: String = ""

                var weight: Number = 0

                var sloc: String = ""
                var status: String = ""
                var remark: String = ""

                var receive_date: Date = Date()

                var shift_id: Int = 0
                var shift_date: Date = Date()
                var shift_text: String = ""

                var material_code: String = ""
                var material_name: String = ""
                var material_desc: String = ""

                var time_stamp: Date = Date()

            }

            class SLoc : Objects.EntityClient<SLoc>() {
                var sloc: String? = null
            }

            class Status : Objects.EntityClient<Status>() {
                var statusText: String? = null
            }
        }

        class Bin : Objects.EntityClient<Bin>() {
            var date: Date? = null
            var shift: Int = 0
            var queue_id: Int = 0
            var queue_no: Int = 0

            var lot_no: String? = null
            var material_code: String? = null
            var material_name: String? = null
            var material_sesc: String? = null

            var serial_no: String? = null
            var serial_desc: String? = null

            var species_code: String? = null
            var species_origin_code: String? = null

            var cold_storage_size_code: String? = null
            var location_description: String? = null

            var batch_id: Int = 0
            var batch: String? = null
            var batch_no: String? = null
            var batch_no1: String? = null
            var batch_no2: String? = null

            var gross_weight: Number = 0
            var tare_weight: Number = 0
            var net_weight: Number = 0

            var receive_date: Date? = null
        }


        class Batch : Objects.EntityClient<Batch> {
            constructor() {
                this.bins = Objects.HashSetClient<Bin>()
                this.tags = Objects.HashSetClient<Tag>()
            }

            var date: Date? = null
            var shift: Int? = null
            var queue_id: Int? = null
            var queue_no: Int? = null
            var batch: String? = null
            var batch_no: String? = null
            var batch_ref: String? = null
            var cold_storage_size_code: String? = null
            var species_code: String? = null
            var queue_time_plan: Date? = null
            var queue_time_actual: Date? = null
            var queue_time_melt_plan: Date? = null
            var queue_time_melt_actual: Date? = null
            var queue_time_cut_plan: Date? = null
            var queue_time_cut_actual: Date? = null
            var queue_arrange_start: Date? = null
            var queue_arrange_finish: Date? = null
            var queue_remark_description: String? = null
            var product_code_description: String? = null
            var product_group_description: String? = null

            val bins: Objects.HashSetClient<Bin>
            val tags: Objects.HashSetClient<Tag>
        }

        class Tag : Objects.EntityClient<Tag>() {
            var TagState: TagState? = null
            var tag: String = ""
            var _guid: UUID = UUID.randomUUID()
            var Scrapes: Objects.HashSetClient<Scrape> = Objects.HashSetClient()


            init {
                this.Scrapes = Objects.HashSetClient<Scrape>()
                this.tag = ""
                this._guid = UUID.randomUUID()
                this.TagState = Skipjack.TagState.NEW
            }


            var date: Date = Date()

            var shift: Int = 0

            var shift_id: Int = 0

            var shift_code: String = ""

            var queue_id: Int = 0

            var queue_no: Int = 0

            var batch_group_id: Int = 0
            var batch_group_text: String = ""

            var product_code_description: String = ""
            var product_group_description: String = ""

            var queue_remark_description: String = ""

            var batch_no: String = ""

            var species_base_code: String = ""

            var species_id: Int = 0

            var species_code: String = ""

            var species_origin_code: String = ""

            var species_size_id: Int = 0

            var species_size_code: String = ""

            var species_size_quantity: BigDecimal = BigDecimal.ZERO

            var species_size_weight: BigDecimal = BigDecimal.ZERO

            var quantity_tray: Int = 0

            var quantity_each: Int = 0

            var quantity_tray_scrape: BigDecimal = BigDecimal.ZERO

            var quantity_each_scrape: BigDecimal = BigDecimal.ZERO

            var tag_color_id: Int = 0

            var tag_color_code: String = ""

            var tag_color_type: String = ""

            var cook_rack_id: Int = 0

            var cook_rack_no: String = ""

            var cook_rack_sequence_no: Int = 0

            var rack_arrange_start: Date = Date()

            var rack_arrange_finish: Date? = null

            var is_deleted: Boolean = false

            var deleted_time: Date? = null

            var scrape_line_no: Int? = null

            var scrape_time: Date? = null

            var yield_line_no: Int? = null

            var is_cooked: Boolean = false

            var cook_id: Int? = null

            var cook_sequence_no: Int? = null

            var cook_line_no: Int? = null

            var cook_line_sequence: Int? = null

            var cook_sub_id: Int? = null

            var cook_sub_no: Int? = null

            var batch_cook_id: Int? = null

            var batch_cook_no: Int? = null

            var batch_cook_temperature: BigDecimal? = null

            var batch_cook_time: Date? = null

            var lasted_print_time: Date? = null

            var TagType: TagType? = null

            var Rack: Rack? = null


            val date_text: String
                get() = date.toString("dd/MM/yyyy")
            val shift_code_text: String
                get() = shift_code
            val tag_text: String
                get() = tag
            val tag_type_text: String
                get() = tag_color_type

            val batch_no_text: String
                get() = batch_no
            val species_code_text: String
                get() = species_code
            val species_size_code_text: String
                get() = species_size_code
            val cook_rack_sequence_text: String
                get() = cook_rack_sequence_no.toString()

            val quantity_tray_text: String
                get() = quantity_tray.toString()
            val quantity_each_text: String
                get() = quantity_each.toString()
            val quantity_text: String
                get() {
                    if (quantity_each > 0) {
                        return "${quantity_tray.toString()}/${quantity_each.toString()}"
                    } else {
                        return quantity_tray.toString()
                    }
                }

            val rack_arrange_time_text: String
                get() = rack_arrange_start.toString("HH:mm")

            val cook_line_no_text: String
                get() = cook_line_no?.toString() ?: ""
            val cook_rack_no_text: String
                get() = cook_rack_no

            val scrape_time_text: String
                get() = scrape_time?.toString("HH:mm") ?: ""

            val scrape_line_no_text: String
                get() = scrape_line_no?.toString() ?: ""
            val yield_line_no_text: String
                get() = yield_line_no?.toString() ?: ""

            val queue_no_text: String
                get() = queue_no.toString()
            val queue_remark_description_text: String
                get() = queue_remark_description
            val lasted_print_time_text: String
                get() = lasted_print_time?.toString("dd/MM/yyyy HH:mm") ?: ""
        }

        class Scrape : Objects.EntityClient<Scrape>() {
            var tag_id: Int = 0
            var date: Date? = null
            var shift: Int = 0
            var scrape_line_no: Int = 0
            var yield_line_no: Int = 0
            var quantity_tray: Int = 0
            var quantity_each: Int = 0
            var time_stamp: Date? = null
        }

        class BatchGroup : Objects.EntityClient<BatchGroup>() {
            var date: Date? = null
            var shift: Int = 0
            var shift_code: String? = null
            var shift_description: String? = null
            var batch_group_text: String? = null
            var view_index: Int = 0
            var batch_count: Int = 0
            var bin_count: Int = 0
            var net_weight: Double = 0.0
            var time_stamp: Date? = null
        }


        class Cook : Objects.EntityClient<Cook>() {
            var subs: Objects.HashSetClient<CookSub>? = null
            var tags: Objects.HashSetClient<Tag>? = null

            init {
                this.subs = Objects.HashSetClient<CookSub>()
                this.tags = Objects.HashSetClient<Tag>()
            }

            var date: Date? = null
            var shift: Int = 0
            var shift_id: Int = 0
            var shift_sequence: Int = 0
            var line_no: Int = 0
            var line_sequence: Int = 0
            var rack_count: Int = 0
        }

        class CookBatch : Objects.EntityClient<CookBatch>() {
            var tags: Objects.HashSetClient<Tag>? = null
            var temperatures: Objects.HashSetClient<CookTemperature>? = null
            var cookBatchState: CookBatchState? = null

            init {
                this.tags = Objects.HashSetClient<Tag>()
                this.temperatures = Objects.HashSetClient<CookTemperature>()
                this.cookBatchState = Skipjack.CookBatchState.NEW
            }

            var date: Date? = null
            var shift: Int? = null

            var shift_id: Int? = null
            var sequence_no: Int? = null

            // ตู้อบปลา
            var line_no: Int? = null

            // ลำดับในแต่ละตู้อบ
            var line_sequence: Int? = null

            var cook_id: Int? = null
            var cook_sub_id: Int? = null
            var cook_sub_no: Int? = null
            var batch_cook_no: Int? = null
            var batch_cook_time: Date? = null

            var queue_id: Int? = null
            var queue_no: Int? = null
            var batch_no: String? = null

            var batch_group_id: Int? = null
            var batch_group_text: String? = null

            var queue_remark_description: String? = null

            var product_code_description: String? = null
            var product_group_description: String? = null

            var species_base_code: String? = null
            var species_id: Int? = null
            var species_code: String? = null

            var species_size_id: Int? = null
            var species_size_code: String? = null

            var rack_count: Int? = null
            var rack_scrape: Int? = null

            var tags_count: Int? = null
            var tags_scrape: Int? = null

            var steam_on_time: Date? = null
            var steam_off_time: Date? = null

            var scrape_begin_time: Date? = null
            var scrape_finish_time: Date? = null
            var scrape_span_text: String? = null

            var scrape_line_text: String? = null
            var temperature_average: Date? = null

        }

        class CookSub : Objects.EntityClient<CookSub> {
            constructor() {
                this.batchSet = Objects.HashSetClient<CookBatch>()
                this.rackSet = Objects.HashSetClient<Rack>()
            }

            var date: Date? = null
            var shift: Int = 0

            var sequence_no: Int = 0
            var line_no: Int = 0
            var line_sequence: Int = 0

            var cook_id: Int = 0
            var cook_sub_no: Int = 0
            var rack_count: Int = 0

            var batchSet: Objects.HashSetClient<CookBatch>? = null
            var rackSet: Objects.HashSetClient<Rack>? = null
        }

        class CookTemperature : Objects.EntityClient<CookTemperature>() {
            var cook_batch_id: Int = 0
            var temperature: Number = 0.0
            var tamperature_time: Date = Date()
        }

        class Rack : Objects.EntityClient<Rack>() {
            var date: Date? = null
            var shift: Int = 0
            var shift_code: String? = null
            var rack_sequence: Int? = null
            var rack_no: String? = null
            var rack_arrange_start: Date = Date()
            var rack_arrange_finish: Date? = null
            var tag_count: Int = 0
            var is_cooked: Boolean = false
            var cook_id: Int? = null
            var cook_sequence_no: Int? = null
            var cook_line_no: Int? = null
            var cook_line_sequence: Int? = null
            var cook_sub_id: Int? = null
            var cook_sub_no: Int? = null
            var batch_cook_id: Int? = null
            var batch_cook_no: Int? = null
            var batch_cook_time: Date? = null
            var tags: Objects.HashSetClient<Tag> = Objects.HashSetClient<Tag>()
        }

        class Queue : Objects.EntityClient<Queue>() {

            var lot_no: String = ""
            var material_code: String = ""
            var material_name: String = ""
            var material_sesc: String = ""

            var time_stamp: Date? = null

            var SpeciesOrigin: Masters.SpeciesOrigin = Masters.SpeciesOrigin()
            var Species: Masters.Species = Masters.Species()

            var QueueRange: Masters.QueueRange = Masters.QueueRange()
            var QueueType: Masters.QueueType = Masters.QueueType()
            var QueueState: Skipjack.QueueState = Skipjack.QueueState.EMPTY_QUEUE


            var VCColors: Objects.HashSetClient<Masters.VCColor> = Objects.HashSetClient()
            var Batchs: Objects.HashSetClient<Batch> = Objects.HashSetClient()
            var Bins: Objects.HashSetClient<Bin> = Objects.HashSetClient()
            var Tags: Objects.HashSetClient<Tag> = Objects.HashSetClient()

            var batch_no: String = ""
            var queue_type_code: String = ""
            var batch_group_text: String = ""
            var species_base_code: String = ""
            var species_code: String = ""
            var species_origin_code: String = ""
            var queue_remark_description: String = ""

            init {
                this.QueueState = Skipjack.QueueState.CREATE_QUEUE
                this.VCColors = Objects.HashSetClient<Masters.VCColor>()
                this.Bins = Objects.HashSetClient<Bin>()
                this.Batchs = Objects.HashSetClient<Batch>()
                this.Tags = Objects.HashSetClient<Tag>()

                this.batch_no = ""
                this.queue_type_code = ""
                this.batch_group_text = ""
                this.species_base_code = ""
                this.species_code = ""
                this.species_origin_code = ""
                this.queue_remark_description = ""
            }

            var DateShift: DateShift? = null

            var date: Date = Date()
            var shift: Int = 0

            var shift_code: String = ""
            var shift_description: String = ""

            var queue_no: Int = 0
            var batch: String = ""

            var product_code_description: String = ""
            var product_group_description: String = ""

            var cold_storage_size_code: String = ""
            var location_description: String = ""

            var batch_group_id: Int = 0

            var species_base_id: Int = 0
            var species_base_description: String = ""

            var species_id: Int = 0
            var species_description: String = ""

            var species_origin_id: Int = 0
            var species_origin_description: String = ""

            var batch_weight: BigDecimal? = null
            var batch_weight_estimate: BigDecimal? = null

            val batch_weight_estimation: BigDecimal?
                get() = if (batch_weight != null && batch_weight_estimate != null)
                    (batch_weight_estimate!! - batch_weight!!) / batch_weight!!
                else null

            val batch_weight_estimation_percent: String
                get() = if (batch_weight_estimation != null)
                    String.format("%0.2f%%", batch_weight_estimation!! * 100.toBigDecimal())
                else ""

            var queue_range_id: Int = 0
            var queue_range_description: String = ""

            var queue_type_id: Int = 0
            var queue_type_description: String = ""

            var gross_weight: BigDecimal = BigDecimal.ZERO
            var tare_weight: BigDecimal = BigDecimal.ZERO
            var net_weight: BigDecimal = BigDecimal.ZERO

            var queue_time_plan: Date? = null
            var queue_time_actual: Date? = null
            var queue_time_thaw_plan: Date? = null
            var queue_time_thaw_actual: Date? = null
            var queue_time_cut_plan: Date? = null
            var queue_time_cut_actual: Date? = null

            var queue_arrange_start: Date? = null
            var queue_arrange_finish: Date? = null

            var batch_count: Int = 0
            var bin_count: Int = 0
            var tag_count: Int = 0
            var rack_count: Int = 0

            var vc_color_text: String = ""

            var is_vccolor_dirty: Boolean = false

            var material_description: String = ""


            var first_bin_time: Date? = null
            val first_bin_time_text: String
                get() = first_bin_time?.let { SimpleDateFormat("HH:mm").format(it) } ?: ""

            var percent_salt_cold_storage: BigDecimal = BigDecimal.ZERO
            val percent_salt_cold_storage_text: String
                get() = if (percent_salt_cold_storage > BigDecimal.ZERO)
                    percent_salt_cold_storage.setScale(2, RoundingMode.HALF_EVEN).toString()
                else ""

            var percent_salt_inspection: BigDecimal = BigDecimal.ZERO
            val percent_salt_inspection_text: String
                get() = if (percent_salt_inspection > BigDecimal.ZERO)
                    percent_salt_inspection.setScale(2, RoundingMode.HALF_EVEN).toString()
                else ""
        }

        enum class QueueStyle {
            FULL,
            NORMAL,
            COMPACT
        }

        enum class QueueState {
            CREATE_QUEUE,
            EMPTY_QUEUE,
            CREATE_TAG,
            STEAM_ON,
            STEAM_OFF,
            TEMPERATURE_REC,
            SCRAPE_LINE,
            CLOSE_QUEUE
        }

        enum class TagState {
            NEW,
            CREATE_TAG,
            DELETE_TAG,
            STEAM_ON,
            STEAM_OFF,
            TEMPERATURE_REC,
            SCRAPE_LINE,
            CLOSE_TAG
        }

        enum class TagType {
            DS,
            NS,
            USA,
            SPECIAL
        }

        enum class CookBatchState {
            NEW,
            STEAM_ON,
            STEAM_OFF,
            TEMPERATURE_REC,
            SCRAPE_LINE,
            CLOSE_BATCH
        }
    }
}
