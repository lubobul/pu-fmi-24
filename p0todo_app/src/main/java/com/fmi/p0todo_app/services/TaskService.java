package com.fmi.p0todo_app.services;

import com.fmi.p0todo_app.models.TaskModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private ArrayList<TaskModel> taskCollection = new ArrayList<>();
    private SequenceGenerator sequenceGenerator;

    public TaskService(SequenceGenerator sequenceGenerator){
        this.sequenceGenerator = sequenceGenerator;
    }

    public TaskModel getTask(int id){
        Optional<TaskModel> taskOptional
                = taskCollection
                .stream()
                .filter(task -> task.getId() == id)
                .findFirst();

        return taskOptional.orElse(null);
    }

    public ArrayList<TaskModel> getAllTasks(){
        return taskCollection;
    }

    public List<TaskModel> getTasks(String title){
        return taskCollection
                .stream()
                .filter(task -> task.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    public TaskModel addTask(TaskModel task){
        task.setId(this.sequenceGenerator.getNextId());
        this.taskCollection.add(task);

        return task;
    }

    public TaskModel updateTask(TaskModel task){
        for(int i =0; i < taskCollection.size(); i++){
            var selectedTask = this.taskCollection.get(i);

            if(selectedTask.getId() == task.getId()){
                this.taskCollection.set(i, task);
                return task;
            }
        }

        return null;
    }

    public TaskModel deleteTask(int taskId){
        for(int i =0; i < taskCollection.size(); i++){
            var selectedTask = this.taskCollection.get(i);

            if(selectedTask.getId() == taskId){
                this.taskCollection.remove(i);
                return selectedTask;
            }
        }

        return null;
    }
}
