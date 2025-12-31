package org.superdata.medismart.common.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.superdata.medismart.utils.StringUtils;

public enum GraphNodeType {
    Check("检查"),
    Department("科室"),
    Disease("疾病"),
    Drug("药品"),
    Food("食物"),
    Producer("生产商"),
    Symptom("症状");

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String name;

    GraphNodeType(String desc) {
        this.description = StringUtils.capitalizeFirstLetter(StringUtils.uncapitalize(desc));
    }

}
