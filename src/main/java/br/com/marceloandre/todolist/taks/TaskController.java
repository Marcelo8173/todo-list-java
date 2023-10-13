package br.com.marceloandre.todolist.taks;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taksRepository;

    @PostMapping
    public ResponseEntity created(@RequestBody TaskModel taksModel, HttpServletRequest request) {
        System.out.println(request.getAttribute("idUser"));
        taksModel.setIdUser((UUID) request.getAttribute("idUser"));

        LocalDateTime currentTime = LocalDateTime.now();

        if(currentTime.isAfter(taksModel.getStartAt()) || currentTime.isAfter(taksModel.getEndAt())){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio/final deve ser maior do que a data atual");
        }

        if(taksModel.getStartAt().isAfter(taksModel.getEndAt())){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que data de termino");
        }
       var taks = this.taksRepository.save(taksModel);

        return  ResponseEntity.status(HttpStatus.OK).body(taks);
    }

    @GetMapping
    public List<TaskModel> list(HttpServletRequest request) {
        return this.taksRepository.findByIdUser((UUID) request.getAttribute("idUser"));
    }

    @PutMapping("/{id}")
    public TaskModel update(@RequestBody TaskModel taskModel, @PathVariable UUID id,  HttpServletRequest request) {
        var userID = request.getAttribute("userId");
        taskModel.setIdUser((UUID) userID);
        taskModel.setId((UUID) id);

        return this.taksRepository.save(taskModel);
    }

}
