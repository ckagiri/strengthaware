package com.ligitabl.scoring;

import java.util.Objects;

/**
 * Represents a team's profile including strength ratings and derived attack/defense values.
 * Used by the strength-aware score generator to calculate realistic match outcomes.
 */
public class TeamProfile {
    private final String name;
    private final String code;
    private final TeamStrength category;
    private final int rating;
    
    /**
     * Creates a team profile with the given attributes.
     * 
     * @param name Team's full name (e.g., "Manchester City")
     * @param code Team's 3-letter code (e.g., "MCI")
     * @param category Team's strength category
     * @param rating Team's rating (0-100)
     * @throws IllegalArgumentException if rating is not in valid range for category
     */
    public TeamProfile(String name, String code, TeamStrength category, int rating) {
        this.name = Objects.requireNonNull(name, "Team name cannot be null");
        this.code = Objects.requireNonNull(code, "Team code cannot be null");
        this.category = Objects.requireNonNull(category, "Team category cannot be null");
        this.rating = clamp(rating, 0, 100);
        
        if (!category.isValidRating(rating)) {
            throw new IllegalArgumentException(
                String.format("Rating %d is not valid for category %s (valid range: %d-%d)",
                    rating, category, category.getMinRating(), category.getMaxRating()));
        }
    }
    
    /**
     * Gets the attacking strength multiplier (0.5 to 1.5).
     * 
     * Formula: 0.5 + (rating / 100.0)
     * - Rating 0:   0.5x (weak attack)
     * - Rating 50:  1.0x (average attack)
     * - Rating 100: 1.5x (elite attack)
     * 
     * @return Attack strength multiplier
     */
    public double getAttackingStrength() {
        return 0.5 + (rating / 100.0);
    }
    
    /**
     * Gets the defensive strength reduction factor (0.0 to 0.5).
     * Used to reduce opponent's expected goals.
     * 
     * Formula: (rating / 100.0) * 0.5
     * - Rating 0:   0.0 (no defensive reduction)
     * - Rating 50:  0.25 (reduces opponent goals by 25%)
     * - Rating 100: 0.5 (reduces opponent goals by 50%)
     * 
     * @return Defense strength reduction factor
     */
    public double getDefensiveStrength() {
        return (rating / 100.0) * 0.5;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCode() {
        return code;
    }
    
    public TeamStrength getCategory() {
        return category;
    }
    
    public int getRating() {
        return rating;
    }
    
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %s [%d]", name, code, category, rating);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamProfile that = (TeamProfile) o;
        return rating == that.rating &&
               Objects.equals(code, that.code) &&
               category == that.category;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code, category, rating);
    }
}
