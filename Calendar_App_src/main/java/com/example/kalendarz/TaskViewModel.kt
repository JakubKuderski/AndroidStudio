package com.example.kalendarz

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(private val repository: TaskItemRepository): ViewModel()
{
    //val taskItems: LiveData<List<TaskItem>> = repository.allTaskItems(taskItem).asLiveData()
    fun getTaskItems(date: LocalDate): LiveData<List<TaskItem>>
    {
        return repository.allTaskItems(date).asLiveData()
    }


    fun addTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        Log.d("takviewmodel", "${taskItem.date}")
        repository.insertTaskItem(taskItem)
    }

    fun updateTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.updateTaskItem(taskItem)
    }

    fun deleteTaskItem(taskItem: TaskItem) = viewModelScope.launch { repository.deleteTaskItem(taskItem) }
}

class TaskItemModelFactory(private val repository: TaskItemRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(repository) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}