package com.note.list.di

import android.content.Context
import androidx.room.Room
import com.note.list.data.local.todo.ToDoDao
import com.note.list.data.local.todo.ToDoListDatabase
import com.note.list.data.repository.todo.ToDoListRepositoryImpl
import com.note.list.domain.todo.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToDoDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ToDoListDatabase::class.java,
        "to_do_list_database"
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: ToDoListDatabase) = database.toDoDao()

    @Singleton
    @Provides
    fun provideRepository(toDoDao: ToDoDao) : TodoRepository{
        return ToDoListRepositoryImpl(toDoDao)
    }
}