package com.redhat.uxl.services.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Profile progress info dto.
 */
@Data
public class ProfileProgressInfoDTO implements Serializable {

    private Integer paths;
    private Integer courses;
    private Integer activities;
    private Integer ceCredits;

    private List<AchievementDTO> achievements = new ArrayList<>();

}
