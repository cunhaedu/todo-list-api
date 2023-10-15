package br.com.cunhaedu.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.cunhaedu.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  public static final String USER_ID = "userId";

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("")
  public ResponseEntity<TaskEntity> create(
    @RequestBody() TaskEntity taskData,
    HttpServletRequest request
  ) {
    var userId = request.getAttribute(USER_ID);
    taskData.setUserId((UUID) userId);

    var currentDate = LocalDateTime.now();

    if(currentDate.isAfter(taskData.getStartAt())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Start date should be after current date"
      );
    }

    if(taskData.getEndAt().isBefore(taskData.getStartAt())) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "End date should be after start date"
      );
    }

    var savedTask = this.taskRepository.save(taskData);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
  }

  @GetMapping("")
  public ResponseEntity<List<TaskEntity>> getTasks(HttpServletRequest request) {
    var userId = request.getAttribute(USER_ID);

    var tasks = this.taskRepository.findByUserId((UUID) userId);
    return ResponseEntity.status(HttpStatus.OK).body(tasks);
  }

  @PutMapping("/{taskId}")
  public ResponseEntity<TaskEntity> update(
    @RequestBody() TaskEntity taskData,
    @PathVariable() UUID taskId,
    HttpServletRequest request
  ) {
    var userId = request.getAttribute(USER_ID);
    taskData.setUserId((UUID) userId);
    taskData.setId(taskId);

    var task = this.taskRepository.findById(taskId);

    if(task.isEmpty()) {
      throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "Task not found"
      );
    }

    Utils.copyNonNullProperties(taskData, task);

    var updatedTask = this.taskRepository.save(taskData);
    return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
  }
}
