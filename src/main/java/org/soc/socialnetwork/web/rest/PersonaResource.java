package org.soc.socialnetwork.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.soc.socialnetwork.domain.Persona;
import org.soc.socialnetwork.repository.PersonaRepository;
import org.soc.socialnetwork.web.rest.util.HeaderUtil;
import org.soc.socialnetwork.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Persona.
 */
@RestController
@RequestMapping("/api")
public class PersonaResource {

    private final Logger log = LoggerFactory.getLogger(PersonaResource.class);
        
    @Inject
    private PersonaRepository personaRepository;
    
    /**
     * POST  /personas -> Create a new persona.
     */
    @RequestMapping(value = "/personas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Persona> createPersona(@Valid @RequestBody Persona persona) throws URISyntaxException {
        log.debug("REST request to save Persona : {}", persona);
        if (persona.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("persona", "idexists", "A new persona cannot already have an ID")).body(null);
        }
        Persona result = personaRepository.save(persona);
        return ResponseEntity.created(new URI("/api/personas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("persona", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personas -> Updates an existing persona.
     */
    @RequestMapping(value = "/personas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Persona> updatePersona(@Valid @RequestBody Persona persona) throws URISyntaxException {
        log.debug("REST request to update Persona : {}", persona);
        if (persona.getId() == null) {
            return createPersona(persona);
        }
        Persona result = personaRepository.save(persona);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("persona", persona.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personas -> get all the personas.
     */
    @RequestMapping(value = "/personas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Persona>> getAllPersonas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Personas");
        Page<Persona> page = personaRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/personas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /personas/:id -> get the "id" persona.
     */
    @RequestMapping(value = "/personas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Persona> getPersona(@PathVariable Long id) {
        log.debug("REST request to get Persona : {}", id);
        Persona persona = personaRepository.findOne(id);
        return Optional.ofNullable(persona)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personas/:id -> delete the "id" persona.
     */
    @RequestMapping(value = "/personas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersona(@PathVariable Long id) {
        log.debug("REST request to delete Persona : {}", id);
        personaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("persona", id.toString())).build();
    }
}
