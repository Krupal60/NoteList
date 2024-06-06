package com.note.list.di

import android.content.Context
import androidx.room.Room
import com.note.list.data.repository.note.NoteRepositoryImpl
import com.note.list.data.local.note.NoteDao
import com.note.list.data.local.note.NoteDatabase
import com.note.list.domain.note.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object NotesDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        "note_database"
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: NoteDatabase) = database.noteDao()


    @Singleton
    @Provides
    fun provideNoteRepository(dao: NoteDao) : NoteRepository {
        return NoteRepositoryImpl(dao)
    }

}