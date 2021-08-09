package com.an0nn30.releasetracker.controllers;


import com.an0nn30.releasetracker.entites.Release;
import com.an0nn30.releasetracker.entites.utils.PagingHeaders;
import com.an0nn30.releasetracker.entites.utils.PagingResponse;
import com.an0nn30.releasetracker.services.ReleaseService;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * ReleaseService.java
 *
 * Main release tracker controller. Nothing much to be said here.
 *
 * See README.md for api specs
 *
 */
@RestController
@RequestMapping("api/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseService releaseService;

    @Transactional
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Release create(@RequestBody Release release) {
        return releaseService.create(release);
    }

    @Transactional
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Release update(@PathVariable(name = "id") Integer id, @RequestBody Release release) { return releaseService.update(id, release); }

    @Transactional
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Integer id) { releaseService.delete(id); }

    @Transactional
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Release get(@PathVariable(name = "id") Integer id) { return releaseService.get(id); }

    //
    // My favorite function.
    // Creates a Specification at runtime for each query to filter the resp.
    // Work smarter not harder ;)
    // Instead of created specs manually, this api is using a lib that will
    // generate the Specs at runtime for us, handling any specified filter we
    // want to handle.
    //
    @Transactional
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Release>> get(
            @And({
                    @Spec(path = "createdAtDate", params = "createdAtDateAfter", spec = GreaterThan.class),
                    @Spec(path = "createdAtDate", params = "createdAtDateBefore", spec = LessThan.class),
                    @Spec(path = "createdAtDate", params = {"createdAtDateGt", "createdAtDateLt"}, spec = Between.class),
                    @Spec(path = "createdAtDate", params = "createdAtDate", spec = Equal.class),

                    @Spec(path = "releaseDate", params = {"releaseDateBefore"}, spec = LessThan.class),
                    @Spec(path = "releaseDate", params = {"releaseDateAfter"}, spec = GreaterThan.class),
                    @Spec(path = "releaseDate", params = {"releaseDateGt", "releaseDateLt"}, spec = Between.class),
                    @Spec(path = "releaseDate", params = {"releaseDate"}, spec = Equal.class),

                    @Spec(path = "lastUpdateAtDate", params = {"lastUpdateAtDateBefore"}, spec = LessThan.class),
                    @Spec(path = "lastUpdateAtDate", params = {"lastUpdateAtDateAfter"}, spec = GreaterThan.class),
                    @Spec(path = "lastUpdateAtDate", params = {"lastUpdateAtDateGt", "lastUpdateAtDateLt"}, spec = Between.class),
                    @Spec(path = "lastUpdateAtDate", params = {"lastUpdateAtDate"}, spec = Equal.class),

                    @Spec(path = "status", params = "status", spec = Equal.class),

                    @Spec(path = "name", params = "name", spec = Like.class),
                    @Spec(path = "description", params = "description", spec = Like.class)

            }) Specification<Release> spec,
            Sort sort,
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        final PagingResponse response = releaseService.get(spec, headers, limit, offset, sort);
        return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);

    }

    private HttpHeaders returnHttpHeaders(PagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }
}
