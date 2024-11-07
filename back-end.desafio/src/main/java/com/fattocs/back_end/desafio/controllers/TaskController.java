package com.fattocs.back_end.desafio.controllers;

import com.fattocs.back_end.desafio.data.vo.v1.TaskVO;
import com.fattocs.back_end.desafio.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task/v1")
@Tag(name = "Task", description = "Endpoints for Managing Task")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping
    @Operation(summary = "Finds all Task", description = "Finds all task", tags = {"Task"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskVO.class))
                            )
                    }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    public List<TaskVO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Finds a Task", description = "Finds a Task",
            tags = {"Task"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskVO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public TaskVO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PostMapping()
    @Operation(summary = "Adds a new Task",
            description = "Adds a new Task",
            tags = {"Task"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = TaskVO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public TaskVO create(@RequestBody TaskVO task) {
        return service.create(task);
    }

    @PutMapping()
    @Operation(summary = "Updates a Task",
            description = "Updates a Task",
            tags = {"Task"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(mediaType = "application/json",schema = @Schema(implementation = TaskVO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public TaskVO update(@RequestBody TaskVO task) {
        return service.update(task);
    }


    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a Task",
            description = "Deletes a Task",
            tags = {"Task"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}/{order}")
    @Operation(summary = "Change presentation order a specific Task by your ID and new order",
            description = "Change presentation order a specific Task by your ID and new order",
            tags = {"Task"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = TaskVO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public TaskVO updatePresentationOrder(@PathVariable(value = "id") Long id, @PathVariable(value = "order") Long order) {
        return service.updatePresentationOrder(id,order);
    }
}
