package com.ansela.myalarmproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ansela.myalarmproject1.R
import com.ansela.myalarmproject1.databinding.ActivityMainBinding
import com.ansela.myalarmproject1.AlarmReceiver
import com.ansela.myalrmapp.fragment.DatePickerFragment
import com.ansela.myalrmapp.fragment.TimePickerFragment
import com.ansela.myalrmapp.room.Alarm
import com.ansela.myalrmapp.room.AlarmDB
import kotlinx.android.synthetic.main.activity_one_time_alarm.*
import kotlinx.android.synthetic.main.activity_one_time_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OneTimeAlarm : AppCompatActivity(), View.OnClickListener,
    DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {

    private var binding: ActivityMainBinding? = null

    private lateinit var alarmReceiver: AlarmReceiver

    val db by lazy { AlarmDB(this) }


    companion object {
        private const val DATE_PICKER_TAG = "DataPicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePicker"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_one_time_alarm)

        btn_set_date_one_time.setOnClickListener(this)
        btn_set_time_one_time.setOnClickListener(this)

        btn_add_set_one_time_alarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_set_date_one_time -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_set_time_one_time -> {
                val timePickerFragmentOntime = TimePickerFragment()
                timePickerFragmentOntime.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_add_set_one_time_alarm -> {
                val onceDate = tv_once_date.text.toString()
                val oneceTime = tv_once_time.text.toString()
                val onceMessage = et_note_one_time.text.toString()

                alarmReceiver.setOneTimeAlarm(
                    this,AlarmReceiver.TYPE_ONE_TIME,
                    onceDate,
                    oneceTime,
                    onceMessage

                )

                CoroutineScope(Dispatchers.IO).launch{
                    db.alarmDao().addAlarm(
                        Alarm(0,oneceTime,onceDate,onceMessage,AlarmReceiver.TYPE_ONE_TIME)
                    )

                    finish()
                }
            }
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year,month,dayOfMonth)

        val dateFormatOneTime = SimpleDateFormat("dd-MM-yyyyy",Locale.getDefault())

        tv_once_date.text = dateFormatOneTime.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val timeFormatOneTime = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_ONCE_TAG -> tv_note_one_time.text = timeFormatOneTime.format(calendar.time)
            else ->{

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
