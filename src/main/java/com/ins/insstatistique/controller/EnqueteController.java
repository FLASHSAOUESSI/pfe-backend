package com.ins.insstatistique.controller;

import com.ins.insstatistique.dto.CreateEnqueteRecord;
import com.ins.insstatistique.dto.EnqueteResponseRecord;
import com.ins.insstatistique.service.EnqueteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController // Combines @Controller and @ResponseBody
@RequestMapping("/api/enquetes") // Base path for enquete-related endpoints
@RequiredArgsConstructor // Lombok for constructor injection
public class EnqueteController {

    private final EnqueteService enqueteService; // Inject the service

    @PostMapping // Handles HTTP POST requests to /api/v1/enquetes
    public ResponseEntity<EnqueteResponseRecord> createEnquete(@RequestBody CreateEnqueteRecord createEnqueteRecord) {
        try {
            EnqueteResponseRecord savedEnquete = enqueteService.saveEnquete(createEnqueteRecord);
            // Return the saved entity and HTTP status 201 (Created)
            return new ResponseEntity<>(savedEnquete, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            // Example of handling validation errors from the service
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); // Or return an error object
        } catch (Exception e) {
            // Generic error handling
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping // Handles HTTP GET requests to /api/v1/enquetes
    public ResponseEntity<List<EnqueteResponseRecord>> getAllEnquetes() {
        List<EnqueteResponseRecord> enquetes = enqueteService.getAllEnquetes();
        return new ResponseEntity<>(enquetes, HttpStatus.OK);
    }
    
    // --- Optional: Add endpoints for other operations ---
    /*
    @GetMapping("/{id}") // Handles HTTP GET requests to /api/v1/enquetes/{id}
    public ResponseEntity<Enquete> getEnqueteById(@PathVariable Long id) {
        return enqueteService.getEnqueteById(id)
                .map(enquete -> new ResponseEntity<>(enquete, HttpStatus.OK)) // Found: 200 OK
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Not found: 404 Not Found
    }

    

    @DeleteMapping("/{id}") // Handles HTTP DELETE requests to /api/v1/enquetes/{id}
    public ResponseEntity<Void> deleteEnquete(@PathVariable Long id) {
         try {
            enqueteService.deleteEnquete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success, no content to return: 204
         } catch (RuntimeException e) { // Catch specific exception if service throws one for not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         } catch (Exception e) {
             // Log the exception
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
    }
    */

}