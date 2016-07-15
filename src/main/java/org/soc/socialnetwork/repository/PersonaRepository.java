package org.soc.socialnetwork.repository;

import org.soc.socialnetwork.domain.Persona;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Persona entity.
 */
public interface PersonaRepository extends JpaRepository<Persona,Long> {

}
