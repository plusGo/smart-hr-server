package com.plus.go.smart.hr.server.controller;

import com.plus.go.smart.hr.server.model.po.Function;
import com.plus.go.smart.hr.server.service.FunctionService;
import com.plus.jpa.model.QueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "functions", produces = "application/json;charset=utf-8")
public class FunctionController {
    private final FunctionService functionService;

    public FunctionController(final FunctionService functionService) {
        this.functionService = functionService;
    }

    @PostMapping("page")
    Page<Function> findPage(@RequestBody final QueryParameters queryParameters) {
        return functionService.findPage(queryParameters);
    }


    @PostMapping
    Function save(@RequestBody final Function function) {
        return functionService.save(function);
    }

    @PutMapping
    Function update(@RequestBody final Function function) {
        return functionService.update(function);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@RequestParam final List<String> ids) {
        functionService.delete(ids);
    }

}
