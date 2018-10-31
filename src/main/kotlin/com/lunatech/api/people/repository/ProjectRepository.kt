package com.lunatech.api.people.repository

import com.lunatech.api.people.model.Project
import org.springframework.data.repository.CrudRepository

interface ProjectRepository : CrudRepository<Project, Int>