package org.superdata.medismart.entity.kg;

import lombok.Getter;

@Getter
public enum Relation {
    ACCOMPANY_WITH("伴随"),
    BELONGS_TO("属于"),
    COMMON_DRUG("常用药"),
    DO_EAT("可食用"),
    DRUGS_OF("药品"),
    HAVE_SYMPTOM("症状"),
    NEED_CHECK("需检查"),
    NO_EAT("忌食"),
    RECOMMEND_DRUG("推荐药"),
    RECOMMEND_EAT("推荐食");

    private String description;
    private String name;

    Relation(String name) {
        this.description = this.name().toLowerCase();
    }

}
