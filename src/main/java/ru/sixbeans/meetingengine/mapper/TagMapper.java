package ru.sixbeans.meetingengine.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.sixbeans.meetingengine.entity.Tag;
import ru.sixbeans.meetingengine.model.TagData;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    List<TagData> map(Collection<Tag> tagData);
}
