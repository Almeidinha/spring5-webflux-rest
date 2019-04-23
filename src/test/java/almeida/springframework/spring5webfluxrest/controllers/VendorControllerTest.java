package almeida.springframework.spring5webfluxrest.controllers;

import almeida.springframework.spring5webfluxrest.domain.Vendor;
import almeida.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUo() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getVendors() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("fName1").lastName("lName1").build(),
                        Vendor.builder().firstName("fName2").lastName("lName2").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        BDDMockito.given(vendorRepository.findById("some_id"))
                .willReturn(Mono.just(Vendor.builder().firstName("fName").lastName("lName").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/some_id")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    public void create() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("firstName").lastName("lastName").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void update() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("firstName").lastName("lastName").build());

        webTestClient.put()
                .uri("/api/v1/vendors/uuiduuiduuid")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void patchWithNoChange() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> categoryMono = Mono.just(Vendor.builder().build());

        webTestClient.patch()
                .uri("/api/v1/vendors/{id}", UUID.randomUUID())
                .body(categoryMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        BDDMockito.verify(vendorRepository, Mockito.never()).save(any());
    }

    @Test
    public void patchWithChange() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> categoryMono = Mono.just(Vendor.builder()
                .firstName("New First Name")
                .lastName("New Last Name").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/{id}", UUID.randomUUID())
                .body(categoryMono, Vendor.class)
                .exchange()
                .expectStatus().isOk();

        BDDMockito.verify(vendorRepository,
                Mockito.times(1)).save(any());
    }

    /*@Test
    public void delete() throws Exception {

        webTestClient.delete()
                .uri("/api/v1/vendors/{id}", UUID.randomUUID())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody(String.class)
                .isEqualTo("Delete Succesfully!");


    }*/

}