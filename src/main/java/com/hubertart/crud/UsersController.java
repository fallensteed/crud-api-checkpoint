package com.hubertart.crud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository repository;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UsersController(UsersRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<Users> all(){
        return this.repository.findAll();
    }

    @PostMapping("")
    public String create(@RequestBody Users user){
        return gson.toJson(this.repository.save(user));
    }

    @GetMapping("/{id}")
    public Optional<Users> findById(@PathVariable Long id){
        return this.repository.findById(id);
    }

    @PatchMapping("/{id}")
    public Users update(@RequestBody Users user){
        return this.repository.save(user);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id){
        this.repository.deleteById(id);
        String count = "{ \"count\": " + this.repository.count() + "}";
        return count;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody Users user){
        return "";
    }
}
