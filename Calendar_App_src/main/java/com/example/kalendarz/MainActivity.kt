package com.example.kalendarz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kalendarz.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var selectedDate: LocalDate = LocalDate.now()
    //private var currentDate: String = LocalDate.now().toString()

    //override fun onConfigurationChanged(newConfig: Configuration) {
    //    super.onConfigurationChanged(newConfig)
        //val orientation = newConfig.orientation
    //}

    override fun onSaveInstanceState(outState: Bundle) {

        // Save the user's current game state.
        outState.run {
            putString(currentDate, selectedDate.toString())
        }

        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can restore the view hierarchy.
        super.onRestoreInstanceState(savedInstanceState)

        // Restore state members from saved instance.
        savedInstanceState.run {
            selectedDate = LocalDate.parse(getString(currentDate))
        }
    }

    companion object{
        internal var currentDate: String = LocalDate.now().toString()
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidgets()
        if(savedInstanceState != null)
        {
            with(savedInstanceState)
            {
                setContentView(R.layout.activity_main)
                initWidgets()
                selectedDate = LocalDate.parse(getString(currentDate))
                //selectedDate = LocalDate.parse(currentDate)
                setMonthView()
            }
        }
        else {
            selectedDate = LocalDate.now()
            setMonthView()
        }
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        monthYearText = findViewById(R.id.monthYearTV)
    }

    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        calendarRecyclerView.layoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        Log.d("ASD", "$firstOfMonth")
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        Log.d("ASDs", "$dayOfWeek")
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun DayOfMonth(date: LocalDate,position: Int): String{
        val formatter = DateTimeFormatter.ofPattern("DD")
        return date.format(formatter)
    }

    fun previousMonthAction(view: View) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    override fun onItemClick(position: Int, dayText: String) {
        val firstOfMonth = selectedDate.withDayOfMonth(1)
        var kliknieta_data:LocalDate
        //val asd = firstOfMonth.dayOfWeek.value
        //Log.d("ASD","$asd" )
        //Log.d("QWE", "$position")
        kliknieta_data = selectedDate.withDayOfMonth((position-(firstOfMonth.dayOfWeek.value)+1))
        if (dayText != "") {
            Log.d("zxc ","$kliknieta_data")
            val myIntent = Intent(this, TODOListActivity()::class.java)
            myIntent.putExtra("Data", "$kliknieta_data")
            //startActivity(myIntent)
            this.startActivity(myIntent)
            //startActivity(Intent(this, TODOListActivity()::class.java))
        }
    }

    /*private fun setTODOView()
    {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            //binding.todoListRecyclerView.apply
            //
            binding
            {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }*/

}
