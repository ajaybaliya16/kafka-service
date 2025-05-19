package com.test_core.thingsboard.Tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {

    //	@ApiModel
    private Long id;
    //	    @ApiModelProperty(position = 2, value = "Type")
    private String type;
    //	    @ApiModelProperty(position = 3, value = "Name")
    private String name;
    //	    @ApiModelProperty(position = 4, value = "Meta Data Id")
    private Long metaDataId;

}
