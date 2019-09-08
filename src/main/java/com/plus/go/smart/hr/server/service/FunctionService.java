package com.plus.go.smart.hr.server.service;

import com.plus.go.smart.hr.server.exception.ValidateException;
import com.plus.go.smart.hr.server.model.po.Function;
import com.plus.go.smart.hr.server.repository.FunctionRepository;
import com.plus.jpa.model.QueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunctionService {
    private FunctionRepository functionRepository;

    public FunctionService(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public Function save(final Function functionModel) {
        return functionRepository.save(functionModel);
    }

    public Function update(final Function function) {
        final Function dbFunction = functionRepository.findById(function.getId())
                .orElseThrow(() -> new ValidateException("功能不存在"));
        dbFunction.setName(function.getName());

        return functionRepository.save(dbFunction);
    }

    public void delete(final List<String> ids) {
        ids.forEach(functionRepository::deleteById);
    }

    public Page<Function> findPage(QueryParameters queryParameters) {
        return functionRepository.findPage(queryParameters);
    }
}
