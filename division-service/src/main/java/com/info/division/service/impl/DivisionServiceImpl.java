package com.info.division.service.impl;

import org.springframework.stereotype.Service;

import com.info.division.model.Division;
import com.info.division.repository.DepartmentRepository;
import com.info.division.service.DivisionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DivisionServiceImpl implements DivisionService {

   private final DepartmentRepository departmentRepository;

   @Override
   public Division save(Division division) {
      return departmentRepository.save(division);
   }

   @Override
   public Division findById(Long departmentId) {
      return departmentRepository.findById(departmentId).orElse(null);
   }

}
