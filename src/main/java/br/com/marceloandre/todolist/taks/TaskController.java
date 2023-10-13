package br.com.marceloandre.todolist.taks;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
}
