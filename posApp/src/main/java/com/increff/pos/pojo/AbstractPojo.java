package com.increff.pos.pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;
@Getter
@Setter
@MappedSuperclass
public class AbstractPojo {

    @Version
    @JsonIgnore
    private Integer version;

    @CreationTimestamp
    @JsonIgnore
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    private ZonedDateTime updatedAt;
}

