package com.fattocs.back_end.desafio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fattocs.back_end.desafio.controllers.TaskController;
import com.fattocs.back_end.desafio.data.vo.v1.TaskVO;
import com.fattocs.back_end.desafio.domain.Task;
import com.fattocs.back_end.desafio.exceptions.RequiredObjectIsNullException;
import com.fattocs.back_end.desafio.exceptions.ResourceNotFoundException;
import com.fattocs.back_end.desafio.mapper.DozerMapper;
import com.fattocs.back_end.desafio.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class TaskService {

    private Logger logger = Logger.getLogger(TaskService.class.getName());

    @Autowired
    TaskRepository repository;

    public List<TaskVO> findAll() {

        logger.info("Finding all task");
        var tasks = DozerMapper.parseListObjects(repository.findAll(), TaskVO.class);
        tasks.stream().map(p -> p.add(linkTo(methodOn(TaskController.class).findById(p.getKey())).withSelfRel()));
        return tasks;
    }

    public TaskVO findById(Long id) {

        logger.info("Finding task with id = " + id);

        var task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhuma task encontrada para o ID: " + id));
        var taskVO = DozerMapper.parseObject(task, TaskVO.class);
        taskVO.add(linkTo(methodOn(TaskController.class).findById(id)).withSelfRel());
        return taskVO;
    }

    public TaskVO create(TaskVO taskVO) {

        if (taskVO == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one task");
        var task = DozerMapper.parseObject(taskVO, Task.class);
        Long maxOrder = repository.findMaxPresentationOrder();
        task.setPresentationOrder(maxOrder + 1);
        var newTaskVO = DozerMapper.parseObject(repository.save(task), TaskVO.class);
        return newTaskVO;
    }

    public TaskVO update(TaskVO newTaskVO) {

        if (newTaskVO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one task");
        var task = repository.findById(newTaskVO.getKey()).orElseThrow(() ->
                new ResourceNotFoundException("Nenhuma task encontrada para o ID: " + newTaskVO.getKey()));

        task.setName(newTaskVO.getName());
        task.setCost(newTaskVO.getCost());
        task.setLimitDate(newTaskVO.getLimitDate());
        task.setPresentationOrder(newTaskVO.getPresentationOrder());
        var taskVO = DozerMapper.parseObject(repository.save(task),TaskVO.class);
        taskVO.add(linkTo(methodOn(TaskController.class).findById(taskVO.getKey())).withSelfRel());
        return taskVO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting task with id = " + id);

        // Verifica se a tarefa existe
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhuma task encontrada para o ID: " + id));

        // Salva a ordem de apresentação da tarefa a ser excluida
        Long deletedOrder = task.getPresentationOrder();

        // Exclui
        repository.delete(task);

        // Atualiza a ordem de apresentação dos registros restantes
        repository.updatePresentationOrderAfterDeletion(deletedOrder);
    }


}
