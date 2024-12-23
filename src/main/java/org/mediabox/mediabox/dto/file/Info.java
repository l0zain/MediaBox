package org.mediabox.mediabox.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Info {
    private String username;
    private List<FileResponse> files;
}
