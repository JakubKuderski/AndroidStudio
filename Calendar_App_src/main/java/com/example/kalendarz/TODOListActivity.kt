package com.example.kalendarz


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kalendarz.databinding.ActivityTodolistBinding
import java.time.LocalDate
import com.example.kalendarz.DailyItems
import java.util.*
import kotlin.collections.ArrayList

class TODOListActivity() : AppCompatActivity(), TaskItemClickListener
{
    lateinit var wybranaData: LocalDate
    private lateinit var binding: ActivityTodolistBinding
    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((application as TodoApplication).repository)
    }


    override fun onSaveInstanceState(outState: Bundle) {

        // Save the user's current game state.
        outState.run {
            putString(DailyItems.date, wybranaData.toString())

        }

        // Always call the superclass so it can save the view hierarchy state.
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Always call the superclass so it can restore the view hierarchy.
        super.onRestoreInstanceState(savedInstanceState)

        // Restore state members from saved instance.
        savedInstanceState.run {
            wybranaData = LocalDate.parse(getString(DailyItems.date))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        wybranaData=LocalDate.parse(intent.getStringExtra("Data"))
        Log.d("KALSDASD",wybranaData.toString())
        super.onCreate(savedInstanceState)
        binding = ActivityTodolistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //taskViewModel2 = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null,wybranaData.toString()).show(supportFragmentManager, "newTaskTag")
        }
        setRecyclerView()
    }

    private fun setRecyclerView()
    {
        val mainActivity = this
        taskViewModel.getTaskItems(wybranaData).observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: TaskItem)
    {
        NewTaskSheet(taskItem, wybranaData.toString()).show(supportFragmentManager,"newTaskTag")
    }


}
