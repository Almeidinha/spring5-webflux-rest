package almeida.springframework.spring5webfluxrest.controllers;

import almeida.springframework.spring5webfluxrest.domain.Vendor;
import almeida.springframework.spring5webfluxrest.repository.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    Flux<Vendor> getVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        return vendorRepository.findById(id).map(vd -> {
            if ( ! vendor.equals(vd)) {
                if (vd.getFirstName() != vendor.getFirstName()) {
                    vd.setFirstName(vendor.getFirstName());
                }
                if (vd.getLastName() != vendor.getLastName()) {
                    vd.setLastName(vendor.getLastName());
                }
                vendorRepository.save(vd).subscribe(vdr -> {
                    return;
                });
            }
            return vd;
        });
    }

    @DeleteMapping("/api/v1/vendors/{id}")
    public Mono<ResponseEntity<String>>  delete(@PathVariable String id) {

        return vendorRepository.deleteById(id)
                .then(Mono.just(new ResponseEntity<>("Delete Successfully!", HttpStatus.ACCEPTED)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
