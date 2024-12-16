package org.example.mainservice.enums;

public enum Status {
    NEW("Новая заявка"),
    IN_PROGRESS("В работе"),
    COMPLETED("Завершена"),
    CANCELLED("Отменена"),
    ON_HOLD("Приостановлена"),
    REJECTED("Отклонена");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

