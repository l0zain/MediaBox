package org.mediabox.mediabox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mediabox.mediabox.dto.file.FileResponse;
import org.mediabox.mediabox.entity.File;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "path", source = "path")
    FileResponse toDto(File file);
}
