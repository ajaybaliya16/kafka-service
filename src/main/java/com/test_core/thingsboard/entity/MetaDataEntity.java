package com.test_core.thingsboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import static com.test_core.thingsboard.entity.ModelConstants.*;
import com.test_core.thingsboard.Tables.MetaData;

@Data
@Entity
@Table(schema = "BARC", name = "META_DATA")
public class MetaDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_PROPERTY)
    private Long id;
    @Column(name = TYPE_PROPERTY)
    private String type;
    @Column(name = NAME_PROPERTY)
    private String name;
    @Column(name = META_DATA_ID)
    private Long metaDataId;

    public MetaDataEntity() {
        super();
    }

    public MetaDataEntity(MetaData metaData) {
        if (metaData.getId() != null)
            this.setId(metaData.getId());
        this.type = metaData.getType();
        this.name = metaData.getName();
        this.metaDataId = metaData.getMetaDataId();
    }


    public MetaData toData() {
        MetaData metaData = new MetaData();
        metaData.setId(this.id);
        metaData.setType(this.type);
        metaData.setName(this.name);
        metaData.setMetaDataId(this.metaDataId);
        return metaData;
    }

}
