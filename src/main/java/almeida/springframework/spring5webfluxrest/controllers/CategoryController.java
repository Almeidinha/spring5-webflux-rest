package almeida.springframework.spring5webfluxrest.controllers;

import almeida.springframework.spring5webfluxrest.domain.Category;
import almeida.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/v1/categories")
    public Flux<Category> list() {
        return categoryRepository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    public Mono<Category> getById(@PathVariable String id) {
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/categories")
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/api/v1/categories/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/api/v1/categories/{id}")
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
       return categoryRepository.findById(id).map(cat -> {

           if (cat.getDescription() != category.getDescription()) {
               cat.setDescription(category.getDescription());
               categoryRepository.save(cat).subscribe(categ -> {
                   return;
               });
           }
           return cat;
       });
    }

    @DeleteMapping("/api/v1/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void>  delete(@PathVariable String id) {
        categoryRepository.findById(id).subscribe(category -> {
            categoryRepository.delete(category).subscribe();
        });

        return Mono.empty();
    }

}
