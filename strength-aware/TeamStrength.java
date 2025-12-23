package com.ligitabl.scoring;

/**
 * Team strength categories for competitive soccer leagues.
 * Each category represents a tier of team quality with associated rating ranges.
 */
public enum TeamStrength {
    /**
     * Elite teams - Title contenders (ratings 85-95)
     * Expected finish: Top 3 positions
     */
    ELITE(85, 95),
    
    /**
     * Strong teams - European qualification level (ratings 70-84)
     * Expected finish: Positions 4-6
     */
    STRONG(70, 84),
    
    /**
     * Medium teams - Safe mid-table (ratings 55-69)
     * Expected finish: Positions 7-9
     */
    MEDIUM(55, 69),
    
    /**
     * Weak teams - Relegation concerns (ratings 40-54)
     * Expected finish: Position 10
     */
    WEAK(40, 54),
    
    /**
     * Relegation teams - Bottom tier (ratings 0-39)
     * Expected finish: Bottom 2 positions
     */
    RELEGATION(0, 39);
    
    private final int minRating;
    private final int maxRating;
    
    TeamStrength(int minRating, int maxRating) {
        this.minRating = minRating;
        this.maxRating = maxRating;
    }
    
    public int getMinRating() {
        return minRating;
    }
    
    public int getMaxRating() {
        return maxRating;
    }
    
    /**
     * Validates if a rating is within this category's range.
     */
    public boolean isValidRating(int rating) {
        return rating >= minRating && rating <= maxRating;
    }
    
    /**
     * Gets the appropriate category for a given rating.
     */
    public static TeamStrength fromRating(int rating) {
        for (TeamStrength strength : values()) {
            if (strength.isValidRating(rating)) {
                return strength;
            }
        }
        throw new IllegalArgumentException("Rating " + rating + " does not match any category");
    }
}
