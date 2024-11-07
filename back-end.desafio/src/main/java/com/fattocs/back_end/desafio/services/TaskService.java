package com.fattocs.back_end.desafio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fattocs.back_end.desafio.controllers.TaskController;
import com.fattocs.back_end.desafio.data.vo.v1.TaskVO;
import com.fattocs.back_end.desafio.domain.Task;
import com.fattocs.back_end.desafio.exceptions.RequiredObjectIsNullException;
import com.fattocs.back_end.desafio.exceptions.ResourceNotFoundException;
import com.fattocs.back_end.desafio.exceptions.UniqueConstraintViolationException;
import com.fattocs.back_end.desafio.mapper.DozerMapper;
import com.fattocs.back_end.desafio.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TaskService {

    private Logger logger = Logger.getLogger(TaskService.class.getName());

    @Autowired
    TaskRepository repository;

    public List<TaskVO> findAll() {

        logger.info("Finding all tasks");

        var tasks = DozerMapper.parseListObjects(repository.findAll(), TaskVO.class);

        // Ordena a lista pelo campo presentationOrder
        tasks.sort(Comparator.comparing(TaskVO::getPresentationOrder));

        tasks.forEach(p -> p.add(linkTo(methodOn(TaskController.class).findById(p.getKey())).withSelfRel()));

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
        try {
            var task = DozerMapper.parseObject(taskVO, Task.class);
            Long maxOrder = repository.findMaxPresentationOrder();
            task.setId(null);
            task.setPresentationOrder(maxOrder + 1);
            var newTaskVO = DozerMapper.parseObject(repository.save(task), TaskVO.class);
            newTaskVO.add(linkTo(methodOn(TaskController.class).findById(newTaskVO.getKey())).withSelfRel());
            return newTaskVO;

        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException(extractFieldAndValueFromMessage(e.getMessage()));
        }
    }

    public TaskVO update(TaskVO newTaskVO) {

        if (newTaskVO == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one task");

        try {

            var task = repository.findById(newTaskVO.getKey()).orElseThrow(() -> new ResourceNotFoundException("Nenhuma task encontrada para o ID: " + newTaskVO.getKey()));

            task.setName(newTaskVO.getName());
            task.setCost(newTaskVO.getCost());
            task.setLimitDate(newTaskVO.getLimitDate());
            //Presentation order só é trocada no metodo especifico.
            var taskVO = DozerMapper.parseObject(repository.save(task), TaskVO.class);
            taskVO.add(linkTo(methodOn(TaskController.class).findById(taskVO.getKey())).withSelfRel());
            return taskVO;
        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintViolationException(extractFieldAndValueFromMessage(e.getMessage()));
        }
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting task with id = " + id);

        // Verifica se a tarefa existe
        var task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhuma task encontrada para o ID: " + id));

        // Salva a ordem de apresentação da tarefa a ser excluida
        Long deletedOrder = task.getPresentationOrder();

        // Exclui
        repository.delete(task);

        // Atualiza a ordem de apresentação dos registros restantes
        repository.updatePresentationOrder(deletedOrder);
    }

    @Transactional
    public TaskVO updatePresentationOrder(Long id, Long newOrder) {

        logger.info("Updating the presentation order of task with id = " + id + " to order: " + newOrder);


        var task = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Nenhuma task encontrada para o ID: " + id));

        Long currentOrder = task.getPresentationOrder();

        // Verifica se a nova ordem é igual à ordem atual
        if (Objects.equals(currentOrder, newOrder)) {
            return DozerMapper.parseObject(task, TaskVO.class);
        }

        // Ajusta as ordens das outras tarefas
        if (currentOrder < newOrder) {
            repository.decrementOrderInRange(newOrder, currentOrder); // A ordem atual está antes da nova ordem
        } else {
            repository.incrementOrderInRange(newOrder, currentOrder); // A ordem atual está depois da nova ordem
        }

        // Atualiza a tarefa com a nova ordem desejada
        task.setPresentationOrder(newOrder);
        var updatedTask = repository.save(task);

        var taskVO = DozerMapper.parseObject(updatedTask, TaskVO.class);
        taskVO.add(linkTo(methodOn(TaskController.class).findById(id)).withSelfRel());
        return taskVO;

    }

    private String extractFieldAndValueFromMessage(String message) {
        // Expressão regex para extrair o campo e o valor
        Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\) already exists");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            String field = matcher.group(1);
            if (Objects.equals(field, "name")) {
                field = "Nome";
            }
            String value = matcher.group(2);
            return "O campo '" + field + "' com o valor '" + value + "' já existe.";
        }
        return "Esse recurso já existe.";
    }


}
