package com.example.kalendarz

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TaskItemRepository(private val taskItemDao: TaskItemDao)
{
    //val allTaskItems: Flow<List<TaskItem>> = taskItemDao.loadTaskItemsFromDay(taskItem.date)

    @WorkerThread
    fun  allTaskItems(date: LocalDate): Flow<List<TaskItem>>
    {
        return taskItemDao.loadTaskItemsFromDay(date.toString())
    }
    @WorkerThread
    suspend fun insertTaskItem(taskItem: TaskItem)
    {
        taskItemDao.insertTaskItem(taskItem)
        Log.d("asd", taskItem.date)
        println(DailyItems.date.toString())
    }

    @WorkerThread
    suspend fun updateTaskItem(taskItem: TaskItem)
    {
        taskItemDao.updateTaskItem(taskItem)
    }

    suspend fun deleteTaskItem(taskItem: TaskItem)
    {
        taskItemDao.deleteTaskItem(taskItem)
    }
}