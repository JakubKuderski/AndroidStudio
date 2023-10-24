package com.example.kalendarz

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.kalendarz.databinding.FragmentNewTaskSheetBinding
import java.time.LocalTime

class NewTaskSheet(var taskItem: TaskItem?, val wybranaData: String) : BottomSheetDialogFragment()
{
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        if (taskItem != null)
        {
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.desc)
            //if(taskItem!!.dueTime()!! != null){
            //    dueTime = taskItem!!.dueTime()!!
            //    updateTimeButtonText()
            //}
        }
        else
        {
            binding.taskTitle.text = "New Task"
        }

        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)
        binding.deleteButton.setOnClickListener{
            deleteAction()
        }
        binding.saveButton.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }



    private fun openTimePicker() {
        if(dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()

    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d",dueTime!!.hour,dueTime!!.minute)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun deleteAction() {
        taskItem?.let { taskViewModel.deleteTaskItem(it) }
        dismiss()
    }

    private fun saveAction()
    {
        val data = wybranaData
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()
        val dueTimeString = if(dueTime == null) null else TaskItem.timeFormatter.format(dueTime)
        if(taskItem == null)
        {
            Log.d("ASD", "zxc")
            if(dueTimeString?.let { TaskItem(data,name,desc, it) }!=null)
            {
                taskViewModel.addTaskItem(dueTimeString.let { TaskItem(data,name,desc, it) })
            }
            else
            {
                taskViewModel.addTaskItem(TaskItem(data,name,desc, null))
            }

                //val newTask = dueTimeString?.let { TaskItem(data,name,desc, it) }
           // if (newTask != null) {

           // }
        }
        else
        {
            Log.d("zxc", "zxc")
            taskItem!!.date = data
            taskItem!!.name = name
            taskItem!!.desc = desc
            taskItem!!.dueTimeString = dueTimeString
            taskItem!!.id?.let { taskViewModel.updateTaskItem(taskItem!!) }
        }
        binding.name.setText("")
        binding.desc.setText("")
        dismiss()
    }

}