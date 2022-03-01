package com.ansela.myalarmproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ansela.myalarmproject1.R
import com.ansela.myalarmproject1.databinding.ActivityMainBinding
import com.ansela.myalarmproject1.AlarmReceiver
import com.ansela.myalrmapp.fragment.TimePickerFragment
import com.ansela.myalrmapp.room.Alarm
import com.ansela.myalrmapp.room.AlarmDB
import kotlinx.android.synthetic.main.activity_repeating_alarm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RepeatingAlarm : AppCompatActivity(), View.OnClickListener,   TimePickerFragment.DialogTimeListener {

    private var binding: ActivityMainBinding? = null

    private lateinit var alarmReceiver: AlarmReceiver

    val db by lazy { AlarmDB(this) }

    companion object{
        private const val TIME_PICKER_REPEAT_TAG ="TimePickerRepeat"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_repeating_alarm)

        btn_set_time_repeating.setOnClickListener(this)

        btn_add_set_repeating_alarm.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_set_time_repeating -> {
                val timePickerFragmentRepeating = TimePickerFragment()
                timePickerFragmentRepeating.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_add_set_repeating_alarm -> {
                val repeatTime = tv_repeating_time.text.toString()
                val repeatingMessage = et_note_repeating.text.toString()

                alarmReceiver.setRepeatingAlarm(
                    this, AlarmReceiver.TYPE_REPEATING,
                    repeatTime,
                    repeatingMessage
                )

                CoroutineScope(Dispatchers.IO).launch {
                    db.alarmDao().addAlarm(
                        Alarm(0,repeatTime,"Repeating Alarm",repeatingMessage,AlarmReceiver.TYPE_REPEATING)
                    )
                    finish()
                }
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
        calendar.set(Calendar.MINUTE,minute)

        val timeFormatRepeating = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_REPEAT_TAG -> tv_repeating_time.text = timeFormatRepeating.format(calendar.time)
            else -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}