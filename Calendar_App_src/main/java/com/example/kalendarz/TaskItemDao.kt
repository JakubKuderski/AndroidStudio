package com.example.kalendarz

import androidx.room.*
import com.example.kalendarz.DailyItems.*
import kotlinx.coroutines.flow.Flow
@Dao
interface TaskItemDao
{
    @Query("SELECT * FROM task_item_table ORDER BY id ASC")
    fun allTaskItems(): Flow<List<TaskItem>>

    @Query("SELECT * FROM task_item_table WHERE date = :data ORDER BY id ASC ")
    fun loadTaskItemsFromDay(data: String): Flow<List<TaskItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskItem(taskItem: TaskItem)

    @Update
    suspend fun updateTaskItem(taskItem: TaskItem)

    @Delete
    suspend fun deleteTaskItem(taskItem: TaskItem)

}