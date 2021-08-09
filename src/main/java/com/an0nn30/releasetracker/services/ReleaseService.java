package com.an0nn30.releasetracker.services;

import com.an0nn30.releasetracker.entites.utils.PagingHeaders;
import org.springframework.http.HttpHeaders;
import com.an0nn30.releasetracker.entites.Release;
import com.an0nn30.releasetracker.entites.utils.PagingResponse;
import com.an0nn30.releasetracker.exception.InvalidStatusException;
import com.an0nn30.releasetracker.exception.MalformedBodyException;
import com.an0nn30.releasetracker.exception.MissingIDException;
import com.an0nn30.releasetracker.exception.ReleaseNotFoundException;
import com.an0nn30.releasetracker.repos.ReleaseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/***
 * ReleaseService.java
 *
 * Release service class to handle all business logic.
 *
 */

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReleaseService {
    private final ReleaseRepo releaseTrackerRepo;

    // Quick and dirty way to store states. Could have in DB and validate with query. Didn't do that.
    //TODO: Create table in DB that contains states
    private final Set<String> STATES = Set.of(
            "Created",
            "In Development",
            "On DEV",
            "QA Done on DEV",
            "On staging",
            "QA done on STAGING",
            "On PROD",
            "Done");

    public Release get(Integer id) {
        return releaseTrackerRepo.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);
    }

    /**
     * get element using Criteria.
     *
     * @param spec    *
     * @param limit page limit (optional)
     * @param offset page offset (optional)
     * @param sort    sort criteria
     * @return retrieve elements with pagination
     */
    public PagingResponse get(Specification<Release> spec, HttpHeaders headers, Integer limit, Integer offset, Sort sort) {
        if (limit != null) {
            return get(spec, PageRequest.of(offset, limit, sort));
        } else if (isRequestPaged(headers)) {
            return get(spec, buildPageRequest(headers, sort));
        } else {
            List<Release> entities = get(spec, sort);
            return new PagingResponse((long) entities.size(), 0L, 0L, 0L, 0L, entities);
        }
    }

    /**
     * get elements using Criteria.
     *
     * @param spec     *
     * @param pageable pagination data
     * @return retrieve elements with pagination
     */
    public PagingResponse get(Specification<Release> spec, Pageable pageable) {
        Page<Release> page = releaseTrackerRepo.findAll(spec, pageable);
        List<Release> content = page.getContent();
        return new PagingResponse(page.getTotalElements(), (long) page.getNumber(), (long) page.getNumberOfElements(), pageable.getOffset(), (long) page.getTotalPages(), content);
    }

    /**
     * get elements using Criteria.
     *
     * @param spec *
     * @return elements
     */
    public List<Release> get(Specification<Release> spec, Sort sort) {
        return releaseTrackerRepo.findAll(spec, sort);
    }

    /**
     * delete element
     *
     * @param id element ID
     * @throws EntityNotFoundException Exception when retrieve entity
     */
    public void delete(Integer id) {
        Release entity = releaseTrackerRepo.findById(id)
                .orElseThrow(ReleaseNotFoundException::new);
        releaseTrackerRepo.delete(entity);
    }

    /**
     * create element.
     *
     * @param release element to create
     * @return element after creation
     * //     * @throws CreateWithIdException   Exception lancée lors de la création d'un objet existant
     * @throws EntityNotFoundException Exception lors de récupération de l'entité en base
     */
    public Release create(Release release){
        if (!STATES.contains(release.getStatus())) {
            throw new InvalidStatusException();
        }
        release.setLastUpdateAtDate(LocalDate.now());
        release.setCreatedAtDate(LocalDate.now());
        return save(release);
    }

    /**
     * update element
     *
     * @param id   element identifier
     * @param release element to update
     * @return element after update
     * @throws EntityNotFoundException Exception when retrieve entity
     */
    public Release update(Integer id, Release release) {
        try {
            if (release.getId() == null) {
                throw new MissingIDException();
            } else if (!id.equals(release.getId())) {
                throw new RuntimeException(String.format("Can not update entity, the resource ID (%d) not match the objet ID (%d).", id, release.getId()));
            }
            return save(release);
        } catch (org.hibernate.PropertyValueException e) {
            throw new MalformedBodyException();
        }
    }



    /**
     * create/update elements
     *
     * @param release element to save
     * @return element after save
     */
    protected Release save(Release release) {
        return releaseTrackerRepo.save(release);
    }

    private boolean isRequestPaged(HttpHeaders headers) {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) && headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
    }

    private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
        int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
        int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
        return PageRequest.of(page, size, sort);
    }
}
