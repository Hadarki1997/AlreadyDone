package com.example.alreadydone;

public class Campaign {
    private String title;
    private String description;
    private String imageUrl;
    private int currentAmount;
    private int goalAmount;
    private int donors;
    private int daysLeft;

    public Campaign(String title, String description, String imageUrl, int currentAmount, int goalAmount, int donors, int daysLeft) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.currentAmount = currentAmount;
        this.goalAmount = goalAmount;
        this.donors = donors;
        this.daysLeft = daysLeft;
    }

    // Getters and setters for each field
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public int getGoalAmount() {
        return goalAmount;
    }

    public int getDonors() {
        return donors;
    }

    public int getDaysLeft() {
        return daysLeft;
    }
}
