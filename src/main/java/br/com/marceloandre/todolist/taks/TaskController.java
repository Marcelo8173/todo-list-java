package br.com.marceloandre.todolist.taks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taksRepository;

    @PostMapping
    public TaskModel created(@RequestBody TaskModel taksModel) {
       var taskCreted = this.taksRepository.save(taksModel);

        return taskCreted;
    }
}
