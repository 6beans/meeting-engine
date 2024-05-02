package ru.sixbeans.meetingengine.model;


import ru.sixbeans.meetingengine.entity.Tag;

import java.util.stream.Collectors;

public record TagData(
        Long id,
        String title,
        String category
) {
    public static TagData from(Tag tag) {
        return new TagData(
                tag.getId(),
                tag.getTitle(),
                tag.getCategory()
        );
    }

}