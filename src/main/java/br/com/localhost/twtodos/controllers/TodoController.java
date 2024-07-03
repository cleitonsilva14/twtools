package br.com.localhost.twtodos.controllers;

import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import br.com.localhost.twtodos.models.Todo;
import br.com.localhost.twtodos.repositories.TodoRepository;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class TodoController {
    

    private final TodoRepository todoRepository;
    

    public TodoController(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    // ou assim @RequestMapping(path = "/", method = RequestMethod.GET) 
    // return "home";  nome do template que ele vai procurar. Busca em src/main/resources/templates/
    @GetMapping("/") 
    public ModelAndView list(){
        

        return new ModelAndView(
            "todo/list",
                Map.of("todos", todoRepository.findAll(Sort.by("deadline")))
            );
    }

    // aula 06 - 

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("todo/form", Map.of("todo", new Todo()));
    }
    
    // BindingResult traz o resultado da validacao
    @PostMapping("/create")
    public String create(@Valid Todo todo, BindingResult result) { 
        
        if(result.hasErrors()){
            return "todo/form";
        }
        todoRepository.save(todo);

        // quando salvar uma todo vai redirecionar para /
        return "redirect:/";
    }
    

    // aula 07 -
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var todo = todoRepository.findById(id); //
        if(todo.isEmpty()){
            // não foi encontrado a todo com id informado
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        // se foi encontrada a Todo com o id informado
        return new ModelAndView("todo/form", Map.of("todo", todo.get()));
    }
    

    @PostMapping("/edit/{id}") // Valid aula 9
    public String edit(@Valid Todo todo, BindingResult result) {


        if(result.hasErrors()){
            return "todo/form";
        }

        // se a Todo já existir faz edicao. Se nao existir faz o cadastro      
        todoRepository.save(todo);

        return "redirect:/";
    }
    


    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        var todo = todoRepository.findById(id);
        if(todo.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ModelAndView("todo/delete", Map.of("todo", todo.get()));
    
    }
    

    // aula 8
    @PostMapping("/delete/{id}")
    public String delete(Todo todo) {
        todoRepository.delete(todo);
        return "redirect:/";
    }


    // aula 10 - rotas que alteram o estado do objeto usar POST
    @PostMapping("/finish/{id}")
    public String finish(@PathVariable Long id) {
        
        var optionalTodo = todoRepository.findById(id); // optinal pois nao sabemos se sera encontrada no banco

        if(optionalTodo.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var todo = optionalTodo.get();

        todo.markHasFinished();
        todoRepository.save(todo);


        return "redirect:/";

    }
    
    



}
