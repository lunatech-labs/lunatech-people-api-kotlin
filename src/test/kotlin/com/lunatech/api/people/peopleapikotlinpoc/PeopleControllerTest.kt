package com.lunatech.api.people.peopleapikotlinpoc

import com.lunatech.api.people.controllers.ProjectController
import com.lunatech.api.people.model.Client
import com.lunatech.api.people.model.Project
import com.lunatech.api.people.repository.ProjectRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Matchers.any

class PeopleControllerTest{

    @Mock
    lateinit var projectRepository: ProjectRepository

    @InjectMocks
    lateinit var sut: ProjectController

    lateinit var data: MutableList<Project>
    var initialDataCount: Int = 0

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this);

        data = loadData()
        initialDataCount = data.size

        Mockito.`when`(projectRepository.findAll()).thenReturn(data)
        Mockito.`when`(projectRepository.save(any(Project::class.java))).thenAnswer({i -> save(i.arguments[0] as Project)})
        Mockito.`when`(projectRepository.deleteById(any())).thenAnswer({i -> delete(i.arguments[0] as Int)})
    }

    @Test
    fun testGetAllProjects(){
        val result = sut.getProjects()
        assertEquals(initialDataCount, result.count())
    }

    @Test
    fun testSaveProject(){
        val project = Project(id = 4, name = "Project 4", client = Client(4, "Client 4"))

        sut.saveProject(project)

        val result = sut.getProjects()
        assertEquals(initialDataCount + 1, result.count())
    }

    @Test
    fun testUpdateProject(){
        val oldProject = Project(id = 4, name = "Project 4", client = Client(4, "Client 4"))
        val newProject = Project(id = 4, name = "Project 41", client = Client(3, "Client 3"))

        sut.saveProject(oldProject)
        sut.updateProject(newProject)

        val result = sut.getProjects()
        assertEquals(initialDataCount + 1, result.count())
        assertTrue(result.contains(newProject))
    }

    @Test
    fun testDeleteProject(){
        val project = Project(id = 4, name = "Project 4", client = Client(4, "Client 4"))

        sut.saveProject(project)
        sut.deleteProject(project)

        val result = sut.getProjects()
        assertFalse(result.contains(project))
    }

    fun loadData(): MutableList<Project>{
        val project1 = Project(id = 1, name = "Project 1", client = Client(1, "Client 1"))
        val project2 = Project(id = 2, name = "Project 2", client = Client(2, "Client 2"))
        val project3 = Project(id = 3, name = "Project 3", client = Client(3, "Client 3"))

        return mutableListOf(project1, project2, project3)
    }

    fun save(project: Project): Project{
        val exist = data.filter{x -> x.id == project.id}.getOrNull(0)
        if(exist != null){
            val index = data.indexOf(exist)
            data.removeAt(index)
            data.add(index, project)
        }else{
            data.add(project)
        }

        return project
    }

    fun delete(projectId: Int){
        val exist = data.filter{x -> x.id == projectId}.getOrNull(0)
        if(exist != null) data.remove(exist)
    }


}