package org.immport.data.airrstandard.yaml;

import java.util.HashSet;

import org.immport.data.airrstandard.util.AirrStandardConstants;

public class AirrTemplates {

    public static final HashSet<String> AIRR_ATTRIBUTES = new HashSet<String>();

    static {
        AIRR_ATTRIBUTES.add("study");
        AIRR_ATTRIBUTES.add("subject");
        AIRR_ATTRIBUTES.add("diagnosis");
        AIRR_ATTRIBUTES.add("sample");
        AIRR_ATTRIBUTES.add("cellProcessing");
        AIRR_ATTRIBUTES.add("nucleicAcidProcessing");
        AIRR_ATTRIBUTES.add("rawSequenceData");
        AIRR_ATTRIBUTES.add("softwareProcessing");
        AIRR_ATTRIBUTES.add("alignment");
        AIRR_ATTRIBUTES.add("rearrangement");
    }

    private Entity study;
    private Entity subject;
    private Entity diagnosis;
    private Entity sample;
    private Entity cellProcessing;
    private Entity nucleicAcidProcessing;
    private Entity rawSequenceData;
    private Entity softwareProcessing;
    private Entity alignment;
    private Entity rearrangement;

    public Entity getStudy() {
        return study;
    }

    public void setStudy(Entity study) {
        this.study = study;
    }

    public Entity getSubject() {
        return subject;
    }

    public void setSubject(Entity subject) {
        this.subject = subject;
    }

    public Entity getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Entity diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Entity getSample() {
        return sample;
    }

    public void setSample(Entity sample) {
        this.sample = sample;
    }

    public Entity getCellProcessing() {
        return cellProcessing;
    }

    public void setCellProcessing(Entity cellProcessing) {
        this.cellProcessing = cellProcessing;
    }

    public Entity getNucleicAcidProcessing() {
        return nucleicAcidProcessing;
    }

    public void setNucleicAcidProcessing(Entity nucleicAcidProcessing) {
        this.nucleicAcidProcessing = nucleicAcidProcessing;
    }

    public Entity getRawSequenceData() {
        return rawSequenceData;
    }

    public void setRawSequenceData(Entity rawSequenceData) {
        this.rawSequenceData = rawSequenceData;
    }

    public Entity getSoftwareProcessing() {
        return softwareProcessing;
    }

    public void setSoftwareProcessing(Entity softwareProcessing) {
        this.softwareProcessing = softwareProcessing;
    }

    public Entity getAlignment() {
        return alignment;
    }

    public void setAlignment(Entity alignment) {
        this.alignment = alignment;
    }

    public Entity getRearrangement() {
        return rearrangement;
    }

    public void setRearrangement(Entity rearrangement) {
        this.rearrangement = rearrangement;
    }

    public String toString() {
        StringBuffer str = new StringBuffer(AirrStandardConstants.OPEN_PAREN);
        str.append(String.join(AirrStandardConstants.EQUALS, "study",
                (getStudy() == null) ? AirrStandardConstants.EMPTY_STR : getStudy().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "subject",
                (getSubject() == null) ? AirrStandardConstants.EMPTY_STR : getSubject().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "diagnosis",
                (getDiagnosis() == null) ? AirrStandardConstants.EMPTY_STR : getDiagnosis().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "sample",
                (getSample() == null) ? AirrStandardConstants.EMPTY_STR : getSample().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "cellProcessing",
                (getCellProcessing() == null) ? AirrStandardConstants.EMPTY_STR : getCellProcessing().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "nucleicAcidProcessing",
                (getNucleicAcidProcessing() == null) ? AirrStandardConstants.EMPTY_STR
                        : getNucleicAcidProcessing().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "rawSequenceData",
                (getRawSequenceData() == null) ? AirrStandardConstants.EMPTY_STR : getRawSequenceData().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "softwareProcessing",
                (getSoftwareProcessing() == null) ? AirrStandardConstants.EMPTY_STR
                        : getSoftwareProcessing().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "alignment",
                (getAlignment() == null) ? AirrStandardConstants.EMPTY_STR : getAlignment().toString()));
        str.append(AirrStandardConstants.COMMA);
        str.append(String.join(AirrStandardConstants.EQUALS, "rearrangement",
                (getRearrangement() == null) ? AirrStandardConstants.EMPTY_STR : getRearrangement().toString()));
        str.append(AirrStandardConstants.CLOSE_PAREN);
        return str.toString();
    }
}
