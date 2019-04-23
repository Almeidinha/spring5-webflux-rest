package almeida.springframework.spring5webfluxrest.bootatrap;

import almeida.springframework.spring5webfluxrest.domain.Category;
import almeida.springframework.spring5webfluxrest.domain.Vendor;
import almeida.springframework.spring5webfluxrest.repository.CategoryRepository;
import almeida.springframework.spring5webfluxrest.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        categoryRepository.count().subscribe(aLong -> {
            if (aLong == 0) {
                // load data
                System.out.println("###  Loading Data on Bootstrap  ###");

                loadCategories();
                loadVendors();


            }
        });
    }

    private void loadVendors() {
        Vendor buck   = Vendor.builder().firstName("Joe").lastName("Buck").build();
        Vendor weston = Vendor.builder().firstName("Michael").lastName("Weston").build();
        Vendor waters = Vendor.builder().firstName("Jessie").lastName("Waters").build();
        Vendor nershi = Vendor.builder().firstName("Bill").lastName("Nershi").build();
        Vendor buffet = Vendor.builder().firstName("Jimmy").lastName("Buffet").build();

        vendorRepository.saveAll(Flux.just(buck, weston, waters, nershi, buffet)).subscribe();

        vendorRepository.count().subscribe(count -> {
            System.out.println("Loaded Vendors: " + count);
        });
    }

    private void loadCategories() {


        Category fruits = Category.builder().description("Fruits").build();
        Category nuts   = Category.builder().description("Nuts").build();
        Category breads = Category.builder().description("Breads").build();
        Category meats  = Category.builder().description("Meats").build();
        Category eggs   = Category.builder().description("Eggs").build();

        categoryRepository.saveAll(Flux.just(fruits, nuts, breads, meats, eggs)).subscribe();

        categoryRepository.count().subscribe(count -> {
            System.out.println("Loaded Categories: " + count);
        });

    }
}
