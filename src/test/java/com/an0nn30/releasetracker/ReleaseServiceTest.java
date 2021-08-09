package com.an0nn30.releasetracker;

import com.an0nn30.releasetracker.entites.Release;
import com.an0nn30.releasetracker.entites.utils.PagingHeaders;
import com.an0nn30.releasetracker.entites.utils.PagingResponse;
import com.an0nn30.releasetracker.exception.ReleaseNotFoundException;
import com.an0nn30.releasetracker.repos.ReleaseRepo;
import com.an0nn30.releasetracker.services.ReleaseService;
import com.an0nn30.releasetracker.utils.Builder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReleaseServiceTest {



    @Mock
    private ReleaseRepo releaseRepo;

    @InjectMocks
    private ReleaseService releaseService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test(expected = ReleaseNotFoundException.class)
    public void get_should_throw_EntityNotFoundException() throws ReleaseNotFoundException {
        // Given

        // When
        releaseService.get(1);

        // Then
        fail();
    }

    @Test
    public void get_should_return_the_entity() {
        // Given
        when(releaseRepo.findById(1)).thenReturn(Optional.of(Builder.release(
                1,
                "release_1",
                "This is release number 1",
                "Created",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2021, 6, 1),
                LocalDate.of(2021, 5, 1))));

        // When
        Release foundRelease = releaseService.get(1);

        // Then
        assertThat(1, equalTo(foundRelease.getId()));
        assertThat("release_1", equalTo(foundRelease.getName()));
        assertThat("This is release number 1", equalTo(foundRelease.getDescription()));
        assertThat(LocalDate.of(2022, 1, 1), equalTo(foundRelease.getReleaseDate()));
        assertThat(LocalDate.of(2021, 6, 1), equalTo(foundRelease.getCreatedAtDate()));
        assertThat(LocalDate.of(2021, 5, 1), equalTo(foundRelease.getLastUpdateAtDate()));
    }

    @Test
    public void get_by_specification_should_return_entities_list() {
        Specification querySpec = mock(Specification.class);
        Sort sort = mock(Sort.class);

        when(releaseRepo.findAll(querySpec, sort))
                .thenReturn(
                        Arrays.asList(Builder.release(1,
                                        "test_release_1",
                                        "A Test Release 1",
                                        "Created",
                                        LocalDate.of(2021, 1, 1),
                                        LocalDate.of(2006, 8, 29),
                                        LocalDate.of(2021, 1, 1)),
                                Builder.release(2,
                                        "test_release_2",
                                        "A Test Release 2",
                                        "Created",
                                        LocalDate.of(2023, 1, 1),
                                        LocalDate.of(2005, 8, 29),
                                        LocalDate.of(2021, 1, 1))));

        List<Release> foundRelease = releaseService.get(querySpec, sort);

        verify(releaseRepo, times(1)).findAll(querySpec, sort);
        assertThat(2, equalTo(foundRelease.size()));
        assertThat(1, equalTo(foundRelease.get(0).getId()));
        assertThat("test_release_1", equalTo(foundRelease.get(0).getName()));
        assertThat("A Test Release 1", equalTo(foundRelease.get(0).getDescription()));
        assertThat("Created", equalTo(foundRelease.get(0).getStatus()));
        assertThat(LocalDate.of(2021, 1, 1), equalTo(foundRelease.get(0).getReleaseDate()));
        assertThat(LocalDate.of(2006, 8, 29), equalTo(foundRelease.get(0).getCreatedAtDate()));
        assertThat(LocalDate.of(2021, 1, 1), equalTo(foundRelease.get(0).getLastUpdateAtDate()));
        assertThat(2, equalTo(foundRelease.get(1).getId()));
        assertThat("test_release_2", equalTo(foundRelease.get(1).getName()));
        assertThat("A Test Release 2", equalTo(foundRelease.get(1).getDescription()));
        assertThat("Created", equalTo(foundRelease.get(1).getStatus()));
        assertThat(LocalDate.of(2023, 1, 1), equalTo(foundRelease.get(1).getReleaseDate()));
        assertThat(LocalDate.of(2005, 8, 29), equalTo(foundRelease.get(1).getCreatedAtDate()));
        assertThat(LocalDate.of(2021, 1, 1), equalTo(foundRelease.get(1).getLastUpdateAtDate()));

    }

    @Test
    public void get_by_specification_and_pagination_should_return_paged_entities_list() {
        // Given
        Specification querySpec = mock(Specification.class);
        Sort sort = mock(Sort.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(0));
        headers.add(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(2));

        PageRequest pageRequest = PageRequest.of(0, 2, sort);

        Page<Release> releases = Mockito.mock(Page.class);
        when(releaseRepo.findAll((Specification<Release>) any(), (Pageable) any())).thenReturn(releases);

        // When
        releaseService.get(querySpec, headers, 2, 0, sort);

        verify(releaseRepo, times(1)).findAll(querySpec, pageRequest);
    }

    @Test
    public void get_by_specification_and_sort_should_return_entities_list() {
        Specification querySpec = mock(Specification.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(PagingHeaders.COUNT.getName(), String.valueOf(0));
        Sort sort = mock(Sort.class);
        List<Release> releaseList = Arrays.asList(
                Builder.release(1,
                        "test_release_1",
                        "A Test Release 1",
                        "Created",
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2005, 8, 29),
                        LocalDate.of(2021, 1, 1)),
                Builder.release(2,
                        "test_release_2",
                        "A Test Release 2",
                        "Created",
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2005, 8, 29),
                        LocalDate.of(2021, 1, 1)),
                Builder.release(3,
                        "test_release_3",
                        "A Test Release 3",
                        "Created",
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2005, 8, 29),
                        LocalDate.of(2021, 1, 1))
        );

        when(releaseRepo.findAll((Specification<Release>) any(), (Sort) any())).thenReturn(releaseList);

        PagingResponse sortedList = releaseService.get(querySpec, headers, null, null, sort);

        verify(releaseRepo, times(1)).findAll(querySpec, sort);
        assertThat(0L, equalTo(sortedList.getPageNumber()));
        assertThat(0L, equalTo(sortedList.getPageSize()));
        assertThat(0L, equalTo(sortedList.getPageTotal()));
        assertThat(3, equalTo(sortedList.getElements().size()));
    }

    @Test
    public void create_should_return_saved_entity() {
        // Given
        Release release = Builder.release(1,
                "test_release_1",
                "A Test Release 1",
                "Created",
                LocalDate.of(2023, 1, 1),
                null,
                null);

        when(releaseRepo.save(any())).thenReturn(release);
        ArgumentCaptor<Release> argument = ArgumentCaptor.forClass(Release.class);

        // When
        final Release createdRelease = releaseService.create(release);

        verify(releaseRepo, times(1)).save(argument.capture());
        assertThat(1, equalTo(createdRelease.getId()));
        assertThat("test_release_1", equalTo(createdRelease.getName()));
        assertThat("A Test Release 1", equalTo(createdRelease.getDescription()));
        assertThat(LocalDate.of(2023, 1, 1), equalTo(createdRelease.getReleaseDate()));
        assertThat(LocalDate.now(), equalTo(createdRelease.getCreatedAtDate()));
        assertThat(LocalDate.now(), equalTo(createdRelease.getLastUpdateAtDate()));

    }


    @Test
    public void update_should_return_updated_entity() {
        // Given
        Release release = Builder.release(1,
                "test_release_1",
                "A Test Release 1",
                "Created",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2005, 8, 29),
                LocalDate.of(2021, 1, 1));


        when(releaseRepo.save(any())).thenReturn(release);

        ArgumentCaptor<Release> argument = ArgumentCaptor.forClass(Release.class);

        Release updatedRelease = releaseService.update(1, release);

        verify(releaseRepo, times(1)).save(argument.capture());

        assertThat(1, equalTo(updatedRelease.getId()));
        assertThat("test_release_1", equalTo(updatedRelease.getName()));
        assertThat("A Test Release 1", equalTo(updatedRelease.getDescription()));
        assertThat(LocalDate.of(2023, 1, 1), equalTo(updatedRelease.getReleaseDate()));
        assertThat(LocalDate.of(2005, 8, 29), equalTo(updatedRelease.getCreatedAtDate()));
        assertThat(LocalDate.of(2021, 1, 1), equalTo(updatedRelease.getLastUpdateAtDate()));
    }

    @Test(expected = RuntimeException.class)
    public void save_should_throws_UpdateIdMismatchException() {
        // Given
        Release release = Builder.release(1,
                "test_release_1",
                "A Test Release 1",
                "Created",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2005, 8, 29),
                LocalDate.of(2021, 1, 1));
        // When
        releaseService.update(2, release);

        // Then
        fail();
    }

    @Test(expected = RuntimeException.class)
    public void save_should_throws_Exception() {
        // Given
        Release release = Builder.release(1,
                "test_release_1",
                "A Test Release 1",
                "Created",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2005, 8, 29),
                LocalDate.of(2021, 1, 1));
        // When
        releaseService.update(2, release);

        // Then
        fail();
    }

    @Test
    public void delete() {
        // Given
        when(releaseRepo.findById(1)).thenReturn(Optional.of(Builder.release(1,
                "test_release_1",
                "A Test Release 1",
                "Created",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2005, 8, 29),
                LocalDate.of(2021, 1, 1))));
        ArgumentCaptor<Release> argumentCaptor = ArgumentCaptor.forClass(Release.class);

        // When
        releaseService.delete(1);

        // Then
        verify(releaseRepo, times(1)).delete(argumentCaptor.capture());
        assertThat(1, equalTo(argumentCaptor.getValue().getId()));
    }
}
