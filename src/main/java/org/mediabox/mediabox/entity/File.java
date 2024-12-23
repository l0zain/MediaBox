package org.mediabox.mediabox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="files")
@NoArgsConstructor
@AllArgsConstructor
@Access(AccessType.FIELD)
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "file_name", nullable = false)
    private String name;
    @Column(name = "type_file", length = 4, nullable = false)
    private String type;
    @Column(name = "file_size", nullable = false)
    private Long size;
    @Column(name = "upload_date")
    private LocalDateTime uploadedAt;
    @Column(name = "url_file")
    private String path;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id")
    private User user;
}
