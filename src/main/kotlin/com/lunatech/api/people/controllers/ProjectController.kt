package com.lunatech.api.people.controllers

import com.lunatech.api.people.model.Project
import com.lunatech.api.people.repository.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ProjectController{

    @Autowired
    lateinit var projectRepository: ProjectRepository

    @GetMapping("/api/projects")
    fun getProjects() = projectRepository.findAll()

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/projects")
    fun saveProject(@RequestBody project: Project): Project {
        return projectRepository.save(project)
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/projects")
    fun updateProject(@RequestBody project: Project): Project {
        return projectRepository.save(project)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/api/projects")
    fun deleteProject(@RequestBody project: Project): Unit {
        projectRepository.deleteById(project.id)
    }
}