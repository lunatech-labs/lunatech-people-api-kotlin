package com.lunatech.api.people.controllers

import com.lunatech.api.people.model.Assignment
import com.lunatech.api.people.repository.AssignmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AssignmentController {

    @Autowired
    lateinit var assignmentRepository: AssignmentRepository

    @GetMapping("/api/assignments")
    fun getAssignments() = assignmentRepository.findAll()

}