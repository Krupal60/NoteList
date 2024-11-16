package com.note.list.data.repository.todo

import com.note.list.data.local.todo.ToDoDao
import com.note.list.domain.todo.ToDo
import com.note.list.domain.todo.TodoRepository
import com.note.list.domain.todo.toToDo
import com.note.list.domain.todo.toToDoList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ToDoListRepositoryImpl @Inject constructor(private val dao: ToDoDao) : TodoRepository {

    override fun getToDoListByNotDone(): Flow<Result<List<ToDo>>> {
          return  dao.getToDoListByNotDone().map {
           try {
               Result.success(it.map { todo -> todo.toToDo() })
           }catch (e : Exception){
               Result.failure(e)
           }
        }
    }

    override fun getToDoListByDone(): Flow<Result<List<ToDo>>> {
        return  dao.getToDoListByDone().map {
            try {
                Result.success(it.map { todo -> todo.toToDo() })
            }catch (e : Exception){
                Result.failure(e)
            }
        }
    }

    override suspend fun getToDoListDetail(id: Int): ToDo {
        return dao.getToDoListDetail(id).toToDo()
    }

    override suspend fun upsert(toDo: ToDo) {
       dao.upsert(toDo.toToDoList())
    }

    override suspend fun delete(toDo: ToDo) {
       dao.delete(toDo.toToDoList())
    }
}