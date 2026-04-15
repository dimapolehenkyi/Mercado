package com.example.mercado.courses.moduleContent.enums;

public enum ModuleResourceType {
    PDF, DOCX, TXT, RTF, XLSX, CSV, PPTX, PNG, JPG, JPEG, SVG,
    GIF, MP4, WEBM, MOV, AVI, MP3, WAV, OGG, JAVA, PY, JS, HTML,
    CSS, SQL, ZIP, RAR, TAR, GZ, JSON, XML, MD, EPUB;

    public boolean isAudio() {
        return this == MP3 || this == WAV || this == OGG;
    }

    public boolean isImage() {
        return this == PNG || this == JPG || this == JPEG || this == SVG || this == GIF;
    }

    public boolean isDocument() {
        return this == PDF || this == DOCX || this == XLSX || this == PPTX || this == TXT || this == RTF || this == CSV || this == MD || this == EPUB;
    }

    public boolean isCode() {
        return this == JAVA || this == PY || this == JS || this == HTML || this == CSS || this == SQL;
    }

    public boolean isArchive() {
        return this == ZIP || this == RAR || this == TAR || this == GZ;
    }
}
