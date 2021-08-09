package com.an0nn30.releasetracker.utils;

import com.an0nn30.releasetracker.entites.Release;

import java.time.LocalDate;

public class Builder {

    public static Release release(Integer id,
                                  String name,
                                  String desc,
                                  String status,
                                  LocalDate releaseDate,
                                  LocalDate createdAtDate,
                                  LocalDate lastUpdateAtDate) {
        Release release = new Release();
        release.setId(id);
        release.setName(name);
        release.setDescription(desc);
        release.setStatus(status);
        release.setCreatedAtDate(createdAtDate);
        release.setReleaseDate(releaseDate);
        release.setLastUpdateAtDate(lastUpdateAtDate);
        return release;
    }

}
