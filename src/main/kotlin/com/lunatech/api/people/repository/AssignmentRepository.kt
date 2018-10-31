package com.lunatech.api.people.repository

import com.lunatech.api.people.model.Assignment
import org.springframework.data.repository.CrudRepository

interface AssignmentRepository: CrudRepository<Assignment, Int>